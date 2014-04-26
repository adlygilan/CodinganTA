package adlygilang.utama;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adlygilang.json.JSONParser;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListResi extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> resiList;

	// url to get all products list
	private static String url_all_resi = "http://lacak-men.web.id/gpstracker/list_resi.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_RESI = "no_resi";
	private static final String TAG_DETAIL = "detail";
	private static final String TAG_LIST = "listresi";

	// products JSONArray
	JSONArray listresi = null;

	Button tambahresi;
	String username;
	String no_resi, noresi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_all_resi);

		tambahresi = (Button) findViewById(R.id.tambahresi);
		// Hashmap for ListView
		resiList = new ArrayList<HashMap<String, String>>();
		username = getIntent().getStringExtra("user");

		// Loading products in Background Thread
		new LoadAllResi().execute();

		// Get listview
		ListView lv = getListView();

		// on seleting single product
		// launching Edit Product Screen
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String no_resi = ((TextView) view.findViewById(R.id.noresi))
						.getText().toString();
				String load = "1";

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						AktifitasFragment.class);
				// sending pid to next activity
				in.putExtra("username", username);
				in.putExtra("no_resi", no_resi);
				in.putExtra("list", load);

				// starting new activity and expecting some response
				// back
				startActivityForResult(in, 100);

			}
		});

		tambahresi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ListResi.this, TambahResi.class);
				intent.putExtra("user", username);
				startActivity(intent);
				finish();
			}
		});

	}

	// Response from Edit Product Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if result code 100
		if (resultCode == 100) {
			// if result code 100 is received
			// means user edited/deleted product
			// reload this screen again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

	}

	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadAllResi extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ListResi.this);
			pDialog.setMessage("Loading resi list. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All resi from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));

			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_resi, "GET",
					params);

			// Check your log cat for JSON reponse
			Log.d("All Resi: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// products found
					// Getting Array of Products
					listresi = json.getJSONArray(TAG_LIST);

					// looping through All Products
					for (int i = 0; i < listresi.length(); i++) {
						JSONObject c = listresi.getJSONObject(i);

						// Storing each json item in variable
						noresi = c.getString(TAG_RESI);
						String detail = c.getString(TAG_DETAIL);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_RESI, noresi);
						map.put(TAG_DETAIL, detail);

						// adding HashList to ArrayList
						resiList.add(map);
					}
				} else {
					// no products found
					// // Launch Add New product Activity
					Intent i = new Intent(getApplicationContext(),
							TambahResi.class);
					i.putExtra("user", username);
					// // Closing all previous activities
					// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			no_resi = noresi;

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(ListResi.this,
							resiList, R.layout.list_item, new String[] {
									TAG_DETAIL, TAG_RESI }, new int[] {
									R.id.detail, R.id.noresi });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		AlertDialog.Builder builder = new AlertDialog.Builder(ListResi.this);
		builder.setMessage("Apakah Anda ingin kembali ke halaman Login?")
				.setPositiveButton("Tidak",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						})
				.setNegativeButton("Ya", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(ListResi.this, Login.class);
						startActivity(intent);
						finish();
					}
				}).show();

	}

}
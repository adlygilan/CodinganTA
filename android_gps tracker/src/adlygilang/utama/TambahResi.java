package adlygilang.utama;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import adlygilang.json.JSONParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TambahResi extends Activity {
	// Progress Dialog
	private ProgressDialog pDialog;

	// url to create new product
	private static String url_tambah_resi = "http://lacak-men.web.id/gpstracker/tambah_resi.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	JSONParser jsonParser = new JSONParser();
	EditText noresi, detail;
	Button tambahresi;
	String noResi, detailBarang, username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tambahresi);

		noresi = (EditText) findViewById(R.id.noresi);
		detail = (EditText) findViewById(R.id.detail);
		tambahresi = (Button) findViewById(R.id.tambah_resi);

		username = getIntent().getStringExtra("user");

		tambahresi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new TambahResii().execute();
			}
		});
	}

	/**
	 * Background Async Task to Create new product
	 * */
	class TambahResii extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TambahResi.this);
			pDialog.setMessage("Sedang menambah resi...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			noResi = noresi.getText().toString();
			detailBarang = detail.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("noresi", noResi));
			params.add(new BasicNameValuePair("detail", detailBarang));
			params.add(new BasicNameValuePair("user", username));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_tambah_resi,
					"POST", params);

			// check log cat for response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					Intent i = new Intent(getApplicationContext(),
							ListResi.class);
					i.putExtra("user", username);
					startActivity(i);

					// closing this screen
					finish();
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}

}
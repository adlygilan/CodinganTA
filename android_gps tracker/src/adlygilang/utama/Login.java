package adlygilang.utama;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import adlygilang.deteksi.koneksi.ConnectionDetector;
import adlygilang.json.JSONParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText username, password;
	String usernames, passwords;
	Button login, regist;
	int statlogin = 1;

	// url to create new product
	private static String url_login = "http://lacak-men.web.id/gpstracker/login.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Edit Text

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.passw);

		login = (Button) findViewById(R.id.login);
		regist = (Button) findViewById(R.id.register);

		regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Login.this, Registerasi.class);
				startActivity(intent);
				finish();
			}
		});

		// button click event
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				new CreateNewProduct().execute();

			}
		});
		// creating connection detector class instance
		cd = new ConnectionDetector(getApplicationContext());

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		// check for Internet status
		if (isInternetPresent) {
			// Internet Connection is Present
			// make HTTP requests
			// showAlertDialog(Splash.this, "Internet Connection",
			// "You have internet connection", true);
		} else {
			// Internet connection is not present
			// Ask user to connect to Internet
			AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
			builder.setMessage("Tidak ada koneksi internet !")
					.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									System.exit(0);
								}
							}).show();

		}

	}

	/**
	 * Background Async Task to Create new product
	 * */
	class CreateNewProduct extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Loging in...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			usernames = username.getText().toString();
			passwords = password.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", usernames));
			params.add(new BasicNameValuePair("password", passwords));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_login, "POST",
					params);

			// check log cat for response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					statlogin = 1;
					Intent i = new Intent(getApplicationContext(),
							ListResi.class);
					i.putExtra("user", usernames);
					startActivity(i);
					// closing this screen
					finish();
				} else if (success == 0) {
					statlogin = 0;
					//failed
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
			if (statlogin == 0) {
				pDialog.setMessage("Username/Password salah!");
//				pDialog.dismiss();
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
		builder.setMessage("Apakah Anda ingin keluar?")
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
						System.exit(0);
					}
				}).show();
	}
}

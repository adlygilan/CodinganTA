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
import android.widget.Toast;

public class Registerasi extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText namadepan, namabelakang, username, password, repassword, email,
			alamat, nohp;
	String namadepans, namabelakangs, usernames, passwords, repasswords,
			emails, alamats, nohps;
	Button regist, login;

	// url to create new product
	private static String url_create_product = "http://lacak-men.web.id/gpstracker/registerasi.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerasi);

		// Edit Text
		namadepan = (EditText) findViewById(R.id.namadepan);
		namabelakang = (EditText) findViewById(R.id.namabelakang);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		repassword = (EditText) findViewById(R.id.repassword);
		email = (EditText) findViewById(R.id.email);
		alamat = (EditText) findViewById(R.id.alamat);
		nohp = (EditText) findViewById(R.id.nohp);

		regist = (Button) findViewById(R.id.regist);
		login = (Button) findViewById(R.id.login);

		// button click event
		regist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				usernames = username.getText().toString();
				passwords = password.getText().toString();
				repasswords = repassword.getText().toString();

				if (!(repasswords.equals(passwords))) {
					Toast.makeText(Registerasi.this, "Password tidak sama !",
							Toast.LENGTH_SHORT).show();
				} else if (usernames.isEmpty()) {
					Toast.makeText(Registerasi.this,
							"username tidak boleh kosong !", Toast.LENGTH_SHORT)
							.show();
				} else {
					new CreateNewProduct().execute();
				}
			}
		});

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Registerasi.this, Login.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
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
			pDialog = new ProgressDialog(Registerasi.this);
			pDialog.setMessage("Registering...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			namadepans = namadepan.getText().toString();
			namabelakangs = namabelakang.getText().toString();
			usernames = username.getText().toString();
			passwords = password.getText().toString();
			emails = email.getText().toString();
			alamats = alamat.getText().toString();
			nohps = nohp.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("namadepan", namadepans));
			params.add(new BasicNameValuePair("namabelakang", namabelakangs));
			params.add(new BasicNameValuePair("username", usernames));
			params.add(new BasicNameValuePair("password", passwords));
			params.add(new BasicNameValuePair("email", emails));
			params.add(new BasicNameValuePair("alamat", alamats));
			params.add(new BasicNameValuePair("nohp", nohps));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_product,
					"POST", params);

			// check log cat for response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					Intent i = new Intent(getApplicationContext(), Login.class);
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

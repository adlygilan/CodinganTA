package adlygilang.utama;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import adlygilang.json.JSONParser;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class DeleteResi extends Activity {

	String no_resi, user;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// url to get all products list
	private static String url_delet_resi = "http://lacak-men.web.id/gpstracker/delet_resi.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_NO_RESI = "no_resi";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		no_resi = getIntent().getStringExtra("no_resi");
		user = getIntent().getStringExtra("user");

		new DeleteProduct().execute();

	}

	/*****************************************************************
	 * Background Async Task to Delete Product
	 * */
	class DeleteProduct extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
		}

		/**
		 * Deleting product
		 * */
		protected String doInBackground(String... args) {

			// Check for success tag
			int success;
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("no_resi", no_resi));
				params.add(new BasicNameValuePair("user", user));

				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(url_delet_resi,
						"GET", params);

				// check your log for json response
				Log.d("User & resi", user + " " + no_resi);
				Log.d("Delete Product", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// product successfully deleted
					// notify previous activity by sending code 100
					Intent i = getIntent();
					// send result code 100 to notify about product deletion
					setResult(100, i);
					finish();
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

		}

	}

}

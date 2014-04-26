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
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Peta extends Activity implements OnItemSelectedListener {

	// Google Map
	private GoogleMap googleMap;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> koordinatList;

	// url to get all products list
	private static String url_get_coordinate = "http://lacak-men.web.id/gpstracker/gps_get_coordinate.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ID_GPS = "id_gps";
	private static final String TAG_DATE = "date";
	private static final String TAG_TIME = "time";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_LATITUDE = "latitude";

	// products JSONArray
	JSONArray koordinat = null;

	String id_gpss;

	int a = 0;

	Spinner pilihMarker;

	Double lats[], lons[];
	String dats[], tims[];

	// LAT LONG DAYKOL
	// double latitude; // = -6.974842;
	// double longitude; // = 107.633838;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.peta);

		pilihMarker = (Spinner) findViewById(R.id.pilihanMarker);

		id_gpss = getIntent().getStringExtra("id_gps");

		koordinatList = new ArrayList<HashMap<String, String>>();

		new LoadAllCoordinate().execute();

		try {
			// Loading map
			initilizeMap();

			// Changing map type
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

			// Showing / hiding your current location
			googleMap.setMyLocationEnabled(true);

			// Enable / Disable zooming controls
			googleMap.getUiSettings().setZoomControlsEnabled(true);

			// Enable / Disable my location button
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			// Enable / Disable Compass icon
			googleMap.getUiSettings().setCompassEnabled(true);

			// Enable / Disable Rotate gesture
			googleMap.getUiSettings().setRotateGesturesEnabled(true);

			// Enable / Disable zooming functionality
			googleMap.getUiSettings().setZoomGesturesEnabled(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	/**
	 * function to load map If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadAllCoordinate extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
		}

		/**
		 * getting All resi from url
		 * */
		protected String doInBackground(String... args) {

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id_gps", id_gpss));

			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_get_coordinate,
					"GET", params);

			// Check your log cat for JSON reponse
			Log.d("Koordinat: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// products found
					// Getting Array of Products
					koordinat = json.getJSONArray(TAG_ID_GPS);

					String latitudes[] = new String[koordinat.length()];
					String longitudes[] = new String[koordinat.length()];
					String dates[] = new String[koordinat.length()];
					String times[] = new String[koordinat.length()];

					Double latitude[] = new Double[koordinat.length()];
					Double longitude[] = new Double[koordinat.length()];
					
					lats = new Double[koordinat.length()];
					lons = new Double[koordinat.length()];
					dats = new String[koordinat.length()];
					tims = new String[koordinat.length()];

					List<String> kategoriWaktu = new ArrayList<String>();

					kategoriWaktu.add("Show All");

					// looping through All Products
					for (int i = 0; i < koordinat.length(); i++) {
						JSONObject c = koordinat.getJSONObject(i);

						// Storing each json item in variable
						latitudes[i] = c.getString(TAG_LATITUDE);
						longitudes[i] = c.getString(TAG_LONGITUDE);
						dates[i] = c.getString(TAG_DATE);
						times[i] = c.getString(TAG_TIME);

						kategoriWaktu.add(dates[i] + " " + times[i]);

						latitude[i] = Double.parseDouble(latitudes[i]
								.replaceAll("-", "")) * -1;
						longitude[i] = Double.parseDouble(longitudes[i]);

						Log.d("lat" + i + ": ", latitude[i].toString());
						Log.d("long" + i + ": ", longitude[i].toString());
						
						a = i;

						lats[i] = latitude[i];
						lons[i] = longitude[i];
						dats[i] = dates[i];
						tims[i] = times[i];

					}
					// Creating adapter for spinner
					ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
							Peta.this, android.R.layout.simple_spinner_item,
							kategoriWaktu);

					// Drop down layout style - list view with radio button
					dataAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					// attaching data adapter to spinner
					pilihMarker.setAdapter(dataAdapter);

					pilihMarker.setOnItemSelectedListener(Peta.this);

				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// String items = parent.getItemAtPosition(position).toString();

		Integer item = pilihMarker.getSelectedItemPosition();

		if (item == 0) {
			googleMap.clear();

			for (int i = 0; i <= a; i++) {
				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(lats[i], lons[i])).title(
						"tanggal: " + dats[i] + " waktu: " + tims[i]);
				if (i == 0) {
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
					googleMap.addMarker(marker);

				} else if (i == (koordinat.length() - 1)) {
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED));
					googleMap.addMarker(marker);

				} else {
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
					googleMap.addMarker(marker);
				}

			}
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(lats[a], lons[a])).zoom(15).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

		}

		int b = a + 1;
		String waktu[] = new String[b];

		for (int i = 0; i <= a; i++) {
			waktu[i] = dats[i] + " " + tims[i];
		}

		for (int i = 1; i <= a + 1; i++) {
			if (item == i) {
				googleMap.clear();

				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(lats[i - 1], lons[i - 1])).title(
						"tanggal: " + dats[i - 1] + " waktu: " + tims[i - 1]);
				marker.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				googleMap.addMarker(marker);

				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(lats[i - 1], lons[i - 1])).zoom(15)
						.build();

				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));

			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}

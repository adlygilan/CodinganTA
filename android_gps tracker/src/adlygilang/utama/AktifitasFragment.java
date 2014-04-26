package adlygilang.utama;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adlygilang.json.JSONParser;
import adlygilang.tabswipe.TabsPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Button;

public class AktifitasFragment extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	Button peta;

	String username, no_resi, list;
	String id_gpss, tanggalkirims, origins, destinations, shippers, consignees,
			status;

	// Tab titles
	private String[] tabs = { "Info Barang", "Sensor", "About" };
	// url to get all products list
	private static String url_detail_resi = "http://lacak-men.web.id/gpstracker/get_resi_detail.php";
	private static String url_get_sensor = "http://lacak-men.web.id/gpstracker/gps_get_sensor.php";

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_RESI = "no_resi";

	private static final String TAG_ID_GPS = "id_gps";
	private static final String TAG_TANGGAL_KIRIM = "tanggal_kirim";
	private static final String TAG_ORIGIN = "origin";
	private static final String TAG_DESTINATION = "destination";
	private static final String TAG_SHIPPER = "shipper";
	private static final String TAG_CONSIGNEE = "consignee";
	private static final String TAG_STATUS = "status";

	private static final String TAG_DATE = "date";
	private static final String TAG_TIME = "time";
	private static final String TAG_XAXIS = "xaxis";
	private static final String TAG_YAXIS = "yaxis";
	private static final String TAG_ZAXIS = "zaxis";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabswipe);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		no_resi = getIntent().getStringExtra("no_resi");
		username = getIntent().getStringExtra("username");
		list = getIntent().getStringExtra("list");

		if (list != null) {
			new GetResiDetails().execute();
		}
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	/**
	 * Background Async Task to Get complete product details
	 * */
	class GetResiDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// pDialog.setMessage("Loading resi details. Please wait...");
			// pDialog.setIndeterminate(false);
			// pDialog.setCancelable(true);
			// pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... params) {

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					// Check for success tag
					int success;
					try {
						// Building Parameters
						Log.d("nomer resi: ", no_resi);

						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("no_resi", no_resi));

						StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
								.permitAll().build();
						StrictMode.setThreadPolicy(policy);

						// getting product details by making HTTP request
						// Note that product details url will use GET request
						JSONObject json = jParser.makeHttpRequest(
								url_detail_resi, "GET", params);

						// check your log for json response
						Log.d("Single Resi Details", json.toString());

						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received product details
							JSONArray productObj = json.getJSONArray(TAG_RESI); // JSON
																				// Array
							// get first product object from JSON Array
							JSONObject product = productObj.getJSONObject(0);

							// product with this pid found
							no_resi = product.getString(TAG_RESI);
							id_gpss = product.getString(TAG_ID_GPS);
							origins = product.getString(TAG_ORIGIN);
							destinations = product.getString(TAG_DESTINATION);
							shippers = product.getString(TAG_SHIPPER);
							consignees = product.getString(TAG_CONSIGNEE);
							tanggalkirims = product
									.getString(TAG_TANGGAL_KIRIM);
							status = product.getString(TAG_STATUS);

							try {
								// Building Parameters
								Log.d("nomer resi: ", no_resi);

								List<NameValuePair> paramss = new ArrayList<NameValuePair>();
								paramss.add(new BasicNameValuePair("id_gps",
										id_gpss));

								StrictMode.ThreadPolicy policyy = new StrictMode.ThreadPolicy.Builder()
										.permitAll().build();
								StrictMode.setThreadPolicy(policyy);

								// getting product details by making HTTP
								// request
								// Note that product details url will use GET
								// request
								JSONObject jsons = jParser.makeHttpRequest(
										url_get_sensor, "GET", paramss);

								// check your log for json response
								Log.d("Single Resi Details", jsons.toString());

								// json success tag
								success = jsons.getInt(TAG_SUCCESS);
								if (success == 1) {
									// successfully received product details
									JSONArray productObjs = jsons
											.getJSONArray(TAG_ID_GPS); // JSON
																		// Array
									String date[] = new String[productObjs
											.length()];
									String time[] = new String[productObjs
											.length()];
									String xaxis[] = new String[productObjs
											.length()];
									String yaxis[] = new String[productObjs
											.length()];
									String zaxis[] = new String[productObjs
											.length()];

									Integer pjgArray = 0;

									for (int i = 0; i < productObjs.length(); i++) {
										// product with this pid found
										JSONObject products = productObjs
												.getJSONObject(i);

										date[i] = products.getString(TAG_DATE);
										time[i] = products.getString(TAG_TIME);
										xaxis[i] = products
												.getString(TAG_XAXIS);
										yaxis[i] = products
												.getString(TAG_YAXIS);
										zaxis[i] = products
												.getString(TAG_ZAXIS);

										pjgArray = i + 1;
									}
									String a = pjgArray.toString();

									Intent intent = new Intent(
											getApplicationContext(),
											AktifitasFragment.class);

									intent.putExtra("no_resi", no_resi);
									intent.putExtra("id_gps", id_gpss);
									intent.putExtra("origin", origins);
									intent.putExtra("destination", destinations);
									intent.putExtra("shipper", shippers);
									intent.putExtra("consignee", consignees);
									intent.putExtra("tanggalkirim",
											tanggalkirims);
									intent.putExtra("status", status);
									intent.putExtra("username", username);

									intent.putExtra("pjgArray", a);

									for (int i = 0; i < productObjs.length(); i++) {
										intent.putExtra("date" + i, date[i]);
										intent.putExtra("time" + i, time[i]);
										intent.putExtra("xaxis" + i, xaxis[i]);
										intent.putExtra("yaxis" + i, yaxis[i]);
										intent.putExtra("zaxis" + i, zaxis[i]);
									}
									startActivity(intent);
									finish();
								} else {

								}
							} catch (JSONException exception) {

							}
						} else {
							// product with pid not found
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			// pDialog.dismiss();
			//

		}
	}

}

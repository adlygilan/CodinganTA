package adlygilang.utama;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class TabInfoBarangFragment extends Fragment {
	Button peta, deletresi;

	String no_resi, username, id_gpss, tanggalkirims, origins, destinations,
			shippers, consignees, status;
	EditText id_gps, noresi, tanggalkirim, origin, destination, shipper,
			consignee, shipmentstatus;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.tabinfobarang_fragment,
				container, false);

		username = getActivity().getIntent().getStringExtra("username");
		no_resi = getActivity().getIntent().getStringExtra("no_resi");
		tanggalkirims = getActivity().getIntent()
				.getStringExtra("tanggalkirim");
		id_gpss = getActivity().getIntent().getStringExtra("id_gps");
		origins = getActivity().getIntent().getStringExtra("origin");
		destinations = getActivity().getIntent().getStringExtra("destination");
		shippers = getActivity().getIntent().getStringExtra("shipper");
		consignees = getActivity().getIntent().getStringExtra("consignee");
		status = getActivity().getIntent().getStringExtra("status");

		id_gps = (EditText) rootView.findViewById(R.id.idgps);
		noresi = (EditText) rootView.findViewById(R.id.noresi);
		tanggalkirim = (EditText) rootView.findViewById(R.id.tanggalkirim);
		origin = (EditText) rootView.findViewById(R.id.origin);
		destination = (EditText) rootView.findViewById(R.id.destination);
		shipper = (EditText) rootView.findViewById(R.id.shipper);
		consignee = (EditText) rootView.findViewById(R.id.consignee);
		shipmentstatus = (EditText) rootView.findViewById(R.id.shipmentstatus);

		noresi.setText(no_resi);
		id_gps.setText(id_gpss);
		tanggalkirim.setText(tanggalkirims);
		origin.setText(origins);
		destination.setText(destinations);
		shipper.setText(shippers);
		consignee.setText(consignees);
		shipmentstatus.setText(status);

		deletresi = (Button) rootView.findViewById(R.id.deletresi);
		peta = (Button) rootView.findViewById(R.id.peta);

		peta.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String a = id_gps.getText().toString();
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), Peta.class);
				intent.putExtra("id_gps", a);
				startActivity(intent);
			}
		});

		deletresi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String a = noresi.getText().toString();
				String user = username;
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), DeleteResi.class);
				intent.putExtra("no_resi", a);
				intent.putExtra("user", user);

				startActivity(intent);
				System.exit(0);
				
			}
		});

		return rootView;
	}

}

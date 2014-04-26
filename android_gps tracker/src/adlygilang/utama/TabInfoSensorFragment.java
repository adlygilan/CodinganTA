package adlygilang.utama;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TabInfoSensorFragment extends Fragment {

	TableLayout tabelsensor;
	String pjgArrays;
	int pjgArray;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rooView = inflater.inflate(R.layout.tabinfosensor_fragment,
				container, false);

		tabelsensor = (TableLayout) rooView.findViewById(R.id.tabelsensor);

		pjgArrays = getActivity().getIntent().getStringExtra("pjgArray");

		if (pjgArrays != null) {
			pjgArray = Integer.parseInt(pjgArrays);

			String xaxis[] = new String[pjgArray];
			String yaxis[] = new String[pjgArray];
			String zaxis[] = new String[pjgArray];

			String date[] = new String[pjgArray];
			String time[] = new String[pjgArray];

			for (int i = 0; i < pjgArray; i++) {
				xaxis[i] = getActivity().getIntent()
						.getStringExtra("xaxis" + i);
				yaxis[i] = getActivity().getIntent()
						.getStringExtra("yaxis" + i);
				zaxis[i] = getActivity().getIntent()
						.getStringExtra("zaxis" + i);

				date[i] = getActivity().getIntent().getStringExtra("date" + i);
				time[i] = getActivity().getIntent().getStringExtra("time" + i);
			}

			for (int i = 1; i <= pjgArray; i++) {
				TableRow tabelbaris = new TableRow(rooView.getContext());

				Integer nomer = i;
				TextView no = new TextView(rooView.getContext());
				no.setText(nomer.toString());
				no.setBackgroundResource(R.drawable.border);
				no.setGravity(Gravity.CENTER);
				tabelbaris.addView(no);

				TextView tanggal = new TextView(rooView.getContext());
				tanggal.setText(date[i - 1]);
				tanggal.setBackgroundResource(R.drawable.border);
				tanggal.setGravity(Gravity.CENTER);
				tabelbaris.addView(tanggal);

				TextView waktu = new TextView(rooView.getContext());
				waktu.setText(time[i - 1]);
				waktu.setBackgroundResource(R.drawable.border);
				waktu.setGravity(Gravity.CENTER);
				tabelbaris.addView(waktu);

				TextView xs = new TextView(rooView.getContext());
				xs.setText(xaxis[i - 1]);
				xs.setBackgroundResource(R.drawable.border);
				xs.setGravity(Gravity.CENTER);
				tabelbaris.addView(xs);

				TextView ys = new TextView(rooView.getContext());
				ys.setText(yaxis[i - 1]);
				ys.setBackgroundResource(R.drawable.border);
				ys.setGravity(Gravity.CENTER);
				tabelbaris.addView(ys);

				TextView zs = new TextView(rooView.getContext());
				zs.setText(zaxis[i - 1]);
				zs.setBackgroundResource(R.drawable.border);
				zs.setGravity(Gravity.CENTER);
				tabelbaris.addView(zs);

				tabelsensor.addView(tabelbaris);

			}

		} else {

		}
		return rooView;

	}
}

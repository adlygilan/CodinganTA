package adlygilang.tabswipe;

import adlygilang.utama.TabAboutFragment;
import adlygilang.utama.TabInfoBarangFragment;
import adlygilang.utama.TabInfoSensorFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new TabInfoBarangFragment();
		case 1:
			return new TabInfoSensorFragment();
		case 2:
			return new TabAboutFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}

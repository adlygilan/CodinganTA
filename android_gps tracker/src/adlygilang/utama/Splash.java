package adlygilang.utama;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class Splash extends Activity {
	private final int durasi = 5000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(Splash.this, Login.class);
				Splash.this.startActivity(mainIntent);
				Splash.this.finish();
			}
		}, durasi);

		Toast.makeText(Splash.this, "Adly Gilang K - 111128371",
				Toast.LENGTH_SHORT).show();

	}
}

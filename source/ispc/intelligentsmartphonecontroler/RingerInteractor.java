package ispc.intelligentsmartphonecontroler;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import ispc.intelligentsmartphonecontroler.R;
import android.widget.TextView;

public class RingerInteractor extends Activity {

	MediaPlayer mPlayer;
	ImageButton imb;
	TextView txt;
	Context cn = getBaseContext();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ringer_interactor);
		imb = (ImageButton) findViewById(R.id.imageButton1);
		txt = (TextView) findViewById(R.id.num);
		txt.setText(ispc.intelligentsmartphonecontroler.GVars.Ringer_num);

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);

		//String strRingtonePreference = sp.getString("Ringtone", "No key found");

		//Toast.makeText(cn, strRingtonePreference, 1).show();
		//Log.v("Errr", strRingtonePreference);

		mPlayer = MediaPlayer.create(this, R.raw.pp);

		//Log.v("Errr", strRingtonePreference);

		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		try {
			mPlayer.prepare();
		}

		catch (Exception e) {
			/*Log.v("Ring err", e.getMessage());
			e.printStackTrace();
*/
		}


		mPlayer.setLooping(true);
		Log.v("RING", "Loop");

		mPlayer.start();
		Log.v("RING", "START");

		imb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub

				mPlayer.stop();
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ispc.intelligentsmartphonecontroler.GVars.Ringer_num = "";
				finish();
				startActivity(intent);

			}
		});
	}

}

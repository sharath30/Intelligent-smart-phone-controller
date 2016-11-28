package ispc.intelligentsmartphonecontroler;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

public class ProfileChangerService extends Service {

	AudioManager mAudioManager;
	int counter = 0;
	static final int UPDATE_INTERVAL = 1000;
	private Timer timer = new Timer();

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		if (sp.getBoolean("PChanger", false) == true) {
			ispc.intelligentsmartphonecontroler.GVars.Profile_Changer_flag = 1;
		}
		// db.update_Setting(3, 1);
		doSomethingRepeatedly();
		return START_STICKY;

	}

	private void doSomethingRepeatedly() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				// Log.d("MyService", String.valueOf(++counter));
				if (ispc.intelligentsmartphonecontroler.GVars.Profile_Changer_Send == 1
						&& ispc.intelligentsmartphonecontroler.GVars.Profile_Changer_num != "") {
					
					mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
					
					int ringermode = mAudioManager.getRingerMode();
					
					if (ringermode == AudioManager.RINGER_MODE_SILENT
							|| ringermode == AudioManager.RINGER_MODE_VIBRATE) {
						
						
						mAudioManager
								.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						// Toast.makeText(getBaseContext(), "Profile Changed",
						// Toast.LENGTH_SHORT).show();
					}

					ispc.intelligentsmartphonecontroler.GVars.Profile_Changer_num = "";
					ispc.intelligentsmartphonecontroler.GVars.Profile_Changer_Send = 0;

					// startManagingCursor(c);

				}
			}
		}, 0, UPDATE_INTERVAL);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ispc.intelligentsmartphonecontroler.GVars.Profile_Changer_flag = 0;

		ispc.intelligentsmartphonecontroler.GVars.Profile_Changer_num = "";
		ispc.intelligentsmartphonecontroler.GVars.Profile_Changer_Send = 0;
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
	}

}
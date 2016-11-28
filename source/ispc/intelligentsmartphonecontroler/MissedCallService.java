package ispc.intelligentsmartphonecontroler;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

public class MissedCallService extends Service {
	static int i = 0;
	Uri allContacts;
	String ss = "";

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
		
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
		
		if (sp.getBoolean("MCall", false) == true) {
			ispc.intelligentsmartphonecontroler.GVars.missed_call_flag = 1;
		}
		// db.update_Setting(1, 1);
		doSomethingRepeatedly();
		return START_STICKY;

	}

	private void doSomethingRepeatedly() {
		
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {

				if (ispc.intelligentsmartphonecontroler.GVars.Missed_call_Send == 1
						&& ispc.intelligentsmartphonecontroler.GVars.missed_call_num != "") {
					
					allContacts = Uri.parse("content://call_log/calls");
					try {
						// Cursor c=getContentResolver().query(allContacts,
						// null, null, null, null);
						// c.moveToFirst();
						
						String res = "";
						
						Cursor c = getContentResolver()
								.query(android.provider.CallLog.Calls.CONTENT_URI,
										null,
										android.provider.CallLog.Calls.TYPE
												+ " = ? AND "
												+ android.provider.CallLog.Calls.NEW
												+ " = ?",
										new String[] {
												Integer.toString(android.provider.CallLog.Calls.MISSED_TYPE),
												"1" },
										android.provider.CallLog.Calls.DATE
												+ " DESC ");
						
						if (c.moveToFirst()) {
							do {
								
								if (!(c.getString(c
										.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME)) == null)) {
									res = res
											+ c.getString(c
													.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME))
											+ ":"
											+ c.getString(c
													.getColumnIndex(android.provider.CallLog.Calls.NUMBER))
											+ ",";
								} else {
									res = res
											+ "Unamed:"
											+ c.getString(c
													.getColumnIndex(android.provider.CallLog.Calls.NUMBER))
											+ ",";
								}
							} while (c.moveToNext());
						}

						// ss=c.getString(c.getColumnIndex(
						// android.provider.CallLog.Calls.NUMBER ));
						SmsManager sms = SmsManager.getDefault();
						if(res!="")
						sms.sendTextMessage(
								ispc.intelligentsmartphonecontroler.GVars.missed_call_num, null,
								res, null, null);
						else
							sms.sendTextMessage(
									ispc.intelligentsmartphonecontroler.GVars.missed_call_num, null,
									"No New Missed call", null, null);
						ispc.intelligentsmartphonecontroler.GVars.missed_call_num = "";
						ispc.intelligentsmartphonecontroler.GVars.Missed_call_Send = 0;
					} catch (Exception ex) {

					//	Toast.makeText(getBaseContext(), ex.toString(),
						//		Toast.LENGTH_SHORT).show();
						Log.v("mcall", ex.toString());
					}
					// startManagingCursor(c);

				}
			}
		}, 0, UPDATE_INTERVAL);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ispc.intelligentsmartphonecontroler.GVars.missed_call_flag = 0;
		// db.update_Setting(1, 0);
		ispc.intelligentsmartphonecontroler.GVars.missed_call_num = "";
		ispc.intelligentsmartphonecontroler.GVars.Missed_call_Send = 0;
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
	}

}
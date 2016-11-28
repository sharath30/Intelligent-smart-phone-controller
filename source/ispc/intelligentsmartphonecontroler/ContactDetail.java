package ispc.intelligentsmartphonecontroler;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

public class ContactDetail extends Service {

	Uri allContacts;
	String ss = "";
	int matched = 0;
	int contactnumber = 0;

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
		if (sp.getBoolean("CDetail", false) == true) {
			ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_flag = 1;
		}

		// db.update_Setting(2, 1);
		doSomethingRepeatedly();
		return START_STICKY;

	}

	private void doSomethingRepeatedly() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				// Log.d("MyService", String.valueOf(++counter));
				if (ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_Send == 1
						&& ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_num != "") {
					
					// allContacts=Uri.parse("content://contacts/people/1");
					
					allContacts = ContactsContract.Contacts.CONTENT_URI;
					
					try {
						Cursor c = getContentResolver().query(allContacts,
								null, null, null, null);

						if (c.moveToFirst()) {
							do {
								String contactID = c
										.getString(c
												.getColumnIndex(ContactsContract.Contacts._ID));
								String contactDisplayName = c
										.getString(c
												.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
								contactnumber = c
										.getInt(c
												.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

								if (contactDisplayName
										.toLowerCase()
										.contains(
												ispc.intelligentsmartphonecontroler.GVars.contactname
														.toLowerCase())) {
									matched = 1;
									if (contactnumber == 1) {
										Cursor phoneCursor = getContentResolver()
												.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
														null,
														ContactsContract.CommonDataKinds.Phone.CONTACT_ID
																+ "= "
																+ contactID,
														null, null);
										
										ss = ss + contactDisplayName + ":";
										while (phoneCursor.moveToNext()) {

											Log.v("Content Providers",
													phoneCursor
															.getString(phoneCursor
																	.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

											ss = ss
													+ phoneCursor
															.getString(phoneCursor
																	.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
													+ ",";
										}
										ss = ss + " ";
									}
								}
							} while (c.moveToNext());
						}
						
						SmsManager sms = SmsManager.getDefault();
						
						if (matched == 1 && contactnumber == 1) {
							sms.sendTextMessage(
									ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_num,
									null, ss, null, null);
							contactnumber = 0;
							matched = 0;
							ss = "";
						} else if (matched == 1 && contactnumber == 0) {
							sms.sendTextMessage(
									ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_num,
									null, "Person dosen't have phone number",
									null, null);
							matched = 0;
							ss = "";
						} else if (matched == 0) {
							sms.sendTextMessage(
									ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_num,
									null, "Sorry i dont have number", null,
									null);
							ss = "";
						}
						ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_num = "";
						ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_Send = 0;
					} catch (Exception ex) {

						// Toast.makeText(getBaseContext(), ex.toString(),
						// Toast.LENGTH_SHORT).show();
					}
					// startManagingCursor(c);

				}
			}
		}, 0, UPDATE_INTERVAL);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_flag = 0;
		// db.update_Setting(2, 0);
		ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_num = "";
		ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_Send = 0;
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
	}

}
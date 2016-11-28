package ispc.intelligentsmartphonecontroler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/*To make a Mobile to get $Ring$ via SMS
  
 Syntax: [key] */

public class SMSReceiver4 extends BroadcastReceiver {
	String senderTel;

	@Override
	public void onReceive(Context context, Intent intent) {
		// ---get the SMS message passed in---
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		if (ispc.intelligentsmartphonecontroler.GVars.Ringer_flag == 1) {
			Bundle bundle = intent.getExtras();

			SmsMessage[] msgs = null;
			String str = "";
			if (bundle != null) {
				// ---retrieve the SMS message received---
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[pdus.length];
				for (int i = 0; i < msgs.length; i++) {
					msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					if (i == 0) {
						senderTel = msgs[i].getOriginatingAddress();
					}
					str += msgs[i].getMessageBody().toString();
				}
				str = str.trim();
				/*
				 * DBAdapter db1=new DBAdapter(context); db1.open(); int
				 * i=db1.getContact(Long.valueOf(senderTel)); db1.close();
				 */
				if (str.equals(sp.getString("rsetting", "null"))) {
					Log.i("IF", "Ring key MATCH");

					// Toast.makeText(context, "Special",
					// Toast.LENGTH_SHORT).show();
					// SmsManager sms = SmsManager.getDefault();
					// sms.sendTextMessage(senderTel, null, "got it", null,
					// null);
					ispc.intelligentsmartphonecontroler.GVars.Ringer_Send = 1;
					ispc.intelligentsmartphonecontroler.GVars.Ringer_num = senderTel;
					this.abortBroadcast();

				} else {

					ispc.intelligentsmartphonecontroler.GVars.Ringer_Send = 0;
					ispc.intelligentsmartphonecontroler.GVars.Ringer_num = "";
				}

				// ---launch the MainActivity---

			} else {
				ispc.intelligentsmartphonecontroler.GVars.Ringer_Send = 0;
				ispc.intelligentsmartphonecontroler.GVars.Ringer_num = "";
			}
		}
	}
}

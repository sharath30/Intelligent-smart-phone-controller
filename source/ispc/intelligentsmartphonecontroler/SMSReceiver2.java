package ispc.intelligentsmartphonecontroler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/*To get '$Contact$ details via msg 
 
 Syntax: [ key:name ]*/

public class SMSReceiver2 extends BroadcastReceiver {
	String senderTel;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		/*Delet*/
		
		TelephonyManager tMgr=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber1 = tMgr.getLine1Number();
		
		/*Delet*/
		
		// ---get the SMS message passed in---
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		if (ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_flag == 1) {
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
				String[] msg = str.split(":");
				/*
				 * DBAdapter db1=new DBAdapter(context); db1.open(); int
				 * i=db1.getContact(Long.valueOf(senderTel)); db1.close();
				 */
				try {
					if (msg[0].equals(sp.getString("cdetailkey", "null"))) {
						// Toast.makeText(context, "Special",
						// Toast.LENGTH_SHORT)
						// .show();
						// SmsManager sms = SmsManager.getDefault();
						// sms.sendTextMessage(senderTel, null, "got it", null,
						// null);
						ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_Send = 1;
						ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_num = senderTel;
						ispc.intelligentsmartphonecontroler.GVars.contactname = msg[1];
						this.abortBroadcast();
					} else {
						// Toast.makeText(context, str,
						// Toast.LENGTH_SHORT).show();
						ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_Send = 0;
						ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_num = "";
					}
				} catch (Exception ex) {
				}

				// ---launch the MainActivity---

			} else {
				ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_Send = 0;
				ispc.intelligentsmartphonecontroler.GVars.Contact_Detail_num = "";
			}
		}
	}
}

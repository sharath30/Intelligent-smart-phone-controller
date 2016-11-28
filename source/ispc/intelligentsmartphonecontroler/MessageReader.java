package ispc.intelligentsmartphonecontroler;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.*;

/*To $Read Mesaage$ from mobile by sending msg 

 Syntax: [key:all] */

public class MessageReader extends BroadcastReceiver {
	String senderTel;

	public String findNameByAddress(Context ct, String addr) {

		Uri myPerson = Uri.withAppendedPath(
				ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
				Uri.encode(addr));

		String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

		Cursor cursor = ct.getContentResolver().query(myPerson, projection,
				null, null, null);

		if (cursor.moveToFirst()) {

			String name = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

			Log.e("Found contact name", name);

			cursor.close();

			return name;
		}

		cursor.close();
		Log.e("", "Not Found contact name");

		return addr;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// ---get the SMS message passed in---
		
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		if (sp.getBoolean("MReader", false) == true) {
			
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
						Log.e("Sender Num", senderTel);
					}
					str += msgs[i].getMessageBody().toString();
				}
				str = str.trim();
				/*
				 * DBAdapter db1=new DBAdapter(context); db1.open(); int
				 * i=db1.getContact(Long.valueOf(senderTel)); db1.close();
				 */
				String[] msg = str.split(":");
				if (msg[0].equals(sp.getString("mreaderkey", "null"))) {
					if (msg.length > 1) {
						Uri allContacts = ContactsContract.Contacts.CONTENT_URI;
						String id = "";
						String contactDisplayName = "";
						int fflg = 0;

						Cursor cn = context.getContentResolver().query(
								allContacts, null, null, null, null);

						if (cn.moveToFirst()) {
							do {
								String contactID = cn
										.getString(cn
												.getColumnIndex(ContactsContract.Contacts._ID));
								contactDisplayName = cn
										.getString(cn
												.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

								if (contactDisplayName.contains(msg[1].trim())) {
									id = contactID;
									fflg = 1;
									break;
								}
							} while (cn.moveToNext());
						}
						
						String sendMess = "";
						SmsManager sms = SmsManager.getDefault();
						Uri sms_content = Uri.parse("content://sms/inbox");

						//To retrieve Unread msgs
						String where = "read = 0";

						String[] arr = new String[] { "_id", "thread_id",
								"address", "person", "date", "body" };
						
						Cursor c = context.getContentResolver().query(
								sms_content, arr, where, null, null);
						
						ContentValues values = new ContentValues();
						values.put("read", true);
						
						if (c.moveToFirst()) {
							do {
								if (c.getString(3) != null) {
									if (id.equals(c.getString(3))) {
										sendMess = sendMess + "Message from "
												+ contactDisplayName + ":"
												+ c.getString(5) + " , ";
										context.getContentResolver().update(
												sms_content, values,
												"_id=" + c.getInt(0), null);
									}
								}
								if (c.getString(2).contains(msg[1])) {

									sendMess = sendMess + "Message from "
											+ c.getString(2) + ":"
											+ c.getString(5) + " , ";
									context.getContentResolver().update(
											sms_content, values,
											"_id=" + c.getInt(0), null);
								}
								if (msg[1].contains("all")
										|| msg[1].contains("All")) {
									/*
									 * if (c.getString(3) != null) { Cursor cur
									 * = context .getContentResolver()
									 * .query(allContacts, null,
									 * ContactsContract.Contacts._ID + "= ?",
									 * new String[] { String.valueOf(c
									 * .getString(3)) }, null);
									 * cur.moveToFirst(); sendMess = sendMess +
									 * "Message from " + c.getString(2) + "(" +
									 * cur.getString(cur
									 * .getColumnIndex(ContactsContract
									 * .Contacts.DISPLAY_NAME)) + "):" +
									 * c.getString(5) + " , ";
									 * context.getContentResolver().update(
									 * sms_content, values, "_id=" +
									 * c.getInt(0), null); } else {
									 */
									sendMess = sendMess
											+ "Message from "
											+ findNameByAddress(context,
													c.getString(2)) + ":"
											+ c.getString(5) + " , ";
									context.getContentResolver().update(
											sms_content, values,
											"_id=" + c.getInt(0), null);
									// }
								}
								Log.v("eror",
										c.getString(2) + " " + c.getString(5));

							} while (c.moveToNext());
							if (!sendMess.equals(""))
								Log.e("to Num", senderTel);
								Log.e("msg to be sent", sendMess);
							sms.sendTextMessage(senderTel, null, sendMess,
									null, null);
						} else {
							sms.sendTextMessage(senderTel, null,
									"No New message received", null, null);
						}
						//

						this.abortBroadcast();
					}

				}

				// ---launch the MainActivity---

			}
		}
	}
}

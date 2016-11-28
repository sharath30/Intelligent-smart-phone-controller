package ispc.intelligentsmartphonecontroler;

import java.util.List;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/*To get the current $Location$ of the Mobile via Msg

Syntax: [key]*/

public class LocationTracker extends BroadcastReceiver {

	LocationManager lm;
	LocationListener locationListener;
	String senderTel;
	Context cn;

	@Override
	public void onReceive(Context context, Intent intent) {

		cn = context;
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		if (sp.getBoolean("LTracker", false) == true) {
			
			Bundle bundle = intent.getExtras();
			
			SmsMessage[] msgs = null;
			String str = "";
			
			if (bundle != null) {
				senderTel = "";
				
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[pdus.length];
				
				for (int i = 0; i < msgs.length; i++) {
					msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					if (i == 0) {
						senderTel = msgs[i].getOriginatingAddress();
					}
					str += msgs[i].getMessageBody().toString();
					str.trim();
				}
				if (str.equals(sp.getString("ltraker", null))) {
					// torus.be_with_me.util.EnableGPS.setGPSOn(context);
					// Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
					
					lm = (LocationManager) context
							.getSystemService(Context.LOCATION_SERVICE);
					locationListener = new MyLocationListener();
					
					lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
							0, locationListener);
					this.abortBroadcast();
				} else {
					Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
				}

			}
		}

	}

	private class MyLocationListener implements LocationListener {
		
		@Override
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				SmsManager sms = SmsManager.getDefault();
				String res = "";
				double lat = loc.getLatitude();
				double lng = loc.getLongitude();

				Geocoder geocoder = new Geocoder(cn, Locale.getDefault());
				// Toast.makeText(this, String.valueOf()), duration)
				try {
					List<Address> addresses = geocoder.getFromLocation(lat,
							lng, 1);
					// Toast.makeText(context,
					// addresses.get(0).toString(),Toast.LENGTH_SHORT).show();
					Log.v("LongAdd", addresses.get(0).toString());
					
					if (addresses != null) {
						Address returnedAddress = addresses.get(0);
						
						StringBuilder strReturnedAddress = new StringBuilder(
								"Address:\n");
						
						for (int i = 0; i < returnedAddress
								.getMaxAddressLineIndex(); i++) {
							
							strReturnedAddress.append(
									returnedAddress.getAddressLine(i)).append(
									"\n");
						}
						
						Log.v("SmallAdd", strReturnedAddress.toString());

						res = strReturnedAddress.toString();

					} else {

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.v("error", e.toString());
					if (e.toString()
							.equals("java.lang.IndexOutOfBoundsException: Invalid index 0, size is 0"))

						res = "Current Gps Location not Recognized";

					if (e.toString()
							.equals("java.io.IOException: Unable to parse response from server"))
						res = "Unable to fetch exact location";

				}
				res = res + " Use these link to check on the location on map ";
				
				sms.sendTextMessage(senderTel, null, res, null, null);
				
				sms.sendTextMessage(senderTel, null,
						"http://maps.google.com/maps?q=" + loc.getLatitude()
								+ "," + loc.getLongitude(), null, null);
				
				lm.removeUpdates(locationListener);

				lm.removeUpdates(locationListener);
			}

		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

}

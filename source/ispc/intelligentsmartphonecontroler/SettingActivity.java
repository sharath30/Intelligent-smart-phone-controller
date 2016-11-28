package ispc.intelligentsmartphonecontroler;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity {

	CheckBox mcall, contact, mreader, pchanger, ringer, ltrack, blue;
	EditText mcallkey, contactkey, mreaderkey, pchangerkey, ringerkey,
			ltrackkey;
	SharedPreferences shpref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		shpref = PreferenceManager.getDefaultSharedPreferences(this);
		
		mcall = (CheckBox) findViewById(R.id.missedcallset);
		mcallkey = (EditText) findViewById(R.id.missedCallSettingkey);
		contact = (CheckBox) findViewById(R.id.contactdetailset);
		contactkey = (EditText) findViewById(R.id.contactdetailsettingkey);
		mreader = (CheckBox) findViewById(R.id.messagereaderset);
		mreaderkey = (EditText) findViewById(R.id.messagereadersettingkey);
		pchanger = (CheckBox) findViewById(R.id.profilechangerset);
		pchangerkey = (EditText) findViewById(R.id.profilechangersettingkey);
		ringer = (CheckBox) findViewById(R.id.ringerserviceset);
		ringerkey = (EditText) findViewById(R.id.ringerservicesettingkey);
		ltrack = (CheckBox) findViewById(R.id.locationtrackerset);
		ltrackkey = (EditText) findViewById(R.id.locationtrackersettingkey);
		//blue = (CheckBox) findViewById(R.id.bluetoothserviceset);
		defaultsetting();
		
		
		mcall.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = shpref.edit();
				
				if (isChecked) {
					editor.putBoolean("MCall", true);
					editor.putString("mcallkey", mcallkey.getText().toString()
							.trim());
					startService(new Intent(SettingActivity.this,
							MissedCallService.class));
				} else {
					editor.putBoolean("MCall", false);
					editor.putString("mcallkey", mcallkey.getText().toString()
							.trim());
					stopService(new Intent(SettingActivity.this,
							MissedCallService.class));
				}
				editor.commit();
			}
		});
		
		contact.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = shpref.edit();
				if (isChecked) {
					editor.putString("cdetailkey", contactkey.getText()
							.toString().trim());
					editor.putBoolean("CDetail", true);
					startService(new Intent(SettingActivity.this,
							ContactDetail.class));
				} else {
					editor.putBoolean("CDetail", false);
					editor.putString("cdetailkey", contactkey.getText()
							.toString().trim());
					stopService(new Intent(SettingActivity.this,
							ContactDetail.class));
				}
				editor.commit();
			}
		});
		
		pchanger.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = shpref.edit();
				if (isChecked) {
					editor.putString("pchanger", pchangerkey.getText()
							.toString().trim());
					editor.putBoolean("PChanger", true);
					startService(new Intent(SettingActivity.this,
							ProfileChangerService.class));
				} else {
					editor.putBoolean("PChanger", false);
					editor.putString("pchanger", pchangerkey.getText()
							.toString().trim());
					stopService(new Intent(SettingActivity.this,
							ProfileChangerService.class));
				}
				editor.commit();
			}
		});
		
		ringer.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = shpref.edit();
				if (isChecked) {
					editor.putString("rsetting", ringerkey.getText().toString()
							.trim());
					
					editor.putBoolean("RSetting", true);
					
					startService(new Intent(SettingActivity.this,
							RingerService.class));
				} else {
					editor.putBoolean("RSetting", false);
					editor.putString("rsetting", ringerkey.getText().toString()
							.trim());
					stopService(new Intent(SettingActivity.this,
							RingerService.class));
				}
				editor.commit();
			}
		});
		
		ltrack.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = shpref.edit();
			
				if (isChecked) {
					editor.putString("ltraker", ltrackkey.getText().toString()
							.trim());
					editor.putBoolean("LTracker", true);
					
					Toast.makeText(getApplicationContext(), "Service Started", 0).show();
					
				} else {
					editor.putString("ltraker", ltrackkey.getText().toString()
							.trim());
					editor.putBoolean("LTracker", false);
					
					Toast.makeText(getApplicationContext(), "Service Destroyed", Toast.LENGTH_SHORT).show();
					
				}
				editor.commit();
			}
		});
	
		mreader.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = shpref.edit();
				if (isChecked) {
					
					editor.putString("mreaderkey", mreader.getText().toString()
							.trim());
					editor.putBoolean("MReader", true);
					
					Toast.makeText(getApplicationContext(), "Service Started", 0).show();
					
				} else {
					editor.putString("mreaderkey", mreader.getText().toString()
							.trim());
					editor.putBoolean("MReader", false);
					Toast.makeText(getApplicationContext(), "Service Destroyed", Toast.LENGTH_SHORT).show();
				}
				editor.commit();
			}
		});
	
	}

	void defaultsetting() {
       
		mcallkey.setText(shpref.getString("mcallkey", ""));
		contactkey.setText(shpref.getString("cdetailkey", ""));
		pchangerkey.setText(shpref.getString("pchanger", ""));
		ringerkey.setText(shpref.getString("rsetting", ""));
		ltrackkey.setText(shpref.getString("ltraker", ""));
		mreaderkey.setText(shpref.getString("mreaderkey", ""));
		mcall.setChecked(shpref.getBoolean("MCall", false));
		contact.setChecked(shpref.getBoolean("CDetail", false));
		pchanger.setChecked(shpref.getBoolean("PChanger", false));
		ringer.setChecked(shpref.getBoolean("RSetting", false));
		ltrack.setChecked(shpref.getBoolean("LTracker", false));
		mreader.setChecked(shpref.getBoolean("MReader", false));
		//blue.setChecked(shpref.getBoolean("bluetoothservice", false));
		
	}
	
	@Override
	public void onBackPressed()
	{
		SharedPreferences.Editor editor = shpref.edit();
		editor.putString("mcallkey", mcallkey.getText().toString()
				.trim());
		editor.putString("cdetailkey", contactkey.getText()
				.toString().trim());
		editor.putString("pchanger", pchangerkey.getText()
				.toString().trim());
		editor.putString("rsetting", ringerkey.getText().toString()
				.trim());
		editor.putString("ltraker", ltrackkey.getText().toString()
				.trim());
		editor.putString("mreaderkey", mreaderkey.getText().toString()
				.trim());
		editor.commit();
		finish();
	}

}

package ispc.intelligentsmartphonecontroler;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceManager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainMenu extends Activity implements OnClickListener,
		OnItemClickListener {
	Button b1, b2, b3, b4, b5, b6, b7, b8;

	String[] setting = { "Change User Profile", "Change Password", "Setting",
			"About ISPC", "Help", "Exit" };
	String[] uid = null;
	
	static int flg = 0;
	ListView listView;

	// Intent in;
	SharedPreferences shpref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		shpref = PreferenceManager.getDefaultSharedPreferences(this);
		
		
		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, setting));
		listView.setOnItemClickListener(this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// if
	}

	@Override
	protected void onResume() {
		// SharedPreferences sp1 = PreferenceManager
		// .getDefaultSharedPreferences(this);

		super.onResume();
		/*
		 * if (sp1.getBoolean("Setting", false) == true) { changesetting();
		 * SharedPreferences.Editor edit = sp1.edit();
		 * edit.putBoolean("Setting", false); edit.commit(); }
		 */

	}

	@Override
	public void onBackPressed() {

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		finish();
		startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(b1)) {
			startActivityForResult(new Intent(this, Profile.class), 0);
		} else if (v.equals(b2)) {
			startActivityForResult(new Intent(this, ChangePassword.class), 1);
		}/*
		

		} else if (v.equals(b5)) {
			startActivity(new Intent(this, About.class));
		} else if (v.equals(b6)) {

		} else if (v.equals(b7)) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			finish();
			startActivity(intent);
		}
		/*
		 * } else if (v.equals(b8)) { startActivity(new Intent(this,
		 * ContactList.class)); }
		 */

	}

	
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (setting[arg2].equals("Setting")) {
			startActivity(new Intent(this, SettingActivity.class));
		}
		if (setting[arg2].equals("Change User Profile")) {
			startActivityForResult(new Intent(this, Profile.class), 0);
		}
		if (setting[arg2].equals("Change Password")) {
			startActivityForResult(new Intent(this, ChangePassword.class), 1);
		}
		if (setting[arg2].equals("About ISPC")) {
			startActivity(new Intent(this, About.class));
		}
		if (setting[arg2].equals("Help")) {
		   startActivity(new Intent(this, Help.class));
		}
		if (setting[arg2].equals("Exit")) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
	}
}

package ispc.intelligentsmartphonecontroler;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Login extends Activity implements OnClickListener {

	Button b1;
	EditText etxt1;
	
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		b1 = (Button) findViewById(R.id.changeprofile);
		etxt1 = (EditText) findViewById(R.id.editText1);

		b1.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		
		if (sp.getBoolean("checkAccount", false) == false) {
			
		} else {
			
		}
		if (sp.getBoolean("checkAccount", false) == false) {
			b1.setText("Signup");
			etxt1.setEnabled(false);
		}
		else
		{
			b1.setText("Login");
			etxt1.setEnabled(true);	
		}
	}

	@Override
	public void onBackPressed() {

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(intent);

	}


	@Override
	public void onClick(View v) {

		if (v.equals(b1)) {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());

			if (sp.getBoolean("checkAccount", false) == false) {
				startActivityForResult(new Intent(this, Profile.class), 0);
			} else {
				if (sp.getString("Password", "ISPC").equals(
						etxt1.getText().toString())) {
					Toast.makeText(getBaseContext(), "Record Matched",
							Toast.LENGTH_SHORT).show();
					etxt1.setText("");
					Intent in = new Intent(this, MainMenu.class);
					in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(in);
				} else {
					Toast.makeText(getBaseContext(), "Record Not Matched",
							Toast.LENGTH_SHORT).show();
				}
			}

		}
		

	}

	

	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		{
			if (reqCode == 0) {
				if (resultCode == Activity.RESULT_OK) {
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					SharedPreferences.Editor ed = sp.edit();
					ed.putBoolean("checkAccount", true);
					ed.commit();
					Toast.makeText(getBaseContext(), "Record Matched",
							Toast.LENGTH_SHORT).show();
					etxt1.setText("");
					Intent in = new Intent(this, MainMenu.class);
					in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(in);
				}
			}
		}
	}
}

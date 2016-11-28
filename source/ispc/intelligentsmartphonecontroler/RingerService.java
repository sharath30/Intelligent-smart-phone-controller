package ispc.intelligentsmartphonecontroler;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

public class RingerService extends Service {
	
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
         SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
         if(sp.getBoolean("RSetting", false)==true)
         {
        	 ispc.intelligentsmartphonecontroler.GVars.Ringer_flag=1;
         }
        // db.update_Setting(4, 1);
         doSomethingRepeatedly();
        return START_STICKY;
       
        
 }
private void doSomethingRepeatedly() {
      timer.scheduleAtFixedRate(new TimerTask() {
      public void run() {
    // Log.d("MyService", String.valueOf(++counter));
    	  if(ispc.intelligentsmartphonecontroler.GVars.Ringer_Send==1&&ispc.intelligentsmartphonecontroler.GVars.Ringer_num!="")
          {
           // Bundle extras = new Bundle();
   		   //extras.putString("num", ispc.intelligentsmartphonecontroler.GVars.Ringer_num.toString());
   		   //in.putExtras(extras);
           try
           {
        	   ispc.intelligentsmartphonecontroler.GVars.Ringer_Send=0;
        	   Intent in=new Intent(getBaseContext(),RingerInteractor.class);
        	   in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   		   startActivity(in);
           }
           catch(Exception e){
        	  Log.d("Result", e.toString());
        	   // Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
           }
           //ispc.intelligentsmartphonecontroler.GVars.Ringer_num="";
          
         
   		//startManagingCursor(c);
   		
         }
    }
     }, 0, UPDATE_INTERVAL);
    }
@Override
public void onDestroy() {
      super.onDestroy();
      ispc.intelligentsmartphonecontroler.GVars.Ringer_flag=0;
     // db.update_Setting(4, 0);
      ispc.intelligentsmartphonecontroler.GVars.Ringer_num="";
      ispc.intelligentsmartphonecontroler.GVars.Ringer_Send=0;
       Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
     }

 
}
package com.example.smartphone_logger;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.*;
import android.media.AudioManager;
import static java.lang.System.out;
import android.os.Bundle;
import android.os.IBinder;
import android.content.IntentFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.os.BatteryManager;
import java.util.Timer; 
import android.app.Service;
import java.util.TimerTask;
import java.util.List;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.text.StaticLayout;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class TestService extends Service{
	final static String TAG = "MyService";
	final static int INTERVAL_PERIOD = 1000;
	public static Timer timer = new Timer();
	String current_app;
	String before_app;
	SharedPreferences sharedPref;
	LogCollect logc = new LogCollect();
	
	public IBinder onBind(Intent intent) {
		return null; 
	}
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		timer.scheduleAtFixedRate(new TimerTask(){ 
			@Override
			public void run() {
				/*
				UsageStatsManager stats = (UsageStatsManager) getSystemService("usagestats");
				UsageEvents usageEvents = stats.queryEvents(start, end);
				*/
				UsageStatsManager usm = (UsageStatsManager)getApplicationContext().getSystemService("usagestats");
				//UsageEvents events = usm.queryEvents(time - interval, time);

				/*今現在開いてるアプリを調べる*/

				/*
				ActivityManager test = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
				current_app = (test.getRunningTasks(1).get(0).topActivity.getPackageName());
				
				//以前開いていたアプリを調べる
				sharedPref = getSharedPreferences("apply_data", MODE_PRIVATE);
				before_app = sharedPref.getString("current_app","");
				
				if(current_app.equals(before_app)){
					Log.i("そのまま",current_app+","+before_app);
					
				}else{
					Log.i("変わった",current_app+","+before_app);
					logc.LogWrite(System.currentTimeMillis(),35,current_app);
					
					//Preference
					sharedPref = getSharedPreferences("apply_data", MODE_PRIVATE);
			        Editor editor = sharedPref.edit();
			        editor.putString("current_app", current_app);
			        editor.commit();
				}
				*/
				} }, 0, INTERVAL_PERIOD);
		return START_STICKY;
	}

}

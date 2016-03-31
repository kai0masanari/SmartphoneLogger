package com.example.smartphone_logger;

import android.app.Activity;
import android.app.ActivityManager;

import android.content.*;
import android.media.AudioManager;
import static java.lang.System.out;
import android.os.Bundle;
import android.os.Environment;
import android.content.IntentFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.BatteryManager;

import java.io.File;
import java.util.Timer; 

import android.app.Service;
import java.util.TimerTask;
import java.util.List;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.text.StaticLayout;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class VolumeReceiver extends Activity implements OnClickListener {
	public static String currentvol2;
	public static String app_name;
	SharedPreferences sharedPref;
	LogCollect logc = new LogCollect();
	
	//UI
	private Button button1;
	private TextView view;
	
	//取得するデータ
	private String android_id;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//button登録
		button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(clicked);
		view = (TextView)findViewById(R.id.textView3); //current state
		android_id = android.provider.Settings.Secure.getString(getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
		
		//intent-filter登録
		registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
		registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
		startService(new Intent(VolumeReceiver.this, TestService.class));
		//ActivityManager test = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		//app_name = (test.getRunningTasks(1).get(0).topActivity.getPackageName()); //初期化
		app_name = "";
        //Log.i("[BroadcastReceiver]", app_name);
        //Preference
        sharedPref = getSharedPreferences("apply_data", MODE_PRIVATE);
        Editor editor = sharedPref.edit();
        editor.putString("current_app", app_name);
        editor.commit();
	}
	
	
	BroadcastReceiver mybroadcast = new BroadcastReceiver() {
		//When Event is published, onReceive method is called
		@Override
		public void onReceive(Context context, Intent intent) {
			//インテント受け取り
			if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
				logc.LogWrite(System.currentTimeMillis(),1,"1");
				//Log.i("[BroadcastReceiver]", "Screen ON");
			}else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
				logc.LogWrite(System.currentTimeMillis(),2,"0");
				//Log.i("[BroadcastReceiver]", "Screen OFF");
			}else if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
				int status_b = intent.getIntExtra("status",0);
				if (status_b == BatteryManager.BATTERY_STATUS_CHARGING) {
					sharedPref = getSharedPreferences("apply_data", MODE_PRIVATE);
					Boolean before_b = sharedPref.getBoolean("battery",false);
        		
					if(before_b == false){
						sharedPref = getSharedPreferences("apply_data", MODE_PRIVATE);
						Editor editor = sharedPref.edit();
						editor.putBoolean("battery", true);
						editor.commit();
						logc.LogWrite(System.currentTimeMillis(),11,"1");
						Log.i("[BroadcastReceiver]", "Charging");
					}
        		
				} else if (status_b == BatteryManager.BATTERY_STATUS_DISCHARGING) {
					sharedPref = getSharedPreferences("apply_data", MODE_PRIVATE);
					Boolean before_b = sharedPref.getBoolean("battery",true);
        		
					if(before_b){
						sharedPref = getSharedPreferences("apply_data", MODE_PRIVATE);
						Editor editor = sharedPref.edit();
						editor.putBoolean("battery", false);
						editor.commit();
						logc.LogWrite(System.currentTimeMillis(),12,"0");
					}
				}
			}else if(intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)){
				int status_p = intent.getIntExtra("status",0);
        		if (status_p == 0) {
        			logc.LogWrite(System.currentTimeMillis(),7,"0");
        			//Log.i("[BroadcastReceiver]", "Headset off");
        		}else if(status_p == 1){
        			logc.LogWrite(System.currentTimeMillis(),6,"1");
        			//Log.i("[BroadcastReceiver]", "Headset on");
        		}
			}
		}
	};
	
	 

	private View.OnClickListener clicked = new View.OnClickListener() {	 
		public void onClick(View v) {
			if (v.getId() == R.id.button1) {
	
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		//受信を開始
		IntentFilter filter = new IntentFilter();
		registerReceiver(myReceiver,filter);
	}
	
	public BroadcastReceiver myReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
		}
	};
	
	//音量取得用
	public static String getAudioManager(Context context) {
        AudioManager test = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        currentvol2 = Integer.toString(test.getStreamVolume(AudioManager.STREAM_RING));
        return currentvol2;
    }
	
	/*
	//プロセス取得用
	public static String getActivityManager(Context context) {
        ActivityManager test = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) test.getRunningTasks(1);
        app_name = info.processName;
        return app_name;
    }
    */
	
	public void onUserLeaveHint(){
        //ホームボタンが押された時や、他のアプリが起動した時に呼ばれる
        //戻るボタンが押された場合には呼ばれない
		ActivityManager test = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		app_name = (test.getRunningTasks(1).get(0).topActivity.getPackageName());
        //Log.i("[BroadcastReceiver]", app_name);
        //Preference
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor = sharedPref.edit();
        editor.putString("current_app", app_name);
        editor.commit();
        logc.LogWrite(System.currentTimeMillis(),35,app_name);
        /*
		ActivityManager mActiviyManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processList = mActiviyManager.getRunningAppProcesses();
		for(RunningAppProcessInfo process : processList) {
		    Log.i("","pid:"+process.pid);
		    Log.i("","processName:"+process.processName);
		}
		*/
	}
	
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	


	//public BroadcastReceiver receiver;
	public static class InnerReceiver extends BroadcastReceiver{
		LogCollect logc = new LogCollect();
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("android.media.RINGER_MODE_CHANGED")) {
				//着信モードが変更された時の処理を記述	
				if(intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1) == AudioManager.RINGER_MODE_VIBRATE){
					//out.println("Change to vibrate");
					logc.LogWrite(System.currentTimeMillis(),24,"1");
				}else if(intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1) == AudioManager.RINGER_MODE_SILENT){
					//out.println("Change to silent");
					logc.LogWrite(System.currentTimeMillis(),24,"0");
				}else{
					logc.LogWrite(System.currentTimeMillis(),25,"2");
				}
			} else if(intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
				//音量が変更された時の処理を記述
				String ringvol = getAudioManager(context);
				
				//現状だと取得できるのはシステム音量のみ
				if(ringvol != Integer.toString(0)){
					logc.LogWrite(System.currentTimeMillis(),37,ringvol);
				}else{
					//何もしない
				}
			}
		}
	}



	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
}

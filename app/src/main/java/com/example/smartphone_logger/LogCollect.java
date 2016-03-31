package com.example.smartphone_logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

public class LogCollect extends Activity{
	Const _const = new Const();
	
	public void LogWrite(long time, int event, String note){
		
		String Path = Environment.getExternalStorageDirectory().getPath() + _const.filename;
		
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(Path, true), "UTF-8"));
			String write_str = String.valueOf(time) + "," + Integer.toString(event) + "," + note + "," + "\n";
	        bw.write(write_str);
	        bw.flush();
	        bw.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
		
	}
}

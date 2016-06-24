package com.example.smartphone_logger;

import android.app.Application;
import android.content.Context;

/**
 * Created by kai on 2016/06/07.
 */
public class Global extends Application {
    private static Global sInstance;
    public static Context globalContext;

    //計測フラグの保持
    public boolean collect_flag = false;

    //計測に用いるメソッドの切り替え
    public static int u_permission = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static synchronized Global getInstance() {
        return sInstance;
    }

    public Global(){

    }
}

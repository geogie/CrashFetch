package com.george.fullscreen;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.george.fullscreen.crashsafe.Cockroach;
import com.george.fullscreen.crashsafe.CrashLog;
import com.george.fullscreen.crashsafe.ExceptionHandler;

/**
 * Created By George
 * Description:
 */
public class App extends Application {
    private static final String TAG = "App-测试";
    @Override
    public void onCreate() {
        super.onCreate();
        final Thread.UncaughtExceptionHandler sysExcepHandler = Thread.getDefaultUncaughtExceptionHandler();
        Cockroach.install(this, new ExceptionHandler() {
            @Override
            protected void onUncaughtExceptionHappened(Thread thread, Throwable throwable) {
                Log.e(TAG,"-->onUncaughtExceptionHappened:"+thread+"<--",throwable);
                CrashLog.saveCrashLog(getApplicationContext(), throwable);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            protected void onBandageExceptionHappened(Throwable throwable) {
                throwable.printStackTrace();//打印警告级别log，该throwable可能是最开始的bug导致的，无需关心
            }

            @Override
            protected void onEnterSafeMode() {
                Log.d(TAG,"onEnterSafeMode");
            }

            @Override
            protected void onMayBeBlackScreen(Throwable e) {
                super.onMayBeBlackScreen(e);
                Thread thread = Looper.getMainLooper().getThread();
                Log.e("AndroidRuntime", "--->onUncaughtExceptionHappened:" + thread + "<---", e);
                //黑屏时建议直接杀死app
                sysExcepHandler.uncaughtException(thread, new RuntimeException("black screen"));
            }
        });
    }
}

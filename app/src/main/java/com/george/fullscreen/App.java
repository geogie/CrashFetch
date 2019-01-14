package com.george.fullscreen;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.george.fullscreen.crashsafe.Cockroach;
import com.george.fullscreen.crashsafe.CrashLog;
import com.george.fullscreen.crashsafe.DebugSafeModeTipActivity;
import com.george.fullscreen.crashsafe.DebugSafeModeUI;
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
        install();
    }

    private void install(){
        final Thread.UncaughtExceptionHandler sysExcepHandler = Thread.getDefaultUncaughtExceptionHandler();
        final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        DebugSafeModeUI.init(this);
        Cockroach.install(this, new ExceptionHandler() {
            @Override
            protected void onUncaughtExceptionHappened(Thread thread, Throwable throwable) {
                Log.e(TAG,"-->onUncaughtExceptionHappened:"+thread+"<--",throwable);
                CrashLog.saveCrashLog(getApplicationContext(), throwable);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        toast.setText("捕获到异常崩溃信息");
                        toast.show();
                    }
                });
            }

            @Override
            protected void onBandageExceptionHappened(Throwable throwable) {
                throwable.printStackTrace();//打印警告级别log，该throwable可能是最开始的bug导致的，无需关心
                toast.setText("Cockroach Worked");
                toast.show();
            }

            @Override
            protected void onEnterSafeMode() {
                Log.d(TAG,"onEnterSafeMode");
                Toast.makeText(App.this, "已经进入crash状态", Toast.LENGTH_LONG).show();
                DebugSafeModeUI.showSafeModeUI();
                if (BuildConfig.DEBUG) {
                    Intent intent = new Intent(App.this, DebugSafeModeTipActivity.class);// crash模式：crash UI
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 因为是Application启动，则需要新栈
                    startActivity(intent);
                }
            }

            @Override
            protected void onMayBeBlackScreen(Throwable e) {
                Thread thread = Looper.getMainLooper().getThread();
                Log.e("AndroidRuntime", "--->onUncaughtExceptionHappened:" + thread + "<---", e);
                //黑屏时建议直接杀死app
                sysExcepHandler.uncaughtException(thread, new RuntimeException("black screen"));
            }
        });
    }
}

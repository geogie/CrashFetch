package com.george.fullscreen.crashsafe;

import android.os.Message;

/**
 * Created By George
 * Description:
 * 规范：api兼容处理具体实现下发，在各个对应的场景中实现杀死当前异常的activity
 */
public interface IActivityKiller {
    void finishLaunchActivity(Message message);

    void finishResumeActivity(Message message);

    void finishPauseActivity(Message message);

    void finishStopActivity(Message message);
}

package com.eot_app.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.eot_app.nav_menu.jobs.job_controller.ChatController;


/**
 * Created by aplite_pc302 on 3/6/19.
 */

public class GetKillEvent_ToDestryNotication extends Service {

    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        clearAllPendingTask();
    }

    /**
     * indicate/show for user offline when application kill
     */
    private void clearAllPendingTask() {
        try {
            ChatController.getInstance().setAppUserOnline(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

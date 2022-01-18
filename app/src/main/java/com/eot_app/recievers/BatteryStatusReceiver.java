package com.eot_app.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.eot_app.firebases.RealTimeDBController;

import java.util.HashMap;

public class BatteryStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        if (deviceStatus == BatteryManager.BATTERY_STATUS_DISCHARGING
                || deviceStatus == BatteryManager.BATTERY_STATUS_CHARGING) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryLevel = (int) (((float) level / (float) scale) * 100.0f);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("battery", batteryLevel);
            RealTimeDBController.setStatus(hashMap);
            //  HyperLog.i("battery", "" + batteryLevel+" status:"+deviceStatus);

        }

    }
}

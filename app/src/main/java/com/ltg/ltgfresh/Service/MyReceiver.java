package com.ltg.ltgfresh.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ltg.ltgfresh.Network.NetworkUtil;


public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if(status.isEmpty()) {
            status="No Internet Connection";
        }
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}

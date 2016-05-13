package com.android.phoneservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OpenDevice openDevice = new OpenDevice();
        registerReceiver(openDevice,new IntentFilter(Intent.ACTION_BOOT_COMPLETED));
        Log.v("MainActivity","MainActivity,onCreate");
        startService(new Intent(this,PhoneService.class));
    }
    //内部类 接收开机广播
    private class OpenDevice extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
                startService(new Intent(MainActivity.this,PhoneService.class));
            }
        }
    }
}

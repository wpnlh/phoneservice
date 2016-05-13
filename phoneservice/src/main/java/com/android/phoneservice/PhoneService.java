package com.android.phoneservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhoneService extends Service {
    SimpleDateFormat sf = new SimpleDateFormat("yy.MM.dd|HH:mm:ss");
    private static final String TAG = "MainActivity";
    public PhoneService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //从系统服务中获得电话管理器对象
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //电话管理器对象监听电话通话状态
        tm.listen(new PhoneListener(),PhoneStateListener.LISTEN_CALL_STATE);
        Log.v("MainActivity","Service,onCreate");
    }

    class PhoneListener extends PhoneStateListener {
        private MediaRecorder recorder;
        private boolean recording;
        private String phoneNumber;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://闲时
                    Log.v("MainActivity","闲时");
                    if (recording) {
                        recorder.stop();
                        recorder.release();
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃
                    Log.v(TAG,"响铃");
                    phoneNumber = incomingNumber;//获得来电号码
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://通话
                    Log.v(TAG,"通话");
                    try {
    /*获得录音器*/
                        recorder = new MediaRecorder();
    /*设置声音来源*/
                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    /*设置输出格式*/
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
 /*设置音频编码格式*/
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
  /*保存的文件路径*/
                        File file = Environment.getExternalStorageDirectory();
    /*保存的文件名*/
                        String name =/*当前日期+来电号码*/ sf.format(new Date(System.currentTimeMillis()))
                                + "_" + phoneNumber;
                        File audioFile = new File(file, name);
/*设置输出文件保存地址*/  Log.v(TAG,audioFile.getAbsolutePath());
                        recorder.setOutputFile(audioFile.getAbsolutePath());

        /*录音准备*/     Log.v(TAG,"准备录音");
                        recorder.prepare();
                        Log.v(TAG,"开始录音");
        /*开始录音*/     recorder.start();
                        recording = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

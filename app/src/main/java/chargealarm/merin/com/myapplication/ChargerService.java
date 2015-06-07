package chargealarm.merin.com.myapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.merin.chargealarm.R;

import java.io.IOException;

public class ChargerService extends Service{

    //  merin
    //Intent batteryStatus;
    public MediaPlayer myMedia;


    public Notification serviceNoti= null;
    public Intent notiIntent = null;
    public PendingIntent pIntent = null;

    public String TAG = "merin";

    public int batterylevel;
    public Uri fileuri;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        //super.onCreate();
        Toast.makeText(ChargerService.this, "onCreate method", Toast.LENGTH_LONG).show();
        Log.e(TAG, "onCreate");
        myMedia =  MediaPlayer.create(ChargerService.this, R.raw.alarm);
        myMedia.setLooping(true);
        //myMedia.setVolume(0.5f, 0.5f);

        //create the intent for battery changed, so this activity can be caught to check new battery status
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        this.registerReceiver(this.batteryInfoReceiver,	ifilter);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        //super.onDestroy();
        Toast.makeText(ChargerService.this, "onDestroy method", Toast.LENGTH_LONG).show();
        Log.e(TAG, "onDestroy");
        myMedia.stop();
        stopForeground(true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Toast.makeText(ChargerService.this, "onStartCommand method", Toast.LENGTH_LONG).show();
        Log.e(TAG, "onStartCommand");
        batterylevel = intent.getIntExtra(MainActivity.B_LEVEL, 100);
        fileuri = intent.getExtras().getParcelable(MainActivity.FILE_NAME);
        if(fileuri != null){
            myMedia = MediaPlayer.create(ChargerService.this, fileuri);
        }

        Toast.makeText(ChargerService.this, "Battery Level :"  + batterylevel, Toast.LENGTH_LONG).show();

        serviceNoti = new Notification(R.drawable.alarm_clock,"Charge Alarm Set",System.currentTimeMillis());
        notiIntent = new Intent(this,MainActivity.class);
        notiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|intent.FLAG_ACTIVITY_SINGLE_TOP);
        pIntent = PendingIntent.getActivity(this, 0, notiIntent, 0);
        serviceNoti.setLatestEventInfo(this, "Charage Alarm", "Charge Alarm set !!", pIntent);
        serviceNoti.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(1234, serviceNoti);

        return super.onStartCommand(intent, flags, startId);

    }




    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            int  plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
            //stop the media player as soon as the power cord is removed
            if (plugged == 0){
                myMedia.stop();
                try {
                    myMedia.prepare();
                } catch (IllegalStateException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                ChargerService.this.stopSelf();
            }


            //start the media player as soon as the power level is 100% and power cord is connected
            if (level == batterylevel && (plugged != 0) ) {
                myMedia.start();
            }

        }
    };



}

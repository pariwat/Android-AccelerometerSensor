package com.example.pariwatleamsumran.acc_fall;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by pariwatleamsumran on 4/10/16 AD.
 */
public class acc_service extends Service implements SensorEventListener {

    private String TAG = "com.example.pariwatleamsumran.acc_fall";
    private SensorManager sensorManager;
    private long lastUpdate;

    public acc_service(){

    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Service Acc Enable");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if (accelationSquareRoot >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;

        }
        System.out.println("bank data: X:"+x+" Y:"+y+" Z:"+z);
        Intent i = new Intent(TAG+".SOME_MESSAGE");
        i.putExtra("x",x);
        i.putExtra("y",y);
        i.putExtra("z",z);
        sendBroadcast(i);
    }

}

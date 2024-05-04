package com.example.lab4_20182895;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.TextView;

import okhttp3.Handshake;

public class SensorAccListener implements SensorEventListener {
    private static String TAG = "msg-test-SensorAccListener";

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        if(sensorType == Sensor.TYPE_ACCELEROMETER){
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            // Calculamos el módulo de la aceleración
            float modulo = (float) Math.sqrt(x * x + y * y + z * z);

            Log.d(TAG,"Módulo de la aceleración: " + modulo);
            Log.d(TAG,"Valor de x: " + x);
            Log.d(TAG,"Valor de y: " + y);
            Log.d(TAG,"Valor de z: " + z);

            /*if (modulo> 15){
                TextView findBy


            }*/
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

package com.maxim.androidsensors;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends Activity implements SensorEventListener {
    TextView tv1=null;
    private SensorManager mSensorManager;
    private Sensor senAccelerometer;
    private Sensor senGYroscope;
    private Sensor senMotion;

    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;
    private float lastX, lastY, lastZ;

    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        ActionBar ab = getActionBar();

        tv1 = (TextView) findViewById(R.id.content_textview);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            Toast.makeText(this,"Accelorator is available",Toast.LENGTH_SHORT).show();
            senAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null){
            Toast.makeText(this,"Gyroskope is available",Toast.LENGTH_SHORT).show();
            senGYroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            mSensorManager.registerListener(this, senGYroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT) != null){
            Toast.makeText(this,"Motion is available",Toast.LENGTH_SHORT).show();
            senMotion = mSensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT);
            mSensorManager.registerListener(this, senMotion, SensorManager.SENSOR_DELAY_NORMAL);
        }

        List<Sensor> mList= mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 1; i < mList.size(); i++) {
            tv1.append("\nSensor Name: " + mList.get(i).getName() + "\nHersteller: " + mList.get(i).getVendor() + "\nVersion: " + mList.get(i).getVersion() +"\n");
        }
    }

    private void initializeViews() {

        currentX = (TextView) findViewById(R.id.number_1);
        currentY = (TextView) findViewById(R.id.number_2);
        currentZ = (TextView) findViewById(R.id.number_3);

        maxX = (TextView) findViewById(R.id.number_4);
        maxY = (TextView) findViewById(R.id.number_5);
        maxZ = (TextView) findViewById(R.id.number_6);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // clean current values
        displayCleanValues();
        // display the current x,y,z accelerometer values
        displayCurrentValues();
        // display the max x,y,z accelerometer values
        displayMaxValues();
        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        // if the change is below 2, it is just plain noise
        if (deltaX < 2)
            deltaX = 0;
        if (deltaY < 2)
            deltaY = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    // display the current x,y,z accelerometer values
    public void displayCurrentValues() {
        currentX.setText(Float.toString(deltaX));
        currentY.setText(Float.toString(deltaY));
        currentZ.setText(Float.toString(deltaZ));
    }

    // display the max x,y,z accelerometer values
    public void displayMaxValues() {
        if (deltaX > deltaXMax) {
            deltaXMax = deltaX;
            maxX.setText(Float.toString(deltaXMax));
        }
        if (deltaY > deltaYMax) {
            deltaYMax = deltaY;
            maxY.setText(Float.toString(deltaYMax));
        }
        if (deltaZ > deltaZMax) {
            deltaZMax = deltaZ;
            maxZ.setText(Float.toString(deltaZMax));
        }
    }

}


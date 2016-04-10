package com.example.pariwatleamsumran.acc_fall;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,OnChartValueSelectedListener{


    private int counter =0;
    private LineChart mChart;
    private TextView info;
    private long time_old;
    private String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String filename ="log1.csv";
    File f;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        verifyStoragePermissions(this);
        f = new File(PATH+"/"+filename);

        System.out.println("bank  PATH: "+PATH+"/"+filename);


        info = (TextView) findViewById(R.id.info);
        time_old = System.currentTimeMillis();
        {
            mChart = (LineChart) findViewById(R.id.chart1);
            mChart.setOnChartValueSelectedListener(this);

            // no description text
            mChart.setDescription("");
            mChart.setNoDataTextDescription("You need to provide data for the chart.");

            // enable touch gestures
            mChart.setTouchEnabled(true);

            // enable scaling and dragging
            mChart.setDragEnabled(true);
            mChart.setScaleEnabled(true);
            mChart.setDrawGridBackground(false);

            // if disabled, scaling can be done on x- and y-axis separately
            mChart.setPinchZoom(true);
            mChart.setAutoScaleMinMaxEnabled(true);
            // set an alternative background color
            mChart.setBackgroundColor(Color.LTGRAY);

//            LineData data = new LineData();
//            data.setValueTextColor(Color.WHITE);
//
//            // add empty data
//            mChart.setData(data);

            ArrayList<String> xVals = new ArrayList<String>();
            for (int i = 0; i < 30; i++) {
                xVals.add("");
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

            for (int z = 0; z < 3; z++) {

                ArrayList<Entry> values = new ArrayList<Entry>();
                for (int i = 0; i < 30; i++) {
                    double val = (Math.random() * 30) + 3;
                    values.add(new Entry((float) val, i));
                }

                LineDataSet d = new LineDataSet(values, "DataSet " + (z + 1));
                d.setLineWidth(2.5f);

                int color = mColors[z % mColors.length];
                d.setColor(color);
                d.setDrawValues(false);
                d.setDrawCircles(false);
                dataSets.add(d);
            }
            LineData data = new LineData(xVals, dataSets);
            mChart.setData(data);
            mChart.invalidate();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        startService();
    }

    private int[] mColors = new int[] {
            ColorTemplate.COLORFUL_COLORS[0],
            ColorTemplate.COLORFUL_COLORS[1],
            ColorTemplate.COLORFUL_COLORS[2]
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startService(){
        startService(new Intent(this, acc_service.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    IntentFilter filter = new IntentFilter("com.example.pariwatleamsumran.acc_fall.SOME_MESSAGE");
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            {

                long speed = System.currentTimeMillis() - time_old;
                DecimalFormat df = new DecimalFormat("0.00");
                info.setText("Freq: " + df.format((float) (1.0 / speed)) + " Hz Time: " + (speed / 1000.0));
                float x = intent.getFloatExtra("x", 0);
                float y = intent.getFloatExtra("y", 0);
                float z = intent.getFloatExtra("z", 0);
                String buf ="X:" + x + "  Y:" + y + "  Z:" + z + "\n";

                try {
                    org.apache.commons.io.FileUtils.writeStringToFile(f,buf);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                LineData data = mChart.getData();
                ILineDataSet dx = data.getDataSetByIndex(0);
                ILineDataSet dy = data.getDataSetByIndex(1);
                ILineDataSet dz = data.getDataSetByIndex(2);
                data.addXValue("");

                dx.addEntry(new Entry(x, dx.getEntryCount()));
                dy.addEntry(new Entry(y, dy.getEntryCount()));
                dz.addEntry(new Entry(z, dz.getEntryCount()));

                mChart.notifyDataSetChanged();
                mChart.setVisibleXRangeMaximum(120);
                mChart.moveViewToX(data.getXValCount() - 121);

                counter++;
                if (counter > 10) {
                    counter = 0;
//                    dx.removeFirst();
//                    dy.removeFirst();
//                    dz.removeFirst();
                }
            }
        }
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }
}

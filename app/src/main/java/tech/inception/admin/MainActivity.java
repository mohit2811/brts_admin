package tech.inception.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void set_timing(View view) {

        Intent i = new Intent(MainActivity.this , Timing_options.class);
        startActivity(i);
    }

    public void add_buses(View view) {
        Intent i = new Intent(MainActivity.this , Bus_Options.class);
        startActivity(i);
    }

    public void add_stops(View view) {
        Intent i = new Intent(MainActivity.this , Bus_stop_options.class);
        startActivity(i);
    }

    public void add_routes(View view) {

        Intent i = new Intent(MainActivity.this , Routes_options.class);
        startActivity(i);
    }
}

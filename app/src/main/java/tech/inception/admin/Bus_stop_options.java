package tech.inception.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Bus_stop_options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop_options);
    }

    public void view_added_stops(View view) {

        Intent i = new Intent(Bus_stop_options.this , View_stops.class);
        startActivity(i);
    }

    public void add_new_stop(View view) {
        Intent i = new Intent(Bus_stop_options.this , Add_Stops.class);
        startActivity(i);
    }
}

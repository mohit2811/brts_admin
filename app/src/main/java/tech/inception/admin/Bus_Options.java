package tech.inception.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Bus_Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus__options);
    }

    public void add_new_bus(View view) {

        Intent i = new Intent(Bus_Options.this , Add_Buses.class);
        startActivity(i);
    }

    public void view_added_buses(View view) {
        Intent i = new Intent(Bus_Options.this , View_buses.class);
        startActivity(i);
    }
}

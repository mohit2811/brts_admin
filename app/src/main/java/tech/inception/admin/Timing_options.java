package tech.inception.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Timing_options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_options);
    }

    public void view_added_timings(View view) {

        startActivity(new Intent(Timing_options.this , View_timings.class));
    }

    public void add_new_timing(View view) {

        startActivity(new Intent(Timing_options.this , Add_Timings.class));

    }
}

package tech.inception.admin;

import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tech.inception.admin.sampledata.createbuss;
import tech.inception.admin.sampledata.createroute;
import tech.inception.admin.sampledata.createstop;
import tech.inception.admin.sampledata.createtime;

public class Add_Timings extends AppCompatActivity {

    Spinner route_spinner, stop_spinner, bus_spinner;

    List<String> route_ids, stop_ids, bus_ids;
    EditText bus_timing;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__timings);
        route_ids = new ArrayList<>();
        stop_ids = new ArrayList<>();
        bus_ids = new ArrayList<>();
        bus_timing = (EditText) findViewById(R.id.bus_timing);
        route_spinner = (Spinner) findViewById(R.id.route_spinner);
        stop_spinner = (Spinner) findViewById(R.id.stop_spinner);
        bus_spinner = (Spinner) findViewById(R.id.bus_spinner);
        bus_timing = (EditText) findViewById(R.id.bus_timing);
        get_routes();
        bus_timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Add_Timings.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        bus_timing.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        route_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                get_stops(route_ids.get(route_spinner.getSelectedItemPosition()));
                get_buses(route_ids.get(route_spinner.getSelectedItemPosition()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void submit_timing(View view) {


        if (bus_timing.getText().toString().equals("")) {
            Toast.makeText(Add_Timings.this, "please add timings ", Toast.LENGTH_SHORT).show();
            return;
        }
        String ridd = route_ids.get(route_spinner.getSelectedItemPosition());
        String stop_id = stop_ids.get(stop_spinner.getSelectedItemPosition());
        String bus_id = bus_ids.get(bus_spinner.getSelectedItemPosition());
        String timing = bus_timing.getText().toString();

        createtime data = new createtime(ridd, bus_id,stop_id, timing);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("Time").push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {

            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(Add_Timings.this, "Time Added", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(Add_Timings.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void get_routes() {
        final List<String> routes = new ArrayList<>();
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        data.getReference().child("route").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    createroute details = data.getValue(createroute.class);
                    routes.add(details.from_loc + " - to - " + details.to_loc);
                    details.r_id = data.getKey();

                    route_ids.add(details.r_id);
                }
                ArrayAdapter<String> data1 = new ArrayAdapter<String>(Add_Timings.this, android.R.layout.simple_dropdown_item_1line, routes);
                System.out.println("cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
                route_spinner.setAdapter(data1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void get_stops(final String s) {

        final List<String> stops = new ArrayList<>();
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        data.getReference().child("stop").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    createstop details = data.getValue(createstop.class);

                    details.s_id = data.getKey();
                    if (details.routeidd.equals(s)) {
                        stops.add(details.S_name);
                        stop_ids.add(details.s_id);

                    }
                }
                ArrayAdapter<String> data1 = new ArrayAdapter<String>(Add_Timings.this, android.R.layout.simple_dropdown_item_1line, stops);
                stop_spinner.setAdapter(data1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void get_buses(final String s) {
        final List<String> bus = new ArrayList<>();
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        data.getReference().child("bus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    createbuss details = data.getValue(createbuss.class);

                    details.b_id = data.getKey();
                    if (details.routeidd.equals(s)) {
                        bus.add(details.b_name);
                        bus_ids.add(details.b_id);
                    }

                }
                ArrayAdapter<String> data1 = new ArrayAdapter<String>(Add_Timings.this, android.R.layout.simple_dropdown_item_1line, bus);
                bus_spinner.setAdapter(data1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

package tech.inception.admin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tech.inception.admin.sampledata.createbuss;
import tech.inception.admin.sampledata.createroute;

public class Add_Buses extends AppCompatActivity {

    Spinner  route_spinner;

    List<String> route_ids;
    EditText bus_name , bus_number ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__buses);


        route_spinner = (Spinner)findViewById(R.id.route_spinner);

        bus_name = (EditText) findViewById(R.id.bus_name);
        bus_number = (EditText) findViewById(R.id.bus_number);



        route_ids = new ArrayList<>();

        route_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        get_routes();

    }



    public void get_routes()
    { final List<String> routes = new ArrayList<>();
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        data.getReference().child("route").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    createroute details = data.getValue(createroute.class);
                    routes.add(details.from_loc+" - to - "+details.to_loc);
                    details.r_id=data.getKey();
                    route_ids.add(details.r_id);

                }
                ArrayAdapter<String> data1 = new ArrayAdapter<String>(Add_Buses.this ,android.R.layout.simple_dropdown_item_1line,routes);
                route_spinner.setAdapter(data1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void submit_bus(View view) {

        String name = bus_name.getText().toString();
        String number = bus_number.getText().toString();
        if(name.equals(""))
        {
            Toast.makeText(Add_Buses.this , "please enter bus name" ,Toast.LENGTH_SHORT).show();
            return;
        }

        if(number.equals(""))
        {
            Toast.makeText(Add_Buses.this , "please enter bus number" ,Toast.LENGTH_SHORT).show();
            return;
        }
        String idd= route_ids.get(route_spinner.getSelectedItemPosition());
        createbuss data = new createbuss(idd, number,name);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("bus").push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {

            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {

                    Toast.makeText(Add_Buses.this , "Bus Added" , Toast.LENGTH_SHORT).show();

                }
                else {

                    Toast.makeText(Add_Buses.this , "Error" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

package tech.inception.admin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import tech.inception.admin.sampledata.createstop;

public class Add_Stops extends AppCompatActivity {

    EditText stop_name ;
    Spinner routes ;
    List<String> routes_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__stops);

         routes_ids = new ArrayList<>();

        stop_name = (EditText) findViewById(R.id.stop);
        routes = (Spinner) findViewById(R.id.route_spinner);


        get_routes();
    }

    public void submit_stop(View view) {

        String stop = stop_name.getText().toString();
        if(stop.equals(""))
        {
            Toast.makeText(Add_Stops.this , "please enter stop name" ,Toast.LENGTH_SHORT).show();
            return;
        }
        String idd=routes_ids.get(routes.getSelectedItemPosition());
        createstop data = new createstop(idd,stop);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("buss").child(idd).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {

            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {

                    Toast.makeText(Add_Stops.this , "Stop Added" , Toast.LENGTH_SHORT).show();

                }
                else {

                    Toast.makeText(Add_Stops.this , "Error" , Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void get_routes()
    { final List<String> routes = new ArrayList<>();
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        data.getReference().child("routes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    createroute details = data.getValue(createroute.class);
                    routes.add(details.from_loc+" - to - "+details.to_loc);
                    routes_ids.add(details.idd);

                }
                ArrayAdapter<String> data1 = new ArrayAdapter<String>(Add_Stops.this ,android.R.layout.simple_dropdown_item_1line,routes);
                Add_Stops.this.routes.setAdapter(data1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

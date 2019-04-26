package tech.inception.admin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import tech.inception.admin.sampledata.createroute;

public class Add_Routes extends AppCompatActivity {

    String loc_from , loc_to ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__routes);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("", "Place: " + place.getName());

                loc_from = place.getName().toString();


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("", "An error occurred: " + status);
            }
        });

        PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("", "Place: " + place.getName());

                loc_to = place.getName().toString();


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("", "An error occurred: " + status);
            }
        });
    }

    public void submit_route(View view)

    {
        if(loc_from.equals(""))
        {
            Toast.makeText(Add_Routes.this , "please enter from location" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(loc_to.equals(""))
        {
            Toast.makeText(Add_Routes.this , "please enter to location" , Toast.LENGTH_SHORT).show();
            return;
        }
        String idd=loc_from+loc_to;
        createroute data = new createroute(idd,loc_from,loc_to);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("route").push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {

            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {

                    Toast.makeText(Add_Routes.this , "Routes Added" , Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {

                    Toast.makeText(Add_Routes.this , "Error" , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

package tech.inception.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tech.inception.admin.sampledata.createbuss;
import tech.inception.admin.sampledata.createroute;
import tech.inception.admin.sampledata.createstop;

public class View_stops extends AppCompatActivity {
    ArrayList<createstop> stop_list;
    RecyclerView stop_recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stops);
        stop_list = new ArrayList<>();

        stop_recycler = findViewById(R.id.stop_recycle);

        stop_recycler.setLayoutManager(new LinearLayoutManager(View_stops.this , LinearLayoutManager.VERTICAL, false));
    }


    public void get_stop()
    {

        FirebaseDatabase data =FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("stop").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stop_list.clear();


                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    createstop details = data.getValue(createstop.class);
                    System.out.println("rrrrrr");
                    details.s_id=data.getKey();
                    stop_list.add(details);

                    Adapter adapter = new Adapter();

                    stop_recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        get_stop();
    }

    public class view_holder extends RecyclerView.ViewHolder{

        TextView s_name,floc,tloc;
        Button del;
        public view_holder(View itemView) {
            super(itemView);


            s_name= itemView.findViewById(R.id.stop_id);
            floc= itemView.findViewById(R.id.from_id);
            tloc= itemView.findViewById(R.id.to_id);
            del = itemView.findViewById(R.id.delete_stop);
        }
    }

    public class Adapter extends RecyclerView.Adapter<view_holder>
    {

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {

            view_holder v = new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_cell,parent , false ));

            return v ;
        }

        @Override
        public void onBindViewHolder(view_holder holder, int position) {


            final createstop data=stop_list.get(position);
            holder.s_name.setText(data.S_name);
            holder.floc.setText(from_details(data.routeidd));
            holder.tloc.setText(to_details(data.routeidd));
            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference().child("stop").child(data.s_id);
                    mPostReference.removeValue();
                }
            });
        }

        @Override
        public int getItemCount() {
            return stop_list.size();
        }
    }

    private String from_details(final String routeidd) {
        final String[] floc = new String[1];

        FirebaseDatabase data =FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("route").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    createroute details = data.getValue(createroute.class);
                    System.out.println("rrrrrr");
                    if(details.r_id.equals(routeidd))
                    {
                         floc[0] =details.from_loc;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return floc[0];
    }
    private String to_details(final String routeidd) {
        final String[] tloc = new String[1];

        FirebaseDatabase data =FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("route").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    createroute details = data.getValue(createroute.class);
                    System.out.println("rrrrrr");
                    if(details.r_id.equals(routeidd))
                    {
                        tloc[0] =details.to_loc;

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return tloc[0];
    }
}
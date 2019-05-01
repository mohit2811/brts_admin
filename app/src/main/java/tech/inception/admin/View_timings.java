package tech.inception.admin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tech.inception.admin.sampledata.createbuss;
import tech.inception.admin.sampledata.createroute;
import tech.inception.admin.sampledata.createstop;
import tech.inception.admin.sampledata.createtime;

public class View_timings extends AppCompatActivity {
    ArrayList<createtime> time_list;
    RecyclerView time_recycler;
    String from_, to_, bus_num, stop_;
ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_timings);
        time_list = new ArrayList<>();
pd=new ProgressDialog(this);
pd.setTitle("Wait!!!");
pd.setMessage("Loading!!!!!!");
pd.show();
        time_recycler = findViewById(R.id.time_recycle);

        time_recycler.setLayoutManager(new LinearLayoutManager(View_timings.this, LinearLayoutManager.VERTICAL, false));
    }


    public void get_time() {

        FirebaseDatabase data = FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("Time").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                time_list.clear();
                pd.hide();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    createtime details = data.getValue(createtime.class);
                    System.out.println("rrrcccccccccccccccccccccccccccccccccccccccccccrrr");
                    details.t_id = data.getKey();
                    time_list.add(details);
                    Adapter adapter = new Adapter();
                    time_recycler.setAdapter(adapter);
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

        get_time();
    }

    public class view_holder extends RecyclerView.ViewHolder {

        Button del;
        TextView bid, from_id, to_id, stop_id, time_id;

        public view_holder(View itemView) {
            super(itemView);

            bid = itemView.findViewById(R.id.bus_id);
            from_id = itemView.findViewById(R.id.from_id);
            to_id = itemView.findViewById(R.id.to_id);
            stop_id = itemView.findViewById(R.id.stop_id);
            time_id = itemView.findViewById(R.id.timing_id);
            del = itemView.findViewById(R.id.delete_time);
        }
    }

    public class Adapter extends RecyclerView.Adapter<view_holder> {

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {

            view_holder v = new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.timing_cell, parent, false));

            return v;
        }

        @Override
        public void onBindViewHolder(view_holder holder, int position) {


            final createtime data = time_list.get(position);
            get_routes(data.routeidd,holder.from_id,holder.to_id);
            get_bus(data.bus_id,holder.bid);
            get_stop(data.stop_id,holder.stop_id);

            holder.time_id.setText(data.time);
            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference().child("time").child(data.t_id);
                    mPostReference.removeValue();
                }
            });
        }

        @Override
        public int getItemCount() {
            return time_list.size();
        }
    }

    private void get_bus(String bus_id, final TextView bid) {
        System.out.println(bus_id+"vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        data.getReference().child("bus").child(bus_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                createbuss details = dataSnapshot.getValue(createbuss.class);
                bid.setText(details.b_num);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void get_stop(final String stop_id, final TextView stopId) {
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        data.getReference().child("stop").child(stop_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                createstop details = dataSnapshot.getValue(createstop.class);
                stopId.setText(details.S_name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void get_routes(String routeidd, final TextView floc, final TextView tloc) {
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        data.getReference().child("route").child(routeidd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                createroute details=dataSnapshot.getValue(createroute.class);
                floc.setText(details.from_loc);
                tloc.setText(details.to_loc);

                System.out.println(from_+to_+"mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
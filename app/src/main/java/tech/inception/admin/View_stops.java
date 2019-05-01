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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tech.inception.admin.sampledata.createroute;
import tech.inception.admin.sampledata.createstop;

public class View_stops extends AppCompatActivity {
    ArrayList<createstop> stop_list;
    RecyclerView stop_recycler;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stops);
        stop_list = new ArrayList<>();
        pd=new ProgressDialog(this);
        pd.setTitle("Wait!!!");
        pd.setMessage("Loading!!!!!!");
        pd.show();
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
pd.hide();

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
            get_routes(data.routeidd,holder.floc,holder.tloc);
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
    private void get_routes(String routeidd, final TextView floc, final TextView tloc) {
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        data.getReference().child("route").child(routeidd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                createroute details=dataSnapshot.getValue(createroute.class);
                floc.setText(details.from_loc);
                tloc.setText(details.to_loc);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }
}
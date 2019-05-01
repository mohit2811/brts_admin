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

import tech.inception.admin.sampledata.createbuss;
import tech.inception.admin.sampledata.createroute;

public class View_buses extends AppCompatActivity {
    ArrayList<createbuss> bus_list;
    RecyclerView bus_recycler;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_buses);
        bus_list = new ArrayList<>();
        pd=new ProgressDialog(this);
        pd.setTitle("Wait!!!");
        pd.setMessage("Loading!!!!!!");
        pd.show();
        bus_recycler = findViewById(R.id.bus_recycle);

        bus_recycler.setLayoutManager(new LinearLayoutManager(View_buses.this , LinearLayoutManager.VERTICAL, false));
    }


    public void get_busses()
    {
        FirebaseDatabase data =FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("bus").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bus_list.clear();

pd.hide();
                for (DataSnapshot data : dataSnapshot.getChildren())
                {

                    createbuss details = data.getValue(createbuss.class);
                    System.out.println("rrrrrr");
                    details.b_id=data.getKey();
                    bus_list.add(details);

                    Adapter adapter = new Adapter();

                    bus_recycler.setAdapter(adapter);
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

        get_busses();
    }

    public class view_holder extends RecyclerView.ViewHolder{

        TextView bus_name,bus_num,from,to ;
        Button del;
        public view_holder(View itemView) {
            super(itemView);

            bus_name = itemView.findViewById(R.id.bus_name);
            bus_num = itemView.findViewById(R.id.bus_number);
            from = itemView.findViewById(R.id.from_id_bus);
            to = itemView.findViewById(R.id.to_id);
            del = itemView.findViewById(R.id.delete_bus);
        }
    }

    public class Adapter extends RecyclerView.Adapter<view_holder>
    {

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {

            view_holder v = new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_cell,parent , false ));

            return v ;
        }

        @Override
        public void onBindViewHolder(view_holder holder, final int position) {


            final createbuss data=bus_list.get(position);
            holder.bus_name.setText(data.b_name);
            holder.bus_num.setText(data.b_num);
            get_routes(data.routeidd,holder.from,holder.to);
            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference().child("bus").child(data.b_id);
                    mPostReference.removeValue();
                }
            });
        }

        @Override
        public int getItemCount() {
            return bus_list.size();
        }
    }

    private void get_routes(String routeidd, final TextView floc, final TextView tloc) {
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        System.out.println(routeidd+"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        data.getReference().child("route").child(routeidd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
                createroute details=dataSnapshot.getValue(createroute.class);
                floc.setText(details.from_loc);
                tloc.setText(details.to_loc);
                System.out.println(details.from_loc+"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
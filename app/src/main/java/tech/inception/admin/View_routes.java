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

public class View_routes extends AppCompatActivity {
    ArrayList<createroute> route_list;
    RecyclerView route_recycler;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_routes);
        route_list = new ArrayList<>();
        pd=new ProgressDialog(this);
        pd.setTitle("Wait!!!");
        pd.setMessage("Loading!!!!!!");
        pd.show();
        route_recycler = findViewById(R.id.route_recycle);

        route_recycler.setLayoutManager(new LinearLayoutManager(View_routes.this , LinearLayoutManager.VERTICAL, false));
    }


    public void get_route()
    {

        FirebaseDatabase data =FirebaseDatabase.getInstance();
        System.out.println("rrrr");
        data.getReference().child("route").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                route_list.clear();
pd.hide();

                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    createroute details = data.getValue(createroute.class);
                    System.out.println("rrrrrr");
                    details.r_id=data.getKey();
                    route_list.add(details);

                    Adapter adapter = new Adapter();

                    route_recycler.setAdapter(adapter);
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

        get_route();
    }

    public class view_holder extends RecyclerView.ViewHolder{

        TextView route_id,from_id,to_id ;
        Button del;
        public view_holder(View itemView) {
            super(itemView);

            route_id = itemView.findViewById(R.id.route_id);
            from_id = itemView.findViewById(R.id.from_id);
            to_id = itemView.findViewById(R.id.to_id);
            del = itemView.findViewById(R.id.delete_route);
        }
    }

    public class Adapter extends RecyclerView.Adapter<view_holder>
    {

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {

            view_holder v = new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.routes_cell,parent , false ));

            return v ;
        }

        @Override
        public void onBindViewHolder(view_holder holder, int position) {


            final createroute data=route_list.get(position);
            holder.route_id.setText(data.idd);
            holder.from_id.setText(data.from_loc);
            holder.to_id.setText(data.to_loc);
            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference().child("route").child(data.r_id);
                    mPostReference.removeValue();
                }
            });
        }

        @Override
        public int getItemCount() {
            return route_list.size();
        }
    }
}
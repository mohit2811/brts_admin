package tech.inception.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Routes_options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_options);
    }

    public void add_new_route(View view) {

        Intent i = new Intent(Routes_options.this , Add_Routes.class);
        startActivity(i);
    }

    public void view_added_routes(View view) {

        Intent i = new Intent(Routes_options.this , View_routes.class);
        startActivity(i);
    }
}

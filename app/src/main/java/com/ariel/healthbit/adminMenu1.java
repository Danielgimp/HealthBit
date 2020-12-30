package com.ariel.healthbit;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class adminMenu1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu1);
        Toolbar toolbar = findViewById(R.id.upload);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button update = (Button) findViewById(R.id.updateP); //logout, and move to the login activity
        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(getApplicationContext(), updateProducts.class);
                startActivity(myIntent);
            }
        });


        Button newPro1 = (Button) findViewById(R.id.newPro2); //logout, and move to the login activity
        newPro1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(getApplicationContext(), newProducts.class);
                startActivity(myIntent);
            }
        });

        Button ordersButton = (Button) findViewById(R.id.orders); //logout, and move to the login activity
        ordersButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(getApplicationContext(), activityOrders.class);
                startActivity(myIntent);
            }
        });





    }

}
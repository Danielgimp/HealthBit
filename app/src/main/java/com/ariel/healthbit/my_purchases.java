package com.ariel.healthbit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class my_purchases extends AppCompatActivity {
    DatabaseReference refOrders;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<Order> allUserOrders=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchases);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbalWEIGHTTRACKER);
        setSupportActionBar(toolbar);

        refOrders = FirebaseDatabase.getInstance().getReference("Orders");
        refOrders.addListenerForSingleValueEvent( new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot post : dataSnapshot.getChildren() ){
                    Order currOrder=new Order(post.getValue(Order.class));
                    if(currOrder.getUserUID().equals(currentFirebaseUser.getUid()))
                    {
                        allUserOrders.add(currOrder);
                    }
                }
                int a=0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DecimalFormat;
import java.util.Random;

public class myprofile extends AppCompatActivity
{
    private double calculateBmi(double weight, double height) //calculate the BMI by weight&height
    {
        double bmiResult = weight / Math.pow(height/100, 2);
        return bmiResult;
    }
    Toolbar toolbar;
    TextView fullname,datebirth,height,email,bmi,weight,phone;
    Button update;
    DatabaseReference refUser,refDetails;
    FirebaseAuth fb;
    FirebaseUser user;
    private static final Random RANDOM = new Random();
    private int lastX = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null)
        {
            Intent myIntent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(myIntent);
        }
        // definition the references to db.
        refUser= FirebaseDatabase.getInstance().getReference("users").child(fb.getInstance().getUid());
        refDetails= FirebaseDatabase.getInstance().getReference("users").child(fb.getInstance().getUid()).child("details");
        //initialize the xml objects
        fullname=(TextView)findViewById(R.id.myprofile_fullname);
        datebirth=(TextView)findViewById(R.id.myprofile_date1);
        email=(TextView)findViewById(R.id.activity_orders_uid);
        height=(TextView)findViewById(R.id.activity_orders_products);
        bmi=(TextView)findViewById(R.id.myprofile_BMI);
        weight=(TextView)findViewById(R.id.activity_orders_price);
        update=(Button) findViewById(R.id.updateprofile_update);
        phone=(TextView)findViewById(R.id.myprofile_phone) ;
        toolbar=(Toolbar)findViewById(R.id.toolbarMYPROFILE);
        setSupportActionBar(toolbar);
        //read user from db
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                if(user!=null) {
                    fullname.setText(user.name + " " + user.lname);
                    email.setText(user.email);
                    phone.setText(user.phone);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        };
        refUser.addValueEventListener(postListener);
        //read details from db
        ValueEventListener DetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Details det=dataSnapshot.getValue(Details.class);
                if(det!=null) {
                    double h = det.height;
                    double w = det.weight;
                    double BMIcalc = calculateBmi(w, h);
                    DecimalFormat df2 = new DecimalFormat("#.##");
                    bmi.setText(String.valueOf("my current \n BMI is \n \n" + df2.format(BMIcalc)));
                    datebirth.setText(det.date);
                    height.setText("" + h);
                    weight.setText("" + w);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        };
        refDetails.addValueEventListener(DetailsListener);

        toolbar = (Toolbar) findViewById(R.id.toolbarMYPROFILE);
        setSupportActionBar(toolbar);

        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), updateprofile.class);
                startActivity(myIntent);
                finish();
            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) //toolbar definition
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public void onBackPressed()
    {
        Intent myIntent = new Intent(getApplicationContext(), MainProfile.class);
        startActivity(myIntent);
    }

}
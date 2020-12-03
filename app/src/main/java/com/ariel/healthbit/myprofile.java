package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ariel.healthbit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class myprofile extends AppCompatActivity
{
    private double calculateBmi(double weight, double height) //calculate the BMI by weight&height
    {
        double bmiResult = weight / Math.pow(height/100, 2);
        return bmiResult;
    }
    Toolbar toolbar;
    TextView fullname,datebirth,height,email,bmi;
    DatabaseReference refUser,refDetails;
    FirebaseAuth fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        refUser= FirebaseDatabase.getInstance().getReference("users").child(fb.getInstance().getUid());
        refDetails= FirebaseDatabase.getInstance().getReference("users").child(fb.getInstance().getUid()).child("details");

        fullname=(TextView)findViewById(R.id.myprofile_fullname);
        datebirth=(TextView)findViewById(R.id.myprofile_date);
        email=(TextView)findViewById(R.id.myprofile_mail);
        height=(TextView)findViewById(R.id.myprofile_height);
        bmi=(TextView)findViewById(R.id.myprofile_BMI);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User post = dataSnapshot.getValue(User.class);
                fullname.setText("  Full Name: \n"+post.name+" "+post.lname);
                email.setText("  Email: \n"+post.email);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        };
        refUser.addValueEventListener(postListener);

        ValueEventListener DetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Details det=dataSnapshot.getValue(Details.class);
                double h=det.height;
                double w=det.weight;
                double BMIcalc=calculateBmi(w,h);
                DecimalFormat df2 = new DecimalFormat("#.##");
               bmi.setText(String.valueOf("my current \n BMI is \n \n" +df2.format(BMIcalc)));
                datebirth.setText("Birth Date: \n"+det.date);
                height.setText("Height: \n"+det.height);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        refDetails.addValueEventListener(DetailsListener);

        toolbar = (Toolbar) findViewById(R.id.toolbarMYPROFILE);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) //toolbar definition
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
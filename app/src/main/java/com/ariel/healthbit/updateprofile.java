package com.ariel.healthbit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class updateprofile extends AppCompatActivity
{
    private double calculateBmi(double weight, double height) //calculate the BMI by weight&height
    {
        double bmiResult = weight / Math.pow(height/100, 2);
        return bmiResult;
    }
    TextView email,bmi;
    EditText weight,height,number,name,lname,phone;
    Button update;
    Toolbar toolbar;
    DatabaseReference refUser,refDetails;
    FirebaseAuth auth;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        // definition the references to db.
        refUser= FirebaseDatabase.getInstance().getReference("users").child(auth.getInstance().getUid());
        refDetails= FirebaseDatabase.getInstance().getReference("users").child(auth.getInstance().getUid()).child("details");
        //initialize the xml objects
        bmi=(TextView)findViewById(R.id.updateprofile_BMI);
        email=(TextView)findViewById(R.id.activity_orders_uid);
        name=(EditText)findViewById(R.id.update_name);
        lname=(EditText)findViewById(R.id.update_lastname);
        phone=(EditText)findViewById(R.id.update_phone);
        weight=(EditText)findViewById(R.id.update_weight);
        height=(EditText)findViewById(R.id.update_height);
        update=(Button)findViewById(R.id.updateprofile_update);
        toolbar = (Toolbar) findViewById(R.id.toolbarUpdate);
        setSupportActionBar(toolbar);

        //set as hint the current data (User)
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                email.setText(user.email);
                name.setHint(user.name);
                lname.setHint(user.lname);
                phone.setHint(user.phone);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        };
        refUser.addValueEventListener(postListener);

        //set as hint the current data (Details)
        ValueEventListener DetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Details det=dataSnapshot.getValue(Details.class);
                    double h = det.height;
                    double w = det.weight;
                double BMIcalc = calculateBmi(w, h);
                DecimalFormat df2 = new DecimalFormat("#.##");
                bmi.setText(String.valueOf("my current \n BMI is \n \n" + df2.format(BMIcalc)));
                    height.setHint(""+h);
                    weight.setHint(""+w);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        refDetails.addValueEventListener(DetailsListener);

        //update button
        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Map<String, Object> childDetails = new HashMap<>();
                Map<String, Object> childUsers = new HashMap<>();
                double h=0,w=0;
                //converts the inputs to strings.
                String TextName=name.getText().toString();
                String TextLName=lname.getText().toString();
                String TextPhone=phone.getText().toString();
                String Textweight=weight.getText().toString();
                String Textheight=height.getText().toString();

                //name and lname checks
                if (TextName.length()==1)
                {
                    name.setError("invalid name");
                    return;
                }
                if (TextLName.length()==1)
                {
                    lname.setError("invalid last name");
                    return;
                }
                //weight checks
                if(!Textweight.isEmpty())
                {
                    w=Double.parseDouble(Textweight);
                    if(w<30||w>400)
                    {
                        weight.setError("invalid value");
                        return;
                    }
                }
                //height checks
                if(!Textheight.isEmpty())
                {
                    h=Double.parseDouble(Textheight);
                    if(h<3)
                    {
                        height.setError("use centimeters");
                        return;
                    }
                    if(h<55||h>250)
                    {
                        height.setError("invalid value");
                        return;
                    }

                }
                //checks phone validity
                if(TextPhone.length()<=5&&TextPhone.length()>0)
                {
                        phone.setError("phone is invalid!");
                        return;
                }

                //updates the non-empty data
                if(!TextName.isEmpty())
                {
                    childUsers.put("/name",TextName);
                }
                if(!TextLName.isEmpty())
                {
                    childUsers.put("/lname",TextLName);
                }
                if(!TextPhone.isEmpty())
                {
                    childUsers.put("/phone",TextPhone);
                }
                if(!Textweight.isEmpty())
                {
                    childDetails.put("/weight" ,w);
                }
                if(!Textheight.isEmpty())
                {
                    childDetails.put("/height" ,h);
                }
                refDetails.updateChildren(childDetails);
                refUser.updateChildren(childUsers);

                //back to myprofile.xml //!!!!!!!!!!!!!!!
                Intent myIntent = new Intent(getApplicationContext(), myprofile.class);
                startActivity(myIntent);
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
        Intent myIntent = new Intent(getApplicationContext(), myprofile.class);
        startActivity(myIntent);
    }
}
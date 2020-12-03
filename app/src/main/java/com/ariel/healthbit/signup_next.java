package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class signup_next extends AppCompatActivity
{
    Toolbar toolbar;
    Button register;
    EditText height,weight;
    RadioButton gend,male;
    RadioGroup genderR;
    DatePicker birthdate;
    TextView birthdate_textview;
    ProgressBar prog;
    FirebaseAuth aut;
     @Override
    protected void onCreate(Bundle savedInstanceState)
     {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_next);

        toolbar = (Toolbar) findViewById(R.id.toolbarNextStep);
        setSupportActionBar(toolbar);


        //initialize values
         height=(EditText)findViewById(R.id.nextstep_height);
         weight=(EditText)findViewById(R.id.nextstep_weight);
         birthdate=(DatePicker)findViewById(R.id.nextstep_birthdate);
         genderR=(RadioGroup) findViewById(R.id.nextstep_radioGroup);
         birthdate_textview=(TextView)findViewById(R.id.nextstep_birth);

         //final register button
        register = (Button) findViewById(R.id.nextstep_register);
        register.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                int year,month,day; //birth date initialize
                year=birthdate.getYear();
                month=birthdate.getMonth()+1;
                day=birthdate.getDayOfMonth();
                prog=(ProgressBar)findViewById(R.id.nextstep_prog);
                //check if the date are makes sense.(the age of the user is realible)
               // if (year<=Calendar.getInstance().get(Calendar.YEAR)&&year>=Calendar.getInstance().get(Calendar.YEAR)-5)
               // {
                //    birthdate_textview.setError("select birth date");
                  //  return;
               // }
                //check validity of the height value.
                String Textheight=height.getText().toString();
                double h;
                if(Textheight.isEmpty())
                {
                   height.setError("height is required!");
                    return;
                }
                else
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
                //check validity of weight value
                String Textweight=weight.getText().toString();
                double w;
                if(Textweight.isEmpty())
                {
                    weight.setError("weight is required!");
                    return;
                }
                else
                {
                    w=Double.parseDouble(Textweight);
                    if(w<30||w>400)
                    {
                        weight.setError("invalid value");
                        return;
                    }
                }

                String gender=" ";
                int select=genderR.getCheckedRadioButtonId();
                gend=(RadioButton)findViewById(select);
                male= (RadioButton) findViewById(R.id.nextstep_male);

                if (gend==null)
                {
                    male.setError("select gender!");
                    return;
                }
                else
                {
                    gender=gend.getText().toString();
                }
                prog.setVisibility(View.VISIBLE);
                //create Detail's object and add it to db
                Details d=new Details(h, w, new Date(year-1900, month-1, day), gender);
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");
                ref.child(aut.getInstance().getUid()).child("details").setValue(d);
                prog.setVisibility(View.GONE);
                //open main menu
                Intent myIntent = new Intent(getApplicationContext(), MainProfile.class);
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
}
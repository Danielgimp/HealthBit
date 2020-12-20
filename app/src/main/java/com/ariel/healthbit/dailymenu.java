package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.ariel.healthbit.R;

public class dailymenu extends AppCompatActivity
{
   Toolbar toolbar;
    public static int type = 1;
    Button back;
    Button btn1, btn2, btn3, btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailymenu);

        toolbar = (Toolbar) findViewById(R.id.toolbarDAILYMENU);
        setSupportActionBar(toolbar);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);

        btn1.setOnClickListener(new View.OnClickListener()  //back to main menu
        {
            public void onClick(View view) {
                dailymenu.type = 1;
                Intent myIntent = new Intent(getApplicationContext(), list_menu_activity.class);
                startActivity(myIntent);
            }

        });

        btn2.setOnClickListener(new View.OnClickListener()  //back to main menu
        {
            public void onClick(View view) {
                dailymenu.type = 2;
                Intent myIntent = new Intent(getApplicationContext(), list_menu_activity.class);
                startActivity(myIntent);
            }

        });

        btn3.setOnClickListener(new View.OnClickListener()  //back to main menu
        {
            public void onClick(View view) {
                dailymenu.type = 3;
                Intent myIntent = new Intent(getApplicationContext(), list_menu_activity.class);
                startActivity(myIntent);
            }

        });

        btn4.setOnClickListener(new View.OnClickListener()  //back to main menu
        {
            public void onClick(View view) {
                dailymenu.type = 4;
                Intent myIntent = new Intent(getApplicationContext(), list_menu_activity.class);
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
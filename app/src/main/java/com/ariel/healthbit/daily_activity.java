package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.ariel.healthbit.R;

public class daily_activity extends AppCompatActivity
{
   Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbarDAILYACTIVITY);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)//toolbar definition
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
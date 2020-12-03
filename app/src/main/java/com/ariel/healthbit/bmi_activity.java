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
import android.widget.SeekBar;
import android.widget.TextView;

import com.ariel.healthbit.R;

import java.text.DecimalFormat;

public class bmi_activity extends AppCompatActivity
{
    private double calculateBmi(double weight, double height) //calculate the BMI by weight&height
    {
        double bmiResult = weight / Math.pow(height/100, 2);
        return bmiResult;
    }

    private String BMImeaning(double bmiValue) //return the meaning of the BMI result.
    {

        if (bmiValue < 16)
        {
            return "severely underweight";
        }
        else if (bmiValue < 18.5)
        {

            return "underweight";
        }
        else if (bmiValue < 25)
        {

            return "normal";
        }
        else if (bmiValue < 30)
        {
            return "overweight";
        }
        else
            return "Obese";
    }
    Toolbar toolbar;
    Button BMI;
    SeekBar heightS,weightS;
    TextView cm,kg;

    @RequiresApi(api = Build.VERSION_CODES.O) //required when setting a minimum at the seekbar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbarBMI);
        setSupportActionBar(toolbar);

        heightS=(SeekBar)findViewById(R.id.seekbar_height);
        heightS.setMax(240); //define min and max value.
        heightS.setMin(40);
        cm=(TextView)findViewById(R.id.heightT);
        heightS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
               int height = seekBar.getProgress();
               cm.setText(String.valueOf(height)); //sets the current value while using the seekbar.
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
               int height = seekBar.getProgress();
                cm.setText(String.valueOf(height));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) //set the final value
            {
               int height = seekBar.getProgress();
                cm.setText(String.valueOf(height));
            }
        });

        weightS=(SeekBar)findViewById(R.id.seekbar_weight);
        weightS.setMax(150);
        weightS.setMin(10);
        kg=(TextView)findViewById(R.id.weightT);
        weightS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) //set the current value while using the seekbar
            {
                int weight = seekBar.getProgress();
                kg.setText(String.valueOf(weight));
                // bmiResultTextView.setText(String.valueOf(calculateBmi(weight, height)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                int weight = seekBar.getProgress();
                kg.setText(String.valueOf(weight));
                //bmiResultTextView.setText(String.valueOf(calculateBmi(weight, height)));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int weight = seekBar.getProgress();
                kg.setText(String.valueOf(weight));
                //  bmiResultTextView.setText(String.valueOf(calculateBmi(weight, height)));
            }
        });

        BMI = (Button) findViewById(R.id.bmi_calc); //calculate the result BMI by the values which defined at the seekbar.
        BMI.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                DecimalFormat df2 = new DecimalFormat("#.##");
                double weight=Double.parseDouble(kg.getText().toString());
                double height=Double.parseDouble(cm.getText().toString());
                double bmiValue=calculateBmi(weight,height);
                TextView result = (TextView) findViewById(R.id.result);
                result.setText(String.valueOf(df2.format(bmiValue) + " - " + BMImeaning(bmiValue)));
            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) //tool bar definition
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
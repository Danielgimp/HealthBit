package com.ariel.healthbit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class weighttracker extends AppCompatActivity
{
    Button addweight,openGraph;
    TextView  currentweight;
    GraphView graphview;
    Toolbar toolbar;
    DatabaseReference refWeight;
    DatabaseReference refWeights;
    FirebaseAuth auth;
    FirebaseUser user;
    EditText updateWeight;
    ArrayList<Long> td1=new ArrayList<>();

    double w=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weighttracker);
        toolbar = (Toolbar) findViewById(R.id.toolbalWEIGHTTRACKER);
        setSupportActionBar(toolbar);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null)
        {
            Intent myIntent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(myIntent);
        }

        //db reference
        refWeight = FirebaseDatabase.getInstance().getReference("users").child(auth.getInstance().getUid()).child("details");
        refWeights = FirebaseDatabase.getInstance().getReference("users").child(auth.getInstance().getUid()).child("details").child("weights");
        //initialize objects
        graphview = (GraphView) findViewById(R.id.weightTracker_graph);
        currentweight = (TextView) findViewById(R.id.weighttracker_text);
        addweight = (Button) findViewById(R.id.weighttracker_addweight);
        openGraph = (Button) findViewById(R.id.weighttracker_openGraph);
        updateWeight = (EditText) findViewById(R.id.weighttracker_updateweight);

        //open and close graph's button
        openGraph.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (graphview.getVisibility() == View.INVISIBLE) {
                    graphview.setVisibility(View.VISIBLE);
                }
                else
                    {
                    graphview.setVisibility(View.INVISIBLE);
                }
            }

        });

        ValueEventListener WeightsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //read list of weights from db
                ArrayList<Long> td = (ArrayList<Long>) dataSnapshot.getValue();
                if(td!=null)
                {
                    //add new weight to db-update also in the graph.
                    addweight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            //update list by  adding new weight.
                            Map<String, Object> childDetails = new HashMap<>();
                            String Textweight = updateWeight.getText().toString();
                            if (Textweight.isEmpty()) //check empty EditText
                            {
                                updateWeight.setError("weight is required!");
                                return;
                            } else { //check validity of weight
                                w = Double.parseDouble(Textweight); //covert to double view
                                if (w < 30 || w > 400) {
                                    updateWeight.setError("invalid value");
                                    return;
                                }
                            }
                            String pattern = "yyyy-MM-dd";
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                            String currentDate = simpleDateFormat.format(new Date()); //writes current date as a simple format.
                            //add new weight to db by date as key.
                            childDetails.put("weight",w);
                            refWeights = FirebaseDatabase.getInstance().getReference("users").child(auth.getInstance().getCurrentUser().getUid()).child("details").child("weights");
                            refWeight = FirebaseDatabase.getInstance().getReference("users").child(auth.getInstance().getCurrentUser().getUid()).child("details");

                            for (int i = 0; i < td.size(); i++) {
                                long weight = td.get(i);
                                td1.add(weight);
                            }
                            long wL=(new Double(w)).longValue();
                            td1.add(wL);
                            childDetails.put("weights",td1);
                            refWeight = FirebaseDatabase.getInstance().getReference("users").child(auth.getInstance().getCurrentUser().getUid()).child("details");
                            refWeight.updateChildren(childDetails);
                            //update td list as empty list.
                            td1=new ArrayList<>();

                        }

                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }

        };
        refWeights.addValueEventListener(WeightsListener);

        //show current weight in TextView. (read from db).
        ValueEventListener DetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Details det = dataSnapshot.getValue(Details.class);
                if(det!=null)
                {
                    //shoe current weight
                    double w = det.weight;
                    currentweight.setText("My Current Weight: \n" + det.weight);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        refWeight.addValueEventListener(DetailsListener);

        refWeights = FirebaseDatabase.getInstance().getReference("users").child(auth.getInstance().getCurrentUser().getUid()).child("details").child("weights");

         ValueEventListener WeightsListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                ArrayList<Long> td = (ArrayList<Long>) dataSnapshot.getValue();
                if(td!=null) {
                    //ArrayList<Long> values = new ArrayList<>(td.values());
                    DataPoint[] dp = new DataPoint[td.size()];
                    for (int i = 0; i < td.size(); i++) {
                        long weight = td.get(i);
                        DataPoint data = new DataPoint(i, weight);
                        dp[i] = data;
                    }
                    LineGraphSeries<DataPoint> bgseries = new LineGraphSeries<>(dp);
                    graphview.addSeries(bgseries);
                    graphview.getViewport().setMinY(0);
                    graphview.setBackgroundColor(getResources().getColor(R.color.button));
                    graphview.getViewport().setScalable(true);
                    graphview.getViewport().setScrollable(true);
                    graphview.setTitle("Weight Tracker");
                    graphview.setTitleColor(getResources().getColor(android.R.color.white));
                    // legend
                    bgseries.setThickness(15);
                    bgseries.setDrawBackground(true);
                    bgseries.setBackgroundColor(R.color.button);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }

        };
        refWeights.addValueEventListener(WeightsListener1);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


}
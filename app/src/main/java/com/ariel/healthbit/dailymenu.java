package com.ariel.healthbit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ariel.healthbit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class dailymenu extends AppCompatActivity
{
   Toolbar toolbar;
    public static int type = 1;
    TextView counter;
    Button back;
    Button btn1, btn2, btn3, btn4;

    public static int count_all = 0;
    public Product xP = null;
    public String dP  = null;
    public FirebaseDatabase database;
    public DatabaseReference ref;
    public Query event_of_today;
    public ArrayList<String> product_keys = new ArrayList<>();
    public ArrayList<Product> product_values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailymenu);

        toolbar = (Toolbar) findViewById(R.id.toolbarDAILYMENU);
        setSupportActionBar(toolbar);

        counter = (TextView) findViewById(R.id.textView3);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);

        btn1.setOnClickListener(new View.OnClickListener()  //back to main menu
        {
            public void onClick(View view) {
                dailymenu.type = 1;
                count_all = 0;
                Intent myIntent = new Intent(getApplicationContext(), list_menu_activity.class);
                startActivity(myIntent);
            }

        });

        btn2.setOnClickListener(new View.OnClickListener()  //back to main menu
        {
            public void onClick(View view) {
                dailymenu.type = 2;
                count_all = 0;
                Intent myIntent = new Intent(getApplicationContext(), list_menu_activity.class);
                startActivity(myIntent);
            }

        });

        btn3.setOnClickListener(new View.OnClickListener()  //back to main menu
        {
            public void onClick(View view) {
                dailymenu.type = 3;
                count_all = 0;
                Intent myIntent = new Intent(getApplicationContext(), list_menu_activity.class);
                startActivity(myIntent);
            }

        });

        btn4.setOnClickListener(new View.OnClickListener()  //back to main menu
        {
            public void onClick(View view) {
                dailymenu.type = 4;
                count_all = 0;
                Intent myIntent = new Intent(getApplicationContext(), list_menu_activity.class);
                startActivity(myIntent);
            }

        });

        database = FirebaseDatabase.getInstance();


        try {
            get_all_products();
            Thread.sleep(1000);
            //update_calorie();
            update_checker();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void update_checker() {
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

// This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (dailymenu.count_all == 0) {
                    update_calorie();
                }
            }
        }, 0, 15, TimeUnit.SECONDS);
    }

    public void update_calorie() {
        ref = database.getReference("ProductsEvents/"+ list_menu_activity.get_uid());
        event_of_today = ref.orderByChild("start").equalTo(list_menu_activity.get_today_date());
        event_of_today.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    count_all = 0;
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        ProductEvent pe = ds.getValue(ProductEvent.class);
                        //Log.d("pe_id", pe.getProductID() + "dsa");
                        Product p = get_product_by_id(pe.getProductID());
                        if (p != null) {
                            Log.d("Here99999", "OK");
                            count_all = count_all + (p.getCal() * pe.getCount());
                            counter.setText(String.valueOf(count_all) + " Calorie");
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        counter.setText("0 Calorie");



    }

    public Product get_product_by_id(String id) {
        Log.d("counter_all", product_keys.size()+"-a");
        int counter = 0;
        if (id != null ) {
            for (String p_s : product_keys) {
                Log.d("Here11", "OK");
                if (p_s != null ) {
                    if (p_s.contentEquals(id)) {
                        Log.d("Here11_name", p_s);
                        xP = product_values.get(counter);
                        dP = p_s;
                        return xP;
                    }
                }
                counter++;
                Log.d("Here12", "OK");
            }
            Log.d("Here13", "OK");
        }

        return null;
    }

    public void get_all_products() {
        database.getReference("ProductsAllUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Product p = ds.getValue(Product.class);
                    product_keys.add(ds.getKey());
                    product_values.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.getReference("ProductsUsers/"+ list_menu_activity.get_uid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Product p = ds.getValue(Product.class);
                    Log.d("CAL", "C="+p.getCal());
                    product_keys.add(ds.getKey());
                    product_values.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
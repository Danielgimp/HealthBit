package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class store extends AppCompatActivity {

    storeProduct product1,product2,product3,product4,product5,product6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSTORE);
        setSupportActionBar(toolbar);

        //add all products from "Store" on Firebase to an array list
        DatabaseReference productsRefernce= FirebaseDatabase.getInstance().getReference("Store");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map <String,storeProduct> td= (Map <String,storeProduct>) dataSnapshot.getValue();
                ArrayList<storeProduct> data=new ArrayList<storeProduct>();
                storeProduct product;
                for (Entry<String, storeProduct> entry : td.entrySet()) {
                    HashMap nana=entry.getValue();
                    Long LunitsInStock= (Long) nana.get("unitsInStock");
                    int nitsInStock=LunitsInStock.intValue();
                    Long Lkcal=(Long) nana.get("kcal");
                    int kcal=Lkcal.intValue();
                    Long Lprice=(Long) nana.get("price");
                    double price=Lprice.doubleValue();
                    String name= (String) nana.get("name");
                    String subType= (String) nana.get("subType");
                    product=new storeProduct(name,kcal,price,subType);
                    data.add(product);

                }
                int productsAmount=data.size();
                final LinearLayout lm = (LinearLayout) findViewById(R.id.linearLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                for(int i=0;i<productsAmount;i++)
                {

                    LinearLayout ll = new LinearLayout(getBaseContext());
                    ll.setOrientation(LinearLayout.VERTICAL);

                    TextView productName = new TextView(getBaseContext());
                    productName.setText("Product name: "+data.get(i).getName());
                    ll.addView(productName);

                    TextView kcal = new TextView(getBaseContext());
                    kcal.setText("kCal: "+data.get(i).getKcal());
                    ll.addView(kcal);

                    TextView price = new TextView(getBaseContext());
                    price.setText("Price is: "+data.get(i).getPrice() + " ILS");
                    ll.addView(price);

                    TextView subType = new TextView(getBaseContext());
                    subType.setText("Description: "+data.get(i).getSubType());
                    ll.addView(subType);

                    // Create Button
                    final Button btn = new Button(getBaseContext());
                    // Give button an ID
                    btn.setId(i+1);
                    btn.setText("Add To Cart");
                    // set the layoutParams on the button
                    btn.setLayoutParams(params);
                    //Add button to LinearLayout
                    ll.addView(btn);
                    //Add button to LinearLayout defined in XML
                    lm.addView(ll);
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        };
        productsRefernce.addValueEventListener(postListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
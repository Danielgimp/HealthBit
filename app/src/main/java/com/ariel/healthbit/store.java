package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    storeProduct product1,product2,product3;
    Button gotoCart;
    ArrayList<String> productUID=new ArrayList<String>();
    Order currOrder =new Order("1");
    FirebaseAuth ref=FirebaseAuth.getInstance();
    DatabaseReference refUser,refOrders;
    FirebaseAuth fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSTORE);
        setSupportActionBar(toolbar);

        //add all products from "Store" on Firebase to an array list
        DatabaseReference productsRefernce= FirebaseDatabase.getInstance().getReference("products");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map <String,storeProduct> td= (Map <String,storeProduct>) dataSnapshot.getValue();
                ArrayList<storeProduct>data=new ArrayList<storeProduct>();
                storeProduct product;
                for (Entry <String, storeProduct> entry : td.entrySet()) {
                    HashMap nana=entry.getValue();
                    Long LunitsInStock= (Long) nana.get("inStock");
                    int nitsInStock=LunitsInStock.intValue();
                    Long Lkcal=(Long) nana.get("Kcal");
                    int kcal=Lkcal.intValue();
                    Long Lprice=(Long) nana.get("price");
                    double price=Lprice.doubleValue();
                    String name= (String) nana.get("name");
                    String subType= (String) nana.get("description");
                    productUID.add((String) entry.getKey());
                    product=new storeProduct(name,kcal,price,subType);
                    data.add(product);

                }
                int productsAmount=data.size();
                final LinearLayout lm = (LinearLayout) findViewById(R.id.linearLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 50);
                ArrayList<Button> buttons = new ArrayList<Button>();
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
                    ll.addView(subType,params);

                    // Create Button
                    final Button btn = new Button(getBaseContext());
                    // Give button an ID
                    btn.setId(i);
                    btn.setText("Add To Cart");
                    // set the layoutParams on the button

                    btn.setLayoutParams(params);
                    //Add button to LinearLayout
                    ll.addView(btn);
                    //Add button to LinearLayout defined in XML

                    lm.addView(ll);
                    buttons.add(btn);
                    buttons.get(i).setOnClickListener(handleOnClick(buttons.get(i),data,currOrder));
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        productsRefernce.addValueEventListener(postListener);
        gotoCart=(Button) findViewById(R.id.btnCart);
        setOnClick(gotoCart,currOrder);

    }
    View.OnClickListener handleOnClick(final Button currBtn,ArrayList<storeProduct>data,Order currOrder) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                String productName=productUID.get(currBtn.getId());
                currOrder.setOrderUID(productName);
                currOrder.FillProductInMap(productName);
                int OrdersNumber=currOrder.getItemQuantity(productName);
                currBtn.setText("Add To Cart"+ "(" +OrdersNumber+ ")");

            }
        };
    }

    private void setOnClick(final Button btn, final Order currOrder){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refOrders= FirebaseDatabase.getInstance().getReference("users").child(fb.getInstance().getUid()).child("Orders");
                refOrders.child(fb.getInstance().getUid()).setValue(currOrder.itemQuantity);
                Intent myIntent = new Intent(getApplicationContext(), cart_activity.class);
                startActivity(myIntent);
            }
        });
    }
}
package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class my_purchases extends AppCompatActivity {
    DatabaseReference refOrders, refProducts;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<Order> allUserOrders = new ArrayList<>();
    String ProductName = "";
    String myPurchases = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchases);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbalWEIGHTTRACKER);
        setSupportActionBar(toolbar);

        refOrders = FirebaseDatabase.getInstance().getReference("Orders");
        refOrders.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    Order currOrder = new Order(post.getValue(Order.class));
                    String UserUID = currentFirebaseUser.getUid();
                    if (currOrder.getUserUID().equals(UserUID)) {
                        allUserOrders.add(currOrder);
                    }
                }
                final LinearLayout lm = (LinearLayout) findViewById(R.id.ll_1);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 50);
                TextView tv = new TextView(getBaseContext());
                LinearLayout ll = new LinearLayout(getBaseContext());
                ll.setOrientation(LinearLayout.VERTICAL);
                for (int i = 0; i < allUserOrders.size(); i++) {
                    myPurchases += "Order" + (i + 1) + " Contains: \n";
                    Order ord = new Order(allUserOrders.get(i));
                    for (int j = 0; j < ord.getItemQuantity().size(); j++) {
                        findNameByUID(ord.getItemQuantity().get(j).getItem());
                        myPurchases += "Item : " + ProductName + "\nPrice: " + ord.getItemQuantity().get(j).getPrice() + "\nAmount: " + ord.getItemQuantity().get(j).getAmount() + "\n\n";
                    }
                    myPurchases += "Total Price for this order is: " + ord.getTotalPrice() + "\n\n____________\n\n";
                }

                tv.setText(myPurchases);
                ll.addView(tv, params);
                lm.addView(ll);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void findNameByUID(String UID) {
        refProducts = FirebaseDatabase.getInstance().getReference("products").child(UID).child("name");
        refProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            ProductName = dataSnapshot.getValue(String.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TAG", " it's null.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


}
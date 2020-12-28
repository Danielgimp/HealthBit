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
import java.util.HashMap;

public class my_purchases extends AppCompatActivity {
    DatabaseReference refOrders, refProducts;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<Order> allUserOrders = new ArrayList<>();
    String ProductName = "";
    String myPurchases = "";
    HashMap<String,String> UIDtoName=new HashMap<>();

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
                    Order ord = new Order(allUserOrders.get(i));
                    for (int j = 0; j < ord.getItemQuantity().size(); j++) {
                        refProducts = FirebaseDatabase.getInstance().getReference("products").child(ord.getItemQuantity().get(j).getItem()).child("name");
                        int finalJ = j;
                        refProducts.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    if (dataSnapshot.getValue() != null) {
                                        try {
                                            ord.getItemQuantity().get(finalJ).setItem(dataSnapshot.getValue(String.class));
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

                for (int i = 0; i < allUserOrders.size(); i++) {
                    myPurchases += "Order " + (i + 1) + " Contains: \n";
                    Order ord = new Order(allUserOrders.get(i));
                    for (int j = 0; j < ord.getItemQuantity().size(); j++) {
                        myPurchases += "Item ID: " +  ord.getItemQuantity().get(j).getItem() + "\nPrice: " + ord.getItemQuantity().get(j).getPrice() + "\nAmount: " + ord.getItemQuantity().get(j).getAmount() + "\n\n";
                    }
                    myPurchases += "Total Price for this order is: " + ord.getTotalPrice() + "\n\n__________________\n\n";
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

    }



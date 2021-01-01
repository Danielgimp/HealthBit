package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class cart_activity extends AppCompatActivity {

    DatabaseReference refCurrentOrder, refProducts,refUpdateProducts,refUser;
    FirebaseAuth fb;
    TextView fullname, orderIdtxt;
    String Order_UID = "",Tempoutput="",output="";
    HashMap<String, String> UIDtoName = new HashMap<>();
    HashMap<String,Integer> UnitsPurchasedUIDValue=new HashMap<>();
    Button homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTIPS);
        setSupportActionBar(toolbar);
        fullname = (TextView) findViewById(R.id.HellotxtView);
        orderIdtxt=(TextView) findViewById(R.id.cart_OrderId);
        Order_UID = getIntent().getStringExtra("Unique Order ID");
        refUser= FirebaseDatabase.getInstance().getReference("users").child(fb.getInstance().getUid());
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                if(user!=null) {
                    fullname.setText("Hello "+user.name + " " + user.lname +" !");

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        };
        refUser.addValueEventListener(userListener);

        refProducts = FirebaseDatabase.getInstance().getReference("products");
        String Name="";
        ValueEventListener GetProducts = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String UID=snapshot.getKey();
                    String Name=snapshot.child("name").getValue(String.class);
                    UIDtoName.put(UID,Name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        refProducts.addValueEventListener(GetProducts);

        refCurrentOrder = FirebaseDatabase.getInstance().getReference("Orders").child(Order_UID);
        ValueEventListener postListener = new ValueEventListener() {
            productOrder order;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Order currOrder = new Order(dataSnapshot.getValue(Order.class));
                for (int i = 0; i < currOrder.getItemQuantity().size(); i++) {
                    order = new productOrder(currOrder.getItemQuantity().get(i));
                    int currAmount = order.getAmount();
                    double currPrice = (order.getPrice()/currAmount);
                    double itemTotal=currPrice*currAmount;
                    String currName= UIDtoName.get(order.Item);
                    Tempoutput+="The order you made is detailed below: \nProduct Name: "+ currName + "\nUnits Bought: " + currAmount + "\nPrice for Each Unit: " + currPrice+ "\nItem Total: "+ itemTotal +"\n\n" ;
                    UnitsPurchasedUIDValue.put(order.Item,currAmount);
                }
                output=Tempoutput+"Order Total: "+currOrder.getTotalPrice()+"\n\nThank You for choosing HealthBit!";
                orderIdtxt.setText(output);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        refCurrentOrder.addValueEventListener(postListener);


        homebutton=(Button) findViewById(R.id.bth_homepage);

        homebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                for(Map.Entry<String, Integer> set : UnitsPurchasedUIDValue.entrySet())
                {

                    refUpdateProducts=FirebaseDatabase.getInstance().getReference("products").child(set.getKey()).child("inStock");
                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int oldStock=dataSnapshot.getValue(Integer.class);
                            int currStock=UnitsPurchasedUIDValue.get(set.getKey());
                            refUpdateProducts.setValue(oldStock-currStock);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    refUpdateProducts.addListenerForSingleValueEvent(postListener);

                }

                Intent myIntent = new Intent(getApplicationContext(), MainProfile.class);
                startActivity(myIntent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


}

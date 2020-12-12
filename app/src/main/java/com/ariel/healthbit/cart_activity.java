package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static android.content.pm.PackageInstaller.EXTRA_SESSION_ID;

public class cart_activity extends AppCompatActivity {
    DatabaseReference refUser,refOrders,refUserInfo;
    Double totalPrice= Double.valueOf(0);
    TextView fullname,purchases;
    FirebaseAuth fb;
    ArrayList<storeProduct>data=new ArrayList<storeProduct>();
    HashMap<String,String> name_UID_matches=new HashMap<String,String>();
    String purchaseInfo="";
    Bundle extras = getIntent().getExtras();
    String OrderID=extras.getString("EXTRA_SESSION_ID");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTIPS);
        setSupportActionBar(toolbar);
        fullname=(TextView)findViewById(R.id.HellotxtView);
        refUser= FirebaseDatabase.getInstance().getReference("users").child(fb.getInstance().getUid());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User post = dataSnapshot.getValue(User.class);
                fullname.setText("Hello "+ post.name + " " + post.lname);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        refUser.addValueEventListener(postListener);



        DatabaseReference productsRefernce= FirebaseDatabase.getInstance().getReference("products");
        refUserInfo=FirebaseDatabase.getInstance().getReference(fb.getInstance().getUid()).child("Orders");
        ValueEventListener productsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,storeProduct> td= (Map <String,storeProduct>) dataSnapshot.getValue();
                storeProduct product;
                for (Map.Entry<String, storeProduct> entry : td.entrySet()) {
                    HashMap nana=entry.getValue();
                    String product_db_UID=entry.getKey();
                    Long Lkcal=(Long) nana.get("Kcal");
                    int kcal=Lkcal.intValue();
                    Long Lprice=(Long) nana.get("price");
                    double price=Lprice.doubleValue();
                    String name= (String) nana.get("name");
                    name_UID_matches.put(product_db_UID,name);
                    String subType= (String) nana.get("description");
                    product=new storeProduct(name,kcal,price,subType);
                    data.add(product);
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        productsRefernce.addValueEventListener(productsListener);

        refOrders= FirebaseDatabase.getInstance().getReference("Orders").child("-MOLr8p3qmRzUoWmtwow");

        ValueEventListener ordersListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, HashMap> td= (Map <String, HashMap>) dataSnapshot.getValue();
                for (Map.Entry<String, HashMap> entry : td.entrySet()) {
                    String OrderDetails = entry.getValue().toString();
                    String[] splitData=OrderDetails.split("=");
                    String OrderID=splitData[0].substring(1);
                    String SQuantity=splitData[1].substring(0,1);
                    int Quantity= Integer.parseInt(SQuantity);
                    int IndexInArr=itemIndexinData(OrderID);
                    double currPrice=data.get(IndexInArr).getPrice()*Quantity;
                    totalPrice+=currPrice;

                    purchaseInfo+="The Following Items are in the Cart: \n"+"Name: " + data.get(IndexInArr).getName() + "\n" + "Amount: " + Quantity +"\n" + "Price for each unit: " +data.get(IndexInArr).getPrice() +"\n" + "Total Price for this product: "+currPrice+ "\n\n";

                }
                String TotalPrice="Total for the order is: "+totalPrice;
                purchases=(TextView) findViewById(R.id.OrdertxtView);
                String textOnView=purchaseInfo+TotalPrice;
                purchases.setText(textOnView);

            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        refOrders.addValueEventListener(ordersListener);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public int itemIndexinData(String OrderID)
    {
        Iterator<storeProduct> iterator = data.iterator();
        int i=0;
        while (iterator.hasNext()) {
            storeProduct product = iterator.next();
            if (product.getName().equals(OrderID)) {
                return i;
            }
            i++;
        }
        return 0;
    }
}
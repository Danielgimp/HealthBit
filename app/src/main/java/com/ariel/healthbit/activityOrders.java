package com.ariel.healthbit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.android.gms.common.util.NumberUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class activityOrders extends AppCompatActivity {

    storeProduct product1,product2,product3;
    String getin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSTORE);
        setSupportActionBar(toolbar);





        //add all products from "Store" on Firebase to an array list
        DatabaseReference productsRefernce= FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference ordersRefernce= FirebaseDatabase.getInstance().getReference("Orders");
        productsRefernce.getKey();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, User> td = (Map<String, User>) dataSnapshot.getValue();
                ArrayList<User> data = new ArrayList<User>();
                HashMap<String,HashMap<Integer,String>> hara=new HashMap<String, HashMap<Integer, String>>();
                HashMap<Integer,String> hara2=new HashMap<Integer,String>();
                ArrayList<ArrayList<String>> uidProductOrder = new ArrayList<ArrayList<String>>();
                int j = 0;
                int counterlist=0;
                for (Entry<String, User> entry : td.entrySet())
                {


                   // System.out.println(entry.getKey()+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    if(dataSnapshot.child(entry.getKey()).hasChild("Orders"))
                    {

                        getin=entry.getKey().toString();
                        System.out.println(getin+"``````````````````````````````````````````````````````````````````````````````");
                        System.out.println(getin+"``````````````````````````````````````````````````````````````````````````````");
                        System.out.println(getin+"``````````````````````````````````````````````````````````````````````````````");
                        Map <String,String> pr= (Map <String,String>) dataSnapshot.child(entry.getKey()).child("Orders").child(getin).getValue();
                        for (Entry <String,String> entry2 : pr.entrySet())
                        {

                   //         System.out.println(entry2.getKey()+"!@#$%^&*(*&^%$#$%^&*&^%$#$%^&*&^%$#$%^&*&^%$#$%^&*&^%$#$%^&*&^%$#$%^&*&^%$#%^&*&^%$%^&*&^%$%^&*");


                            hara2.put(j,entry2.getKey());
                            j++;

                        }

                        j=0;
                        hara.put(getin,hara2);



                        counterlist++;
                            System.out.println(dataSnapshot.child(entry.getKey()).child("Orders").getValue()+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                       int indexList=0;


                      /*  String uid = dataSnapshot.child(entry.getKey()).toString();
                        User userOrder;
                        HashMap nana = entry.getValue();
                        String phone = (String) nana.get("phone");
                        String email = (String) nana.get("email");
                        String name = (String) nana.get("name");
                        String lname = (String) nana.get("lname");
//                        String price=(String) nana.get("price");
//                        String nameProduct=(String) nana.get("name");
                        String stock=(String) nana.get("inStock");
                        userOrder = new User(name, lname, email, phone);
                        data.add(userOrder);


                */

                    }
                    counterlist=0;
            }


//                    for(int i=0;i<data.size();i++)
//                        uid.add()

                int productsAmount=data.size();
                final LinearLayout lm = (LinearLayout) findViewById(R.id.linearLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 50);

                for(int i=0;i<productsAmount;i++)
                {
                    LinearLayout ll = new LinearLayout(getBaseContext());
                    ll.setOrientation(LinearLayout.VERTICAL);


                    EditText nameOfClient = new EditText(getBaseContext());
                    nameOfClient.setHint("product name");
                  //  nameOfClient.setText("Name:"+data.get(i).getName());
                    ll.addView(nameOfClient,params);




                    EditText lastnameButton = new EditText(getBaseContext());
                    lastnameButton.setHint("lname");
                  //  lastnameButton.setText("lname:"+data.get(i).getLname());
                    ll.addView(lastnameButton);



                    EditText phoneNumButton = new EditText(getBaseContext());
                    phoneNumButton.setHint("phone");
                   // phoneNumButton.setText("phone:"+data.get(i).getPhone() );
                    ll.addView(phoneNumButton);





                    EditText emailButton = new EditText(getBaseContext());
                    emailButton.setHint("email");
                    //emailButton.setText("email:"+data.get(i).getEmail());
                    ll.addView(emailButton);

                    for (Map.Entry<String, HashMap<Integer, String>> letterEntry : hara.entrySet()) {
                        String letter = letterEntry.getKey();
                        // ...
                        for (Map.Entry<Integer, String> nameEntry : letterEntry.getValue().entrySet()) {
                            System.out.println(nameEntry.getValue()+"!@#$%^&*(*&^%$#$%^&*&^%$#$%^&*&^%$#$%^&*&^%$#$%^&*&^%$#$%^&*&^%$#$%^&*&^%$#%^&*&^%$%^&*&^%$%^&*");

                            // ...
                        }
                    }

//                    Iterator it = hara.entrySet().iterator();
//                    Iterator it2 =hara2.entrySet().iterator();
//
//                    while(it2.hasNext())
//                    {
//                        Map.Entry pair = (Map.Entry)it.next();
//                        if(pair.getKey().equals(getin))
//                        {
//                            while (it2.hasNext())
//                            {
//                                Map.Entry pair2 = (Map.Entry)it2.next();
//                                System.out.print(""+pair2.getValue());
//                            }
//                            System.out.println();
//
//                        }
//
//
//                    }
//                    it.remove();
//                    System.out.print( hara.get("2PwKtPKTBYgTnBXtdrRiLuSDNc83").get(hara2.get(0)));
//                    System.out.print( hara.get("2PwKtPKTBYgTnBXtdrRiLuSDNc83").get(hara2.get(1)));
//                    System.out.print( hara.get("2PwKtPKTBYgTnBXtdrRiLuSDNc83").get(hara2.get(2)));
//                    System.out.print( hara.get("2PwKtPKTBYgTnBXtdrRiLuSDNc83").get(hara2.get(3)));
//                    System.out.print( hara.get("2PwKtPKTBYgTnBXtdrRiLuSDNc83").get(hara2.get(4)));
//                    System.out.print( hara.get("2PwKtPKTBYgTnBXtdrRiLuSDNc83").get(hara2.get(5)));
//
//                    System.out.print( hara.get("69TSaPYYlae9I3BuG3eiQV0SNxy2").get(hara2.get(0)));
//                    System.out.print( hara.get("69TSaPYYlae9I3BuG3eiQV0SNxy2").get(hara2.get(1)));
//
//




                    // Create Button
                    final Button btn = new Button(getBaseContext());
                    // Give button an ID
                    btn.setId(i);
                    btn.setText("Click to update");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Object> mapProduct = new HashMap<>();
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
                            String email=emailButton.getText().toString();
                            String phone = phoneNumButton.getText().toString();
                            String lnameString = lastnameButton.getText().toString();
                            String TextName=nameOfClient.getText().toString().trim();
                            User update=new User(TextName,lnameString,email,phone);
                            mapProduct.put("name",TextName);
                            mapProduct.put("lname",lastnameButton);
                            mapProduct.put("email",email);
                            mapProduct.put("phone",phone);







                        }
                    });
                    System.out.println(hara.get("2PwKtPKTBYgTnBXtdrRiLuSDNc83").get(0));
                    System.out.println(hara.get("2PwKtPKTBYgTnBXtdrRiLuSDNc83").get(1));
                    System.out.println(hara.get("2PwKtPKTBYgTnBXtdrRiLuSDNc83").get(2));
                    System.out.println(hara.get("2PwKtPKTBYgTnBXtdrRiLuSDNc83").get(3));
                    System.out.println(hara.get("2PwKtPKTBYgTnBXtdrRiLuSDNc83").get(4));
                    System.out.println(hara.get("2PwKtPKTBYgTnBXtdrRiLuSDNc83").get(5));
                    System.out.println(hara.get("69TSaPYYlae9I3BuG3eiQV0SNxy2").get(0));
                    System.out.println(hara.get("69TSaPYYlae9I3BuG3eiQV0SNxy2").get(1));

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
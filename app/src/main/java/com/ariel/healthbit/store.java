package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class store extends AppCompatActivity {

    storeProduct product1,product2,product3;

    static int currPage=1,tracker,NumberOfPages,arraySize=0;
    TextView txtName,txtDescription,txtkCal,txtPrice,txtStock;
    Button addToCart1,addToCart2,addToCart3,nextPage,prevPage,stockUp;
    ArrayList<storeProduct> products =new ArrayList<storeProduct>();
    FirebaseAuth fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        product1=new storeProduct();
        product2=new storeProduct("Protein Bars",500,100,"Bubu");
        product3=new storeProduct("Protein Powder",200,100,"Gugu");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSTORE);
        setSupportActionBar(toolbar);
        stockUp= (Button)findViewById(R.id.btn_stockup);
        stockUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DatabaseReference ref1= FirebaseDatabase.getInstance().getReference("Store");
                ref1.child(product1.getName()).setValue(product1);
                ref1.child(product2.getName()).setValue(product2);
                ref1.child(product3.getName()).setValue(product3);
            }
        });

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
                for(int i=0;i<3;i++)
                {
                    String textName="name"+(i+1);
                    int textViewID = getResources().getIdentifier(textName, "id", getPackageName());
                    txtName=(TextView)findViewById(textViewID);

                    String textDescription="description"+(i+1);
                    int textDescriptionID = getResources().getIdentifier(textDescription, "id", getPackageName());
                    txtDescription=(TextView)findViewById(textDescriptionID);

                    String textkCal="kcal"+(i+1);
                    int textkCalID = getResources().getIdentifier(textkCal, "id", getPackageName());
                    txtkCal=(TextView)findViewById(textkCalID);

                    String textPrice="price"+(i+1);
                    int textPriceID = getResources().getIdentifier(textPrice, "id", getPackageName());
                    txtPrice=(TextView)findViewById(textPriceID);

                    String textStock="stock"+(i+1);
                    int textStockID = getResources().getIdentifier(textStock, "id", getPackageName());
                    txtStock=(TextView)findViewById(textStockID);

                    txtName.setText("Product Name: "+data.get(i).getName());
                    txtDescription.setText("Description: "+data.get(i).getSubType());
                    txtkCal.setText("kCal: "+Double.toString(data.get(i).getKcal()));
                    txtPrice.setText("Price: "+Double.toString(data.get(i).getPrice()));
                    txtStock.setText("In Stock: "+Integer.toString(data.get(i).UnitsInStock));
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
package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ariel.healthbit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class store extends AppCompatActivity {

    storeProduct product1,product2,product3;
    static int page=1;
    TextView txtName,txtDescription,txtkCal,txtPrice,txtStock;
    ArrayList storeProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSTORE);
        setSupportActionBar(toolbar);
        Button button= (Button)findViewById(R.id.btn_stockup);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                product1=new storeProduct();
                product2=new storeProduct("Protein Bars",500,100,"Bubu");
                product3=new storeProduct("Protein Powder",200,100,"Gugu");
                DatabaseReference ref1= FirebaseDatabase.getInstance().getReference("Store");
                ref1.child(product1.getName()).setValue(product1);
                ref1.child(product2.getName()).setValue(product2);
                ref1.child(product3.getName()).setValue(product3);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }





}
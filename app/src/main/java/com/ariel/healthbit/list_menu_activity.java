package com.ariel.healthbit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fasterxml.jackson.databind.ObjectReader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class list_menu_activity  extends AppCompatActivity {

    public static FirebaseDatabase database;
    public static DatabaseReference ref;
    public static Query event_of_today;

    public static AutoCompleteTextView search_autoComplete;
    public static ImageButton search_button;
    public static String search_query;
    public static ArrayList<String> search_list;
    public static ArrayList<Product> search_list_products;
    public static ArrayList<String> search_list_products_string;

    public static list_menu_adapter adapter;
    public static ArrayList<ProductEvent> list;
    public static ArrayList<String> list_removed;
    public static ListView listview;
    public static Button list_button;

    public static Context context;
    public static Product xP;
    public static String dP;

    public static String today_date = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_menu);

        context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTIPS);
        setSupportActionBar(toolbar);
        Button back = (Button) findViewById(R.id.backTips);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }

        });

        //Toast.makeText(this, "hello1", Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("ProductsEvents/"+ get_uid());

        search_autoComplete=findViewById(R.id.autoComplete);
        search_button=(ImageButton) findViewById(R.id.btn_search);
        search_list = new ArrayList<>();
        search_list_products = new ArrayList<>();
        search_list_products_string = new ArrayList<>();

        list = new ArrayList<ProductEvent>();
        list_removed = new ArrayList<String>();
        listview = (ListView) findViewById(R.id.listView); // Define the listview
        list_button=(Button) findViewById(R.id.backTips2);

        //Toast.makeText(this, "hello2", Toast.LENGTH_SHORT).show();

        set_search_options_of_owner();
        set_search_options_of_globals();
        set_search_options_init();
        set_search_options_button();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        set_up_user_rows();
        set_adapter();
        set_list_clear_button();




        /*Product p = get_product_by_name_and_cal("a", 100);
        if (p != null ){
            Toast.makeText(this, "P="+ p.getName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "P=NULL", Toast.LENGTH_SHORT).show();
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        dailymenu.count_all = 0;
        super.onBackPressed();
    }

    public static String get_uid() {
        if (MainActivity.uid == null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            MainActivity.uid = user.getUid();
        }
        return MainActivity.uid;
    }

    public static String get_today_date() {
        if (today_date == null) {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            today_date = simpleDateFormat.format(new Date());
        }
        return today_date;
    }

    public static void set_up_user_rows() {
        //list = new ArrayList<ProductEvent>();
        //list.removeAll(list);
        ref = database.getReference("ProductsEvents/"+ get_uid());
        event_of_today = ref.orderByChild("start").equalTo(get_today_date());
        event_of_today.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ProductEvent pe = ds.getValue(ProductEvent.class);
                    Product p = get_product_by_id(pe.getProductID());
                    //Toast.makeText(context, "Exits="+ds.exists(), Toast.LENGTH_SHORT).show();
                    //boolean removed_flag = list_removed.contains(pe.getName());
                    //Toast.makeText(context, "Name="+pe.getName(), Toast.LENGTH_SHORT).show();
                    int c = pe.getCount();
                    if ( list_search_by_fullname(get_fullname_off_obj(p)) == false && pe.getType() == dailymenu.type && pe.getCount() > 0 ) {
                        set_new_row( pe );
                    } else if ( list_search_by_fullname(get_fullname_off_obj(p)) == true && pe.getType() == dailymenu.type && pe.getCount() == 0 ) {
                        list_removed_by_fullname( get_fullname_off_obj(p) );
                        adapter.notifyDataSetChanged();
                    } else {
                        ///Toast.makeText(context, "PE_FN="+pe.getFullName(), Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }
                    //Toast.makeText(context, "H"+pe.getFullName(), Toast.LENGTH_SHORT).show();
                    //&& list_search_by_fullname(pe.getFullName()) == false
                    //if ( pe.getType() == dailymenu.type  ){
                    //    set_new_row( pe );
                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void set_new_row(ProductEvent pe) {
        list.add(pe);
        adapter.notifyDataSetChanged();
    }



    public static void set_adapter() {
        adapter = new list_menu_adapter(list, context);
        listview.setAdapter(adapter);
    }

    public static void clear_adapter() { list_menu_activity.list.removeAll(list); /*list = new ArrayList<ProductEvent>();*/ }

    public void set_list_clear_button() {
        list_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if ( list.size() > 0 ) {
                    remove_all_from_db();
                    clear_adapter();
                    set_adapter();
                    Toast.makeText(context, "The list is cleared!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "The list is allredy empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean list_search_by_fullname(String fullname) {
        for (ProductEvent pe: list) {
            Product p = get_product_by_id(pe.getProductID());
            if (get_fullname_off_obj(p).contentEquals(fullname)) {
                //Toast.makeText(context, "Found", Toast.LENGTH_SHORT).show();
                return true;
            }
            //Toast.makeText(context, "Pe="+pe.getFullName(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static boolean list_removed_by_fullname(String fullname) {
        if (list != null && !list_menu_activity.list.isEmpty() ) {
            int c_ = 0;
            for (ProductEvent pe : list) {
                Product p = get_product_by_id(pe.getProductID());
                if (get_fullname_off_obj(p).contentEquals(fullname)) {
                    list.remove(c_);
                    return true;
                }
                c_ = c_+1;
            }
        }
        return false;
    }

    /*public static void remove_from_db(String content) {
        event_of_today = ref.orderByChild("start").equalTo(get_today_date());
        event_of_today.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ProductEvent pe = ds.getValue(ProductEvent.class);
                    Product p = get_product_by_id(pe.getProductID());

                    if (pe.getType() == dailymenu.type && get_fullname_off_obj(p) == content){
                        Log.d("value", ds.getKey()+"dddd");
                        //ref.child(ds.getKey()).setValue("demo");
                        //Toast.makeText(context, "demo", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    public static void remove_all_from_db() {
        event_of_today = ref.orderByChild("start").equalTo(get_today_date());
        event_of_today.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    //ProductEvent pe = ds.getValue(ProductEvent.class);
                    ref.child(ds.getKey()).setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void set_search_options_of_owner() {
        ref = database.getReference("ProductsUsers/"+get_uid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Product p = ds.getValue(Product.class);
                    search_list.add(get_fullname_off_obj(p));
                    search_list_products.add(p);
                    search_list_products_string.add(ds.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref = database.getReference("ProductsEvents/"+ get_uid());
    }

    public static void set_search_options_of_globals() {
        ref = database.getReference("ProductsAllUsers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Product p = ds.getValue(Product.class);
                    search_list_products.add(p);
                    search_list_products_string.add(ds.getKey());
                    search_list.add(get_fullname_off_obj(p));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref = database.getReference("ProductsEvents/"+ get_uid());
    }

    public static void set_search_options_init() {

        //ArrayAdapter arrayAdapter=new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, search_list);
        ArrayAdapter arrayAdapter=new ArrayAdapter(context, R.layout.search_info, R.id.text, search_list);
        search_autoComplete.setAdapter(arrayAdapter);
        search_autoComplete.setThreshold(1);
        search_autoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforeTextChanged", String.valueOf(s));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onTextChanged", String.valueOf(s));
                list_menu_activity.search_query = String.valueOf(s);
                //Toast.makeText(context, "Hello "+ search_query, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", String.valueOf(s));
            }
        });
    }

    public void set_search_options_button() {
        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (search_list.contains(search_query)) {
                    if (list_search_by_fullname(search_query)==true) {
                        Log.d("ExistValueInYourList", search_query);
                        Toast.makeText(context, "You allredy add this item.", Toast.LENGTH_LONG).show();
                        search_query = "";
                    } else {
                        Log.d("ExistValue", search_query);
                        set_new_row_in_db(search_query);
                        //get_product_by_name_and_cal()

                        //list_menu_activity.clear_adapter();
                        //list_menu_activity.set_up_user_rows();
                        list_menu_activity.set_adapter();
                        //search_query = "";
                    }
                } else {
                    Log.d("UnExistValue", search_query);
                    Intent myIntent = new Intent(context, list_menu_2Activity.class);
                    startActivity(myIntent);
                }
                search_autoComplete.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        search_autoComplete.showDropDown();
                    }
                },500);
                String tmp = search_query;
                search_autoComplete.setText("");
                search_autoComplete.setSelection(search_autoComplete.getText().length());
                search_query = tmp;
            }
        });
    }

    public static String get_fullname_off_obj(Product p) {
        if (p != null) {
            return p.getName() + " ("+ p.getCal() + " cal)";
        }
        return "Error";
    }

    /*public static Product get_product_by_name_and_cal(String name, int cal) {

        //xP = null;
        //dP = null;

        ref = database.getReference("ProductsAllUsers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Product p = ds.getValue(Product.class);
                    Log.d("Pn", "#"+ p.getName() + "#=#" + name + "#");
                    Log.d("Pc", "#"+ String.valueOf(p.getCal() + "#=#" + cal + "#"));
                    Log.d("Pn", "-----");

                    //Log.d("Ps", ""+p.getCal());
                    if ( p.getName().equals(name) && p.getCal() == cal ) {
                        Log.d("Ps", "OK- "+ p.getName());
                        xP = p;
                        dP = ds.getKey();
                        Log.d("Ps", "good");
                    }
                }
                Log.d("here","xp_name="+ xP.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if ( xP == null ) {
            ref = database.getReference("ProductsUsers/"+ get_uid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        Product p = ds.getValue(Product.class);

                        if ( p.getName().equals(name) && p.getCal() == cal ) {
                            xP = p;
                            dP = ds.getKey();
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        ref = database.getReference("ProductsEvents/"+ get_uid());

        return xP;
    }*/

    public static Product get_product_by_name_and_cal(String name, int cal) {
        int counter = 0;
        if (name != null && cal > 0 ) {
            Log.d("Here10", "OK");
            for (Product p : search_list_products) {
                Log.d("Here11", "OK");
                if (p != null ) {
                    if (p.getName() != null && p.getName().contentEquals(name) && cal == cal) {
                        Log.d("Here11_name", p.getName());
                        xP = p;
                        dP = search_list_products_string.get(counter);

                        Log.d("xP_name", p.getName());
                        Log.d("dP_key", dP);
                        return p;
                    }
                }
                counter++;
                Log.d("Here12", "OK");
            }
            Log.d("Here13", "OK");
        }

        return null;
    }

    public static Product get_product_by_id(String id) {
        int counter = 0;
        if (id != null ) {
            Log.d("Here10", "OK");
            for (String p_s : search_list_products_string) {
                Log.d("Here11", "OK");
                if (p_s != null ) {
                    if (p_s == id) {
                        Log.d("Here11_name", p_s);
                        xP = search_list_products.get(counter);
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

    /*public static Product get_product_by_id(String id) {

        Toast.makeText(context, "p_id="+ id, Toast.LENGTH_SHORT).show();


        try {
            double d = Double.parseDouble(id);
            //Log.d("de1", id);
            //Log.d("de2", String.valueOf(d));
            ref = database.getReference("ProductsAllUsers/"+id);
        } catch (Exception e) {
            //Log.d("da4",id);
            ref = database.getReference("ProductsUsers/"+ get_uid() + "/" + id);
        }

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d("Das","OK"+snapshot.getKey());
                xP = snapshot.getValue(Product.class);
                dP = snapshot.getKey();
                //Toast.makeText(context, "XP_N="+xP.getName(), Toast.LENGTH_SHORT).show();
                /*for (DataSnapshot ds: snapshot.getChildren()) {
                    Log.d("Das","OK"+ds.getKey());
                    //Toast.makeText(context, "OK-----------"+ds.getKey(), Toast.LENGTH_SHORT).show();
                    //xP = ds.getValue(Product.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref = database.getReference("ProductsEvents/"+ get_uid());

        return xP;
    }*/

    public static void set_new_row_in_db(String fullname) {
        String[] argc = fullname.split(" \\(");
        argc[1] = argc[1].replace(" cal)", "");
        int cal = Integer.parseInt(argc[1].toString());
        String name = argc[0];
        Log.d("Here", "1");
        int x =1;
        xP=get_product_by_name_and_cal(name, cal);
        if (xP != null) {
            Log.d("SET_NEW_ROW_IN_DB", "NA" + xP.getName());
            Log.d("Here", "4");
            x=0;
        } else {
            Log.d("SET_NEW_ROW_IN_DB", "NULL");
            Log.d("Here", "4A");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            x++;
        }
        Log.d("dP", "DS"+dP);
        //Toast.makeText(context, "N="+name+"d", Toast.LENGTH_SHORT).show();

        String tmp = dP;
        ProductEvent pe = new ProductEvent(dailymenu.type, 1, tmp, get_today_date());
        ref.child(Long.toHexString(Double.doubleToLongBits(Math.random()))).setValue(pe);
        list.add(pe);
        adapter.notifyDataSetChanged();




        //Toast.makeText(context, "argc0="+argc[0], Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, "argc1="+argc[1], Toast.LENGTH_SHORT).show();
    }



}

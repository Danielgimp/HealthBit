package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    /**
     This class draws what actually is presented in the activity_store, in more detail:
     this class fetches all storeProducts element from rootDB(Firebase):\\products and displays all the products available for purchase.
     There is an option to buy the desired product and place a purchase.

     The products are being displayed via Dynamic list of TextViews and Buttons since the store is changing dynamically as well,
     Further explanation will be provided inside this class about each step of the way.
     */
    Button gotoCart;
    ArrayList<String> productUID=new ArrayList<String>();
    ArrayList<Double> prices=new ArrayList<Double>();
    ArrayList<TextView> tvIndexes=new ArrayList<>();
    Order currOrder=new Order("1");
    DatabaseReference refUser,refOrders;
    FirebaseAuth fb;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    TextView inStock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSTORE);
        setSupportActionBar(toolbar);

        /**
         Most of this page actions happens inside the onDataChange scope because of the asynchronous nature of Firebase DB.
         */
        //add all products from "Store" on Firebase to an array list
        DatabaseReference productsRefernce= FirebaseDatabase.getInstance().getReference("products");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map <String,storeProduct> td= (Map <String,storeProduct>) dataSnapshot.getValue();
                ArrayList<storeProduct>data=new ArrayList<storeProduct>();
                storeProduct product;
                /**
                 * This for loop fetches all storeProducts from rootDB(Firebase):\\products and places then inside of an arraylist.
                 * Also there is two Arraylists which are storing products UID and prices chronologically for later use.
                 */
                for (Entry <String, storeProduct> entry : td.entrySet()) {
                    HashMap InnerDataNode=entry.getValue();
                    Long LunitsInStock= (Long) InnerDataNode.get("inStock");
                    int nitsInStock = LunitsInStock != null ? LunitsInStock.intValue() : -1;
                    String Lkcal = (String) InnerDataNode.get("Kcal").toString();
                    Double kcal = Double.parseDouble(Lkcal);
                    Long Lprice=(Long) InnerDataNode.get("price");
                    double price= Lprice != null ? Lprice.doubleValue() : -1;
                    String name= (String) InnerDataNode.get("name");
                    String subType= (String) InnerDataNode.get("description");
                    productUID.add((String) entry.getKey());
                    prices.add(price);
                    product=new storeProduct(name,kcal,price,subType,nitsInStock);
                    data.add(product);

                }
                /**
                 * This part of the code creates a dynamic ScrollView of Text Views and Buttons.
                 * Each storeProducts consists of: Product name,kCal,Price,Description,Current Stock - each of this fields gets every iteration a textView.
                 * For every product there is also a dynamic creation of buttons.
                 * In the end of the for loop there is a custom setOnClickListener, which is needed to process the order correctly, this custom listener would be explained below.
                 */
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
                    ll.addView(subType);

                    inStock=new TextView(getBaseContext());
                    inStock.setText("Currently In Stock: "+data.get(i).UnitsInStock);
                    ll.addView(inStock,params);
                    tvIndexes.add(inStock);
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
                    if(data.get(i).UnitsInStock==0)
                    {
                        buttons.get(i).setEnabled(false);
                    }
                    buttons.get(i).setOnClickListener(handleOnClick(buttons.get(i),data,currOrder,tvIndexes));
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
    /**
     * This custom OnClickListener is handeling adding items to the cart, for this implementation we needed the following input arguments: the corresponding button of the given product,
     * The data array list containing all storeProduct and the array containing the Text Views just to update the Stock text in the products details as well as an Order global variable to init Order for later usage.
     * This custom functions job is to fill a product to an Order class one by one while checking that there is enough quantity.
     */
    View.OnClickListener handleOnClick(final Button currBtn,ArrayList<storeProduct>data,Order currOrder,ArrayList<TextView> tvIndexes) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                String productName=productUID.get(currBtn.getId());
                double price=prices.get(currBtn.getId());
                int stockQuantity=data.get(currBtn.getId()).UnitsInStock;
                    if(stockQuantity==1)
                    {
                        Toast.makeText(getBaseContext(),"Last unit in Stock",Toast.LENGTH_LONG).show();
                        currBtn.setEnabled(false);
                        currOrder.FillProductInArrayList(productName,price);
                        int OrdersNumber=currOrder.getItemQuantity(productName);
                        currBtn.setText("Add To Cart"+ "(" +OrdersNumber+ ")");
                        data.get(currBtn.getId()).setUnitsInStock(stockQuantity-1);
                        tvIndexes.get(currBtn.getId()).setText("Currently In Stock: "+data.get(currBtn.getId()).UnitsInStock);
                    }
                    if(stockQuantity<=0)
                    {
                        currBtn.setEnabled(false);
                    }
                    else if(stockQuantity>1)
                    {
                        currOrder.FillProductInArrayList(productName,price);
                        int OrdersNumber=currOrder.getItemQuantity(productName);
                        currBtn.setText("Add To Cart"+ "(" +OrdersNumber+ ")");
                        data.get(currBtn.getId()).setUnitsInStock(stockQuantity-1);
                        tvIndexes.get(currBtn.getId()).setText("Currently In Stock: "+data.get(currBtn.getId()).UnitsInStock);
                    }
            }
        };
    }

    /**
     * This custom setOnClick is what actually makes the purchase.
     * When the User clicks on Go to Cart this function executes and places the order in the DB with all the products selected on Store activity.
     * The way it works is it sets a mark on the user itself that an order has been made (refUser.setValue("true")), transfers the Unique orderID to the next Activity which is cart,
     * than it fills all the needed data to the Order class and sends the order to the Database.
     * After this is done there is an intent that sends the user to the cart.
     */
    public void setOnClick(final Button btn, final Order currOrder){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currOrder.getItemQuantity().size()>=1)
                {
                    refUser= FirebaseDatabase.getInstance().getReference("users").child(fb.getInstance().getUid()).child("Orders").push();
                    refUser.setValue("true");
                    String orderUID = refUser.getKey();
                    Intent goToCart=new Intent(store.this,cart_activity.class);
                    goToCart.putExtra("Unique Order ID",orderUID);
                    refOrders=FirebaseDatabase.getInstance().getReference("Orders").child(orderUID);
                    currOrder.setUserUID(currentFirebaseUser.getUid());
                    currOrder.setTotalPrice(0);
                    refOrders.setValue(currOrder);
                    startActivity(goToCart);
                }
                else{
                    Toast.makeText(getBaseContext(),"No Orders were made. \nPlease add items to the cart",Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}
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

        // מגדיר משתנים לחיבור הFIREBASE
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("ProductsEvents/"+ get_uid());

        // מגדיר משתנים לחלון החיפוש למעלה
        search_autoComplete=findViewById(R.id.autoComplete);
        search_button=(ImageButton) findViewById(R.id.btn_search);
        search_list = new ArrayList<>();
        search_list_products = new ArrayList<>();
        search_list_products_string = new ArrayList<>();

        // מגדיר משתנים לLISTVIEW + כפתור המחיקה הכללית
        list = new ArrayList<ProductEvent>();
        list_removed = new ArrayList<String>();
        listview = (ListView) findViewById(R.id.listView); // Define the listview
        list_button=(Button) findViewById(R.id.backTips2);


        set_search_options_of_owner(); // מוסיף לאפשרויות החיפוש(search_list) את כל הPODUCTS האישיים של המשתמש המחובר
        set_search_options_of_globals(); // מוסיף לאפשרויות החיפוש(search_list) את כל הPODUCTS הכללים שיש לכולם
        set_search_options_init(); // פונקציה שלוקחת את SEARCH_LIST וAUTOCOMPLITE מחברת ביניהם
        set_search_options_button(); // פונקציה שאחראים על תפקוד הכפתור חיפוש, מסתמך על התוכן מ- AUTOCOMPLITE


        set_up_user_rows(); // מגדיר את השורות שיוצגו הLIST_VIEW או יותר נכון להגיד, מכניס ערכים חדשים אל הLIST
        set_adapter(); // מגדיר את הADPTER שמקשר ביחד את הLIST וLIST_VIEW
        set_list_clear_button(); // פונקציה שאחראית על תפקוד הכפתור ניקוי הכל, באמצעות לחיצה אליו נוצר נקיון של הLIST וLIST_VIEW והגדרת ADAPTER מחדש

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

    // פונקציה להחזרת UID של המשתמש המחובר, על מנת לא דבר הרבה עם ספריות הFirebase ולמנוע שגיאות, יצרנו משתנה גלובלי שדרכו נדבר עם הFIREBASE פעם אחת ונשמור את הערך.
    public static String get_uid() {
        if (MainActivity.uid == null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            MainActivity.uid = user.getUid();
        }
        return MainActivity.uid;
    }

    // אותו דבר כמו הפונקציה מעל שמחזירה את UID כאן אנחנו מחזירים את התאריך של היום בפורמט קבוע, ומחליטים לדבר על הסיפרייה SimpleDateFormat פעם אחת ולשמור במשתנה גלובלי כדי לחסוך זמן וכוח עיבוד מנימליסטי יחסית + אמינות הקוד שמאפשר לנו שליטה ובקרה על התאריך ותיפקוד המערכת.
    public static String get_today_date() {
        if (today_date == null) {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            today_date = simpleDateFormat.format(new Date());
        }
        return today_date;
    }

    // כמו שנרשם בשורה 104
    public static void set_up_user_rows() {
        // מגדיר את הטבלה הרצויה מFIREBASE באמצעות המשתנה הגלובלי REF וQUERY שמוגדרת לתאריך של היום.
        ref = database.getReference("ProductsEvents/"+ get_uid());
        event_of_today = ref.orderByChild("start").equalTo(get_today_date());
        event_of_today.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int co = 0;
                for (DataSnapshot ds: snapshot.getChildren()) {
                    co++;
                    ProductEvent pe = ds.getValue(ProductEvent.class);
                    Product p = get_product_by_id(pe.getProductID());
                    int c = pe.getCount();
                    if ( list_search_by_fullname(get_fullname_off_obj(p)) == false && pe.getType() == dailymenu.type && pe.getCount() > 0 ) {
                        set_new_row( pe );
                        adapter.notifyDataSetChanged();
                    } else if ( list_search_by_fullname(get_fullname_off_obj(p)) == true && pe.getType() == dailymenu.type && pe.getCount() == 0 ) {
                        if (p != null) {
                            list_removed_by_fullname( get_fullname_off_obj(p) );
                            adapter.notifyDataSetChanged();
                        }

                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error append, please reset your app!", Toast.LENGTH_LONG).show();
            }
        });
    }

    // פונקציה מכנית פשוטה שמוסיפה ProductEvent למערך/משתנה גלובלי LIST
    public static void set_new_row(ProductEvent pe) {
        list.add(pe);
    }

    // מגדיר את הADAPTER של הLIST_VIEW(החלק התחתון של הACTIVITY
    public static void set_adapter() {
        adapter = new list_menu_adapter(list, context);
        listview.setAdapter(adapter);
    }

    // פונקציה פשוטה לקיצור הקוד, לנקיון המערך/משתנה גלובלי LIST לכמו חדש. פונקציית העדכון ADAPTER תחויב להקרא פונקציה אחת אחרי זאת.
    public static void clear_adapter() { list_menu_activity.list.removeAll(list); }

    // כמו שרשום בשורה 106
    public void set_list_clear_button() {
        list_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if ( list.size() > 0 ) {
                    remove_all_from_db(); // פונקציה שמכילה את כל השורות בCOUNT=0 (בתוך הFIREBASE הכוונה)
                    clear_adapter(); // כמו שרשום בשורה 188
                    set_adapter(); // מגדיר מחדש את הADAPTER עם LIST ריק בגדלל העדכון בFIREBASE בשורה 196
                    Toast.makeText(context, "The list is cleared!", Toast.LENGTH_SHORT).show(); // תצוגה למשתמש שהכל נמחק
                } else {
                    Toast.makeText(context, "The list is allredy empty!", Toast.LENGTH_SHORT).show(); // תצוגה למשתמש שאין מה למחוק
                }
            }
        });
    }

    // מחפש בתוך המשתמש LIST אובייט PRODUCT מסוים לפי הFULLNAME שלו שמכיל את שמו של המוצר והכמות(שהם בעצם אוביקט PRODUCT)
    public static boolean list_search_by_fullname(String fullname) {
        for (ProductEvent pe: list) {
            Product p = get_product_by_id(pe.getProductID());
            if (get_fullname_off_obj(p).contentEquals(fullname)) {
                return true; // אם האובייקט PRODUCT שנשלח באמצעות פונקציית הToString שלו(Fullname) נמצא אז תחזיר את זה
            }
        }
        return false; // האובייקט לא נמצא ברשימה הגלובלית LIST
    }

    // פונקציה שמאפשרת מחיקה מהרשימה הגלובלית LIST באמצעות תסריט ToString של האובייקט Product
    public static boolean list_removed_by_fullname(String fullname) {
        if (list != null && !list_menu_activity.list.isEmpty() ) {
            int c_ = 0;
            for (ProductEvent pe : list) {
                Product p = get_product_by_id(pe.getProductID());
                if (get_fullname_off_obj(p).contentEquals(fullname)) {
                    list.remove(c_);
                    return true; // אם נמצא ונמחק
                }
                c_ = c_+1;
            }
        }
        return false; // לא נמצא, ואין מה למחוק
    }

    // כמו בשורה 198
    public static void remove_all_from_db() {
        // מגדיר את הFIREBASE ושאילת(QUERY) שלה לפי התאריך של היום.
        event_of_today = ref.orderByChild("start").equalTo(get_today_date());
        event_of_today.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ProductEvent pe = ds.getValue(ProductEvent.class);
                    if (pe.getType() == dailymenu.type) { // אם נמצא ערך לפי המשתמש שהוא מהסוג(כמו: ארוחת בוקר/צוהריים/ערב) אותו דבר
                        pe.setCount(0); // מכיל את הערך בCOUNT ל0
                        ref.child(ds.getKey()).setValue(pe); // שולח בחזרה לFirebase את העדכון הערך 0 החדש בשדה הCOUNT
                    }

                }
                event_of_today.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                event_of_today.removeEventListener(this);
            }
        });
    }

    // כמו שרשום בשורה 98
    public static void set_search_options_of_owner() {
        ref = database.getReference("ProductsUsers/"+get_uid()); // משנה את הREF הגלובלי מהטבלה שאמור לשבת עליה בשותף
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    // מכיל את כל הנתונים מהטבלה בFIREBASE בתור אוביקטים של Product
                    Product p = ds.getValue(Product.class);
                    search_list.add(get_fullname_off_obj(p)); // מוסיף אותם אל תוך המשתנה/מערך גלובלי search_list
                    search_list_products.add(p); // מוסיף אותו גם לרשימה search_list_products
                    search_list_products_string.add(ds.getKey()); // ומכיל את הKEY שלו בטבלה המקבילה לsearch_list_products שהיא search_list_products_string
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref = database.getReference("ProductsEvents/"+ get_uid()); // משנה את הREF הגלובליבחזרה לטבלה שאמור לשבת עליה בשותף
    }

    // כמו פוקנציה מעל, רק בעבור ערכים שאמורים להיות לכל המשתמשים לא משנה מי מחובר
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


    // כמו שרשום בשורה 100
    public static void set_search_options_init() {
        // יוצר ADAPTER חדש בעבור הAUTOCOMPLITE ביחד על הLayout שיצרנו שנקרה search_info
        ArrayAdapter arrayAdapter=new ArrayAdapter(context, R.layout.search_info, R.id.text, search_list);
        search_autoComplete.setAdapter(arrayAdapter); // מגדיר את הADAPTER על הAutocomplite
        search_autoComplete.setThreshold(1); // מגדיר מספר תווים לתחילת תצוגת הערכים הקופצים מתחת לשורת החיפוש.
        search_autoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforeTextChanged", String.valueOf(s));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onTextChanged", String.valueOf(s));
                list_menu_activity.search_query = String.valueOf(s); // ברגע שיש שינוי בשורת החיפוש, תכיל תמיד את ערך הSTRING שלו בתוך משתנה גלובלי, למקרה שהמשתמש יחלץ על הכפתור החיפוש

            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", String.valueOf(s));
            }
        });
    }

    // כמו שרשום בשורה 101
    public void set_search_options_button() {
        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (search_list.contains(search_query)) { // בודק עם השתנה שנשמר באמצעות פונקציה הקודם, מופיע באפשרויות הבחירה של autocomplite
                    if (list_search_by_fullname(search_query)==true) { // עושה בדיקה האם האפשרות שנבחרה כבר קיימת ברשימת הLIST VIEW
                        Log.d("ExistValueInYourList", search_query);
                        Toast.makeText(context, "You allredy add this item.", Toast.LENGTH_LONG).show(); // תצוגה למשתמש שהאפשרות כבר קיימת אצלו בסל/list view
                        search_query = "";
                    } else {
                        Log.d("ExistValue", search_query);
                        set_new_row_in_db(search_query); // מכניסה את הערך החדש לטבלה בfirebase
                        list_menu_activity.set_adapter();
                    }
                } else { // אם שורה 335 לא מתקיימת, ולא נמצא, שולח אותו לActivity חדש ליצור אפשרות כזאת בעבור אותו משתמש בלבד.
                    Log.d("UnExistValue", search_query);
                    Intent myIntent = new Intent(context, list_menu_2Activity.class);
                    startActivity(myIntent);
                }
                // לאחר כל לחיצה על הכפתור תמיד הautocomplite יציע לך את האפשרויות שלו
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

    // פונקציה שמקבלת Product ועושה לו ToString  מחזירה ערך Sring מכיל בתוכו את כל המשתנים של הProduct
    public static String get_fullname_off_obj(Product p) {
        if (p != null) {
            return p.getName() + " ("+ p.getCal() + " cal)";
        }
        return "Error";
    }

    // מחזיר לך אוביקט Product באמצעות שליחת שמו וקלוריות שלו(כל משתניו)
    public static Product get_product_by_name_and_cal(String name, int cal) {
        int counter = 0;
        if (name != null && cal > 0 ) { // אם המשתנים שנשלחו אכן הגיוניים
            for (Product p : search_list_products) {
                if (p != null ) {
                    if (p.getName() != null && p.getName().contentEquals(name) && cal == cal) {
                        xP = p; // מגדיר את הP במשתנה גלובלי
                        dP = search_list_products_string.get(counter); // מגדיר את הKEY של אותו PRODUCT באמצעות משתנה גלובלי
                        return p;
                    }
                }
                counter++;
            }
        }

        return null;
    }

    // דומה לפוקנציה מעל רק לפי הProduct ID של המוצר
    public static Product get_product_by_id(String id) {
        int counter = 0;
        if (id != null ) {
            for (String p_s : search_list_products_string) {
                if (p_s != null ) {
                    if (p_s.contentEquals(id)) {
                        xP = search_list_products.get(counter);
                        dP = p_s;
                        return xP;
                    }
                }
                counter++;
            }
        }

        return null;
    }

    // מוסיף שורה חדשה לטבלה הFIREBASE
    public static void set_new_row_in_db(String fullname) {
        String[] argc = fullname.split(" \\(");
        argc[1] = argc[1].replace(" cal)", "");
        int cal = Integer.parseInt(argc[1].toString());
        String name = argc[0];
        int x =1;
        xP=get_product_by_name_and_cal(name, cal);
        if (xP != null) {
            x=0;
        } else {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            x++;
        }

        String tmp = dP;
        ProductEvent pe = new ProductEvent(dailymenu.type, 1, tmp, get_today_date());
        ref.child(Long.toHexString(Double.doubleToLongBits(Math.random()))).setValue(pe);
        list.add(pe);
        adapter.notifyDataSetChanged();
    }



}

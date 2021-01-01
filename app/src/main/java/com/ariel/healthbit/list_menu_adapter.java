package com.ariel.healthbit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class list_menu_adapter extends BaseAdapter implements ListAdapter {
    private ArrayList<ProductEvent> list = new ArrayList<ProductEvent>();
    private Context context;

    private TextView view_product;
    private EditText edit_count;
    private Button btn_less;
    private Button btn_more;

    public FirebaseDatabase database;
    public DatabaseReference ref;
    public Query event_of_today;

    public list_menu_adapter(ArrayList<ProductEvent> list, Context context) {
        this.list    = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    public EditText get_edit_count() {
        return this.edit_count;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.product_info, null);
        }

        //Handle TextView and display string from your list
        view_product = (TextView) view.findViewById(R.id.view_product);
        view_product.setText(get_fullName(position));

        //Handle buttons and add onClickListeners
        edit_count = (EditText) view.findViewById(R.id.edit_count);
        edit_count.setText(String.valueOf(list.get(position).getCount()));
        btn_less = (Button)view.findViewById(R.id.btn_less);
        btn_more = (Button)view.findViewById(R.id.btn_more);


        btn_less.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int vTmp = list.get(position).getCount() - 1;
                if (vTmp == 999) {
                    list.get(position).setCount(vTmp);
                    update_db_on_count_change(position, vTmp+1);
                    Toast.makeText(v.getContext(), "Removed!", Toast.LENGTH_SHORT).show();
                } else {
                    list.get(position).setCount(vTmp);
                    update_db_on_count_change(position, vTmp+1);
                }


            }
        });
        btn_more.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int vTmp = list.get(position).getCount() + 1;
                list.get(position).setCount(vTmp);
                update_db_on_count_change(position, vTmp-1 );
            }

        });

        edit_count.setText(String.valueOf(list.get(position).getCount()));

        return view;
    }

    public int get_cal(int pos) {
        return list_menu_activity.get_product_by_id(list.get(pos).getProductID()).getCal();
    }
    public String get_name(int pos) {
        return list_menu_activity.get_product_by_id(list.get(pos).getProductID()).getName();
    }

    public String get_fullName(int pos) {
        Product p = list_menu_activity.get_product_by_id(list.get(pos).getProductID());
        return list_menu_activity.get_fullname_off_obj(p);
    }

    public int get_type(int pos) {
        return list.get(pos).getType();
    }

    public void update_db_on_count_change(int position, int before) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("ProductsEvents/"+ list_menu_activity.get_uid() +"/");
        event_of_today = ref.orderByChild("start").equalTo(list_menu_activity.get_today_date());
        event_of_today.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ProductEvent pe = ds.getValue(ProductEvent.class);
                        Product p = list_menu_activity.get_product_by_id(pe.getProductID());
                        if (pe.getType() == get_type(position) && list_menu_activity.get_fullname_off_obj(p).contentEquals(get_fullName(position)) && pe.getCount() == before) {
                            pe.setCount(list.get(position).getCount());
                            ref.child(ds.getKey()).setValue(pe);
                            event_of_today.removeEventListener(this);
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

}

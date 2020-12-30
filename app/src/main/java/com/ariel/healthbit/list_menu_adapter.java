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
        //list.get(pos).getBytes();
        //return list.get(pos);
        //just return 0 if your list items do not have an Id variable.
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

        //Toast.makeText(context, "P="+ String.valueOf(position)+"\nN="+list.get(position).getFullName(), Toast.LENGTH_SHORT).show();

        //Handle TextView and display string from your list
        view_product = (TextView) view.findViewById(R.id.view_product);
        //Toast.makeText(context, "T="+view_product.getText().toString()+";C="+list.get(position).getCount()+";P="+position, Toast.LENGTH_SHORT).show();
        view_product.setText(get_fullName(position));

        //Handle buttons and add onClickListeners
        edit_count = (EditText) view.findViewById(R.id.edit_count);
        edit_count.setText(String.valueOf(list.get(position).getCount()));
        //Toast.makeText(context, "EC="+edit_count.getText(), Toast.LENGTH_SHORT).show();
        btn_less = (Button)view.findViewById(R.id.btn_less);
        btn_more = (Button)view.findViewById(R.id.btn_more);
        //this.value = Integer.parseInt(edit_count.getText().toString());

        btn_less.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int vTmp = list.get(position).getCount() - 1;
                //list.get(position).setCount(vTmp);
                if (vTmp == 999) {

                    /*database = FirebaseDatabase.getInstance();
                    Toast.makeText(context, "Uid="+list_menu_activity.get_uid(), Toast.LENGTH_SHORT).show();
                    ref = database.getReference("ProductsEvents/"+ list_menu_activity.get_uid());
                    event_of_today = ref.orderByChild("start").equalTo(list_menu_activity.get_today_date());
                    event_of_today.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ProductEvent pe = ds.getValue(ProductEvent.class);
                                //Toast.makeText(v.getContext(), "f="+ pe.getFullName() + "\nf="+list.get(position), Toast.LENGTH_SHORT).show();
                                if (pe.getType() == dailymenu.type && pe.getFullName().equals(list.get(position).getFullName())) {
                                    //Toast.makeText(v.getContext(), "show="+ ds.getKey(), Toast.LENGTH_SHORT).show();
                                    list_menu_activity.list_removed.add(ds.getKey());
                                    ref.child(ds.getKey()).removeValue(pe);
                                    //ref.child(ds.getKey()).removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    /*event_of_today.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()) {
                                ProductEvent pe = ds.getValue(ProductEvent.class);
                                //Toast.makeText(v.getContext(), "f="+ pe.getFullName() + "\nf="+list.get(position), Toast.LENGTH_SHORT).show();
                                if (pe.getType() == dailymenu.type && pe.getFullName().contentEquals(list.get(position).getFullName()) ){
                                    //Toast.makeText(v.getContext(), "show="+ ds.getKey(), Toast.LENGTH_SHORT).show();
                                    list_menu_activity.list_removed.add(ds.getKey());
                                    ref.child(ds.getKey()).removeValue(pe);
                                    //ref.child(ds.getKey()).removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    //update_db_on_count_change(position);
                    list_menu_activity.list.remove(position);
                    notifyDataSetChanged();*/
                    list.get(position).setCount(vTmp);
                    update_db_on_count_change(position);
                    Toast.makeText(v.getContext(), "Removed!", Toast.LENGTH_SHORT).show();
                } else {
                    list.get(position).setCount(vTmp);
                    update_db_on_count_change(position);
                    //get_edit_count().setText(String.valueOf(value));
                    //update_db_on_count_change(position);
                    //Toast.makeText(v.getContext(), "Here1"+edit_count.getText().toString(), Toast.LENGTH_LONG).show();
                }


            }
        });
        btn_more.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                //int value = Integer.parseInt();
                //edit_product.setText(value+1);
                //value = Integer.parseInt(edit_count.getText().toString());
                //value = value + 1;
                //edit_count.setText(String.valueOf(value));
                //Toast.makeText(v.getContext(), "Here2 "+ edit_count.getText().toString(), Toast.LENGTH_LONG).show();
                //update_db_on_count_change(position);

                int vTmp = list.get(position).getCount() + 1;
                list.get(position).setCount(vTmp);
                update_db_on_count_change(position);
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

    public void update_db_on_count_change(int position) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("ProductsEvents/"+ list_menu_activity.get_uid() +"/");
        event_of_today = ref.orderByChild("start").equalTo(list_menu_activity.get_today_date());
        event_of_today.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ProductEvent pe = ds.getValue(ProductEvent.class);
                    if (pe.getCount() > 0 ) {
                        Product p = list_menu_activity.get_product_by_id(pe.getProductID());
                        //Toast.makeText(v.getContext(), "f="+ pe.getFullName() + "\nf="+list.get(position), Toast.LENGTH_SHORT).show();
                        if (pe.getType() == get_type(position) && list_menu_activity.get_fullname_off_obj(p).contentEquals(get_fullName(position))) {
                            //Toast.makeText(v.getContext(), "show="+ ds.getKey(), Toast.LENGTH_SHORT).show();
                            pe.setCount(list.get(position).getCount());
                            ref.child(ds.getKey()).setValue(pe);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

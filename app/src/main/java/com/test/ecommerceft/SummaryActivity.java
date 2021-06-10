package com.test.ecommerceft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.ecommerceft.room.Utils;
import com.test.ecommerceft.room.cart.CartDB;
import com.test.ecommerceft.room.items.Item;

import java.util.ArrayList;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {
    int price;
    int discount;
    TextView tvTotalPrice;
    TextView tvdesc;
    TextView tvsubTotal;
    private ArrayList<Item> items;
    private List<Item> item;
    private RecyclerView mRecyclerView;
    //private String mPhoneNumber;
    private CartDB mCartDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //items =getIntent().getExtras().getParcelableArrayList("cart");
        items = Config.selected;
        //price = getIntent().getStringExtra("price");

        calculatePrice(items);
        calculateDiscount(price);
        mRecyclerView = findViewById(R.id.rv_product);
        tvTotalPrice = findViewById(R.id.total_price);
        tvdesc = findViewById(R.id.total_desc);
        tvsubTotal = findViewById(R.id.total);

        tvTotalPrice.setText("₪ " + price);
        tvdesc.setText("₪ "+discount);
        int subTotal = price - discount;
        tvsubTotal.setText("₪ " + subTotal);

        mCartDb = Room.databaseBuilder(getApplicationContext(), CartDB.class, "sample-db").build();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemsAdapter adapter = new ItemsAdapter(items);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void calculateDiscount(int totalPrice) {
        discount = 0;
        discount = (int) (totalPrice * .14);
    }

    private void calculatePrice(ArrayList<Item> itemlist) {

        price = 0;

        for (Item i : itemlist) {
            price += i.getPrice() * i.getCount();
        }
    }


    private class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

        ItemsAdapter(List<Item> itemList) {
            item = new ArrayList<>();
            item = itemList;
        }

        @NonNull
        @Override
        public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false);
            return new ItemsAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemsAdapter.ViewHolder holder, final int position) {
            final Item itm = item.get(position);
            holder.name.setText(itm.getName());
            holder.price.setText("Rs. " + String.valueOf(itm.getPrice()));
            holder.count.setText("x " + String.valueOf(itm.getCount()));

            if (itm.getImage() != null)
                holder.thumbnail.setImageDrawable(Utils.setMyImage(itm.getImage(), SummaryActivity.this));
            else
                holder.thumbnail.setImageDrawable(getDrawable(R.drawable.ic_local_drink_black_24dp));

        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name, price, count;
            private ImageView thumbnail;


            ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.from_name);
                price = itemView.findViewById(R.id.price_text);
                count = itemView.findViewById(R.id.cart_product_quantity_tv);
                thumbnail = itemView.findViewById(R.id.list_image);
            }
        }
    }

}
package com.test.ecommerceft;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.test.ecommerceft.room.Utils;
import com.test.ecommerceft.room.cart.Cart;
import com.test.ecommerceft.room.cart.CartDB;
import com.test.ecommerceft.room.items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {
    private ArrayList<Item> items;
    private List<Item> item;

    private RecyclerView mRecyclerView;
    //private String mPhoneNumber;
    private CartDB mCartDb;
    private TextView mPriceText;
    RelativeLayout rlEmpty;
    RelativeLayout rlcheckout;
    Button btnCheckOut;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Cart");
        items =getIntent().getExtras().getParcelableArrayList("cart");
        //mPhoneNumber = Objects.requireNonNull(this.getIntent().getExtras().getString("phone"));

        btnCheckOut = findViewById(R.id.btn_checkout);
        rlEmpty = findViewById(R.id.rl_empty);
        rlcheckout = findViewById(R.id.checkout);

        if(items.size()<=0){
            rlEmpty.setVisibility(View.VISIBLE);
            rlcheckout.setVisibility(View.GONE);
        }else {
            rlEmpty.setVisibility(View.GONE);
            rlcheckout.setVisibility(View.VISIBLE);
        }

        mRecyclerView = findViewById(R.id.cart_list);
        mPriceText = findViewById(R.id.price);
        mCartDb = Room.databaseBuilder(getApplicationContext(), CartDB.class, "sample-db").build();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemsAdapter adapter = new ItemsAdapter(items);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        calculatePrice(items);


        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Config.selected = items;

                Intent intent = new Intent(CartActivity.this,SummaryActivity.class);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });

    }

    private void calculatePrice(ArrayList<Item> itemlist) {

        price = 0;

        for (Item i : itemlist) {
            price += i.getPrice() * i.getCount();
        }
        mPriceText.setText("₪ " + String.valueOf(price));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.done) {
            if (items.size() == 0) {
                Toast.makeText(this, "Add some items to checkout", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to checkout?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            new InsertCartItem().execute();
                        })
                        .setNegativeButton(android.R.string.no, (dialog, which) -> {
                            dialog.cancel();
                        })
                        .show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertCartItem extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (Item i: items) {
                Cart c = new Cart();
                c.setName(i.getName());
                c.setVariant(i.getVariant());
                c.setImage(i.getImage());
                c.setPrice(i.getPrice());
                c.setInventory(i.getInventory());
                c.setCount(i.getCount());

                mCartDb.getCartDao().insert(c);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(CartActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
            CartActivity.this.finish();
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
                    .inflate(R.layout.cart_item, parent, false);
            return new ItemsAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemsAdapter.ViewHolder holder, final int position) {
            final Item itm = item.get(position);
            holder.name.setText(itm.getName());
            holder.price.setText("₪. "+String.valueOf(itm.getPrice()));
            holder.quantity.setText(itm.getVariant());
            holder.count.setText(String.valueOf(itm.getCount()));
            if(itm.getImage()!=null)
                holder.thumbnail.setImageDrawable(Utils.setMyImage(itm.getImage(),CartActivity.this));
            else
                holder.thumbnail.setImageDrawable(getDrawable(R.drawable.ic_local_drink_black_24dp));


            holder.add.setOnClickListener(v -> {
                itm.setCount(itm.getCount() + 1);
                calculatePrice(items);
                notifyDataSetChanged();
                holder.count.setText(String.valueOf(itm.getCount()));
            });

            holder.remove.setOnClickListener(v -> {
                if (!(itm.getCount() <= 0))
                    itm.setCount(itm.getCount() - 1);
                if (itm.getCount() == 0) {
                    item.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();

                } else holder.count.setText(String.valueOf(itm.getCount()));
            });

        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name, price, quantity, count;
            private ImageView add, remove,thumbnail;


            ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.from_name);
                price = itemView.findViewById(R.id.price_text);
                quantity = itemView.findViewById(R.id.weight_text);
                count = itemView.findViewById(R.id.cart_product_quantity_tv);
                add = itemView.findViewById(R.id.cart_plus_img);
                remove = itemView.findViewById(R.id.cart_minus_img);
                thumbnail = itemView.findViewById(R.id.list_image);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Config.selected = items;
    }
}

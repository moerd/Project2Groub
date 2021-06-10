package com.test.ecommerceft;

import android.annotation.SuppressLint;
import androidx.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.ecommerceft.room.Utils;
import com.test.ecommerceft.room.items.Item;
import com.test.ecommerceft.room.items.ItemDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static ItemDB mItemDB;
    private List<Item> mList = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.products_list);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView navName = header.findViewById(R.id.display_name);
        TextView navPhone = header.findViewById(R.id.display_phone);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        mItemDB = Room.databaseBuilder(getApplicationContext(), ItemDB.class, "sample").build();

        // Database population should occur only once that is when the app is opened first
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.getBoolean("firstTime", false)) {
            new DatabaseAsync().execute();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }
        new QueryDB().execute();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.shopping) {
            Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            drawer.closeDrawer(GravityCompat.START);
            moveTaskToBack(true);
        }
    }

    /**
     * Database seeding
     * Ideally done in a backend or a separate app to manage the items
     */
    private  class DatabaseAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            Integer hours = Integer.valueOf(sdf.format(new Date()));

            Item item = new Item();
            item.setName("Running Sports Shoes");
            item.setPrice(3000);
            item.setInventory(10);
            item.setImage("shoes");
            item.setVariant("50 gm");
            mItemDB.getItemDao().insert(item);

            Item item1 = new Item();
            item1.setName("Badminton Racquet");
            item1.setPrice(480);
            item1.setInventory(10);
            item1.setImage("rocket");
            item1.setVariant("20 gm");
            mItemDB.getItemDao().insert(item1);

            Item item2 = new Item();
            item2.setName("Wanderlust Graphic Tee");
            item2.setPrice(990);
            item2.setInventory(10);
            item2.setImage("shirt");
            item2.setVariant("20 gm");
            mItemDB.getItemDao().insert(item2);

            Item item3 = new Item();
            item3.setName("Pair of 1kg 2kg 3kg");
            item3.setPrice(1500);
            item3.setInventory(10);
            item3.setImage("dumble");
            item3.setVariant("20 gm");
            mItemDB.getItemDao().insert(item3);

            Item item4 = new Item();
            item4.setName("Cricket Tennis Hard Ball");
            item4.setPrice(10);
            item4.setInventory(10);
            item4.setImage("ball");
            item4.setVariant("20 gm");
            mItemDB.getItemDao().insert(item4);


            return null;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cart) {
            ArrayList<Item> item1 = (ArrayList<Item>) Config.selected;
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            Bundle b = new Bundle();
            b.putParcelableArrayList("cart", item1);
            intent.putExtras(b);
            //intent.putExtra("phone", mPhoneNumber);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private class QueryDB extends AsyncTask<Void, Void, Void> {
        private List<Item> newList = new ArrayList<>();
        @Override
        protected Void doInBackground(Void... voids) {
            newList = mItemDB.getItemDao().getAllItems();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mList = newList;

            ItemsAdapter adapter = new ItemsAdapter(mList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
        private List<Item> item;

        ItemsAdapter(List<Item> itemList) {
            item = new ArrayList<>();
            Config.selected = new ArrayList<>();
            this.item = itemList;
        }

        @NonNull
        @Override
        public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemsAdapter.ViewHolder holder, int position) {
            final Item itm = item.get(position);
            holder.thumnail.setImageDrawable(Utils.setMyImage(itm.getImage(),MainActivity.this));
            holder.name.setText(itm.getName());
            holder.price.setText("₪ " + String.valueOf(itm.getPrice()));
            holder.quantity.setText(itm.getVariant());
            holder.count.setText(String.valueOf(itm.getCount()));

            holder.add.setOnClickListener((v) -> {
                itm.setCount(itm.getCount() + 1);
                holder.count.setText(String.valueOf(itm.getCount()));
                /*if (!selected.contains(itm))
                    selected.add(itm);*/
            });

            holder.remove.setOnClickListener(v -> {
                if (!(itm.getCount() <= 0))
                    itm.setCount(itm.getCount() - 1);
                holder.count.setText(String.valueOf(itm.getCount()));
                /*if (itm.getCount() == 0)
                    selected.remove(itm);*/
            });

            holder.btnAdToCart.setOnClickListener(v -> {
                if (!(itm.getCount() <= 0)){
                    if (!Config.selected.contains(itm))
                        Config.selected.add(itm);
                    Toast.makeText(MainActivity.this, ""+itm.getName() + " added to cart", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Quantity should be greater then 0 ", Toast.LENGTH_SHORT).show();
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,ItemDetailActivity.class);
                    Bundle b = new Bundle();
                    b.putString("name", itm.getName());
                    b.putInt("price", itm.getPrice());
                    b.putInt("count", itm.getCount());
                    b.putString("image", itm.getImage());
                    b.putInt("inventory", itm.getInventory());
                    b.putInt("position", position);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name, price, quantity, count;
            private ImageView add, remove,thumnail;
            Button btnAdToCart;

            ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.from_name);
                price = itemView.findViewById(R.id.price_text);
                quantity = itemView.findViewById(R.id.weight_text);
                count = itemView.findViewById(R.id.cart_product_quantity_tv);
                add = itemView.findViewById(R.id.cart_plus_img);
                remove = itemView.findViewById(R.id.cart_minus_img);
                btnAdToCart = itemView.findViewById(R.id.btn_add_to_cart);
                thumnail = itemView.findViewById(R.id.list_image);
            }
        }
    }
}

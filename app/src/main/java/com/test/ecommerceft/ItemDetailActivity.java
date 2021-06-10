package com.test.ecommerceft;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.ecommerceft.room.Utils;
import com.test.ecommerceft.room.items.Item;

public class ItemDetailActivity extends AppCompatActivity {

    String name;
    int price;
    int count;
    int inventory;
    TextView tvPrice;
    TextView tvCartProductQuantity;
    TextView tvName;
    ImageView ivThumb;
    Button btnAdToCart;
    private ImageView add;
    private ImageView remove;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);


        remove = findViewById(R.id.cart_minus_img);
        add = findViewById(R.id.cart_plus_img);
        ivThumb = findViewById(R.id.iv_thumb);
        tvPrice = findViewById(R.id.tv_price);
        btnAdToCart = findViewById(R.id.btn_add_to_cart);
        tvName = findViewById(R.id.tv_name);
        tvCartProductQuantity= findViewById(R.id.cart_product_quantity_tv);

        Bundle bundle = getIntent().getExtras();
        Item item = new Item();
        if(bundle != null){
            name = bundle.getString("name");
            price = bundle.getInt("price");
            count = bundle.getInt("count");
            position = bundle.getInt("position");
            item.setCount(count);
            inventory = bundle.getInt("inventory");

            ivThumb.setImageDrawable(Utils.setMyImage(bundle.getString("image"),ItemDetailActivity.this));
            tvPrice.setText("Price: ₪ "+price);
            tvName.setText(name);
            tvCartProductQuantity.setText(""+item.getCount());
        }

        add.setOnClickListener((v) -> {
            item.setCount(item.getCount() + 1);
            tvCartProductQuantity.setText(String.valueOf(item.getCount()));
                /*if (!selected.contains(itm))
                    selected.add(itm);*/
        });

        remove.setOnClickListener(v -> {
            if (!(item.getCount() <= 0))
                item.setCount(item.getCount() - 1);
                tvCartProductQuantity.setText(String.valueOf(item.getCount()));
                /*if (itm.getCount() == 0)
                    selected.remove(itm);*/
        });

        btnAdToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setName(name);
                item.setImage(bundle.getString("image"));
                item.setCount(item.getCount());
                item.setPrice(price);
                item.setInventory(inventory);
                if (!(item.getCount() <= 0)){
                    if (!Config.selected.contains(item)){
                        Config.selected.add(item);
                        Toast.makeText(ItemDetailActivity.this, ""+name + " added to cart", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ItemDetailActivity.this, "Quantity should be greater then 0 ", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
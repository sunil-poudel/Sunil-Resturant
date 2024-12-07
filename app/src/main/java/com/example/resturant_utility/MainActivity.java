package com.example.resturant_utility;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageButton previous;
    private ImageButton next;
    private Button order;
    private ImageView itemImage;
    private TextView itemName;
    private TextView itemRate;
    private SeekBar seekBar;
    private TextView seekQuantity;
    private TextView seekBill;
    private boolean firstClick = true;
    private int itemIndex=-1;
    private int orderQuantity=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        previous = findViewById(R.id.previous_button);
        next = findViewById(R.id.next_button);
        order = findViewById(R.id.order_button);
        itemImage = findViewById(R.id.item_image);
        itemName = findViewById(R.id.name_of_item);
        itemRate = findViewById(R.id.rate_of_item);
        seekBar = findViewById(R.id.seek_quantity);
        seekQuantity = findViewById(R.id.bill_quantity);
        seekBill = findViewById(R.id.bill_pay);

        itemImage.setImageResource(R.drawable.initial_menu);
        seekBar.setVisibility(View.INVISIBLE);

        order.setVisibility(View.INVISIBLE);

        seekQuantity.setText("");
        seekBill.setText("");

        Items items = new Items();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setVisibility(View.VISIBLE);
                seekBar.setProgress(0);

                itemIndex=(itemIndex>=7)?0:itemIndex+1;
                itemName.setText(items.item[itemIndex]);
                itemImage.setImageResource(items.drawables[itemIndex]);
                itemRate.setText(String.valueOf(items.rate[itemIndex]));

                firstClick=false;
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setVisibility(View.VISIBLE);
                seekBar.setProgress(0);

                itemIndex=(itemIndex<=0)?7:itemIndex-1;
                itemName.setText(items.item[itemIndex]);
                itemImage.setImageResource(items.drawables[itemIndex]);
                itemRate.setText(String.valueOf(items.rate[itemIndex]));

                firstClick=false;
            }

        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int qty, boolean bool) {
                seekQuantity.setText(String.format(Locale.US,"Quuantity: %d", qty));
                seekBill.setText(String.format(Locale.US,"You'll pay: $%.2f", (qty*items.rate[itemIndex])));
                orderQuantity = qty;

                if(qty>0)
                    order.setVisibility(View.VISIBLE);
                else
                    order.setVisibility(View.INVISIBLE);

                order.setBackgroundColor(Color.BLACK);

                Log.d("SEEK", String.format("%d %s", qty, bool));
                Log.d("SEEK", String.format("%d ", itemIndex));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, PayActivity.class);
                    intent.putExtra("orderName", items.item[itemIndex]);
                    intent.putExtra("orderRate", items.rate[itemIndex]);
                    intent.putExtra("orderQuantity", orderQuantity);
                    startActivity(intent);
                }
            });
    }
}

class Items{
    String[] item =
    {
        "Veg momo", "Chicken momo", "Veg chowmin", "Chicken chowmin", "Biriyani", "Chicken chili", "Chicken sausage","Mixed salad"
    };
    float[] rate = {
            170, 220, 150, 200, 250, 190, 180, 200
    };
    int[] drawables = {
            R.drawable.veg_momo,
            R.drawable.chicken_momo,
            R.drawable.veg_chowmein,
            R.drawable.chicken_chowmein,
            R.drawable.biriyani,
            R.drawable.chicken_chilli,
            R.drawable.chicken_sausage,
            R.drawable.mixed_salad
    };
}
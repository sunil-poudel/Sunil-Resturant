package com.example.resturant_utility;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class PayActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView itemRate;
    private TextView itemQuantity;
    private TextView billAmount;
    private ToggleButton tipButton;
    private SeekBar seekBar;
    private TextView tipPercent;
    private TextView tipAmount;
    private TextView finalPay;
    private Button payButton;
    private TextView tipMessage;

    private AlertDialog.Builder alertDialogBuilder;

    private float totalTip = 0.0f;
    private float orderBill = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pay);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        itemName = findViewById(R.id.name);
        itemRate = findViewById(R.id.rate);
        itemQuantity = findViewById(R.id.item_quantity);
        billAmount = findViewById(R.id.amount);
        tipButton = findViewById(R.id.toggleButton);
        seekBar = findViewById(R.id.tip_calculator);
        tipPercent = findViewById(R.id.tip_percentage);
        tipAmount = findViewById(R.id.tip_amount);
        finalPay = findViewById(R.id.final_bill_amount);
        payButton = findViewById(R.id.pay_button);
        tipMessage = findViewById(R.id.tip_message);

        Intent intent = getIntent();
        String orderName = intent.getStringExtra("orderName");
        float orderRate = intent.getFloatExtra("orderRate", 0.0f);
        int orderQuantity = intent.getIntExtra("orderQuantity", 0);
        orderBill = orderQuantity*orderRate;


        itemName.setText(String.format(Locale.US, "Item: %s", orderName));
        itemRate.setText(String.format(Locale.US, "Rate: $%.2f", orderRate));
        itemQuantity.setText(String.format(Locale.US, "Quantity: %d", orderQuantity));
        billAmount.setText(String.format(Locale.US, "Bill amount: $%.2f", orderBill));
        finalPay.setText(String.format(Locale.US, "Final bill: $%.2f", orderBill));

        tipButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    tipMessage.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                    seekBar.setProgress(10);


                } else{
                    seekBar.setVisibility(View.INVISIBLE);
                    tipMessage.setVisibility(View.INVISIBLE);
                    tipPercent.setVisibility(View.INVISIBLE);
                    tipAmount.setVisibility(View.INVISIBLE);
                    totalTip=0;
                    finalPay.setText(String.format(Locale.US, "Final bill: $%.2f", orderBill+totalTip));
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int tip, boolean bool) {
                if(bool) {
                    tipPercent.setVisibility(View.VISIBLE);
                    tipAmount.setVisibility(View.VISIBLE);

                    tipPercent.setText(String.format(Locale.US, "tipping: %d %%", tip));
                    totalTip = ((float) tip / 100) * orderBill;
                    tipAmount.setText(String.format(Locale.US, "amount: $%.2f", totalTip));
                    finalPay.setText(String.format(Locale.US, "Final bill: $%.2f", orderBill+totalTip));
                }
                else{
                    tipPercent.setVisibility(View.INVISIBLE);
                    tipAmount.setVisibility(View.INVISIBLE);
                    totalTip=0;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder = new AlertDialog.Builder(PayActivity.this);

                alertDialogBuilder.setIcon(android.R.drawable.star_big_on);
                alertDialogBuilder.setTitle("Confirm payment?");

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Summary of bill: \n");
                stringBuilder.append("Item: ").append(orderName).append("\n");
                stringBuilder.append("Rate: $").append(orderRate).append("\n");
                stringBuilder.append("Quantity: ").append(orderQuantity).append("\n");
                stringBuilder.append("Amount: $").append(orderBill).append("\n");
                stringBuilder.append("-----------------------------").append("\n");
                stringBuilder.append("Tip: $").append(totalTip).append("\n");
                stringBuilder.append("-----------------------------").append("\n");
                stringBuilder.append("Final bill: ").append(String.format(Locale.US, "$%.2f",orderBill+totalTip)).append("\n");

                alertDialogBuilder.setMessage(stringBuilder);

                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(PayActivity.this, "payment done!", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(PayActivity.this, MainActivity.class);
                        startActivity(intent1);
                    }
                });
                alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(PayActivity.this, "Payment not made!", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
            }
        });

    }
}
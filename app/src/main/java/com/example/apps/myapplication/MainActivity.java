package com.example.apps.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText pass;
    private Button btn;
    private Button btn_alert, act_change;
    private RatingBar rating;
    private TextView textShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
    }

    public void onButtonClick(View v) {
        EditText el1 = (EditText)findViewById(R.id.num1);
        EditText el2 = (EditText)findViewById(R.id.num2);
        TextView resText = (TextView)findViewById(R.id.result);

        int num1 = Integer.parseInt(el1.getText().toString());
        int num2 = Integer.parseInt(el2.getText().toString());
        int resSum = num1 + num2;
        resText.setText(Integer.toString(resSum));
    }

    public void addListenerOnButton() {
        pass = (EditText)findViewById(R.id.editText3);
        btn = (Button) findViewById(R.id.button2);
        btn_alert = (Button) findViewById(R.id.alert);
        act_change = (Button) findViewById(R.id.act_change);

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn.setText("YO");
                        btn.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        Toast.makeText(
                                MainActivity.this, pass.getText(), Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        act_change.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent intent = new Intent("com.example.apps.myapplication.SecondActivity");
                       startActivity(intent);
                    }
                }
        );

        rating = (RatingBar)findViewById(R.id.ratingBar);
        textShow = (TextView)findViewById(R.id.textView2);
        rating.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        textShow.setText("Значение: " + String.valueOf(v));
                    }
                }
        );

        btn_alert.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        btn.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
//                        Toast.makeText(
//                                MainActivity.this, "Мы всё поменяли", Toast.LENGTH_SHORT
//                        ).show();
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
                        a_builder.setMessage("Вы хотите закрыть приложение?")
                                .setCancelable(false)
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                        AlertDialog alert = a_builder.create();
                        alert.setTitle("Закрытие приложения");
                        alert.show();
                    }
                }
        );
    }
}

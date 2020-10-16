package com.example.alphacoffee.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.alphacoffee.R;
import com.example.alphacoffee.model.CuaHang;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    DatabaseReference mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mData = FirebaseDatabase.getInstance().getReference();


    }
}
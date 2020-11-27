package com.example.alphacoffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alphacoffee.R;
import com.example.alphacoffee.fragment.FragmentAccount;
import com.example.alphacoffee.fragment.FragmentNewspaper;
import com.example.alphacoffee.fragment.FragmentStore;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayDeque;
import java.util.Deque;

public class HomeActivity extends AppCompatActivity {

    private TextView tvOrderNgay;
    private BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque = new ArrayDeque<>(3);
    boolean flag = true;
    private LinearLayout layoutMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tvOrderNgay = findViewById(R.id.tvorderngay);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        layoutMenu = findViewById(R.id.layoutmenu);

        //set font tvlogan
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tvOrderNgay.setTypeface(typeface);

        // click vào menu
        layoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProductActivity.class));
            }
        });

        // add account fragment
        integerDeque.push(R.id.menu_newspaper);
        //load fragment
        LoadFragment(new FragmentNewspaper());
        // set account as default fragment
        bottomNavigationView.setSelectedItemId(R.id.menu_newspaper);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //get selected item id
                int id = item.getItemId();
                // check condition
                if (integerDeque.contains(id)){
                    // when deque list contains selected id
                    // check condition
                    if (id == R.id.menu_newspaper){
                        //when selected id is equal to home fragment id
                        // check condition
                        if (integerDeque.size()!=1){
                            //when deque list size is not equal to 1
                            // check condition
                            if (flag){
                                // when flag value is true
                                // add home fragment in deque list
                                integerDeque.addFirst(R.id.menu_newspaper);
                                // set flag = false
                                flag = false;
                            }
                        }
                    }
                    //remove select id from deque list
                    integerDeque.remove(id);
                }
                // push selected id in deque list
                integerDeque.push(id);
                //load fragment
                LoadFragment(getFragment(item.getItemId()));
                //return true
                return true;
            }
        });

    }

    private Fragment getFragment(int itemId) {
        switch (itemId){
            case R.id.menu_store:
                //set checked order fragment
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                //return order fragment
                return new FragmentStore();

            case R.id.menu_newspaper:
                //set checked order fragment
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                //return order fragment
                return new FragmentNewspaper();

            case R.id.menu_account:
                //set checked order fragment
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                //return order fragment
                return new FragmentAccount();

        }
        //set checked default account fragment
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        // return
        return new FragmentNewspaper();

    }

    private void LoadFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, fragment,fragment.getClass().getSimpleName())
                .commit();

    }

    @Override
    public void onBackPressed() {
        // pop to previous fragment
        integerDeque.pop();
        //check condition
        if (!integerDeque.isEmpty()){
            // when deque list is not empty
            //load fragment
            LoadFragment(getFragment(integerDeque.peek()));

        }else {
            // when deque list is empty
            // finish activity
            finish();
        }
    }
}
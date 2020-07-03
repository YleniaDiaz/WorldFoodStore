package com.example.worldfoodstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.worldfoodstore.adapters.PageAdapterTypes;
import com.example.worldfoodstore.extras.StaticParameters;
import com.example.worldfoodstore.pojos.Product;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TypesProductsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1, tab2, tab3;
    public PageAdapterTypes pagerAdapter;
    private ArrayList<Product> products;
    private Toolbar toolbar;
    private String email= StaticParameters.getUser();

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types_products);

        //StaticParameters staticParameters = new StaticParameters();

        toolbar = findViewById(R.id.toolbarTypes);
        toolbar.setTitle("TIPOS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        tabLayout = findViewById(R.id.tabLayout);

        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        tab3 = findViewById(R.id.tab3);

        viewPager = findViewById(R.id.viewPager);

        pagerAdapter = new PageAdapterTypes(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0){
                    pagerAdapter.notifyDataSetChanged();
                }else if(tab.getPosition() == 1) {
                    pagerAdapter.notifyDataSetChanged();
                }else if(tab.getPosition() == 2){
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        products =new ArrayList<>();

    }

    public boolean onCreateOptionsMenu(Menu menu){
        if(email.equalsIgnoreCase("vacio")){
            getMenuInflater().inflate(R.menu.toolbar_users_specific, menu);
            return true;
        }else{
            if(email.equalsIgnoreCase(StaticParameters.getUserRoot())){
                getMenuInflater().inflate(R.menu.toolbar_root_recycler, menu);
                return true;
            }else{
                getMenuInflater().inflate(R.menu.toolbar_users_specific, menu);
                return true;
            }
        }
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item){
        switch (item.getItemId()){

            case R.id.action_search:

                return true;

            case R.id.action_add:
                Intent intent = new Intent(TypesProductsActivity.this, AddProduct.class);
                startActivity(intent);
                return true;

            case R.id.action_modify:
                //goToEdit();
                return true;

            case R.id.action_shopping:
                //goToShoppingCart();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

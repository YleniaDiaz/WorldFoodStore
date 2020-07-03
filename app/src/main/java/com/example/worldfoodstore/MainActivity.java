package com.example.worldfoodstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.worldfoodstore.extras.StaticParameters;
import com.example.worldfoodstore.game.GameActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    //Variables FireBase
    private FirebaseAuth userAuth;
    private String email="";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        toggle=new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if(email.equalsIgnoreCase("")){
            Intent intent = getIntent();
            if(intent.hasExtra("email")){
                email=intent.getStringExtra("email");
                StaticParameters.setUser(email);
            }
        }

        userAuth=FirebaseAuth.getInstance();

        /*TextView emailNavigationView = navigationView.findViewById(R.id.txtEmail);
        emailNavigationView.setText(StaticParameters.getUser());*/
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        System.err.println("ENTRO EN EL METODO -> "+menuItem.getItemId());

        switch (menuItem.getItemId()){

            case R.id.signUp:
                goLogin();
                break;
            case R.id.favorites:
                goFavoriteProducts();
                break;
            case R.id.orders:
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logOut:
                signOff();
                break;
            case R.id.game:
                goToGame();
                break;
        }

        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_users, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item){
        switch (item.getItemId()){

            case R.id.action_search:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goLogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void signOff(){
        userAuth.signOut();

        StaticParameters.setUser("vacio");

        Toast.makeText(MainActivity.this, "Se ha cerrado sesión", Toast.LENGTH_SHORT).show();
    }

    public void goNews(View v){
        Intent intent =  new Intent(MainActivity.this, NewsActivity.class);
        startActivity(intent);
    }

    public void goOffers(View v){
        Intent intent = new Intent(MainActivity.this, OffersActivity.class);
        startActivity(intent);
    }

    public void goTypeProducts(View v){
        Intent intent = new Intent(MainActivity.this, TypesProductsActivity.class);
        startActivity(intent);
    }

    public void goFavoriteProducts(){
        Intent intent = new Intent(MainActivity.this, FavoriteProducts.class);
        startActivity(intent);
    }

    public void goToGame(){
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
    }
}

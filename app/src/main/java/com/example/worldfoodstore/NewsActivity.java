package com.example.worldfoodstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.worldfoodstore.adapters.NewsAdapter;
import com.example.worldfoodstore.extras.GetImgByURL;
import com.example.worldfoodstore.extras.StaticParameters;
import com.example.worldfoodstore.pojos.Product;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView recView;
    private ArrayList <Product> products;

    private DatabaseReference dbReference;
    private StorageReference storage;
    private ValueEventListener listNewsEvent;

    private String email="vacio";

    private Toolbar toolbar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novedades);

        toolbar = findViewById(R.id.toolbarNews);
        toolbar.setTitle("NOVEDADES");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        email= StaticParameters.getUser();

        products =new ArrayList<>();

        startFireBase();

        //listNews();
        prueba();

        recView=findViewById(R.id.RecViewNovedades);
        recView.setHasFixedSize(true);

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
                Intent intent = new Intent(NewsActivity.this, AddProduct.class);
                startActivity(intent);
                return true;

            case R.id.action_modify:
                goToEdit();
                return true;

            case R.id.action_shopping:
                goToShoppingCart();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToEdit(){
        Intent intent = new Intent(NewsActivity.this, AddProduct.class);
        startActivity(intent);
    }

    public void goToShoppingCart(){
        Intent intent = new Intent(NewsActivity.this, ShoppingList.class);
        startActivity(intent);
    }

    public void startFireBase(){
        FirebaseApp.initializeApp(this);
        dbReference= StaticParameters.getDbReference();
        storage = StaticParameters.getStorage();
    }

    public void listNews() {
        dbReference = dbReference.child("Product");
        listNewsEvent=dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.err.println("TAMAÑO -> " + dataSnapshot.getChildrenCount());

                System.err.println(dataSnapshot);

                for (DataSnapshot objSnapShot : dataSnapshot.getChildren()) {
                    Product aux = objSnapShot.getValue(Product.class);

                    if (aux.isNovedad()) {

                        ImageView img = findViewById(R.id.imagenProducto);
                        if (img != null) {
                            String[] url = {aux.getUrlDownload()};
                            new GetImgByURL(img).execute(url);
                        } else {
                            Bitmap imgDefault = BitmapFactory.decodeResource(getResources(), R.mipmap.no_image);
                            aux.setImagen(imgDefault);
                        }
                        products.add(aux);
                    }
                }
                startRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void prueba(){
        dbReference.child("Product").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.err.println("TAMAÑO -> " + dataSnapshot.getChildrenCount());

                System.err.println(dataSnapshot);

                for (DataSnapshot objSnapShot : dataSnapshot.getChildren()) {
                    Product aux = objSnapShot.getValue(Product.class);

                    if (aux.isNovedad()) {

                        ImageView img = findViewById(R.id.imagenProducto);
                        if (img != null) {
                            String[] url = {aux.getUrlDownload()};
                            new GetImgByURL(img).execute(url);
                        } else {
                            Bitmap imgDefault = BitmapFactory.decodeResource(getResources(), R.mipmap.no_image);
                            aux.setImagen(imgDefault);
                        }
                        products.add(aux);
                    }
                }
                startRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void startRecyclerView(){
        final NewsAdapter adaptador=new NewsAdapter(products);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if(email.equalsIgnoreCase(StaticParameters.getUserRoot())){
                    intent = new Intent(NewsActivity.this, SpecificProductRoot.class);
                }else{
                    intent= new Intent(NewsActivity.this, SpecificProductUsers.class);
                }

                Bundle b = new Bundle();

                int pos = recView.getChildAdapterPosition(v);

                Product aux = products.get(pos);

                b.putSerializable("sendedProduct", aux);

                b.putString("email", email);

                intent.putExtras(b);

                startActivity(intent);
            }
        });

        adaptador.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(NewsActivity.this, "LongClick", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        recView.setAdapter(adaptador);
        recView.setLayoutManager(new LinearLayoutManager(NewsActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbReference.removeEventListener(listNewsEvent);
    }

}

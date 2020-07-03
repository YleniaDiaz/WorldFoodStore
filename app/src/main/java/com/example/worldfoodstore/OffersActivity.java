package com.example.worldfoodstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.worldfoodstore.adapters.OfferAdapter;
import com.example.worldfoodstore.extras.StaticParameters;
import com.example.worldfoodstore.pojos.Product;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OffersActivity extends AppCompatActivity {

    private RecyclerView recView;
    private ArrayList<Product> products;

    private String email;

    private FirebaseDatabase database;
    private DatabaseReference dbReference;

    private Toolbar toolbar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas);

        email= StaticParameters.getUser();

        toolbar = findViewById(R.id.toolbarOffers);
        toolbar.setTitle("OFERTAS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        products =new ArrayList<>();

        startFireBase();

        listOffers();

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
                Intent intent = new Intent(OffersActivity.this, AddProduct.class);
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

    public void startFireBase(){
        FirebaseApp.initializeApp(this);
        database= FirebaseDatabase.getInstance();
        dbReference=database.getReference();
    }

    public void listOffers(){
        dbReference.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                products.clear();

                for(DataSnapshot objSnapShot: dataSnapshot.getChildren()){
                    Product aux = objSnapShot.getValue(Product.class);

                    if(!aux.getPrecioOferta().isEmpty()){
                        products.add(aux);
                    }

                    startRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void startRecyclerView(){
        recView=findViewById(R.id.recViewOfertas);
        recView.setHasFixedSize(true);

        final OfferAdapter adaptador=new OfferAdapter(products);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if(email.equalsIgnoreCase(StaticParameters.getUserRoot())){
                    intent = new Intent(OffersActivity.this, SpecificProductRoot.class);
                }else{
                    intent= new Intent(OffersActivity.this, SpecificProductUsers.class);
                }

                Bundle b = new Bundle();

                int pos = recView.getChildAdapterPosition(v);

                Product aux = products.get(pos);

                b.putSerializable("sendedProduct", aux);

                intent.putExtras(b);

                startActivity(intent);
            }
        });

        recView.setAdapter(adaptador);
        recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}

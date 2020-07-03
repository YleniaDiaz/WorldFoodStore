package com.example.worldfoodstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.worldfoodstore.adapters.ShoppingListAdapter;
import com.example.worldfoodstore.extras.StaticParameters;
import com.example.worldfoodstore.pojos.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteProducts extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference dbReference;
    private String email;
    private ArrayList<Product> products;
    private RecyclerView recView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_products);

        toolbar = findViewById(R.id.toolbarFavoriteProducts);
        toolbar.setTitle("PRODUCTOS FAVORITOS");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        email= StaticParameters.getSplitEmail();

        startFireBase();

        listFavoriteProducts();

        recView=findViewById(R.id.recViewFavoriteProducts);
        recView.setHasFixedSize(true);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_users_specific, menu);
        return true;
    }

    public void startFireBase(){
        dbReference= StaticParameters.getDbReference();
    }

    private void listFavoriteProducts(){
        products =new ArrayList<>();

        dbReference.child("User").child(StaticParameters.getSplitEmail()).child("FavoriteProducts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.err.println("ENTRO AQUI?");

                for (DataSnapshot objSnapShot : dataSnapshot.getChildren()) {
                    System.err.println("ENTRO AL FOR 1");

                    Product aux = objSnapShot.getValue(Product.class);

                    System.err.println("ENTRO AL FOR");

                    if (aux!=null) {
                        products.add(aux);
                        System.err.println(aux.getNombre());
                    }else{
                        System.err.println("PRODUCTO NULO??");
                    }

                    if(products.size()>0){
                        createRecycler();
                    }else{
                        System.err.println("JAJA NO AÑADO NADA");
                    }

                    //createRecycler();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createRecycler(){

        System.err.println("LLEGA AQUI?");

        final ShoppingListAdapter adaptador=new ShoppingListAdapter(products);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if(email.equalsIgnoreCase(StaticParameters.getUserRoot())){
                    intent = new Intent(FavoriteProducts.this, SpecificProductRoot.class);
                }else{
                    intent= new Intent(FavoriteProducts.this, SpecificProductUsers.class);
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

        System.err.println("TAMAÑO ARRAY-PRODUCTOS -> " + products.size());

        recView.setAdapter(adaptador);
        recView.setLayoutManager(new LinearLayoutManager(FavoriteProducts.this, LinearLayoutManager.VERTICAL, false));

        recView=findViewById(R.id.recViewOrder);
        recView.setHasFixedSize(true);

        /*if(!products.isEmpty()){
            TextView txt = findViewById(R.id.txtDefault);
            txt.setVisibility(View.INVISIBLE);
            recView.setVisibility(View.GONE);
        }*/
    }
}

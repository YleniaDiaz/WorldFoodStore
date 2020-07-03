package com.example.worldfoodstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.worldfoodstore.extras.GetImgByURL;
import com.example.worldfoodstore.extras.StaticParameters;
import com.example.worldfoodstore.pojos.Order;
import com.example.worldfoodstore.pojos.Product;
import com.google.firebase.database.DatabaseReference;

import java.util.UUID;

public class SpecificProductUsers extends AppCompatActivity {

    private Product specificPoduct;
    private String email = "kk";
    private DatabaseReference dbReference;
    private CheckBox favouriteCheckBox;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_product_users);

        dbReference = StaticParameters.getDbReference();

        Toolbar toolbar = findViewById(R.id.toolbarUser);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        specificPoduct = getSpecificPoduct();

        email = StaticParameters.getUser();

        setView();

        favouriteCheckBox = findViewById(R.id.favouriteCheckbox);
    }

    public void isChecked(View v) {
        if (favouriteCheckBox.isChecked()) {
            dbReference.child("Users").child(StaticParameters.getSplitEmail()).child("FavoritesProducts").child(specificPoduct.getId()).setValue(specificPoduct);
            Toast.makeText(SpecificProductUsers.this, "Producto añadido a favoritos", Toast.LENGTH_SHORT).show();
        } else {
            dbReference.child("Users").child(StaticParameters.getSplitEmail()).child("FavoritesProducts").child(specificPoduct.getId()).removeValue();
            Toast.makeText(SpecificProductUsers.this, "Producto eliminado de favoritos", Toast.LENGTH_SHORT).show();
        }
    }

    private void setView(){
        setImage();

        TextView txtName = findViewById(R.id.nameSpecificProduct);
        txtName.setText(specificPoduct.getNombre());

        TextView txtPrice = findViewById(R.id.priceSpecificProduct);
        String aux;
        if(specificPoduct.getPrecioOferta().equalsIgnoreCase("")){
            aux=specificPoduct.getPrecio()+"€";
            txtPrice.setText(aux);
        }else{
            aux=specificPoduct.getPrecioOferta()+"€";
            txtPrice.setText(aux);
        }
    }

    public void addShoppingList(View v){
        Order order = createAndSetIdOrder();
        addOrderToDatabase(order);
    }

    private Order createAndSetIdOrder(){
        Order order = new Order();

        order.setIdOrder(StaticParameters.getIdOrder());

        if(StaticParameters.getIdOrder().equals("id")){
            order.setIdOrder(UUID.randomUUID().toString());
            StaticParameters.setIdOrder(order.getIdOrder());
        }

        return order;
    }

    private void addOrderToDatabase(Order order){
        if(email!=null){
            dbReference.child("Users").child(StaticParameters.getSplitEmail()).child("Orders").child(order.getIdOrder()).child("products").child(specificPoduct.getId()).setValue(specificPoduct);
            Toast.makeText(SpecificProductUsers.this, "Producto añadido al carrito", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(SpecificProductUsers.this, "No has iniciado sesión", Toast.LENGTH_SHORT).show();
        }
    }
    
    private Product getSpecificPoduct(){
        Product aux;
        Intent intent = getIntent();
        if(intent.hasExtra("sendedProduct")){
            aux=(Product) intent.getSerializableExtra("sendedProduct");
        }else{
            return null;
        }
        return aux;
    }

    private void setImage(){
        ImageView image = findViewById(R.id.imageSpecificProduct);
        if (image!=null){
            String []url = {specificPoduct.getUrlDownload()};
            new GetImgByURL(image).execute(url);
        }else{
            Bitmap imgDefault = BitmapFactory.decodeResource(getResources(), R.mipmap.no_image);
            image.setImageBitmap(imgDefault);
        }
    }
}

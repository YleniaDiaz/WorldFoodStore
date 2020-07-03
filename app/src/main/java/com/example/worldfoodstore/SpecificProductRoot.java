package com.example.worldfoodstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.worldfoodstore.extras.GetImgByURL;
import com.example.worldfoodstore.extras.StaticParameters;
import com.example.worldfoodstore.pojos.Product;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class SpecificProductRoot extends AppCompatActivity {

    private Product specificPoduct;
    private String email="vacio";
    private static String USER_ROOT="root@gmail.com";

    private FirebaseDatabase database;
    private DatabaseReference dbReference;

    private Toolbar toolbar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_product_root);

        Intent intent = getIntent();
        if(intent.hasExtra("sendedProduct")){
            specificPoduct=(Product) intent.getSerializableExtra("sendedProduct");
        }

        /*if(intent.hasExtra("email")){
            email=intent.getStringExtra("email");
        }*/

        email= StaticParameters.getUser();

        toolbar = findViewById(R.id.toolbarRoot);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        startFireBase();

        ImageView imageView = findViewById(R.id.imgProduct);
        TextView txtName=findViewById(R.id.name);
        TextView txtPrice=findViewById(R.id.price);
        TextView txtIsNew=findViewById(R.id.isNew);
        TextView txtIsOffer=findViewById(R.id.isOffer);
        TextView txtTotal=findViewById(R.id.total);

        if (imageView!=null){
            String []url = {specificPoduct.getUrlDownload()};
            new GetImgByURL(imageView).execute(url);
        }else{
            Bitmap imgDefault = BitmapFactory.decodeResource(getResources(), R.mipmap.no_image);
            imageView.setImageBitmap(imgDefault);
        }

        txtName.setText(specificPoduct.getNombre());
        txtPrice.setText(specificPoduct.getPrecio());

        if(specificPoduct.isNovedad()==true){
            txtIsNew.setText("Sí");
        }else {
            txtIsNew.setText("No");
        }

        if(!specificPoduct.getPrecioOferta().isEmpty()){
            txtIsOffer.setText(specificPoduct.getPrecioOferta());
        }else {
            txtIsOffer.setText("0");
        }

        txtTotal.setText("0");
    }

    public void startFireBase(){
        FirebaseApp.initializeApp(this);
        database=FirebaseDatabase.getInstance();
        dbReference=database.getReference();
    }

    public boolean onCreateOptionsMenu(Menu menu){

        if(email.equalsIgnoreCase("vacio")){
            getMenuInflater().inflate(R.menu.toolbar_users_specific, menu);
            return true;
        }else{
            if(email.equalsIgnoreCase(USER_ROOT)){
                getMenuInflater().inflate(R.menu.toolbar_root_specific_product, menu);
                return true;
            }else{
                getMenuInflater().inflate(R.menu.toolbar_users_specific, menu);
                return true;
            }
        }
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item){
        switch (item.getItemId()){

            case R.id.action_modify:
                editProduct();
                return true;

            case R.id.action_delete:
                deleteProduct();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void editProduct(){
        Intent intent = new Intent(SpecificProductRoot.this, AddProduct.class);
        Bundle b = new Bundle();
        b.putSerializable("producto", specificPoduct);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void deleteProduct(){
        dbReference.child("Product").child(specificPoduct.getId()).removeValue();
        Toast.makeText(SpecificProductRoot.this, "Product eliminado correctamente", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SpecificProductRoot.this, NewsActivity.class);
        startActivity(intent);
    }
}

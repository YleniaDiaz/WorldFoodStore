package com.example.worldfoodstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.worldfoodstore.extras.StaticParameters;
import com.example.worldfoodstore.pojos.Product;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class AddProduct extends AppCompatActivity {

    private EditText nameProduct, priceProduct, offerPriceProduct;
    private Button btnAccept, btnCancel, btnAdd;
    private CheckBox checkNews;
    private Spinner typeSpinner;
    private Product aux;
    private final static String [] typeOptions={"Tipo del producto", "Bebida", "Dulce", "Snack"};
    private String type = "por defecto";
    private ImageView imgProduct;
    private Bitmap bitmapImg;

    //Variables FireBase
    private DatabaseReference dbReference;

    private StorageReference storage;
    private static final int PICK_IMG=0;
    private Uri filePath, downloadLink;
    private Product toEdit = null;

    private Toolbar toolbar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        ArrayAdapter <String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, typeOptions);

        startFireBase();

        toolbar = findViewById(R.id.toolbarAddProduct);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        nameProduct = findViewById(R.id.nameProduct);
        priceProduct=findViewById(R.id.priceProduct);
        offerPriceProduct=findViewById(R.id.offerPrice);
        imgProduct=findViewById(R.id.imgVisualizar);
        checkNews=findViewById(R.id.checkNews);

        Intent intent=getIntent();

        if(intent.hasExtra("producto")){
            toolbar.setTitle("Editar producto");
            toEdit = (Product) intent.getSerializableExtra("producto");

            nameProduct.setText(toEdit.getNombre());
            priceProduct.setText(toEdit.getPrecio());
            offerPriceProduct.setText(toEdit.getPrecioOferta());

            if(toEdit.isNovedad()){
                checkNews.isChecked();
            }

        }else{
            toolbar.setTitle("Añadir producto");
        }

        typeSpinner=findViewById(R.id.typeSpinner);
        typeSpinner.setAdapter(typeAdapter);

        btnAdd=findViewById(R.id.addImg);

        btnAccept=findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(toEdit==null){
                    aux= new Product();
                    aux = setProduct(aux);
                    aux.setId(UUID.randomUUID().toString());

                    dbReference.child("Product").child(aux.getId()).setValue(aux);
                    Toast.makeText(AddProduct.this, "Producto añadido", Toast.LENGTH_SHORT).show();
                    goBack();
                }else{
                    toEdit=setProduct(toEdit);
                    dbReference.child("Product").child(toEdit.getId()).setValue(toEdit);
                    Toast.makeText(AddProduct.this, "Producto editado correctamente", Toast.LENGTH_SHORT).show();
                    goBack();
                }
            }
        });

        btnCancel=findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    public void startFireBase(){
        dbReference= StaticParameters.getDbReference();
        storage = StaticParameters.getStorage();
    }

    public Product setProduct(final Product product){

        String name = nameProduct.getText().toString();
        String price = priceProduct.getText().toString();
        String offerPrice = offerPriceProduct.getText().toString();

        product.setNombre(name);
        product.setPrecio(price);
        product.setPrecioOferta(offerPrice);

        if(downloadLink!=null){
            product.setUrlDownload(downloadLink.toString());
        }

        if(checkNews.isChecked()){
            product.setNovedad(true);
        }else{
            product.setNovedad(false);
        }

        type=typeSpinner.getSelectedItem().toString();

        product.setTipo(type);

        return product;
    }

    public void goBack(){
        Intent intent = new Intent(AddProduct.this, NewsActivity.class);
        startActivity(intent);
        finish();
    }

    public void addImgProduct(View v){
        Intent openGalery = new Intent(Intent.ACTION_PICK);
        openGalery.setType("image/*");
        openGalery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(openGalery, "Seleccione una imagen"), PICK_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //COGER IMAGEN DE GALERÍA
        if (requestCode == PICK_IMG && resultCode == RESULT_OK && data!=null && data.getData()!=null){

            filePath = data.getData();

            try{
                bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgProduct.setImageBitmap(bitmapImg);
            }catch (FileNotFoundException e){
                Toast.makeText(AddProduct.this, "No se ha encontrado la imagen", Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                Toast.makeText(AddProduct.this, "Error al guardar la foto IOException", Toast.LENGTH_SHORT).show();
            }


            //GUARDAR IMAGEN EN STORAGE
            if(filePath!=null){
                //final StorageReference imgRef = storage.child("ImgProducts").child(aux.getTipo()).child(filePath.getLastPathSegment());
                final StorageReference imgRef = storage.child("ImgProducts").child(filePath.getLastPathSegment());
                imgRef.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw new Exception();
                        }

                        return imgRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadLink = task.getResult();
                        }
                    }
                });
            }

        }else{
            Toast.makeText(AddProduct.this, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
        }
    }
}

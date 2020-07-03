package com.example.worldfoodstore.tabs;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.worldfoodstore.SpecificProductUsers;
import com.example.worldfoodstore.extras.StaticParameters;
import com.example.worldfoodstore.pojos.Product;
import com.example.worldfoodstore.R;
import com.example.worldfoodstore.SpecificProductRoot;
import com.example.worldfoodstore.adapters.NewsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1 extends Fragment {

    private RecyclerView recView;
    private ArrayList <Product> products;

    private FirebaseDatabase database;
    private DatabaseReference dbReference;

    public Tab1() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab1, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        products =new ArrayList<>();

        startFireBase();

        listProductType();
    }

    public void startFireBase(){
        database= FirebaseDatabase.getInstance();
        dbReference=database.getReference();
    }

    public void listProductType(){
        dbReference.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                products.clear();

                for(DataSnapshot objSnapShot: dataSnapshot.getChildren()){
                    Product aux = objSnapShot.getValue(Product.class);

                    if(aux.getTipo().equalsIgnoreCase("Bebida")){
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
        recView = getView().findViewById(R.id.recViewBebidas);
        recView.setHasFixedSize(true);

        final NewsAdapter adaptador=new NewsAdapter(products);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if(StaticParameters.getUser().equalsIgnoreCase(StaticParameters.getUserRoot())){
                    intent = new Intent(getActivity(), SpecificProductRoot.class);
                }else{
                    intent= new Intent(getActivity(), SpecificProductUsers.class);
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
        recView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

}

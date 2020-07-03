package com.example.worldfoodstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.worldfoodstore.extras.GetImgByURL;
import com.example.worldfoodstore.pojos.Product;
import com.example.worldfoodstore.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> implements View.OnClickListener{

    private View.OnClickListener listener;
    private ArrayList<Product> products;

    public static class OfferViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre;
        private TextView precio;
        private ImageView imagen;

        public OfferViewHolder(View itemView){
            super(itemView);

            nombre = itemView.findViewById(R.id.nombreProducto);
            precio=itemView.findViewById(R.id.precio);
            imagen = itemView.findViewById(R.id.imagenProducto);
        }

        public void bindOffers(Product p){
            nombre.setText(p.getNombre());
            String []url = {p.getUrlDownload()};
            new GetImgByURL(imagen).execute(url);
            precio.setText(p.getPrecioOferta()+"€");
        }
    }

    public OfferAdapter(ArrayList<Product> products){
        this.products = products;
    }

    @Override
    public OfferAdapter.OfferViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType){
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.product_list, viewGroup, false);

        itemView.setOnClickListener(this);

        OfferAdapter.OfferViewHolder ofh = new OfferAdapter.OfferViewHolder(itemView);
        return ofh;
    }

    public void onBindViewHolder(@NotNull OfferAdapter.OfferViewHolder viewHolder, int pos){
        Product item = products.get(pos);
        viewHolder.bindOffers(item);
    }

    public int getItemCount(){
        return products.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    public void onClick(View v){
        if(listener!=null){
            listener.onClick(v);
        }
    }
}


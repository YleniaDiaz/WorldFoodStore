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

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NovedadesViewHolder> implements View.OnClickListener, View.OnLongClickListener{

    private View.OnClickListener listener;
    private View.OnLongClickListener longClickListener;
    private ArrayList<Product> products;

    public static class NovedadesViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre;
        private TextView precio;
        private ImageView imagen;

        public NovedadesViewHolder(View itemView){
            super(itemView);

            nombre = itemView.findViewById(R.id.nombreProducto);
            precio=itemView.findViewById(R.id.precio);
            imagen = itemView.findViewById(R.id.imagenProducto);
        }

        public void bindNovedades(Product p){
            nombre.setText(p.getNombre());

            String []url = {p.getUrlDownload()};
            new GetImgByURL(imagen).execute(url);

            if(p.getPrecioOferta().equalsIgnoreCase("")){
                precio.setText(p.getPrecio()+"€");
            }else{
                precio.setText(p.getPrecioOferta()+"€");
            }
        }
    }



    public NewsAdapter(ArrayList<Product> products){
        this.products = products;
    }

    @Override
    public NovedadesViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType){
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.product_list, viewGroup, false);

        itemView.setOnClickListener(this);

        NovedadesViewHolder nvh = new NovedadesViewHolder(itemView);
        return nvh;
    }

    public void onBindViewHolder(@NotNull NovedadesViewHolder viewHolder, int pos){
        Product item = products.get(pos);
        viewHolder.bindNovedades(item);
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

    public void setOnLongClickListener(View.OnLongClickListener listener){
         longClickListener=listener;
    }

    public boolean onLongClick(View view) {
        if(longClickListener!=null){
            longClickListener.onLongClick(view);
        }
        return false;
    }
}

package com.example.worldfoodstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.worldfoodstore.extras.GetImgByURL;
import com.example.worldfoodstore.R;
import com.example.worldfoodstore.pojos.Product;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> implements View.OnClickListener{

    private View.OnClickListener listener;
    private ArrayList<Product> products;


    /**
     * VIEWHOLDER
     */
    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder{

        private TextView nameProduct;
        private ImageView imgProduct;

        public ShoppingListViewHolder(View itemView){
            super(itemView);

            nameProduct = itemView.findViewById(R.id.nameProductOrder);
            imgProduct = itemView.findViewById(R.id.imgProductOrder);
        }

        public void bindNovedades(Product p){
            nameProduct.setText(p.getNombre());

            String []url = {p.getUrlDownload()};
            new GetImgByURL(imgProduct).execute(url);
        }
    }

    /**
     * ADAPTER
     */
    public ShoppingListAdapter(ArrayList<Product> products){
        this.products = products;
    }

    @Override
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType){
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.products_order, viewGroup, false);

        itemView.setOnClickListener(this);

        ShoppingListAdapter.ShoppingListViewHolder svh = new ShoppingListAdapter.ShoppingListViewHolder(itemView);
        return svh;
    }

    public void onBindViewHolder(@NotNull ShoppingListAdapter.ShoppingListViewHolder viewHolder, int pos){
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
}

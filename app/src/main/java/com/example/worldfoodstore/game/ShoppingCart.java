package com.example.worldfoodstore.game;


import android.widget.ImageView;

import com.example.worldfoodstore.R;


public class ShoppingCart {

    private ImageView img;
    private float position;

    public ShoppingCart(){ }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
        img.setX(position);
    }

    public void setImageToRight(){
        img.setImageResource(R.mipmap.shopping_cart_to_rigth);
    }

    public void setImageToLeft(){
        img.setImageResource(R.mipmap.shopping_cart_to_left);
    }
}

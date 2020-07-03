package com.example.worldfoodstore.game;

import android.widget.ImageView;


public class FallingFood{

    private ImageView img;
    private float position;
    private float velocity;

    public FallingFood(){ }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

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
        img.setY(position);
    }

    public void changeImage(int image){
        img.setImageResource(image);
    }

    /**
     * TODO: MOVER HACIA ABAJO Y TIEMPO QUE TARDA EN BAJAR
     */
}

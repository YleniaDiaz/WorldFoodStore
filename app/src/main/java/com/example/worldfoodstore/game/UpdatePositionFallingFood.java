package com.example.worldfoodstore.game;

import android.widget.ImageView;

import java.util.TimerTask;

public class UpdatePositionFallingFood extends TimerTask {

    private ImageView image;
    private float position;
    private float velocity;

    public UpdatePositionFallingFood(ImageView image, float position, float velocity) {
        this.image = image;
        this.position = position;
        this.velocity = velocity;
    }

    public float getPosition(){
        return position;
    }

    @Override
    public void run() {
        image.setY(position+velocity);
        position += velocity;
    }
}

package com.example.worldfoodstore.extras;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
public class GetImgByURL extends AsyncTask<String, Void, Bitmap> {

    private ImageView img;

    public GetImgByURL(ImageView img){
        this.img=img;
    }

    @Override
    protected Bitmap doInBackground(String... url) {
        String imgURL = url[0];
        Bitmap imgBitmap;
        try{
            InputStream is = new java.net.URL(imgURL).openStream();
            imgBitmap = BitmapFactory.decodeStream(is);
            return imgBitmap;
        }catch (IOException e){
            System.err.println("ERRORCITO -> "+e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        img.setImageBitmap(bitmap);
    }
}

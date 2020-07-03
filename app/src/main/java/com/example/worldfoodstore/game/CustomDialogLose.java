package com.example.worldfoodstore.game;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.worldfoodstore.R;

public class CustomDialogLose {

    private ImageView acceptBtn;
    private Dialog dialog;

    public CustomDialogLose(Context context) {
        initializeDialog(context);
        initializeObjects();
        initializeEvents();
    }

    private void initializeDialog(Context context){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_finish_game_lose);
    }

    private void initializeObjects(){
        acceptBtn = dialog.findViewById(R.id.btn_lose);
    }

    private void initializeEvents(){
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void showDialog(){
        dialog.show();
    }
}

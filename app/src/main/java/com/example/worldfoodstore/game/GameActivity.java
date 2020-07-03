package com.example.worldfoodstore.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.worldfoodstore.R;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private final static int SOUND_GET_OBJECT= 0;
    private final static int SOUND_LOST_OBJECT= 1;
    private final static int SOUND_WIN_GAME= 2;
    private final static int SOUND_GAME_OVER= 3;

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private ShoppingCart shoppingCart;
    private ImageView imgShoppingCart;

    private RelativeLayout gameLayout;
    private float gameLayaoutWidth;
    private float gameLayaoutHeigth;
    private static float CART_WIDTH=280;

    private FallingFood fallingObject;
    private ImageView imageViewFallingFood;
    private int[]imagesFallingFood={R.mipmap.dr_pepper_spiderman_edition, R.mipmap.monster_absolutely_zero, R.mipmap.pringles_paprika};

    private TimerTask updatePosition;
    private final int FPS=80;

    //private TextView scoreTxt;
    private int score;

    private String isStop="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        score=0;

        getIntentFinishGame();
    }

    private void getIntentFinishGame(){
        Intent intent = getIntent();
        if(intent!=null && intent.hasExtra("win")){
            isStop = getIntent().getStringExtra("win");
        }

        if(isStop.equalsIgnoreCase("yes")){
            startDialogWin();
            sound(SOUND_WIN_GAME);
        }else if(isStop.equalsIgnoreCase("no")){
            startDialogLose();
            sound(SOUND_GAME_OVER);
        }
    }

    private void startDialogWin(){
        int codOferr=(int)(Math.random()*99999+1);

        CustomDialogWin dialog = new CustomDialogWin(this);
        dialog.setCodOffer(codOferr+"");
        dialog.showDialog();
    }

    private void startDialogLose(){
        CustomDialogLose dialogLose = new CustomDialogLose(this);
        dialogLose.showDialog();
    }

    public void startGame(View v){
        gameLayout = findViewById(R.id.initialLayout);

        gameLayout.setVisibility(View.INVISIBLE);

        setContentView(R.layout.start_game);

        initializeScore();

        setImageShoppingCart();

        setBounds();

        initializeSensor();

        initializeFallingObjects();

        goDownFallingFood();
    }

    private void initializeScore(){
        //scoreTxt = findViewById(R.id.score);
        //score = Integer.parseInt(scoreTxt.getText().toString());
        score=0;
    }

    private void updateScore(int ref){
        if(ref==0){
            score-=10;
            System.err.println("PUNTUACION INT BOTTOM -> "+score);
            //String aux = score+"";
            //scoreTxt.setText(aux);
        }else{
            score+=20;
            System.err.println("PUNTUACION INT BOTTOM -> "+score);
            //String aux = score+"";
            //scoreTxt.setText(aux);
        }
    }

    private void goDownFallingFood(){
        Timer timer = new Timer();

        updatePosition = new TimerTask() {
            @Override
            public void run() {
                stopGame();
                fallingObject.setVelocity(10);
                float position = fallingObject.getPosition();
                position += fallingObject.getVelocity();
                fallingObject.getImg().setY(position);
                fallingObject.setPosition(position);
                if(checkIfHitsBottomEdge()){
                    score-=10;
                    System.err.println("PUNTUACION BAJA -> "+ score);
                    //updateScore(0);
                }else if(checkIfHitsCart()){
                    score+=20;
                    System.err.println("PUNTUACION SUBE -> "+ score);
                    //updateScore(1);
                }
            }
        };

        timer.scheduleAtFixedRate(updatePosition, 0, 1000/FPS);
    }

    private void stopGame(){
        if(score >= 120){
            updatePosition.cancel();
            Intent intent = new Intent(GameActivity.this, GameActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("win", "yes");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        if(score <= -30){
            updatePosition.cancel();
            Intent intent = new Intent(GameActivity.this, GameActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("win", "no");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    public boolean checkIfHitsBottomEdge(){
        float positionX = (float) (Math.random()*gameLayaoutWidth);
        if(fallingObject.getPosition()>gameLayaoutHeigth){
            sound(SOUND_LOST_OBJECT);
            updatePosition.cancel();
            changePositionFallingFood(positionX);
            //changeImage();
            goDownFallingFood();
            return true;
        }

        return false;
    }

    public boolean checkIfHitsCart(){
        float positionX = (float) (Math.random()*gameLayaoutWidth-70);
        if(checkPositionsCartAndFallingFood()){
            sound(SOUND_GET_OBJECT);
            changePositionFallingFood(positionX);
            //changeImage();
            goDownFallingFood();
            return true;
        }

        return false;
    }

    private void changeImage(){
        updatePosition.cancel();
        System.err.println("ENTROOOOOOOOOOOOOO IMAGEN");
        int image = (int) (Math.random()*imagesFallingFood.length);
        imageViewFallingFood.setImageResource(imagesFallingFood[image]);
        //imageViewFallingFood.setImageDrawable(getResources().getDrawable(imagesFallingFood[image]));
    }

    private void changePositionFallingFood(float positionX){
        updatePosition.cancel();
        fallingObject.setPosition(0);
        fallingObject.getImg().setX(positionX);
    }

    public boolean checkPositionsCartAndFallingFood(){
        Rect object = new Rect();
        fallingObject.getImg().getHitRect(object);
        Rect cart = new Rect();
        shoppingCart.getImg().getHitRect(cart);
        if(Rect.intersects(object, cart)){
            //sound();
            return true;
        }

        return false;
    }

    private void setBounds(){
        gameLayaoutWidth = gameLayout.getWidth();
        gameLayaoutHeigth = gameLayout.getHeight();
    }

    private boolean controlBounds(){
        if (shoppingCart.getPosition() < 0) {
            shoppingCart.setPosition(0);
            return false;
        }

        if (gameLayaoutWidth - CART_WIDTH < shoppingCart.getPosition()) {
            shoppingCart.setPosition(gameLayaoutWidth - CART_WIDTH);
            return false;
        }

        return true;
    }

    private void setImageShoppingCart(){
        imgShoppingCart = findViewById(R.id.cartGame);
        shoppingCart= new ShoppingCart();
        shoppingCart.setImg(imgShoppingCart);
    }

    private void initializeSensor(){
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(sensor==null){
            Toast.makeText(GameActivity.this, "No cuenta con el sensor necesario para jugar", Toast.LENGTH_SHORT).show();
        }

        createSensorEvent();
    }

    private void createSensorEvent(){
        sensorEventListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                float pos = shoppingCart.getPosition();

                if(x<-1){
                    moveToRight(pos);
                    //updateCart.cancel();
                }else if(x>1){
                    moveToLeft(pos);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        start();
    }

    private void initializeFallingObjects(){
        imageViewFallingFood=findViewById(R.id.fallingObject);
        fallingObject = new FallingFood();
        int image = (int) (Math.random()*imagesFallingFood.length);
        imageViewFallingFood.setImageResource(imagesFallingFood[image]);
        fallingObject.setImg(imageViewFallingFood);
    }

    private void moveToRight(float pos){
        if(controlBounds()){
            shoppingCart.setImageToRight();
            shoppingCart.setImg(imgShoppingCart);
            pos+=20;
            shoppingCart.setPosition(pos);
        }
    }

    private void moveToLeft(float pos){
        if(controlBounds()) {
            shoppingCart.setImageToLeft();
            shoppingCart.setImg(imgShoppingCart);
            pos -=20;
            shoppingCart.setPosition(pos);
        }
    }

    private void sound(int sound){
        switch (sound){
            case 0:
                MediaPlayer mediaPlayer =MediaPlayer.create(this, R.raw.get_object);
                mediaPlayer.start();
                break;
            case 1:
                MediaPlayer media =MediaPlayer.create(this, R.raw.lost_object);
                media.start();
                break;
            case 2:
                MediaPlayer player =MediaPlayer.create(this, R.raw.win_game);
                player.start();
                break;
            case 3:
                MediaPlayer media_player =MediaPlayer.create(this, R.raw.game_over);
                media_player.start();
                break;
        }
    }

    private void start(){
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI);
    }
}

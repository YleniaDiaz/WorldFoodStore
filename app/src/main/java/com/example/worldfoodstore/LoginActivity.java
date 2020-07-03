package com.example.worldfoodstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.worldfoodstore.extras.StaticParameters;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    Pattern patternEmail = Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    Pattern patternPassword = Pattern.compile("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,}$\n");

    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;

    //Variables FireBase
    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Iniciar sesión");

        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnAccept);


        /**
         * Métodos de Firebase
         */
        userAuth= FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password=editTextPassword.getText().toString();

                //checkEmailPassword(email, password);

                userAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Si el incio de sesión es correcto, update UI con la información del usuario conectado

                            //Toast.makeText(LoginActivity.this, "Se ha iniciado sesión correctamente", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = userAuth.getCurrentUser();
                            updateUI(user);

                            //StaticParameters.setUser(user.getEmail());

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            /*Bundle b = new Bundle();
                            b.putString("email", user.getEmail());
                            intent.putExtras(b);*/

                            StaticParameters.setUser(user.getEmail());

                            startActivity(intent);
                            finish();
                        } else {
                            //Si falla, se manda un mensaje al usuario

                            Toast.makeText(LoginActivity.this, "No se ha podido iniciar sesión", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser currentUser=userAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Toast.makeText(LoginActivity.this, "Ya has iniciado sesión", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Bundle b = new Bundle();
            b.putString("email", user.getEmail());
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
    }

    public void goToSignUp(View view){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void checkEmailPassword(String email, String password){

        Matcher matcherEmail = patternEmail.matcher(email);
        Matcher matcherPassword = patternEmail.matcher(password);

        if(TextUtils.isEmpty(email)){
            Toast.makeText(LoginActivity.this, "El email no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        } else if(matcherEmail.find()){
            Toast.makeText(LoginActivity.this, "Email no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show();
            return;
        }else if(matcherPassword.find()){
            Toast.makeText(LoginActivity.this, "Contraseña no válida", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}

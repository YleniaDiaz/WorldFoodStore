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
import com.google.firebase.database.DatabaseReference;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailET, passwordET;

    private Button signUpButton;

    private String email, password;

    private FirebaseAuth userAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userAuth=FirebaseAuth.getInstance();

        emailET=findViewById(R.id.emailET);
        passwordET=findViewById(R.id.passwordET);

        signUpButton=findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = emailET.getText().toString();
                password=passwordET.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUpActivity.this, "El email no puede estar vacío", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpActivity.this, "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length() <= 6){
                    Toast.makeText(SignUpActivity.this, "Contraseña demasiado corta", Toast.LENGTH_SHORT).show();
                    return;
                }

                userAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Si el incio de sesión es correcto, update UI con la información del usuario conectado
                                    Toast.makeText(SignUpActivity.this, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();

                                    FirebaseUser user = userAuth.getCurrentUser();
                                    updateUI(user);

                                    DatabaseReference dbReference= StaticParameters.getDbReference();

                                    dbReference.child("Users").setValue(StaticParameters.getSplitEmail());

                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    //Si falla, se manda un mensaje al usuario
                                    Toast.makeText(SignUpActivity.this, "No se ha podido registrar", Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}

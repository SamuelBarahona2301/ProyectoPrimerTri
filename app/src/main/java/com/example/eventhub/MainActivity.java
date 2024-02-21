package com.example.eventhub;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        EditText emailEditText = findViewById(R.id.txtEmail);
        EditText passwordEditText = findViewById(R.id.txtPassword);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Llama al método iniciarSesion con los valores de correo electrónico y contraseña
        iniciarSesion(email, password);
    }

    private void iniciarSesion(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso
                            Toast.makeText(MainActivity.this, "Inicio de sesión exitoso",
                                    Toast.LENGTH_SHORT).show();
                            // Aquí puedes redirigir a la siguiente actividad
                        } else {
                            // Fallo en el inicio de sesión
                            Toast.makeText(MainActivity.this, "Fallo en el inicio de sesión",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void registrar(View view) {
        // Aquí puedes implementar la lógica para registrar nuevos usuarios
    }
}

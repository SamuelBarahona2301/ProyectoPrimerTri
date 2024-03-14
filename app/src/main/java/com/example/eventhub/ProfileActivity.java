package com.example.eventhub;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWebException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    Button buttonLogout;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView textViewFirstName, textViewLastName, textViewDOB, textViewPassword, textCorreo;

    DatabaseReference Usuarios;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewLastName = findViewById(R.id.textViewLastName);
        textCorreo = findViewById(R.id.textCorreo);
        textViewDOB = findViewById(R.id.textViewDOB);
        textViewPassword = findViewById(R.id.textViewPassword);

        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        buttonLogout = findViewById(R.id.buttonLogout);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();

            }
        });


    }

    @Override
    protected void onStart() {
        comprobarIniciosesion();
        super.onStart();
    }

    private void comprobarIniciosesion() {
        if (user != null) {
            CargaDeDatos();
        } else {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void CargaDeDatos() {
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombres = "Nombre: " + snapshot.child("nombre").getValue();
                    String correo = "Correo: " + snapshot.child("correo").getValue();
                    String apellidos = "Apellidos: " + snapshot.child("apellidos").getValue();
                    String fecNac = "Fecha de Nacimiento: " + snapshot.child("fecNacimiento").getValue();
                    String password = "Password: " + snapshot.child("password").getValue();

                    textViewFirstName.setText(nombres);
                    textViewLastName.setText(apellidos);
                    textCorreo.setText(correo);
                    textViewDOB.setText(fecNac);
                    textViewPassword.setText(password);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cerrarSesion() {
        firebaseAuth.signOut();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        Toast.makeText(this, "Se cerro sesión exitosamente", Toast.LENGTH_SHORT).show();
    }
}

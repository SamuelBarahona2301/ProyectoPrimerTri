package com.example.eventhub;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWebException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    Button btnCerrarSesion, btnEliminarCuenta;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView textNombre, textApellidos, textViewDOB, textViewPassword, textCorreo, textRol;

    DatabaseReference usuarios;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textNombre = findViewById(R.id.textNombre);
        textCorreo = findViewById(R.id.txtCorreo);
        textApellidos = findViewById(R.id.textApellidos);
        /*
        textViewDOB = findViewById(R.id.textViewDOB);
        textViewPassword = findViewById(R.id.textViewPassword);
        textRol = findViewById(R.id.textRole);*/

        usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnEliminarCuenta = findViewById(R.id.btnEliminarCuenta);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        btnEliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarCuenta(user.getUid());
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
        usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    /*String fecNac = "Fecha de Nacimiento: " + snapshot.child("fecNacimiento").getValue();
                    String rol = "Rol: " + snapshot.child("rol").getValue();
                    String password = "Password: " + snapshot.child("password").getValue();


                    textViewLastName.setText(apellidos);

                    textViewDOB.setText(fecNac);
                    textRol.setText(rol);
                    textViewPassword.setText(password);*/
                    textNombre.setText((CharSequence) snapshot.child("nombre").getValue());
                    textApellidos.setText((CharSequence) snapshot.child("apellidos").getValue());
                    textCorreo.setText((CharSequence) snapshot.child("correo").getValue());

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

    private void eliminarCuenta(final String usuarioId){
        // Eliminar usuario de la base de datos en tiempo real
        usuarios.child(usuarioId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@NonNull DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    // Éxito al eliminar de la base de datos
                    // Ahora elimina el usuario de la autenticación en Firebase
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Éxito al eliminar el usuario de la autenticación
                                    Toast.makeText(ProfileActivity.this, "Usuario eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    // Error al eliminar el usuario de la autenticación
                                    Toast.makeText(ProfileActivity.this, "Error al eliminar el usuario de la autenticación", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    // Error al eliminar de la base de datos
                    Toast.makeText(ProfileActivity.this, "Error al eliminar el usuario de la base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

package com.example.eventhub;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    Usuario usuario;

    private EditText editNombre, editFechaNac, editApellidos,
            editRol, editPassword, editPasswordRepeat, editMail;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRol;
    private RadioButton radioButtonRol;

    private String txtNombre, txtFechaNac, txtApellidos, txtRol, txtPassword, txtPasswordRepeat, txtMail;

    private DatePickerDialog picker;

    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("usuarios");


        progressBar = findViewById(R.id.progrssBar);
        editNombre = findViewById(R.id.editName);
        editApellidos = findViewById(R.id.editApellidos);
        editFechaNac = findViewById(R.id.editFechaNacimiento);
        editPassword = findViewById(R.id.editPassword);
        editPasswordRepeat = findViewById(R.id.editPasswordRepeat);
        editMail = findViewById(R.id.editEmail);


        // Radiobutton
        radioGroupRol = findViewById(R.id.rdbtnRol);
        radioGroupRol.clearCheck();

        editFechaNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH);
                int anio = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editFechaNac.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, anio, mes, dia);
                picker.show();
            }
        });

        Button buttonRegister = findViewById(R.id.btnRegistrar);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seleccionar = radioGroupRol.getCheckedRadioButtonId();
                radioButtonRol = findViewById(seleccionar);

                txtNombre = editNombre.getText().toString();
                txtApellidos = editApellidos.getText().toString();
                txtFechaNac = editFechaNac.getText().toString();
                txtMail = editMail.getText().toString();
                txtPassword = editPassword.getText().toString();
                txtPasswordRepeat = editPasswordRepeat.getText().toString();


                if (TextUtils.isEmpty(txtNombre)){
                    Toast.makeText(RegisterActivity.this, "Completa el nombre", Toast.LENGTH_LONG).show();
                    editNombre.setError("Se requiere el nombre");
                    editNombre.requestFocus();
                } else if (TextUtils.isEmpty(txtApellidos)) {
                    Toast.makeText(RegisterActivity.this, "Completa los apellidos", Toast.LENGTH_LONG).show();
                    editApellidos.setError("Se requieren los apellidos");
                    editApellidos.requestFocus();
                } else if (TextUtils.isEmpty(txtMail)) {
                    Toast.makeText(RegisterActivity.this, "Completa el email", Toast.LENGTH_LONG).show();
                    editMail.setError("Se requieren el email");
                    editMail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtMail).matches()){
                    Toast.makeText(RegisterActivity.this, "Vuelve a ingresar el email", Toast.LENGTH_LONG).show();
                    editMail.setError("Se requieren email correcto");
                    editMail.requestFocus();
                } else if (TextUtils.isEmpty(txtFechaNac)) {
                    Toast.makeText(RegisterActivity.this, "Completa el fecha de nacimiento", Toast.LENGTH_LONG).show();
                    editFechaNac.setError("Fecha de nacimiento necesario");
                    editFechaNac.requestFocus();
                } else if (radioGroupRol.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegisterActivity.this, "Selecciona el rol correspondiente", Toast.LENGTH_LONG).show();
                    editRol.setError("Rol necesario");
                    editRol.requestFocus();
                }else if (TextUtils.isEmpty(txtPassword)) {
                    Toast.makeText(RegisterActivity.this, "Completa la contraseña", Toast.LENGTH_LONG).show();
                    editPassword.setError("Es necesario la contraseña");
                    editPassword.requestFocus();
                } else if (txtPassword.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Completa la contraseña", Toast.LENGTH_LONG).show();
                    editPassword.setError("La contraseña debe tener minimo 6 caracteres");
                    editPassword.requestFocus();
                } else if (TextUtils.isEmpty(txtPasswordRepeat)) {
                    Toast.makeText(RegisterActivity.this, "Confirma la contraseña", Toast.LENGTH_LONG).show();
                    editPasswordRepeat.setError("Es necesario confirmar la contraseña");
                    editPasswordRepeat.requestFocus();
                } else if (!txtPassword.equals(txtPasswordRepeat)) {
                    Toast.makeText(RegisterActivity.this, "La contraseñas tienen que coincidir", Toast.LENGTH_LONG).show();
                    editPasswordRepeat.setError("Es necesario que coincidan las contraseñas");
                    editPasswordRepeat.requestFocus();

                    editPassword.clearComposingText();
                    editPasswordRepeat.clearComposingText();
                }else {
                    txtRol = radioButtonRol.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);

                    registrarUsuario();
                }
            }
        });

    }

    private void registrarUsuario() {

        auth.createUserWithEmailAndPassword(txtMail, txtPassword).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String userId = auth.getCurrentUser().getUid();

                            usuario = new Usuario();
                            usuario.setId(userId);
                            usuario.setNombre(txtNombre);
                            usuario.setApellidos(txtApellidos);
                            usuario.setFechaNac(txtFechaNac);
                            usuario.setRol(txtRol);

                            databaseReference.child(userId).setValue(usuario, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error == null) {
                                        // Éxito
                                        Toast.makeText(RegisterActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Error
                                        Toast.makeText(RegisterActivity.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthUserCollisionException e){
                                editMail.setError("El email ya se encuentra registrado");
                                editMail.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

}

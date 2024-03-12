package com.example.eventhub;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editNombre, editFechaNac, editApellidos,
                    editRol, editPassword, editPasswordRepeat, editMail;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRol;
    private RadioButton radioButtonRol;
    private Button buttonRegister = findViewById(R.id.btnRegistrar);

    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


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

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seleccionar = radioGroupRol.getCheckedRadioButtonId();
                radioButtonRol = findViewById(seleccionar);

                String txtNombre = editNombre.getText().toString();
                String txtApellidos = editApellidos.getText().toString();
                String txtFechaNac = editFechaNac.getText().toString();
                String txtMail = editMail.getText().toString();
                String txtPassword = editPassword.getText().toString();
                String txtPasswordRepeat = editPasswordRepeat.getText().toString();
                String txtRol;


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
                } else if (TextUtils.isEmpty(txtPassword)) {
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

                    registrarUsuario(txtNombre, txtApellidos, txtMail, txtFechaNac, txtRol, txtPassword);
                }
            }
        });

    }

    private void registrarUsuario(String txtNombre, String txtApellidos, String txtMail, String txtFechaNac,
                                  String txtRol, String txtPassword) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(txtMail, txtPassword).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Registrado correctamente", Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(txtNombre).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            Usuario usuario = new Usuario(txtNombre, txtApellidos, txtRol, txtFechaNac);

                            DatabaseReference referenceUsuario = FirebaseDatabase.getInstance().getReference("usuarios");
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            referenceUsuario.child(firebaseUser.getUid()).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente.", Toast.LENGTH_LONG).show();

                                    }else {
                                        Toast.makeText(RegisterActivity.this, "Registro incorrecto. Intentalo de nuevo", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
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
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

}
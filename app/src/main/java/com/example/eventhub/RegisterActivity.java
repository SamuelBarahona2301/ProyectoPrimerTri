package com.example.eventhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RegisterActivity extends AppCompatActivity {

    private EditText editNombre, editNickName, editFechaNac, editTelefono, editDni, editApellidos,
                    editRol, editPassword;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Registro");
/*
        editNombre = findViewById(R.id.editName);
        editApellidos = findViewById(R.id.editApellidos);
        editFechaNac = findViewById(R.id.editFechaNacimiento);
        editNickName = findViewById(R.id.editNickName);
        editPassword = findViewById(R.id.editPassword);

        radioGroup = findViewById(R.id.rdbtnRol);*/

    }
}
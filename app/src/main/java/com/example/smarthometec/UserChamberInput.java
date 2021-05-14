package com.example.smarthometec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Actividad para ingresar un nuevo aposento.
 */
public class UserChamberInput extends AppCompatActivity {
    Button btn;
    EditText etName;
    String correo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chamber_input);

        correo = getIntent().getStringExtra("email");

        btn = findViewById(R.id.addBttn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName = findViewById(R.id.nombreApo);
                String nombre = etName.getText().toString();
                if(!nombre.equals("")){
                    //IF nombre ya está registrado THEN no pueden haber dos aposentos con el mismo nombre
                    Intent intent = new Intent();
                    intent.putExtra("apo", nombre);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }else{
                    Toast.makeText(UserChamberInput.this, "Por favor defina un nombre.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
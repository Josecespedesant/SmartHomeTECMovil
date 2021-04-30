package com.example.smarthometec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthometec.ui.database.DatabaseHandler;
import com.example.smarthometec.ui.database.User;

public class MainActivity extends AppCompatActivity {
    EditText etEmail, etPass;
    Button loginbtn;
    TextView register;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHandler(this);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        loginbtn = findViewById(R.id.loginbtn);
        register = findViewById(R.id.register);

        loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email,pass;
                email = etEmail.getText().toString();
                pass = etPass.getText().toString();

                if(email.equals("")){
                    Toast.makeText(MainActivity.this,"Correo requerido para iniciar sesión.", Toast.LENGTH_SHORT).show();
                }else if(pass.equals("")){
                    Toast.makeText(MainActivity.this,"Contraseña requerida para iniciar sesión.", Toast.LENGTH_SHORT).show();
                }else if(db.checkUser(email) == false){
                    Toast.makeText(MainActivity.this,"Este correo no se encuentra registrado, por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                }else if(db.getUser(email)!=null){
                    User usr = db.getUser(email);
                    if(!usr.getPass().equals(pass)){
                        Toast.makeText(MainActivity.this,"Contraseña incorrecta, inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent i = new Intent(MainActivity.this, Menu.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Register.class);
                startActivity(i);
                finish();
            }
        });

    }
}
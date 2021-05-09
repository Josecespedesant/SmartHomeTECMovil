package com.example.smarthometec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthometec.ui.database.DatabaseHandler;
import com.example.smarthometec.ui.database.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText etEmail, etPass;
    Button loginbtn;
    TextView register;
    DatabaseHandler db;
    String email, pass;
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
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL("http://192.168.0.14/api/login/verificar");

                                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                    con.setRequestMethod("POST");

                                    con.setRequestProperty("Content-Type", "application/json; utf-8");
                                    con.setRequestProperty("Accept", "application/json");

                                    con.setDoOutput(true);

                                    String jsonInputString = "{\"Nombre\":" + "\"" + "\"" + "," +
                                            "\"Apellido\":" + "\"" +  "\"" + "," +
                                            "\"Correo\":" + "\"" + email + "\"" + "," +
                                            "\"Contraseña\":" + "\"" + pass + "\"" + "," +
                                            "\"Direccion\":" + "\"" +  "\"" + "," +
                                            "\"Continente\":" + "\""  + "\"" + "," +
                                            "\"Pais\":" + "\"" + "\"" + "}";

                                    try (OutputStream os = con.getOutputStream()) {
                                        byte[] input = jsonInputString.getBytes("utf-8");
                                        os.write(input, 0, input.length);
                                    }

                                    int code = con.getResponseCode();
                                    System.out.println(code);

                                    try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                                        StringBuilder response = new StringBuilder();
                                        String responseLine = null;
                                        while ((responseLine = br.readLine()) != null) {
                                            response.append(responseLine.trim());
                                        }
                                        System.out.println(response.toString());
                                        if(response.toString().equals("\"Correcto\"")){
                                            Intent i = new Intent(MainActivity.this, Menu.class);
                                            i.putExtra("email",email);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
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
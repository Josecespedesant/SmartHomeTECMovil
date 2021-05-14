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

import com.example.smarthometec.ui.database.Aposento;
import com.example.smarthometec.ui.database.DatabaseHandler;
import com.example.smarthometec.ui.database.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Actividad del login.
 */
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
                }else{
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

                                String jsonInputString = "{\"Nombre\":" + "null" + "," +
                                        "\"Apellido\":" + "null" + "," +
                                        "\"Correo\":" + "\"" + email + "\"" + "," +
                                        "\"Contrasena\":" + "\"" + pass + "\"" + "," +
                                        "\"Direccion\":" + "null" + "," +
                                        "\"Continente\":" + "null" + "," +
                                        "\"Pais\":" + "null" + "}";

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
                                    if(response.toString().equals("\"Correcto\"")){
                                        if(db.checkUser(email) == false){
                                            try {
                                                User user = new User();
                                                URL getuser = new URL("http://192.168.0.14/api/login/PerfilUsuario");

                                                HttpURLConnection congetuser = (HttpURLConnection) getuser.openConnection();
                                                congetuser.setRequestMethod("POST");

                                                congetuser.setRequestProperty("Content-Type", "application/json; utf-8");
                                                congetuser.setRequestProperty("Accept", "application/json");
                                                congetuser.setDoOutput(true);

                                                try (OutputStream os2 = congetuser.getOutputStream()) {
                                                    byte[] input = jsonInputString.getBytes("utf-8");
                                                    os2.write(input, 0, input.length);
                                                }

                                                int codecongetuser = congetuser.getResponseCode();
                                                System.out.println(codecongetuser);

                                                try (BufferedReader br2 = new BufferedReader(new InputStreamReader(congetuser.getInputStream(), "utf-8"))) {
                                                    StringBuilder response2 = new StringBuilder();
                                                    String responseLine2 = null;
                                                    while ((responseLine2 = br2.readLine()) != null) {
                                                        response2.append(responseLine2.trim());
                                                    }
                                                    System.out.println(response2.toString());
                                                    StringBuilder sb = new StringBuilder(response2.toString());
                                                    sb.deleteCharAt(response2.toString().length()-1);
                                                    sb.deleteCharAt(0);
                                                    String stringArreglado = sb.toString();
                                                    JSONObject obj = new JSONObject(stringArreglado);
                                                    user.setName(obj.getString("Nombre"));
                                                    user.setLastName(obj.getString("Apellido"));
                                                    user.setEmail(obj.getString("Correo"));
                                                    user.setPass(obj.getString("Contrasena"));
                                                    user.setAddress(obj.getString("Direccion"));
                                                    user.setContinent(obj.getString("Continente"));
                                                    user.setCountry(obj.getString("Pais"));
                                                    db.addUser(user);

                                                    Aposento sala = new Aposento();
                                                    Aposento dormitorio = new Aposento();
                                                    Aposento comedor = new Aposento();
                                                    Aposento cocina = new Aposento();

                                                    sala.setName("Sala");
                                                    sala.setUserCorreo(email);

                                                    dormitorio.setName("Dormitorio");
                                                    dormitorio.setUserCorreo(email);

                                                    comedor.setName("Comedor");
                                                    comedor.setUserCorreo(email);

                                                    cocina.setName("Cocina");
                                                    cocina.setUserCorreo(email);

                                                    db.addAposento(sala);
                                                    db.addAposento(dormitorio);
                                                    db.addAposento(comedor);
                                                    db.addAposento(cocina);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                        Intent i = new Intent(MainActivity.this, Menu.class);
                                        i.putExtra("email",email);
                                        startActivity(i);
                                        finish();

                                    }else{
                                        runOnUiThread(()->{
                                            Toast.makeText(MainActivity.this,"Correo o contraseña incorrecta.", Toast.LENGTH_SHORT).show();
                                        });
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
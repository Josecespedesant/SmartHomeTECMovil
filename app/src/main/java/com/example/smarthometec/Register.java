package com.example.smarthometec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.smarthometec.ui.database.Aposento;
import com.example.smarthometec.ui.database.DatabaseHandler;
import com.example.smarthometec.ui.database.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText etName, etLastName, etAddres, etEmail, etPass, etConfirmPass;
    Spinner continents, countries;
    Button register;
    String continentText, countryText;
    ArrayAdapter<CharSequence> adaptercountry;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DatabaseHandler(this);
        //EditText
        etName = findViewById(R.id.etNameR);
        etLastName = findViewById(R.id.etApellidoR);
        etAddres = findViewById(R.id.etDireccionR);
        etEmail = findViewById(R.id.etEmailR);
        etPass = findViewById(R.id.etPassR);
        etConfirmPass = findViewById(R.id.etConfirmPassR);
        //Boton
        register = findViewById(R.id.registerBtn);
        //Configurando spinner de continentes y paises
        continents = findViewById(R.id.continentsS);
        countries = findViewById(R.id.countriesS);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.continents_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        continents.setAdapter(adapter);

        continents.setOnItemSelectedListener(this);

        //Listener
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String nameText, lastNameText, addressText,
                        emailText, passText, confirmText;
                nameText = etName.getText().toString();
                lastNameText = etLastName.getText().toString();
                addressText = etAddres.getText().toString();
                continentText = continents.getSelectedItem().toString();
                countryText = countries.getSelectedItem().toString();
                emailText = etEmail.getText().toString();
                passText = etPass.getText().toString();
                confirmText = etConfirmPass.getText().toString();
                if (emailText.equals("")) {
                    Toast.makeText(Register.this, "Se requiere el correo electrónico.", Toast.LENGTH_SHORT).show();
                }
                else if(nameText.equals("")){
                    Toast.makeText(Register.this, "Se requiere el (los) nombre (s).", Toast.LENGTH_SHORT).show();
                }
                else if(passText.equals("")){
                    Toast.makeText(Register.this, "Se requiere la contraseña.", Toast.LENGTH_SHORT).show();
                }
                else if(lastNameText.equals("")){
                    Toast.makeText(Register.this, "Se requiere el (los) apellido (s).", Toast.LENGTH_SHORT).show();
                }
                else if(addressText.equals("")){
                    Toast.makeText(Register.this, "Se requiere la dirección.", Toast.LENGTH_SHORT).show();
                }
                else if(continentText.equals("")){
                    Toast.makeText(Register.this, "Se requiere el continente.", Toast.LENGTH_SHORT).show();
                }
                else if(countryText.equals("")){
                    Toast.makeText(Register.this, "Se requiere el país.", Toast.LENGTH_SHORT).show();
                }
                else if(confirmText.equals("")){
                    Toast.makeText(Register.this, "Se requiere confirmar la contraseña.", Toast.LENGTH_SHORT).show();
                }
                else if(!passText.equals(confirmText)){
                    Toast.makeText(Register.this, "Se requiere que ambas contraseñas sean iguales.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Registrar
                    if(!db.checkUser(emailText)){
                        User user = new User(emailText,nameText,lastNameText,addressText,passText,continentText,countryText);

                        Aposento sala = new Aposento();
                        Aposento dormitorio = new Aposento();
                        Aposento comedor = new Aposento();
                        Aposento cocina = new Aposento();

                        sala.setName("sala");
                        sala.setUserCorreo(user.getEmail());

                        dormitorio.setName("dormitorio");
                        dormitorio.setUserCorreo(user.getEmail());

                        comedor.setName("comedor");
                        comedor.setUserCorreo(user.getEmail());

                        cocina.setName("cocina");
                        cocina.setUserCorreo(user.getEmail());

                        db.addUser(user);
                        db.addAposento(sala);
                        db.addAposento(dormitorio);
                        db.addAposento(comedor);
                        db.addAposento(cocina);
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL("http://192.168.0.14/api/login/Registrar");

                                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                    con.setRequestMethod("POST");

                                    con.setRequestProperty("Content-Type", "application/json; utf-8");
                                    con.setRequestProperty("Accept", "application/json");

                                    con.setDoOutput(true);

                                    String jsonInputString = "{\"Nombre\":" + "\"" + nameText + "\"" + "," +
                                            "\"Apellido\":" + "\"" + lastNameText + "\"" + "," +
                                            "\"Correo\":" + "\"" + emailText + "\"" + "," +
                                            "\"Contraseña\":" + "\"" + passText + "\"" + "," +
                                            "\"Direccion\":" + "\"" + addressText + "\"" + "," +
                                            "\"Continente\":" + "\"" + continentText + "\"" + "," +
                                            "\"Pais\":" + "\"" + countryText + "\"" + "}";
                                    System.out.println(jsonInputString);

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
                                    }
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Toast.makeText(Register.this, "Su cuenta se ha registrado exitosamente.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Register.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(Register.this, "Un usuario se encuentra haciendo uso de ese correo electrónico, inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String continentSelected = (String) adapterView.getItemAtPosition(i);
        if(continentSelected.equals("America")){
            adaptercountry = ArrayAdapter.createFromResource(this,
                    R.array.america_array, android.R.layout.simple_spinner_item);
            adaptercountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countries.setAdapter(adaptercountry);
        }else if(continentSelected.equals("Europa")){
            adaptercountry = ArrayAdapter.createFromResource(this,
                    R.array.europa_array, android.R.layout.simple_spinner_item);
            adaptercountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countries.setAdapter(adaptercountry);
        } else if(continentSelected.equals("Africa")){
            adaptercountry = ArrayAdapter.createFromResource(this,
                    R.array.africa_array, android.R.layout.simple_spinner_item);
            adaptercountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countries.setAdapter(adaptercountry);
        } else if(continentSelected.equals("Asia")){
            adaptercountry = ArrayAdapter.createFromResource(this,
                    R.array.asia_array, android.R.layout.simple_spinner_item);
            adaptercountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countries.setAdapter(adaptercountry);
        } else if(continentSelected.equals("Oceania")){
            adaptercountry = ArrayAdapter.createFromResource(this,
                    R.array.oceania_array, android.R.layout.simple_spinner_item);
            adaptercountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countries.setAdapter(adaptercountry);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
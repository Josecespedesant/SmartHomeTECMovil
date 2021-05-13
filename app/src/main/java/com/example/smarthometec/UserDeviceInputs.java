package com.example.smarthometec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.smarthometec.ui.aposentos.AposentosFragment;
import com.example.smarthometec.ui.database.DatabaseHandler;
import com.example.smarthometec.ui.database.Dispositivo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserDeviceInputs extends AppCompatActivity {

    Button btn;
    EditText etDescr, etTipo, etMarca, etNumero, etConsumo;
    Spinner dropdown;
    String itemselected, email;
    String descr,tipo,marca,numeroserie,consumo;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_device_inputs);
        dropdown  = findViewById(R.id.spinner1);
        dropdown.setAdapter(AposentosFragment.arrayAdapter);
        email = getIntent().getStringExtra("email");

        db = new DatabaseHandler(this);

        btn = findViewById(R.id.addBttnDevice);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etDescr = findViewById(R.id.descripcionDevice);
                etTipo  = findViewById(R.id.tipoDevice);
                etMarca  = findViewById(R.id.marcaDevice);
                etNumero  = findViewById(R.id.numeroDevice);
                etConsumo  = findViewById(R.id.consumoDispositivo);
                itemselected = dropdown.getSelectedItem().toString();

                 descr = etDescr.getText().toString();
                 tipo = etTipo.getText().toString();
                 marca = etMarca.getText().toString();
                 numeroserie = etNumero.getText().toString();
                 consumo = etConsumo.getText().toString();


                if(etDescr.equals("")){
                    Toast.makeText(UserDeviceInputs.this, "Por favor defina una descripción.", Toast.LENGTH_SHORT).show();
                }else if(etTipo.equals("")){
                    Toast.makeText(UserDeviceInputs.this, "Por favor defina un tipo.", Toast.LENGTH_SHORT).show();
                }else if(etMarca.equals("")){
                    Toast.makeText(UserDeviceInputs.this, "Por favor defina una marca.", Toast.LENGTH_SHORT).show();
                }else if(etNumero.equals("")){
                    Toast.makeText(UserDeviceInputs.this, "Por favor defina un número de serie.", Toast.LENGTH_SHORT).show();
                }else if(etConsumo.equals("")){
                    Toast.makeText(UserDeviceInputs.this, "Por favor defina un valor de consumo.", Toast.LENGTH_SHORT).show();
                }else{
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL("http://192.168.0.14/api/general/EditarDispositivo");

                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setRequestMethod("POST");

                                con.setRequestProperty("Content-Type", "application/json; utf-8");
                                con.setRequestProperty("Accept", "application/json");

                                con.setDoOutput(true);

                                String jsonInputString = "{\"Serie\":" + "\"" + numeroserie +"\"" + "," +
                                        "\"Marca\":" + "\"" + marca + "\"" + "," +
                                        "\"Consumo_Electrico\":"  + consumo  + "," +
                                        "\"Aposento\":" + "\"" + itemselected + "\"" + "," +
                                        "\"Nombre\":" + "\"" + tipo + "\"" + "," +
                                        "\"Decripcion\":" + "\"" + descr  + "\"" + "," +
                                        "\"Tiempo_Garantia\":" + "null" + "," +
                                        "\"Activo\":" + "true" + "," +
                                        "\"Historial_Duenos\":" + "null" + "," +
                                        "\"Distribuidor\":" + "null" + "," +
                                        "\"AgergadoPor\":" + "null" + "," +
                                        "\"Dueno\":" + "\"" +  email + "\"" + "}" ;

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
                                    if(response.toString().equals("\"El dispositivo ha sido actualizado \"")){
                                        Dispositivo dispositivoaux = new Dispositivo(numeroserie,descr,itemselected, email, marca, consumo);
                                        db.addDispositivo(dispositivoaux);

                                        Intent intent = new Intent();
                                        intent.putExtra("descr", descr);
                                        intent.putExtra("tipo", tipo);
                                        intent.putExtra("marca", marca);
                                        intent.putExtra("numero", numeroserie);
                                        intent.putExtra("consumo", consumo);
                                        intent.putExtra("aposento", itemselected);
                                        runOnUiThread(()->{
                                            Toast.makeText(UserDeviceInputs.this, "El dispositivo se ha agregado exitosamente.", Toast.LENGTH_SHORT).show();
                                        });
                                        setResult(Activity.RESULT_OK,intent);
                                        finish();
                                    }else if(response.toString().equals("\"El Dispositivo no se ha entontrado o esta activo\"")){
                                        runOnUiThread(()->{
                                            Toast.makeText(UserDeviceInputs.this, "El número de serie no se ha encontrado o el dispositivo está activo", Toast.LENGTH_SHORT).show();
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



    }
}
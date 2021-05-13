package com.example.smarthometec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.smarthometec.ui.database.DatabaseHandler;
import com.example.smarthometec.ui.database.Dispositivo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DeviceInfo extends AppCompatActivity {
    ListView listView;
    ArrayList<String> listItem;
    DatabaseHandler db;
    ArrayAdapter adapter;
    Switch isonswitch;
    String email;
    String aposento;
    String id;
    boolean isOn;
    int isOnInt;
    Button delete,edit;
    Dispositivo dispositivo;
    String fechain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        isOn = false;
        email = getIntent().getStringExtra("email");
        aposento = getIntent().getStringExtra("aposento");
        id = getIntent().getStringExtra("idserie");
        delete = findViewById(R.id.delete_device);
        edit = findViewById(R.id.edit_device);
        isonswitch = findViewById(R.id.turnonoffdevice);

        listView = findViewById(R.id.lista_de_atributos_dispositivo);
        db = new DatabaseHandler(this);
        listItem = new ArrayList<>();
        viewData();

        isonswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b){
                System.out.println(b);
                if(b){
                    Cursor cursor1 = db.viewDispositivoInfo(email, aposento, id);
                    if(cursor1.getCount() == 0){
                        Toast.makeText(getBaseContext(), "No hay descripción para mostrar", Toast.LENGTH_SHORT).show();
                    }else{
                        dispositivo = new Dispositivo();
                        while(cursor1.moveToNext()) {
                            dispositivo.setNumSerie(cursor1.getString(0));
                            dispositivo.setDescription(cursor1.getString(1));
                            dispositivo.setMarca(cursor1.getString(2));
                            dispositivo.setUserCorreo(cursor1.getString(3));
                            dispositivo.setAposento(cursor1.getString(4));
                            dispositivo.setConsumoElectrico(cursor1.getString(5));
                            dispositivo.setOn(1);
                        }
                        Date dateStart;
                        dateStart = Calendar.getInstance().getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = dateFormat.format(dateStart);
                        System.out.println(date);
                        dispositivo.setInit_date(date);
                        db.updateDispositivo(dispositivo);
                        System.out.println("Ahora está encendido");
                    }
                }
                else{
                    Cursor cursor2= db.viewDispositivoInfo(email, aposento, id);
                    if(cursor2.getCount() == 0){
                        Toast.makeText(getBaseContext(), "No hay descripción para mostrar", Toast.LENGTH_SHORT).show();
                    }else{
                        dispositivo = new Dispositivo();
                        while(cursor2.moveToNext()) {
                            dispositivo.setNumSerie(cursor2.getString(0));
                            dispositivo.setDescription(cursor2.getString(1));
                            dispositivo.setMarca(cursor2.getString(2));
                            dispositivo.setUserCorreo(cursor2.getString(3));
                            dispositivo.setAposento(cursor2.getString(4));
                            dispositivo.setConsumoElectrico(cursor2.getString(5));
                            dispositivo.setOn(0);
                            fechain = cursor2.getString(7);
                        }
                        System.out.println(123);
                        System.out.println(fechain);
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String pattern = "yyyy-MM-dd HH:mm:ss";

                                    SimpleDateFormat formatter1 = new SimpleDateFormat(pattern);
                                    System.out.println("fechain");
                                    System.out.println(fechain);
                                    Date dateStart = formatter1.parse(fechain);
                                    Date dateEnd = Calendar.getInstance().getTime();

                                    int minutes = (int) (dateEnd.getTime() - dateStart.getTime());

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                                    String date = simpleDateFormat.format(dateStart);
                                    System.out.println(date);

                                    URL url = new URL("http://192.168.0.14/api/general/AgregarHistorial");

                                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                    con.setRequestMethod("POST");

                                    con.setRequestProperty("Content-Type", "application/json; utf-8");
                                    con.setRequestProperty("Accept", "application/json");

                                    con.setDoOutput(true);

                                    String jsonInputString = "{\"Serie\":" + "\"" + id +"\"" + "," +
                                            "\"Fecha\":" + "\"" + date +"\"" + "," +
                                            "\"Tiempo_Encendido\":" + "\"" + (minutes/1000)/60 +"\"" +"}" ;

                                    try (OutputStream os = con.getOutputStream()) {
                                        byte[] input = jsonInputString.getBytes("utf-8");
                                        os.write(input, 0, input.length);
                                    }

                                    try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                                        StringBuilder response = new StringBuilder();
                                        String responseLine = null;
                                        while ((responseLine = br.readLine()) != null) {
                                            response.append(responseLine.trim());
                                        }
                                        if(response.toString().equals("\"El historial se ha agregado exitosamente\"")){
                                            runOnUiThread(()->{
                                                db.updateDispositivo(dispositivo);
                                            });
                                        }
                                    }
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        System.out.println("Ahora está apagado");
                    }
                }

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://192.168.0.14/api/general/Desactivar");

                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("POST");

                            con.setRequestProperty("Content-Type", "application/json; utf-8");
                            con.setRequestProperty("Accept", "application/json");

                            con.setDoOutput(true);

                            String jsonInputString = "{\"Serie\":" + "\"" + id +"\"" +"}" ;

                            try (OutputStream os = con.getOutputStream()) {
                                byte[] input = jsonInputString.getBytes("utf-8");
                                os.write(input, 0, input.length);
                            }

                            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                                StringBuilder response = new StringBuilder();
                                String responseLine = null;
                                while ((responseLine = br.readLine()) != null) {
                                    response.append(responseLine.trim());
                                }
                                System.out.println(response.toString());
                                if(response.toString().equals("\"El dispositivo ha sido desactivado \"")){
                                    runOnUiThread(()->{
                                        Toast.makeText(DeviceInfo.this, "El dispositivo ha sido eliminado exitosamente.", Toast.LENGTH_SHORT).show();
                                        db.deleteOneDevice(id, aposento, email);
                                        finish();
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
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(DeviceInfo.this, EditUserDevice.class);
                i.putExtra("email",email);
                i.putExtra("id",id);
                i.putExtra("aposento",aposento);
                startActivity(i);
            }
        });

    }

    private void viewData() {
        Cursor cursor = db.viewDispositivoInfo(email, aposento, id);
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No hay descripción para mostrar", Toast.LENGTH_SHORT).show();
        }
        else {
            while(cursor.moveToNext()) {
                listItem.add("ID: "+cursor.getString(0)); //ID
                listItem.add("Descripción: "+ cursor.getString(1)); //Descripción
                listItem.add("Marca: " + cursor.getString(2)); //Marca
                listItem.add("Usuario: "+ cursor.getString(3)); //user
                listItem.add("Aposento: " + cursor.getString(4)); //Aposento
                listItem.add("Gasto: " + cursor.getString(5) + " kW"); // kw
                isOnInt = Integer.parseInt(cursor.getString(6));
                if(isOnInt == 1){
                    isonswitch.setChecked(true);
                }else{
                    isonswitch.setChecked(false);
                }
            }

            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
            listView.setAdapter(adapter);

        }
    }
}
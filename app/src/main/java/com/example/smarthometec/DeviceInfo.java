package com.example.smarthometec;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.smarthometec.ui.database.DatabaseHandler;
import com.example.smarthometec.ui.database.Dispositivo;

import java.util.ArrayList;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        isOn = false;
        email = getIntent().getStringExtra("email");
        aposento = getIntent().getStringExtra("aposento");
        id = getIntent().getStringExtra("idserie");

        isonswitch = findViewById(R.id.turnonoffdevice);

        listView = findViewById(R.id.lista_de_atributos_dispositivo);
        db = new DatabaseHandler(this);
        listItem = new ArrayList<>();
        viewData();

        isonswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b){
                if(b){
                    Cursor cursor = db.viewDispositivoInfo(email, aposento, id);
                    if(cursor.getCount() == 0){
                        Toast.makeText(getBaseContext(), "No hay descripción para mostrar", Toast.LENGTH_SHORT).show();
                    }else{
                        Dispositivo dispositivo = new Dispositivo();
                        while(cursor.moveToNext()) {
                            dispositivo.setNumSerie(cursor.getString(0));
                            dispositivo.setDescription(cursor.getString(1));
                            dispositivo.setMarca(cursor.getString(2));
                            dispositivo.setUserCorreo(cursor.getString(3));
                            dispositivo.setAposento(cursor.getString(4));
                            dispositivo.setConsumoElectrico(cursor.getString(5));
                            dispositivo.setOn(1);
                        }
                        db.updateDispositivo(dispositivo);
                        System.out.println("Ahora está encendido");
                    }
                }
                else{
                    Cursor cursor = db.viewDispositivoInfo(email, aposento, id);
                    if(cursor.getCount() == 0){
                        Toast.makeText(getBaseContext(), "No hay descripción para mostrar", Toast.LENGTH_SHORT).show();
                    }else{
                        Dispositivo dispositivo = new Dispositivo();
                        while(cursor.moveToNext()) {
                            dispositivo.setNumSerie(cursor.getString(0));
                            dispositivo.setDescription(cursor.getString(1));
                            dispositivo.setMarca(cursor.getString(2));
                            dispositivo.setUserCorreo(cursor.getString(3));
                            dispositivo.setAposento(cursor.getString(4));
                            dispositivo.setConsumoElectrico(cursor.getString(5));
                            dispositivo.setOn(0);
                        }
                        db.updateDispositivo(dispositivo);
                        System.out.println("Ahora está apagado");
                    }
                }

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
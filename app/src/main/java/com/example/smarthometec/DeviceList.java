package com.example.smarthometec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.smarthometec.ui.database.DatabaseHandler;

import java.util.ArrayList;

public class DeviceList extends AppCompatActivity {

    ListView listView;
    ArrayList<String> listItem;
    DatabaseHandler db;
    ArrayAdapter adapter;
    String email;
    String aposento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        email = getIntent().getStringExtra("email");
        aposento = getIntent().getStringExtra("aposento");

        listView = findViewById(R.id.lista_de_dispositivos);
        db = new DatabaseHandler(this);
        listItem = new ArrayList<>();
        viewData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(DeviceList.this,DeviceInfo.class);

                intent.putExtra("email", email);
                intent.putExtra("aposento",aposento);
                intent.putExtra("idserie",id);
                startActivity(intent);
            }
        });




    }

    private void viewData() {
        Cursor cursor = db.viewDispositivos(email, aposento);
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No hay dispositivos para mostrar", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                listItem.add(cursor.getString(0));

            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
            listView.setAdapter(adapter);

        }
    }
}
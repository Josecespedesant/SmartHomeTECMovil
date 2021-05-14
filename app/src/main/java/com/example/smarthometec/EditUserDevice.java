package com.example.smarthometec;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
/**
 * Actividad que permite la edición de un dispositivo.
 */
public class EditUserDevice extends AppCompatActivity {
    Button btn;
    EditText etDescr, etTipo, etMarca, etConsumo;
    String id, email, aposento;
    DatabaseHandler db;
    TextView idNombre;
    Cursor cursor2;
    Dispositivo dispositivo;
    String fechain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_device);
        id = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        aposento = getIntent().getStringExtra("aposento");
        idNombre = findViewById(R.id.deviceIdTextView);
        btn = findViewById(R.id.editDeviceBttn);
        idNombre.setText(id);
        db = new DatabaseHandler(this);
        dispositivo = new Dispositivo();
        cursor2 = db.viewDispositivoInfo(email, aposento, id);
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etDescr = findViewById(R.id.editDescriptionDevice);
                etTipo = findViewById(R.id.editTypeDevice);
                etMarca = findViewById(R.id.editDeviceMarca);
                etConsumo = findViewById(R.id.editConsumoDispositivo);
                if(etDescr.getText().toString().equals("")){
                    Toast.makeText(EditUserDevice.this, "Por favor defina una descripción.", Toast.LENGTH_SHORT).show();
                }else if(etTipo.getText().toString().equals("")){
                    Toast.makeText(EditUserDevice.this, "Por favor defina un tipo.", Toast.LENGTH_SHORT).show();
                }else if(etMarca.getText().toString().equals("")){
                    Toast.makeText(EditUserDevice.this, "Por favor defina una marca.", Toast.LENGTH_SHORT).show();
                }else if(etConsumo.getText().toString().equals("")){
                    Toast.makeText(EditUserDevice.this, "Por favor defina el consumo.", Toast.LENGTH_SHORT).show();
                }else{
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL("http://192.168.0.14/api/general/EditarDispositivoAdmin");

                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setRequestMethod("POST");

                                con.setRequestProperty("Content-Type", "application/json; utf-8");
                                con.setRequestProperty("Accept", "application/json");

                                con.setDoOutput(true);

                                String jsonInputString = "{\"Serie\":" + "\"" + id +"\"" + "," +
                                        "\"Marca\":" + "\"" + etMarca.getText().toString() + "\"" + "," +
                                        "\"Consumo_Electrico\":"  + etConsumo.getText().toString()  + "," +
                                        "\"Aposento\":" + "null" + "," +
                                        "\"Nombre\":" + "\"" + etTipo.getText().toString() + "\"" + "," +
                                        "\"Decripcion\":" + "\"" + etDescr.getText().toString()  + "\"" + "," +
                                        "\"Tiempo_Garantia\":" + "null" + "," +
                                        "\"Activo\":" + "true" + "," +
                                        "\"Historial_Duenos\":" + "null" + "," +
                                        "\"Distribuidor\":" + "null" + "," +
                                        "\"AgergadoPor\":" + "null" + "," +
                                        "\"Dueno\":" + "\"" +  email+ "\"" + "}" ;
                                System.out.println(jsonInputString);
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
                                    if(response.toString().equals("\"El dispositivo ha sido actualizado \"")){
                                        runOnUiThread(()->{
                                            Toast.makeText(EditUserDevice.this, "El dispositivo ha sido editado exitosamente.", Toast.LENGTH_SHORT).show();
                                            db.deleteOneDevice(id, aposento, email);
                                            Dispositivo dispositivoaux = new Dispositivo(id,etDescr.getText().toString(),aposento, email, etMarca.getText().toString(), etConsumo.getText().toString(), dispositivo.isOn(), fechain);
                                            db.updateDispositivo(dispositivoaux);
                                            db.close();
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
            }
        });

    }
}
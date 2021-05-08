package com.example.smarthometec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthometec.ui.database.DatabaseHandler;
import com.example.smarthometec.ui.database.Dispositivo;

public class EditUserDevice extends AppCompatActivity {
    Button btn;
    EditText etDescr, etTipo, etMarca, etConsumo;
    String id, email, aposento;
    DatabaseHandler db;
    TextView idNombre;
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
                    Dispositivo dispositivoaux = new Dispositivo(id,etDescr.getText().toString(),aposento, email, etMarca.getText().toString(), etConsumo.getText().toString());
                    db.updateDispositivo(dispositivoaux);
                    db.close();
                    Toast.makeText(EditUserDevice.this, "La información del dispositivo se ha actualizado.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
}
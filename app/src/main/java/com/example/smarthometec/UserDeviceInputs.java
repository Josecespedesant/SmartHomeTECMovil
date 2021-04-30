package com.example.smarthometec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.smarthometec.ui.aposentos.AposentosFragment;
import com.example.smarthometec.ui.database.Dispositivo;

public class UserDeviceInputs extends AppCompatActivity {

    Button btn;
    EditText etDescr, etTipo, etMarca, etNumero, etConsumo;
    Spinner dropdown;
    String itemselected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_device_inputs);
        dropdown  = findViewById(R.id.spinner1);
        dropdown.setAdapter(AposentosFragment.arrayAdapter);

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

                String descr = etDescr.getText().toString();
                String tipo = etTipo.getText().toString();
                String marca = etMarca.getText().toString();
                int numeroserie = Integer.getInteger(etNumero.getText().toString());
                int consumo = Integer.getInteger(etConsumo.getText().toString());


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
                }

                //ELSE IF numero dispositivo no esta registrado THEN numero no registrado, intente de nuevo
                //ELSE IF nombre ya está registrado THEN no pueden haber dos aposentos con el mismo nombre
                Intent intent = new Intent();
                intent.putExtra("descr", descr);
                intent.putExtra("tipo", tipo);
                intent.putExtra("marca", marca);
                intent.putExtra("numero", numeroserie);
                intent.putExtra("consumo", consumo);
                intent.putExtra("aposento", itemselected);

                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });



    }
}
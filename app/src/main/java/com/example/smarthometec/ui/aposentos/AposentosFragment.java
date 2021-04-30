package com.example.smarthometec.ui.aposentos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smarthometec.R;
import com.example.smarthometec.UserChamberInput;
import com.example.smarthometec.UserDeviceInputs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AposentosFragment extends Fragment {

    private AposentosViewModel aposentosViewModel;
    Button addAposentoApo, addDispositivo;
    ListView listView;
    HashMap<String, List<String>> elementsOfListView; //<Nombre aposento , lista dispositivos>
    public static ArrayAdapter<String>  arrayAdapter;
    List<String> nombres;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_aposentos, container, false);

        listView = root.findViewById(R.id.lista_de_aposentos);
        addAposentoApo = root.findViewById(R.id.addAposentoBtn);
        addDispositivo = root.findViewById(R.id.addDispositivoBtn);

        elementsOfListView = new HashMap<String, List<String>>();

        List<String> listaAux = new ArrayList<String>();
        elementsOfListView.put("Dormitorio", listaAux);
        elementsOfListView.put("Cocina", listaAux);
        elementsOfListView.put("Sala", listaAux);
        elementsOfListView.put("Comedor", listaAux);
        nombres = new ArrayList<>(elementsOfListView.keySet());


        arrayAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, nombres);
        listView.setAdapter(arrayAdapter);

        addAposentoApo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserChamberInput.class);
                startActivityForResult(i, 1);
            }
        });

        addDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserDeviceInputs.class);
                startActivityForResult(i, 2);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        switch(requestCode){
            case(1):{
                if(resultCode == Activity.RESULT_OK){
                    String nombre = data.getStringExtra("apo");
                    List<String> listaAux = new ArrayList<String>();
                    elementsOfListView.put(nombre, listaAux);
                    nombres.add(nombre);
                    arrayAdapter.notifyDataSetChanged();
                }
                break;
            }
            case(2):{
                if(resultCode == Activity.RESULT_OK){
                    String descr = data.getStringExtra("descr");
                    String aposName = data.getStringExtra("aposento");
                    elementsOfListView.get(aposName).add(descr);
                    nombres = new ArrayList<>(elementsOfListView.keySet());
                    System.out.println(descr);
                }
                break;
            }
        }
    }
}
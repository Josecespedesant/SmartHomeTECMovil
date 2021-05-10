package com.example.smarthometec.ui.aposentos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smarthometec.DeviceList;
import com.example.smarthometec.Menu;
import com.example.smarthometec.R;
import com.example.smarthometec.UserChamberInput;
import com.example.smarthometec.UserDeviceInputs;
import com.example.smarthometec.ui.database.Aposento;
import com.example.smarthometec.ui.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AposentosFragment extends Fragment {

    DatabaseHandler db;

    Button addAposentoApo, addDispositivo;
    ListView listView;
    HashMap<String, List<String>> elementsOfListView; //<Nombre aposento , lista dispositivos>
    public static ArrayAdapter<String>  arrayAdapter;
    List<String> nombres;
    String email;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_aposentos, container, false);

        Menu m = (Menu) getActivity();
        email = m.email;

        db = new DatabaseHandler(root.getContext());
        viewAposentos();

        addAposentoApo = root.findViewById(R.id.addAposentoBtn);
        addDispositivo = root.findViewById(R.id.addDispositivoBtn);
        listView = root.findViewById(R.id.lista_de_aposentos);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), DeviceList.class);
                intent.putExtra("email", email);
                intent.putExtra("aposento",name);
                startActivity(intent);
            }
        });

        addAposentoApo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserChamberInput.class);
                i.putExtra("email",email);
                startActivityForResult(i, 1);
                listView.setAdapter(arrayAdapter);
            }
        });

        addDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserDeviceInputs.class);
                i.putExtra("email", email);
                startActivityForResult(i, 2);
                arrayAdapter.notifyDataSetChanged();
            }
        });
        return root;
    }

    private void viewAposentos(){
        Cursor cursor = db.viewAposentos(email);
        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(),"No hay data para mostrar.", Toast.LENGTH_SHORT).show();
        }else{
            elementsOfListView = new HashMap<String, List<String>>();
            while(cursor.moveToNext()){
                List<String> listaAux = new ArrayList<String>();
                elementsOfListView.put(cursor.getString(1), listaAux);
            }
            nombres = new ArrayList<>(elementsOfListView.keySet());
            arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, nombres);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        switch(requestCode){
            case(1):{
                if(resultCode == Activity.RESULT_OK){
                    String nombre = data.getStringExtra("apo");
                    if(!db.checkAposento(email,nombre)){
                        Menu m = (Menu) getActivity();
                        m.listaAposentos.add(nombre);
                        Aposento aposentoaux = new Aposento(nombre,email);
                        db.addAposento(aposentoaux);

                        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                        if(Build.VERSION.SDK_INT>=26){
                            ft.setReorderingAllowed(false);
                        }
                        ft.detach(this).attach(this).commit();

                        viewAposentos();
                    }else{
                        Toast.makeText(getActivity(), "Ya hay un aposento con este nombre.", Toast.LENGTH_SHORT).show();
                    }
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
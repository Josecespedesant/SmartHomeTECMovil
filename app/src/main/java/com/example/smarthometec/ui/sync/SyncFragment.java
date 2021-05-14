package com.example.smarthometec.ui.sync;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.smarthometec.Menu;
import com.example.smarthometec.R;
import com.example.smarthometec.UserDeviceInputs;
import com.example.smarthometec.ui.database.Dispositivo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
/**
 * Clase de la pantalla de sincronización para conectar los últimos cambios con postgres.
 */
public class SyncFragment extends Fragment {

    private SyncViewModel syncViewModel;
    String correo;
    ArrayList<String> listaAposentos;
    String aux;
    Button botonsincro;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        syncViewModel =
                new ViewModelProvider(this).get(SyncViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sincronizar, container, false);
        aux = "";
        Menu m = (Menu) getActivity();
        correo = m.email;
        listaAposentos = m.listaAposentos;

        botonsincro = root.findViewById(R.id.syncro_button);
        botonsincro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!listaAposentos.isEmpty()){
                    for(int i = 0; i<listaAposentos.size(); i++){
                        aux = listaAposentos.get(i);
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL("http://192.168.0.14/api/general/AgregarAposento");

                                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                    con.setRequestMethod("POST");

                                    con.setRequestProperty("Content-Type", "application/json; utf-8");
                                    con.setRequestProperty("Accept", "application/json");

                                    con.setDoOutput(true);

                                    String jsonInputString = "{\"Correo\":" + "\"" + correo +"\"" + "," +
                                            "\"Aposento\":" + "\"" + aux + "\""+"}" ;

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
                                        if(response.toString().equals("\"El Aposento se ha agregado exitosamente\"")){
                                            getActivity().runOnUiThread(()->{
                                                Toast.makeText(getActivity(), "El Aposento se ha agregado exitosamente", Toast.LENGTH_SHORT).show();
                                                try {
                                                    Thread.sleep(500);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            });
                                        }else if(response.toString().equals("\"El Aposento ya ha sido ingresado\"")){
                                            getActivity().runOnUiThread(()->{
                                                Toast.makeText(getActivity(), "El aposento ya ha sido ingresado.", Toast.LENGTH_SHORT).show();
                                                try {
                                                    Thread.sleep(500);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
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

            }
        });

        return root;
    }
}
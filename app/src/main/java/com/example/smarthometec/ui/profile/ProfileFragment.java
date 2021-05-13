package com.example.smarthometec.ui.profile;

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

import com.example.smarthometec.MainActivity;
import com.example.smarthometec.Menu;
import com.example.smarthometec.R;
import com.example.smarthometec.Register;
import com.example.smarthometec.UserDeviceInputs;
import com.example.smarthometec.ui.database.Aposento;
import com.example.smarthometec.ui.database.DatabaseHandler;
import com.example.smarthometec.ui.database.Dispositivo;
import com.example.smarthometec.ui.database.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ProfileFragment extends Fragment {
    TextView userNameTV, lastNameTV, emailTV, dirTV;
    private ProfileViewModel profileViewModel;
    DatabaseHandler db;
    Button deleteacc;

    private static final String ACCEPT_PROPERTY = "application/geo+json;version=1";
    private static final String USER_AGENT_PROPERTY = "xxxx.com (xxxxxxxxx@gmail.com)";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        deleteacc = root.findViewById(R.id.delete_account);


        userNameTV = root.findViewById(R.id.nombreUsuario);
        lastNameTV = root.findViewById(R.id.apellidoUsuario);
        emailTV = root.findViewById(R.id.correoElectronicoUsuario);
        dirTV = root.findViewById(R.id.direccionUsuario);

        db = new DatabaseHandler(root.getContext());

        Menu m = (Menu) getActivity();
        User user = db.getUser(m.email);

        userNameTV.setText(user.getName());
        lastNameTV.setText(user.getLastName());
        emailTV.setText(user.getEmail());
        dirTV.setText(user.getAddress());

        deleteacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {//CORREGIR PORQUE NO SE BORRAN LOS APOSENTOS
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://192.168.0.14/api/login/BorrarUsuario");

                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("POST");

                            con.setRequestProperty("Content-Type", "application/json; utf-8");
                            //con.setRequestProperty("Accept", "application/json");

                            con.setRequestProperty("Accept", ACCEPT_PROPERTY);  // added
                            con.setRequestProperty("User-Agent", USER_AGENT_PROPERTY); // added

                            con.setDoOutput(true);


                            String jsonInputString =
                                    "{\"Nombre\":" +   "null"  + "," +
                                    "\"Apellido\":" +  "null" +  "," +
                                    "\"Correo\":" + "\"" + m.email + "\"" + "," +
                                    "\"Contrasena\":" + "null"  + "," +
                                    "\"Direccion\":" + "null"  + "," +
                                    "\"Continente\":" + "null"  + "," +
                                    "\"Pais\":" + "null"  + "}";

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
                                if(response.toString().equals("\"El usuario ha sido eliminado exitosamente\"")){
                                    db.deleteAposento(new Aposento("", user.getEmail()));
                                    db.deleteDispositivo(new Dispositivo(user.getEmail()));
                                    db.deleteUser(user);
                                    getActivity().runOnUiThread(()->{
                                        Toast.makeText(getContext(), "Su cuenta se ha eliminado exitosamente.", Toast.LENGTH_SHORT).show();
                                    });
                                    Intent i = new Intent(getActivity(),MainActivity.class);
                                    startActivity(i);
                                    getActivity().finish();
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
        return root;
    }
}
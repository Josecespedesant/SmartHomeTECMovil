package com.example.smarthometec.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.smarthometec.MainActivity;
import com.example.smarthometec.Menu;
import com.example.smarthometec.R;
import com.example.smarthometec.ui.database.DatabaseHandler;
import com.example.smarthometec.ui.database.User;


public class ProfileFragment extends Fragment {
    TextView userNameTV, lastNameTV, emailTV, dirTV;
    private ProfileViewModel profileViewModel;
    DatabaseHandler db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

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

        return root;
    }
}
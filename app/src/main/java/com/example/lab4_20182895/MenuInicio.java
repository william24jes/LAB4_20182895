package com.example.lab4_20182895;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab4_20182895.databinding.MenuInicioBinding;

public class MenuInicio extends AppCompatActivity {

    MenuInicioBinding menuInicioBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuInicioBinding = MenuInicioBinding.inflate(getLayoutInflater());
        setContentView(menuInicioBinding.getRoot());
        EdgeToEdge.enable(this);

        menuInicioBinding.botonIngresar.setOnClickListener(v -> {
            if (!tengoInternet()) {
                mostrarDialogoSinInternet();
            } else {
                startActivity(new Intent(MenuInicio.this, NavigationActivity.class));
            }
        });
    }

    public boolean tengoInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void mostrarDialogoSinInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No hay conexión a Internet");
        builder.setMessage("Para utilizar esta aplicación es necesario tener una conexión a Internet.");
        builder.setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Abrir la configuración de red del dispositivo
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cerrar la actividad o realizar alguna otra acción si se desea
                // Por ejemplo, puedes llamar a finish() para cerrar la actividad
                finish();
            }
        });
        builder.setCancelable(false); // Evita que se pueda cerrar el cuadro de diálogo con clic fuera de él
        builder.show();
    }
}

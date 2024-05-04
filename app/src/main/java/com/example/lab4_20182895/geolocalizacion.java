package com.example.lab4_20182895;

import static android.content.Context.SENSOR_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lab4_20182895.Dto.DtoCiudad;
import com.example.lab4_20182895.Recycle.GeolocalizacionAdapter;
import com.example.lab4_20182895.Services.CiudadService;
import com.example.lab4_20182895.databinding.GeolocalizacionBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link geolocalizacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class geolocalizacion extends Fragment {

    GeolocalizacionBinding geolocalizacionBinding;
    CiudadService ciudadService;
    private List<DtoCiudad> ciudadesBuscadas = new ArrayList<>(); // Lista de todas las ciudades buscadas
    SensorManager mSensorManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public geolocalizacion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment geolocalizacion.
     */
    // TODO: Rename and change types and number of parameters
    public static geolocalizacion newInstance(String param1, String param2) {
        geolocalizacion fragment = new geolocalizacion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        geolocalizacionBinding = GeolocalizacionBinding.inflate(inflater, container, false);

        mSensorManager = (SensorManager) requireContext().getSystemService(SENSOR_SERVICE);

        if(mSensorManager != null){ //validar si tengo sensores

            Sensor acelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Sensor magnetometro = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

            if(acelerometer != null) { //validar un sensor en particular
                Toast.makeText(requireContext(), "Sí tiene acelerómetro", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(requireContext(), "Su equipo no dispone de acelerómetro",Toast.LENGTH_SHORT).show();
            }

            if(magnetometro != null) { //validar un sensor en particular
                Toast.makeText(requireContext(), "Sí tiene magnetometro", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(requireContext(), "Su equipo no dispone de magnetometro",Toast.LENGTH_SHORT).show();
            }

            List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
            for(Sensor sensor : sensorList){
                Log.d("msg-test-sensorList","sensorName: " + sensor.getName());
            }
        }else{
            Toast.makeText(requireContext(), "Su dispositivo no posee sensores :(", Toast.LENGTH_SHORT).show();
        }


        //API
        ciudadService = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CiudadService.class);
        //

        geolocalizacionBinding.botonBuscarGeo.setOnClickListener(view -> {
            fetchInfo(geolocalizacionBinding.buscarCiudad.getQuery().toString());
        });

        NavController navController = NavHostFragment.findNavController(geolocalizacion.this);
        geolocalizacionBinding.climaDeGeo.setOnClickListener(view -> {
            navController.navigate(R.id.action_geolocalizacion_to_clima);
        });
        // Inflate the layout for this fragment
        return geolocalizacionBinding.getRoot();
    }

    public void fetchInfo(String ciudad) {
        if (tengoInternet()) {
            ciudadService.getCiudad(ciudad, "1", "8dd6fc3be19ceb8601c2c3e811c16cf1").enqueue(new Callback<List<DtoCiudad>>() {
                @Override
                public void onResponse(Call<List<DtoCiudad>> call, Response<List<DtoCiudad>> response) {
                    if (response.isSuccessful()) {
                        List<DtoCiudad> nuevasCiudades = response.body();
                        if (nuevasCiudades != null && !nuevasCiudades.isEmpty()) {
                            // Agregar las nuevas ciudades a la lista acumulativa
                            ciudadesBuscadas.addAll(nuevasCiudades);

                            // Actualizar el adaptador con todas las ciudades buscadas
                            GeolocalizacionAdapter geolocalizacionAdapter = new GeolocalizacionAdapter();
                            geolocalizacionAdapter.setListaCiudad(ciudadesBuscadas);
                            geolocalizacionBinding.recycleGeo.setAdapter(geolocalizacionAdapter);
                            geolocalizacionBinding.recycleGeo.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<DtoCiudad>> call, Throwable t) {
                    Log.e("fetchInfo", "Error al realizar la solicitud: " + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Log.d("fetchInfo", "No hay conexión a internet");
        }
    }

    public boolean tengoInternet() {
        // Obtener la actividad asociada al Fragment
        Context context = getActivity();
        if (context != null) {
            // Obtener el servicio ConnectivityManager desde el contexto de la actividad
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                // Obtener información de la red
                NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }
        return false; // Si no se pudo obtener el servicio o no hay conexión, retornar falso
    }
}

package com.example.lab4_20182895;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
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
import com.example.lab4_20182895.Dto.DtoClima;
import com.example.lab4_20182895.Recycle.ClimaAdapter;
import com.example.lab4_20182895.Recycle.GeolocalizacionAdapter;
import com.example.lab4_20182895.Services.CiudadService;
import com.example.lab4_20182895.Services.ClimaService;
import com.example.lab4_20182895.databinding.ClimaBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link clima#newInstance} factory method to
 * create an instance of this fragment.
 */
public class clima extends Fragment implements SensorEventListener {

    ClimaBinding climaBinding;
    ClimaService climaService;
    private List<DtoClima> climasBuscados = new ArrayList<>();
    SensorManager mSensorManager;

    private String direccion;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public clima() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment clima.
     */
    // TODO: Rename and change types and number of parameters
    public static clima newInstance(String param1, String param2) {
        clima fragment = new clima();
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

        climaBinding= ClimaBinding.inflate(inflater, container, false);

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
        climaService = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ClimaService.class);
        //

        climaBinding.botonBuscarClima.setOnClickListener(view -> {
            fetchInfo(climaBinding.buscarLatitud.getQuery().toString(), climaBinding.buscarLongitud.getQuery().toString());
        });

        NavController navController = NavHostFragment.findNavController(clima.this);
        climaBinding.geoDeClima.setOnClickListener(view -> {
            navController.navigate(R.id.action_clima_to_geolocalizacion);
        });

        // Inflate the layout for this fragment
        return climaBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        Sensor mMagnetometro = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, mMagnetometro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop() {
        super.onStop();

        mSensorManager.unregisterListener(this);
    }

    public void fetchInfo(String lat, String lon) {
        if (tengoInternet()) {
            climaService.getClima(lat, lon, "792edf06f1f5ebcaf43632b55d8b03fe").enqueue(new Callback<DtoClima>() {
                @Override
                public void onResponse(Call<DtoClima> call, Response<DtoClima> response) {
                    if (response.isSuccessful()) {
                        DtoClima dtoClima = response.body();
                        DtoClima.Wind wind=new DtoClima.Wind();
                        wind.setDeg(getDireccion());
                        if (dtoClima != null) {
                            // Agregar el nuevo objeto DtoClima a la lista
                            dtoClima.setWind(wind);
                            climasBuscados.add(dtoClima);


                            // Actualizar el RecyclerView con la lista actualizada
                            ClimaAdapter climaAdapter = new ClimaAdapter();
                            climaAdapter.setListaClima(climasBuscados);
                            climaBinding.recycleClima.setAdapter(climaAdapter);
                            climaBinding.recycleClima.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                }

                @Override
                public void onFailure(Call<DtoClima> call, Throwable t) {
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        if(sensorType == Sensor.TYPE_MAGNETIC_FIELD){
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];

            // Calcular la dirección del viento basada en los datos del magnetómetro
            String windDirection = calculateWindDirection(x, y);
            System.out.println(x+" "+y);
            Log.i("TAG", x+" "+y);

            // Actualizar la vista con la dirección del viento calculada
            updateWindDirectionInView(windDirection);
        }
    }

    private String calculateWindDirection(float x, float y) {
        // Verificar los valores de los ejes X e Y
        if (x > 0 && y > 0) {
            return "Noreste";
        } else if (x < 0 && y > 0) {
            return "Noroeste";
        } else if (x < 0 && y < 0) {
            return "Suroeste";
        } else if (x > 0 && y < 0) {
            return "Sureste";
        } else if (x == 0 && y > 0) {
            return "Norte";
        } else if (x == 0 && y < 0) {
            return "Sur";
        } else if (x > 0 && y == 0) {
            return "Este";
        } else if (x < 0 && y == 0) {
            return "Oeste";
        } else {
            // Si ambos valores son 0, no se puede determinar la dirección
            return "Indefinido";
        }
    }


    private void updateWindDirectionInView(String windDirection) {
        setDireccion(windDirection);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
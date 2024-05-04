package com.example.lab4_20182895;

import android.content.Context;
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
public class clima extends Fragment {

    ClimaBinding climaBinding;
    ClimaService climaService;
    private List<DtoClima> climasBuscados = new ArrayList<>();

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

        //API
        climaService = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ClimaService.class);
        //

        //FALTA VERIFICAR QUE FUNCIONE CUANDO NO ESTEN VACIOS LOS DOS CAMPOS
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

    public void fetchInfo(String lat, String lon) {
        if (tengoInternet()) {
            climaService.getClima(lat, lon, "792edf06f1f5ebcaf43632b55d8b03fe").enqueue(new Callback<DtoClima>() {
                @Override
                public void onResponse(Call<DtoClima> call, Response<DtoClima> response) {
                    if (response.isSuccessful()) {
                        DtoClima dtoClima = response.body();
                        if (dtoClima != null) {
                            // Agregar el nuevo objeto DtoClima a la lista
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
}
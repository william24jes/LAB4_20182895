package com.example.lab4_20182895.Recycle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20182895.Dto.DtoCiudad;
import com.example.lab4_20182895.R;

import java.util.List;

public class GeolocalizacionAdapter extends RecyclerView.Adapter<GeolocalizacionAdapter.GeolocalizacionViewHolder> {

    private List<DtoCiudad> dtoCiudadList;
    private Context context;

    @NonNull
    @Override
    public GeolocalizacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext(); // Inicializar el contexto aquí
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_geo, parent, false);
        return new GeolocalizacionViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull GeolocalizacionViewHolder holder, int position) {
        DtoCiudad dtoCiudad = dtoCiudadList.get(position);
        holder.dtoCiudad = dtoCiudad;

        TextView recCiudad = holder.itemView.findViewById(R.id.recCiudad);
        TextView recLat = holder.itemView.findViewById(R.id.recLat);
        TextView recLong = holder.itemView.findViewById(R.id.recLong);

        recCiudad.setText(dtoCiudad.getName());
        recLat.setText("Lat: "+dtoCiudad.getLat());
        recLong.setText("Lon: "+dtoCiudad.getLon());

    }

    @Override
    public int getItemCount() {
        return dtoCiudadList.size();
    }

    public List<DtoCiudad> getListaClima() {
        return dtoCiudadList;
    }

    public void setListaCiudad(List<DtoCiudad> dtoCiudadList) {
        this.dtoCiudadList = dtoCiudadList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class GeolocalizacionViewHolder extends RecyclerView.ViewHolder {

        DtoCiudad dtoCiudad;

        public GeolocalizacionViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }






}

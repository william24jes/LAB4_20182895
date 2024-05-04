package com.example.lab4_20182895.Recycle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20182895.Dto.DtoCiudad;
import com.example.lab4_20182895.Dto.DtoClima;
import com.example.lab4_20182895.R;

import java.util.List;

public class ClimaAdapter extends RecyclerView.Adapter<ClimaAdapter.ClimaViewHolder>{
    private List<DtoClima> dtoClimaList;
    private Context context;

    @NonNull
    @Override
    public ClimaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext(); // Inicializar el contexto aquí
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_clima, parent, false);
        return new ClimaViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ClimaAdapter.ClimaViewHolder holder, int position) {
        DtoClima dtoClima = dtoClimaList.get(position);
        holder.dtoClima = dtoClima;

        TextView recCiudadClima = holder.itemView.findViewById(R.id.recCiudadClima);
        TextView recTempActual = holder.itemView.findViewById(R.id.recTempActual);
        TextView recMin = holder.itemView.findViewById(R.id.recMin);
        TextView recMax = holder.itemView.findViewById(R.id.recMax);
        TextView recViento = holder.itemView.findViewById(R.id.recViento);

        recCiudadClima.setText(dtoClima.getName());
        recTempActual.setText(dtoClima.getMain().getTemp());
        recMin.setText("Min: "+dtoClima.getMain().getTemp_min());
        recMax.setText("Max: "+dtoClima.getMain().getTemp_max());
        recViento.setText("Viento: "+dtoClima.getWind().getDeg());
    }

    @Override
    public int getItemCount() {
        return dtoClimaList.size();
    }

    public List<DtoClima> getListaClima() {
        return dtoClimaList;
    }

    public void setListaClima(List<DtoClima> dtoClimaList) {
        this.dtoClimaList = dtoClimaList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class ClimaViewHolder extends RecyclerView.ViewHolder {

        DtoClima dtoClima;

        public ClimaViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}

package com.example.lab4_20182895.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lab4_20182895.Dto.DtoCiudad;

import java.util.List;
public class GeoViewModel extends ViewModel {

    MutableLiveData<List<DtoCiudad>> listMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<List<DtoCiudad>> getListMutableLiveData() {
        return listMutableLiveData;
    }



}

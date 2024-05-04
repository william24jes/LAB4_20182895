package com.example.lab4_20182895;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab4_20182895.databinding.NavigationActivityBinding;

public class NavigationActivity extends AppCompatActivity {

    NavigationActivityBinding navigationActivityBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationActivityBinding = NavigationActivityBinding.inflate(getLayoutInflater());
        setContentView(navigationActivityBinding.getRoot());

    }
}

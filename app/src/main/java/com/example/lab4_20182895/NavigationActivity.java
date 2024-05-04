package com.example.lab4_20182895;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;


public class NavigationActivity extends AppCompatActivity {

    AppBarConfiguration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationActivityBinding = NavigationActivityBinding.inflate(getLayoutInflater());
        setContentView(navigationActivityBinding.getRoot());

    }
}

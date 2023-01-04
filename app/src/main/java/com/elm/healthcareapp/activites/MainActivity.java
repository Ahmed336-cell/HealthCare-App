package com.elm.healthcareapp.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.elm.healthcareapp.R;
import com.elm.healthcareapp.fragments.BloodFragment;
import com.elm.healthcareapp.fragments.DiseaseFragment;
import com.elm.healthcareapp.fragments.HomeFragment;
import com.elm.healthcareapp.fragments.LungFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navApp);
        bottomNavigationView.setSelectedItemId(R.id.homeIcon);
        replace(new HomeFragment());
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeIcon:
                        replace(new HomeFragment());
                        break;
                    case  R.id.bloodIcon:
                        replace(new BloodFragment());
                        break;
                    case R.id.lungsIcon:
                        replace(new LungFragment());
                        break;
                    case R.id.diseaseDetectionIcon:
                        replace(new DiseaseFragment());
                        break;
                    case R.id.hospitalsLocationIcon:
                        break;

                }





                return true;
            }
        });
    }

    private void replace(Fragment fragment){
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }
}
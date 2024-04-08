package com.example.matthewglausfamilymapclient.userinterface.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.matthewglausfamilymapclient.R;
import com.example.matthewglausfamilymapclient.model.DataCache;
import com.example.matthewglausfamilymapclient.userinterface.fragments.MapFragment;
import com.example.matthewglausfamilymapclient.userinterface.fragments.LoginFragment;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Iconify.with(new FontAwesomeModule());

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frameLayout);
        if(fragment == null) {
            fragment = new LoginFragment();
            ((LoginFragment) fragment).setListener(this);
            fragmentManager.beginTransaction().add(R.id.frameLayout, fragment).commit();
        }
        else {
            if(fragment instanceof LoginFragment) {
                ((LoginFragment) fragment).setListener(this);
            }
        }
    }

    @Override
    public void loginDone() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.setShowLifeStoryLines(true);
        dataCache.setShowFamilyTreeLines(true);
        dataCache.setShowSpouseLines(true);
        dataCache.setFilterFathersSide(true);
        dataCache.setFilterMothersSide(true);
        dataCache.setFilterMaleEvents(true);
        dataCache.setFilterFemaleEvents(true);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment mapFragment = new MapFragment();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, mapFragment).commit();
    }
}
package com.example.matthewglausfamilymapclient.userinterface.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.matthewglausfamilymapclient.R;
import com.example.matthewglausfamilymapclient.model.DataCache;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final DataCache dataCache = DataCache.getInstance();

        Switch lifeStoryLinesSwitch = findViewById(R.id.lifeStoryLinesSwitch);
        if(dataCache.getShowLifeStoryLines()) {
            lifeStoryLinesSwitch.setChecked(true);
        }
        lifeStoryLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dataCache.setShowLifeStoryLines(true);
                }
                else {
                    dataCache.setShowLifeStoryLines(false);
                }
            }
        });

        Switch familyTreeLinesSwitch = findViewById(R.id.familyTreeLinesSwitch);
        if(dataCache.getShowFamilyTreeLines()) {
            familyTreeLinesSwitch.setChecked(true);
        }
        familyTreeLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dataCache.setShowFamilyTreeLines(true);
                }
                else {
                    dataCache.setShowFamilyTreeLines(false);
                }
            }
        });

        Switch spouseLinesSwitch = findViewById(R.id.spouseLinesSwitch);
        if(dataCache.getShowSpouseLines()) {
            spouseLinesSwitch.setChecked(true);
        }
        spouseLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dataCache.setShowSpouseLines(true);
                }
                else {
                    dataCache.setShowSpouseLines(false);
                }
            }
        });

        Switch fathersSideFilter = findViewById(R.id.fathersSideSwitch);
        if(dataCache.getFilterFathersSide()) {
            fathersSideFilter.setChecked(true);
        }
        fathersSideFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dataCache.setFilterFathersSide(true);
                }
                else {
                    dataCache.setFilterFathersSide(false);
                }
            }
        });

        Switch mothersSideFilter = findViewById(R.id.mothersSideSwitch);
        if(dataCache.getFilterMothersSide()) {
            mothersSideFilter.setChecked(true);
        }
        mothersSideFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dataCache.setFilterMothersSide(true);
                }
                else {
                    dataCache.setFilterMothersSide(false);
                }
            }
        });

        Switch maleEventsFilter = findViewById(R.id.maleEventsSwitch);
        if(dataCache.getFilterMaleEvents()) {
            maleEventsFilter.setChecked(true);
        }
        maleEventsFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dataCache.setFilterMaleEvents(true);
                }
                else {
                    dataCache.setFilterMaleEvents(false);
                }
            }
        });

        Switch femaleEventsFilter = findViewById(R.id.femaleEventsSwitch);
        if(dataCache.getFilterFemaleEvents()) {
            femaleEventsFilter.setChecked(true);
        }
        femaleEventsFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dataCache.setFilterFemaleEvents(true);
                }
                else {
                    dataCache.setFilterFemaleEvents(false);
                }
            }
        });

        LinearLayout logoutLayout = findViewById(R.id.logoutTexts);
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}
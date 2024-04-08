package com.example.matthewglausfamilymapclient.userinterface.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.matthewglausfamilymapclient.R;
import com.example.matthewglausfamilymapclient.userinterface.fragments.MapFragment;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        String eventID = intent.getStringExtra("EventID");

        FragmentManager fm = this.getSupportFragmentManager();
        MapFragment fragment = (MapFragment)fm.findFragmentById(R.id.frameLayoutEvent);
        if (fragment == null) {
            fragment = createReceivingFragment(eventID);
            fm.beginTransaction()
                    .add(R.id.frameLayoutEvent, fragment)
                    .commit();
        }
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

    private MapFragment createReceivingFragment(String eventID) {
        MapFragment fragment = new MapFragment();

        Bundle arguments = new Bundle();
        arguments.putString("EventID", eventID);
        fragment.setArguments(arguments);

        return fragment;
    }
}
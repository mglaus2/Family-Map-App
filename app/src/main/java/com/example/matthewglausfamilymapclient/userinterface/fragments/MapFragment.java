package com.example.matthewglausfamilymapclient.userinterface.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.matthewglausfamilymapclient.R;
import com.example.matthewglausfamilymapclient.model.DataCache;
import com.example.matthewglausfamilymapclient.userinterface.activities.PersonActivity;
import com.example.matthewglausfamilymapclient.userinterface.activities.SearchActivity;
import com.example.matthewglausfamilymapclient.userinterface.activities.SettingsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Event;
import model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback {
    private static final float BIRTH_COLOR = BitmapDescriptorFactory.HUE_BLUE;
    private static final float MARRIAGE_COLOR = BitmapDescriptorFactory.HUE_RED;
    private static final float DEATH_COLOR = BitmapDescriptorFactory.HUE_GREEN;
    private static final float OTHER_EVENT_COLOR = BitmapDescriptorFactory.HUE_YELLOW;
    private static final int GOOGLE_COLOR_RED = -65536;
    private static final int GOOGLE_COLOR_BLACK = -16777216;
    private static final int GOOGLE_COLOR_GRAY = -65281;

    private GoogleMap map;
    private ImageView genderImage;
    private TextView personAssociatedWithEventName;
    private TextView eventType;
    private TextView eventYear;
    private LinearLayout mapView;
    private Marker currMarker;
    private String passedEventID = null;

    private List<Polyline> listOfLines;
    private List<Marker> listOfMarkers;

    private final DataCache dataCache = DataCache.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);
        setHasOptionsMenu(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                colorRes(R.color.colorBlack).sizeDp(40);

        genderImage = view.findViewById(R.id.genderImage);
        genderImage.setImageDrawable(genderIcon);
        personAssociatedWithEventName = view.findViewById(R.id.nameOfPersonAssociatedWithEvent);
        eventType = view.findViewById(R.id.eventType);
        eventYear = view.findViewById(R.id.eventYear);
        mapView = view.findViewById(R.id.mapTextView);
        listOfLines = new ArrayList<>();
        listOfMarkers = new ArrayList<>();

        if (getArguments() != null) {
            passedEventID = getArguments().getString("EventID");
            setHasOptionsMenu(false);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        generateEvents();

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                eventClicked(marker);
                return true;
            }
        });

        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                Event event = (Event)currMarker.getTag();
                intent.putExtra("PersonID", event.getPersonID());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.searchItem);
        searchMenuItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_search)
                .colorRes(R.color.colorWhite)
                .actionBarSize());

        MenuItem settingsMenuItem = menu.findItem(R.id.settingsItem);
        settingsMenuItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear)
                .colorRes(R.color.colorWhite)
                .actionBarSize());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        Intent intent;
        switch(menu.getItemId()) {
            case R.id.searchItem:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.settingsItem:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(map != null) {
            Event currEvent = null;
            Boolean hasMarker = false;
            if(currMarker != null) {
                currEvent = (Event) currMarker.getTag();
            }
            map.clear();
            clearLines();
            clearMarkers();
            generateEvents();
            if(currMarker != null) {
                for(Marker marker : listOfMarkers) {
                    Event event = (Event)marker.getTag();
                    if(event.equals(currEvent)) {
                        eventClicked(marker);
                        hasMarker = true;
                    }
                }
                if(!hasMarker) {
                    Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                            colorRes(R.color.colorBlack).sizeDp(40);
                    genderImage.setImageDrawable(genderIcon);
                    personAssociatedWithEventName.setText(R.string.family_map_default);
                    eventType.setText("");
                    eventYear.setText("");
                    mapView.setClickable(false);
                }
            }
        }
    }

    @Override
    public void onMapLoaded() {}

    private void generateEvents() {
        Marker marker;
        List<Event> filteredEvents = dataCache.getFilteredEvents();

        for(Event tempEvent : filteredEvents) {
            String eventType = tempEvent.getEventType();
            if (eventType.equals("birth")) {
                marker = map.addMarker(new MarkerOptions().
                        position(new LatLng(tempEvent.getLatitude(), tempEvent.getLongitude())).
                        icon(BitmapDescriptorFactory.defaultMarker(BIRTH_COLOR)));
            } else if (eventType.equals("marriage")) {
                marker = map.addMarker(new MarkerOptions().
                        position(new LatLng(tempEvent.getLatitude(), tempEvent.getLongitude())).
                        icon(BitmapDescriptorFactory.defaultMarker(MARRIAGE_COLOR)));
            } else if (eventType.equals("death")) {
                marker = map.addMarker(new MarkerOptions().
                        position(new LatLng(tempEvent.getLatitude(), tempEvent.getLongitude())).
                        icon(BitmapDescriptorFactory.defaultMarker(DEATH_COLOR)));
            } else {
                marker = map.addMarker(new MarkerOptions().
                        position(new LatLng(tempEvent.getLatitude(), tempEvent.getLongitude())).
                        icon(BitmapDescriptorFactory.defaultMarker(OTHER_EVENT_COLOR)));
            }
            marker.setTag(tempEvent);
            listOfMarkers.add(marker);

            if (Objects.equals(tempEvent.getEventID(), passedEventID)) {
                map.animateCamera(CameraUpdateFactory.zoomTo(3));
                eventClicked(marker);
            }
        }
    }

    private void eventClicked(Marker marker) {
        currMarker = marker;
        Event tempEvent = (Event)currMarker.getTag();
        Person tempPerson = dataCache.getPersonByID(tempEvent.getPersonID());

        String personName = tempPerson.getFirstName() + " " + tempPerson.getLastName();
        String eventInfo = tempEvent.getEventType() + ": " + tempEvent.getCity() + ", " +
                tempEvent.getCountry();
        String year = "(" + tempEvent.getYear() + ")";

        Drawable genderIcon;
        if(tempPerson.getGender().equals("m")) {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                    colorRes(R.color.colorBlue).sizeDp(40);
        }
        else {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                    colorRes(R.color.colorPink).sizeDp(40);
        }

        genderImage.setImageDrawable(genderIcon);
        personAssociatedWithEventName.setText(personName);
        eventType.setText(eventInfo);
        eventYear.setText(year);
        mapView.setClickable(true);

        LatLng eventLocation = new LatLng(tempEvent.getLatitude(), tempEvent.getLongitude());
        map.animateCamera(CameraUpdateFactory.zoomTo(3));
        map.animateCamera(CameraUpdateFactory.newLatLng(eventLocation));

        drawAllLines(tempEvent);
    }

    private void drawAllLines(Event currEvent) {
        clearLines();

        Event spousesEarliestEvent = dataCache.getPersonsEarliestEvent(dataCache.getSpouseID(
                currEvent.getPersonID()));
        if(spousesEarliestEvent != null && dataCache.getShowSpouseLines()) {
            drawOneLine(currEvent, spousesEarliestEvent, GOOGLE_COLOR_RED, 10);
        }

        List<Event> personsLifeEvents = dataCache.getPersonsLifeEvents(currEvent.getPersonID());
        if(dataCache.getShowLifeStoryLines()) {
            Event startEvent = personsLifeEvents.get(0);
            for(Event event : personsLifeEvents) {
                if(!event.equals(startEvent)) {
                    drawOneLine(startEvent, event, GOOGLE_COLOR_BLACK, 10);
                    startEvent = event;
                }
            }
        }

        if(dataCache.getShowFamilyTreeLines()) {
            drawFamilyTreeLines(currEvent, 10);
        }
    }

    private void drawOneLine(Event startEvent, Event endEvent, int googleColor, float width) {
        LatLng startPoint = new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
        LatLng endPoint = new LatLng(endEvent.getLatitude(), endEvent.getLongitude());

        PolylineOptions options = new PolylineOptions().add(startPoint).add(endPoint).color(
                googleColor).width(width);
        Polyline line = map.addPolyline(options);
        listOfLines.add(line);
    }

    private void clearLines() {
        for(Polyline polyline : listOfLines) {
            polyline.remove();
        }
        listOfLines = new ArrayList<>();
    }

    private void clearMarkers() {
        for(Marker marker : listOfMarkers) {
            marker.remove();
        }
        listOfMarkers = new ArrayList<>();
    }

    private void drawFamilyTreeLines(Event currEvent, float width) {
        Person currPerson = dataCache.getPersonByID(currEvent.getPersonID());

        if(currPerson.getFatherID() != null) {
            drawFatherLine(currPerson, currEvent, width);
        }
        if(currPerson.getMotherID() != null) {
            drawMotherLine(currPerson, currEvent, width);
        }
    }

    private void drawFatherLine(Person currPerson, Event currEvent, float width) {
        Event fathersEarliestEvent = dataCache.getPersonsEarliestEvent(currPerson.getFatherID());
        if(fathersEarliestEvent != null) {
            drawOneLine(currEvent, fathersEarliestEvent, GOOGLE_COLOR_GRAY, width);

            drawFamilyTreeLines(fathersEarliestEvent, width / 2);
        }
    }

    private void drawMotherLine(Person currPerson, Event currEvent, float width) {
        Event mothersEarliestEvent = dataCache.getPersonsEarliestEvent(currPerson.getMotherID());
        if(mothersEarliestEvent != null) {
            drawOneLine(currEvent, mothersEarliestEvent, GOOGLE_COLOR_GRAY, width);

            drawFamilyTreeLines(mothersEarliestEvent, width / 2);
        }
    }
}

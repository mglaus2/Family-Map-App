package com.example.matthewglausfamilymapclient.userinterface.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.matthewglausfamilymapclient.R;
import com.example.matthewglausfamilymapclient.model.DataCache;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {
    private final DataCache dataCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Intent intent = getIntent();
        String personID = intent.getStringExtra("PersonID");

        List<Event> personsLifeEvents;
        personsLifeEvents = dataCache.getPersonsLifeEvents(personID);

        Person currPerson = dataCache.getPersonByID(personID);
        List<Person> personsImmediateRelatives;
        personsImmediateRelatives = dataCache.getImmediateFamily(currPerson);

        TextView personsFirstName = findViewById(R.id.personsFirstName);
        personsFirstName.setText(currPerson.getFirstName());

        TextView personsLastName = findViewById(R.id.personsLastName);
        personsLastName.setText(currPerson.getLastName());

        TextView personsGender = findViewById(R.id.personsGender);
        if (currPerson.getGender().equals("m")) {
            personsGender.setText(R.string.male);
        } else {
            personsGender.setText(R.string.female);
        }

        ExpandableListView personActivityExpandableList = findViewById(R.id.personExpandableList);
        personActivityExpandableList.setAdapter(new ExpandableListAdapter(personsLifeEvents,
                personsImmediateRelatives, currPerson));
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

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int LIFE_EVENTS_GROUP_POSITION = 0;
        private static final int IMMEDIATE_FAMILY_GROUP_POSITION = 1;

        private final List<Event> personsLifeEvents;
        private final List<Person> personsImmediateRelatives;
        private final Person currPerson;

        public ExpandableListAdapter(List<Event> personsLifeEvents, List<Person>
                personsImmediateRelatives, Person currPerson) {
            this.personsLifeEvents = personsLifeEvents;
            this.personsImmediateRelatives = personsImmediateRelatives;
            this.currPerson = currPerson;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return personsLifeEvents.size();
                case IMMEDIATE_FAMILY_GROUP_POSITION:
                    return personsImmediateRelatives.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " +
                            groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent,
                        false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    titleView.setText(R.string.lifeEventsTitle);
                    break;
                case IMMEDIATE_FAMILY_GROUP_POSITION:
                    titleView.setText(R.string.immediateFamilyTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " +
                            groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_event_item, parent,
                            false);
                    initializeLifeEventView(itemView, childPosition);
                    break;
                case IMMEDIATE_FAMILY_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_event_item, parent,
                            false);
                    initializeImmediateFamilyView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " +
                            groupPosition);
            }

            return itemView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private void initializeLifeEventView(View lifeEventView, final int childPosition) {
            Drawable eventIcon = new IconDrawable(PersonActivity.this,
                    FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorBlack).sizeDp(40);
            ImageView activityImage = lifeEventView.findViewById(R.id.activityImage);
            activityImage.setImageDrawable(eventIcon);

            Event currEvent = personsLifeEvents.get(childPosition);
            String eventInfo = currEvent.getEventType() + ": " + currEvent.getCity() +
                    ", " + currEvent.getCountry() + " (" + currEvent.getYear() + ")";
            TextView activityInfo = lifeEventView.findViewById(R.id.activityInfo);
            activityInfo.setText(eventInfo);

            String personName = currPerson.getFirstName() + " " + currPerson.getLastName();
            TextView personInfo = lifeEventView.findViewById(R.id.personRelatedInfo);
            personInfo.setText(personName);

            lifeEventView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this,
                            EventActivity.class);
                    intent.putExtra("EventID", currEvent.getEventID());
                    startActivity(intent);
                }
            });
        }

        private void initializeImmediateFamilyView(View immediateFamilyView,
                                                   final int childPosition) {
            Drawable personIcon;
            Person relatedPerson = dataCache.getPersonByID(personsImmediateRelatives.
                    get(childPosition).getPersonID());
            if(relatedPerson.getGender().equals("m")) {
                personIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).
                        colorRes(R.color.colorBlue).sizeDp(40);
            }
            else {
                personIcon = new IconDrawable(PersonActivity.this,
                        FontAwesomeIcons.fa_female).colorRes(R.color.colorPink).sizeDp(40);
            }
            ImageView activityImage = immediateFamilyView.findViewById(R.id.activityImage);
            activityImage.setImageDrawable(personIcon);

            String personName = relatedPerson.getFirstName() + " " + relatedPerson.getLastName();
            TextView personNameWidget = immediateFamilyView.findViewById(R.id.activityInfo);
            personNameWidget.setText(personName);

            String typeOfRelation = dataCache.getFamilyRelation(currPerson, relatedPerson);
            TextView personsRelation = immediateFamilyView.findViewById(R.id.personRelatedInfo);
            personsRelation.setText(typeOfRelation);

            immediateFamilyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this,
                            PersonActivity.class);
                    intent.putExtra("PersonID", relatedPerson.getPersonID());
                    startActivity(intent);
                }
            });
        }
    }
}
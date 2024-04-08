package com.example.matthewglausfamilymapclient.userinterface.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matthewglausfamilymapclient.R;
import com.example.matthewglausfamilymapclient.model.DataCache;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    private static final int EVENTS_ITEM_VIEW_TYPE = 0;
    private static final int PEOPLE_ITEM_VIEW_TYPE = 1;

    private final DataCache dataCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        EditText searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(searchBar.getText().
                        toString());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {}
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

    private class RecyclerViewAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private List<Event> eventsBySearch;
        private List<Person> peopleBySearch;

        public RecyclerViewAdapter(String searchedContent) {
            eventsBySearch = new ArrayList<>(dataCache.getEventsBySearch(searchedContent));
            peopleBySearch = new ArrayList<>(dataCache.getPeopleBySearch(searchedContent));
        }

        @Override
        public int getItemViewType(int position) {
            return position < eventsBySearch.size() ? EVENTS_ITEM_VIEW_TYPE : PEOPLE_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            view = getLayoutInflater().inflate(R.layout.person_event_item, parent,
                    false);

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if(position < eventsBySearch.size()) {
                holder.bind(eventsBySearch.get(position));
            } else {
                holder.bind(peopleBySearch.get(position - eventsBySearch.size()));
            }
        }

        @Override
        public int getItemCount() {
            return eventsBySearch.size() + peopleBySearch.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView activityImage;
        private TextView activityInfo;
        private TextView personInfo;

        private int viewType;
        private Event event;
        private Person person;

        public SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            activityImage = itemView.findViewById(R.id.activityImage);
            activityInfo = itemView.findViewById(R.id.activityInfo);
            personInfo = itemView.findViewById(R.id.personRelatedInfo);
        }

        private void bind(Event event) {
            this.event = event;
            Drawable eventIcon = new IconDrawable(SearchActivity.this,
                    FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorBlack).sizeDp(40);
            activityImage.setImageDrawable(eventIcon);

            String eventInfo = event.getEventType() + ": " + event.getCity() +
                    ", " + event.getCountry() + " (" + event.getYear() + ")";
            activityInfo.setText(eventInfo);

            person = dataCache.getPersonByID(event.getPersonID());
            String personName = person.getFirstName() + " " + person.getLastName();
            personInfo.setText(personName);
        }

        private void bind(Person person) {
            this.person = person;
            Drawable personIcon;
            if(person.getGender().equals("m")) {
                personIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).
                        colorRes(R.color.colorBlue).sizeDp(40);
            }
            else {
                personIcon = new IconDrawable(SearchActivity.this,
                        FontAwesomeIcons.fa_female).colorRes(R.color.colorPink).sizeDp(40);
            }
            activityImage.setImageDrawable(personIcon);

            String personName = person.getFirstName() + " " + person.getLastName();
            activityInfo.setText(personName);
        }

        @Override
        public void onClick(View v) {
            if(viewType == EVENTS_ITEM_VIEW_TYPE) {
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra("EventID", event.getEventID());
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra("PersonID", person.getPersonID());
                startActivity(intent);
            }
        }
    }
}
package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class MatchRegistrationsThird extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "RegistrationsPrograms";
    ListView mListView;
    private ArrayList<String> fidsReg;
    private String url = "";
    private long date1,date2;
    EditText searchFilter;
    private String fg = "";
    private String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_registrations_third);
        db = FirebaseFirestore.getInstance();
        fidsReg = (ArrayList<String>) getIntent().getSerializableExtra("FIDReg");
        date1 = getIntent().getLongExtra("Date1", date1);
        date2 = getIntent().getLongExtra("Date2", date2);
        searchFilter = findViewById(R.id.searchFilter);
        fg = getIntent().getStringExtra("FG");
        mListView = findViewById(R.id.list_view);
        status = getIntent().getStringExtra("Reg");
        collection = getIntent().getStringExtra("Collection");

        if (fg.equals("ALL")) {
            db.collection("AttendanceDemo")
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null)
                                return;

                            ArrayList<Note> details1 = new ArrayList<>();
                            ArrayList<Note> details2 = new ArrayList<>();
                            TreeMap<String, Integer> countName = new TreeMap<>();

                            Log.d("Details", "onEvent: Out");

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                if (countName.containsKey(note.getName())) {
                                    continue;
                                } else {
                                    countName.put(note.getName(),1);
                                    if (fidsReg.contains(note.getFid())) {
                                        details1.add(note);
                                    } else {
                                        details2.add(note);
                                    }
                                }
                            }

                            if (status.equals("Registered")) {
                                final DetailsAdapter adapterD = new DetailsAdapter(MatchRegistrationsThird.this, R.layout.details_layout, details1);
                                mListView.setAdapter(adapterD);

                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String name = adapterD.getItem(position).getName();

                                        Intent intent = new Intent(MatchRegistrationsThird.this, DetailsFinal.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1", date1);
                                        bundle.putLong("Date2", date2);
                                        bundle.putString("URL", adapterD.getItem(position).getUrl());
                                        bundle.putString("Name", name);
                                        bundle.putString("Phone", adapterD.getItem(position).getZmob());
                                        bundle.putString("FG", adapterD.getItem(position).getFg());
                                        bundle.putString("Program", adapterD.getItem(position).getProgram());
                                        bundle.putString("Time", adapterD.getItem(position).getTime());
                                        bundle.putString("Date", adapterD.getItem(position).getZzdate());
                                        bundle.putString("Japa", adapterD.getItem(position).getJapa());
                                        bundle.putString("Reading", adapterD.getItem(position).getZread());
                                        bundle.putString("Area", adapterD.getItem(position).getArea());
                                        bundle.putString("Session", adapterD.getItem(position).getSession());
                                        bundle.putString("URL", adapterD.getItem(position).getUrl());
                                        bundle.putString("Source", adapterD.getItem(position).getSource());
                                        bundle.putString("College", adapterD.getItem(position).getCollege());
                                        bundle.putString("Occupation", adapterD.getItem(position).getOccupation());
                                        bundle.putString("Branch", adapterD.getItem(position).getBranch());
                                        bundle.putString("Zone", adapterD.getItem(position).getZzone());
                                        bundle.putString("Organisation", adapterD.getItem(position).getOrganization());
                                        bundle.putString("TL", adapterD.getItem(position).getZtl());
                                        bundle.putString("Level", adapterD.getItem(position).getZfl());
                                        bundle.putString("Category", adapterD.getItem(position).getCategory());
                                        bundle.putString("FID", adapterD.getItem(position).getFid());
                                        bundle.putString("Collection", collection);

                                        if (adapterD.getItem(position).getRes_interest() != null)
                                            bundle.putString("Res", adapterD.getItem(position).getRes_interest());
                                        else
                                            bundle.putString("Res", "NA");
                                        if (adapterD.getItem(position).getOrigin() != null)
                                            bundle.putLong("Origin", adapterD.getItem(position).getOrigin());
                                        else
                                            bundle.putLong("Origin", 0);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });

                                searchFilter.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        adapterD.getFilter().filter(s.toString());
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
                            }

                            else {

                                final DetailsAdapter adapterD2 = new DetailsAdapter(MatchRegistrationsThird.this, R.layout.details_layout, details2);
                                mListView.setAdapter(adapterD2);

                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String name = adapterD2.getItem(position).getName();

                                        Intent intent = new Intent(MatchRegistrationsThird.this, DetailsFinal.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1", date1);
                                        bundle.putLong("Date2", date2);
                                        bundle.putString("URL", adapterD2.getItem(position).getUrl());
                                        bundle.putString("Name", name);
                                        bundle.putString("Phone", adapterD2.getItem(position).getZmob());
                                        bundle.putString("FG", adapterD2.getItem(position).getFg());
                                        bundle.putString("Program", adapterD2.getItem(position).getProgram());
                                        bundle.putString("Time", adapterD2.getItem(position).getTime());
                                        bundle.putString("Date", adapterD2.getItem(position).getZzdate());
                                        bundle.putString("Japa", adapterD2.getItem(position).getJapa());
                                        bundle.putString("Reading", adapterD2.getItem(position).getZread());
                                        bundle.putString("Area", adapterD2.getItem(position).getArea());
                                        bundle.putString("Session", adapterD2.getItem(position).getSession());
                                        bundle.putString("URL", adapterD2.getItem(position).getUrl());
                                        bundle.putString("Source", adapterD2.getItem(position).getSource());
                                        bundle.putString("College", adapterD2.getItem(position).getCollege());
                                        bundle.putString("Occupation", adapterD2.getItem(position).getOccupation());
                                        bundle.putString("Branch", adapterD2.getItem(position).getBranch());
                                        bundle.putString("Zone", adapterD2.getItem(position).getZzone());
                                        bundle.putString("Organisation", adapterD2.getItem(position).getOrganization());
                                        bundle.putString("TL", adapterD2.getItem(position).getZtl());
                                        bundle.putString("Level", adapterD2.getItem(position).getZfl());
                                        bundle.putString("Category", adapterD2.getItem(position).getCategory());
                                        bundle.putString("FID", adapterD2.getItem(position).getFid());
                                        bundle.putString("Collection", collection);

                                        if (adapterD2.getItem(position).getRes_interest() != null)
                                            bundle.putString("Res", adapterD2.getItem(position).getRes_interest());
                                        else
                                            bundle.putString("Res", "NA");
                                        if (adapterD2.getItem(position).getOrigin() != null)
                                            bundle.putLong("Origin", adapterD2.getItem(position).getOrigin());
                                        else
                                            bundle.putLong("Origin", 0);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });

                                searchFilter.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        adapterD2.getFilter().filter(s.toString());
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
                            }

                        }
                    });
        } else {
            db.collection("AttendanceDemo")
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("fg",fg)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null)
                                return;

                            ArrayList<Note> details1 = new ArrayList<>();
                            ArrayList<Note> details2 = new ArrayList<>();
                            TreeMap<String, Integer> countName = new TreeMap<>();

                            Log.d("Details", "onEvent: Out");

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                } else {
                                    url = note.getUrl();
                                }

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (countName.containsKey(note.getName())) {
                                    continue;
                                } else {
                                    countName.put(note.getName(),1);
                                    if (fidsReg.contains(note.getFid())) {
                                        details1.add(note);
                                    } else {
                                        details2.add(note);
                                    }
                                }
                            }

                            if (status.equals("Registered")) {
                                final DetailsAdapter adapterD = new DetailsAdapter(MatchRegistrationsThird.this, R.layout.details_layout, details1);
                                mListView.setAdapter(adapterD);

                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String name = adapterD.getItem(position).getName();

                                        Intent intent = new Intent(MatchRegistrationsThird.this, DetailsFinal.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1", date1);
                                        bundle.putLong("Date2", date2);
                                        bundle.putString("URL", adapterD.getItem(position).getUrl());
                                        bundle.putString("Name", name);
                                        bundle.putString("Phone", adapterD.getItem(position).getZmob());
                                        bundle.putString("FG", adapterD.getItem(position).getFg());
                                        bundle.putString("Program", adapterD.getItem(position).getProgram());
                                        bundle.putString("Time", adapterD.getItem(position).getTime());
                                        bundle.putString("Date", adapterD.getItem(position).getZzdate());
                                        bundle.putString("Japa", adapterD.getItem(position).getJapa());
                                        bundle.putString("Reading", adapterD.getItem(position).getZread());
                                        bundle.putString("Area", adapterD.getItem(position).getArea());
                                        bundle.putString("Session", adapterD.getItem(position).getSession());
                                        bundle.putString("URL", adapterD.getItem(position).getUrl());
                                        bundle.putString("Source", adapterD.getItem(position).getSource());
                                        bundle.putString("College", adapterD.getItem(position).getCollege());
                                        bundle.putString("Occupation", adapterD.getItem(position).getOccupation());
                                        bundle.putString("Branch", adapterD.getItem(position).getBranch());
                                        bundle.putString("Zone", adapterD.getItem(position).getZzone());
                                        bundle.putString("Organisation", adapterD.getItem(position).getOrganization());
                                        bundle.putString("TL", adapterD.getItem(position).getZtl());
                                        bundle.putString("Level", adapterD.getItem(position).getZfl());
                                        bundle.putString("Category", adapterD.getItem(position).getCategory());
                                        bundle.putString("FID", adapterD.getItem(position).getFid());
                                        bundle.putString("Collection", collection);

                                        if (adapterD.getItem(position).getRes_interest() != null)
                                            bundle.putString("Res", adapterD.getItem(position).getRes_interest());
                                        else
                                            bundle.putString("Res", "NA");
                                        if (adapterD.getItem(position).getOrigin() != null)
                                            bundle.putLong("Origin", adapterD.getItem(position).getOrigin());
                                        else
                                            bundle.putLong("Origin", 0);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });

                                searchFilter.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        adapterD.getFilter().filter(s.toString());
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
                            }

                            else {

                                final DetailsAdapter adapterD2 = new DetailsAdapter(MatchRegistrationsThird.this, R.layout.details_layout, details2);
                                mListView.setAdapter(adapterD2);

                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String name = adapterD2.getItem(position).getName();

                                        Intent intent = new Intent(MatchRegistrationsThird.this, DetailsFinal.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("Date1", date1);
                                        bundle.putLong("Date2", date2);
                                        bundle.putString("URL", adapterD2.getItem(position).getUrl());
                                        bundle.putString("Name", name);
                                        bundle.putString("Phone", adapterD2.getItem(position).getZmob());
                                        bundle.putString("FG", adapterD2.getItem(position).getFg());
                                        bundle.putString("Program", adapterD2.getItem(position).getProgram());
                                        bundle.putString("Time", adapterD2.getItem(position).getTime());
                                        bundle.putString("Date", adapterD2.getItem(position).getZzdate());
                                        bundle.putString("Japa", adapterD2.getItem(position).getJapa());
                                        bundle.putString("Reading", adapterD2.getItem(position).getZread());
                                        bundle.putString("Area", adapterD2.getItem(position).getArea());
                                        bundle.putString("Session", adapterD2.getItem(position).getSession());
                                        bundle.putString("URL", adapterD2.getItem(position).getUrl());
                                        bundle.putString("Source", adapterD2.getItem(position).getSource());
                                        bundle.putString("College", adapterD2.getItem(position).getCollege());
                                        bundle.putString("Occupation", adapterD2.getItem(position).getOccupation());
                                        bundle.putString("Branch", adapterD2.getItem(position).getBranch());
                                        bundle.putString("Zone", adapterD2.getItem(position).getZzone());
                                        bundle.putString("Organisation", adapterD2.getItem(position).getOrganization());
                                        bundle.putString("TL", adapterD2.getItem(position).getZtl());
                                        bundle.putString("Level", adapterD2.getItem(position).getZfl());
                                        bundle.putString("Category", adapterD2.getItem(position).getCategory());
                                        bundle.putString("FID", adapterD2.getItem(position).getFid());
                                        bundle.putString("Collection", collection);

                                        if (adapterD2.getItem(position).getRes_interest() != null)
                                            bundle.putString("Res", adapterD2.getItem(position).getRes_interest());
                                        else
                                            bundle.putString("Res", "NA");
                                        if (adapterD2.getItem(position).getOrigin() != null)
                                            bundle.putLong("Origin", adapterD2.getItem(position).getOrigin());
                                        else
                                            bundle.putLong("Origin", 0);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });

                                searchFilter.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        adapterD2.getFilter().filter(s.toString());
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
                            }

                        }
                    });
        }


    }
}

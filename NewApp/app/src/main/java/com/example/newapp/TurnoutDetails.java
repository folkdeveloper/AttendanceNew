package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class TurnoutDetails extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference fgboys = db.collection("RegistrationDemo");
    ListView mListView;
    public static String color = "";
    private long date1 = 0, date2 = 0;
    private int lower = 0, higher = 0;
    public static String fg = "";
    private String url = "";
    public static String spinPrograms = "";
    public static String spinCategories = "";
    public static String spinSessions = "";
    EditText searchFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnout_details);
        mListView = findViewById(R.id.list_view);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        spinPrograms = getIntent().getStringExtra("SpinPrograms");
        spinCategories = getIntent().getStringExtra("SpinCategories");
        spinSessions = getIntent().getStringExtra("SpinSessions");
        lower = getIntent().getIntExtra("Lower",lower);
        higher = getIntent().getIntExtra("Higher",higher);
        searchFilter = findViewById(R.id.searchFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if ((spinPrograms.equals("ALL")) && (spinCategories.equals("ALL")) && (spinSessions.equals("ALL"))) {
            populateListProgramsAndCategoriesAndSessions();
        } else if ((spinPrograms.equals("ALL")) && (spinCategories.equals("ALL")) && (!spinSessions.equals("ALL"))) {
            populateListProgramsAndCategories();
        } else if ((spinPrograms.equals("ALL")) && (!spinCategories.equals("ALL")) && (spinSessions.equals("ALL"))) {
            populateListProgramsAndSessions();
        } else if ((!spinPrograms.equals("ALL")) && (spinCategories.equals("ALL")) && (spinSessions.equals("ALL"))){
//            populateListCategoriesAndSessions();
        } else if ((spinPrograms.equals("ALL")) && (!spinCategories.equals("ALL")) && (!spinSessions.equals("ALL"))) {
            populateListPrograms();
        } else if ((!spinPrograms.equals("ALL")) && (spinCategories.equals("ALL")) && (!spinSessions.equals("ALL"))) {
            populateListCategories();
        } else if ((!spinPrograms.equals("ALL")) && (!spinCategories.equals("ALL")) && (spinSessions.equals("ALL"))) {
            populateListSessions();
        } else if ((!spinPrograms.equals("ALL")) && (!spinCategories.equals("ALL")) && (!spinSessions.equals("ALL"))) {
            populateList();
        }
    }

    public void populateListProgramsAndCategoriesAndSessions() {
//        if (color.equals("ALL")) {
        fgboys
//                    .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThan("edate", date2)
                .orderBy("edate")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        Log.d("Details", "onEvent: Out");
                        String data = "";
                        ArrayList<Note> details = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);

                            if (note.getUrl() == null) {
                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                            } else {
                                url = note.getUrl();
                            }

                            Log.d("Color", "onEvent: Event"+note.area + note.fg);

                            if ((note.getProbability() >= lower) && (note.getProbability() <= higher)) {
                                details.add(note);
                            }
                        }
                        final ColorsAdapter adapterD = new ColorsAdapter(TurnoutDetails.this,R.layout.details_layout,details);
                        mListView.setAdapter(adapterD);

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
                });
//        } else {
//            fgboys
//                    .whereEqualTo("color",color)
////                    .whereEqualTo("zzdate",date)
//                    .whereEqualTo("fg",fg)
//                    .whereGreaterThanOrEqualTo("edate", date1)
//                    .whereLessThanOrEqualTo("edate", date2)
//                    .orderBy("edate")
//                    .orderBy("fg")
//                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                            if (e != null) {
//                                return;
//                            }
//                            Log.d("Details", "onEvent: Out");
//                            String data = "";
//                            ArrayList<Note> details = new ArrayList<>();
//                            for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
//                                Note note = documentSnapshot.toObject(Note.class);
//
//                                if (note.getUrl() == null) {
//                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
//                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
//                                } else {
//                                    url = note.getUrl();
//                                }
//
//                                Log.d("Color", "onEvent: Event"+note.area + note.fg);
//                                details.add(note);
//                            }
//                            final ColorsAdapter adapterD = new ColorsAdapter(ColorDetails.this,R.layout.details_layout,details);
//                            mListView.setAdapter(adapterD);
//
//                            searchFilter.addTextChangedListener(new TextWatcher() {
//                                @Override
//                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                                }
//
//                                @Override
//                                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                    adapterD.getFilter().filter(s.toString());
//                                }
//
//                                @Override
//                                public void afterTextChanged(Editable s) {
//
//                                }
//                            });
//                        }
//                    });
//        }
    }

    public void populateListProgramsAndCategories() {
        fgboys
//                    .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThan("edate", date2)
                .whereEqualTo("session",spinSessions)
                .orderBy("edate")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        Log.d("Details", "onEvent: Out");
                        String data = "";
                        ArrayList<Note> details = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);

                            if (note.getUrl() == null) {
                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                            } else {
                                url = note.getUrl();
                            }

                            Log.d("Color", "onEvent: Event"+note.area + note.fg);

                            if ((note.getProbability() >= lower) && (note.getProbability() <= higher)) {
                                details.add(note);
                            }
                        }
                        final ColorsAdapter adapterD = new ColorsAdapter(TurnoutDetails.this,R.layout.details_layout,details);
                        mListView.setAdapter(adapterD);

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
                });
    }

    public void populateListProgramsAndSessions() {
        fgboys
//                    .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThan("edate", date2)
                .whereEqualTo("category",spinCategories)
                .orderBy("edate")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        Log.d("Details", "onEvent: Out");
                        String data = "";
                        ArrayList<Note> details = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);

                            if (note.getUrl() == null) {
                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                            } else {
                                url = note.getUrl();
                            }

                            Log.d("Color", "onEvent: Event"+note.area + note.fg);

                            if ((note.getProbability() >= lower) && (note.getProbability() <= higher)) {
                                details.add(note);
                            }
                        }
                        final ColorsAdapter adapterD = new ColorsAdapter(TurnoutDetails.this,R.layout.details_layout,details);
                        mListView.setAdapter(adapterD);

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
                });
    }

    public void populateListPrograms() {
        fgboys
//                    .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThan("edate", date2)
                .whereEqualTo("category",spinCategories)
                .whereEqualTo("session",spinSessions)
                .orderBy("edate")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        Log.d("Details", "onEvent: Out");
                        String data = "";
                        ArrayList<Note> details = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);

                            if (note.getUrl() == null) {
                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                            } else {
                                url = note.getUrl();
                            }

                            Log.d("Color", "onEvent: Event"+note.area + note.fg);

                            if ((note.getProbability() >= lower) && (note.getProbability() <= higher)) {
                                details.add(note);
                            }
                        }
                        final ColorsAdapter adapterD = new ColorsAdapter(TurnoutDetails.this,R.layout.details_layout,details);
                        mListView.setAdapter(adapterD);

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
                });
    }

    public void populateListCategories() {
        fgboys
//                    .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThan("edate", date2)
                .whereEqualTo("session",spinSessions)
                .whereEqualTo("program",spinPrograms)
                .orderBy("edate")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        Log.d("Details", "onEvent: Out");
                        String data = "";
                        ArrayList<Note> details = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);

                            if (note.getUrl() == null) {
                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                            } else {
                                url = note.getUrl();
                            }

                            Log.d("Color", "onEvent: Event"+note.area + note.fg);

                            if ((note.getProbability() >= lower) && (note.getProbability() <= higher)) {
                                details.add(note);
                            }
                        }
                        final ColorsAdapter adapterD = new ColorsAdapter(TurnoutDetails.this,R.layout.details_layout,details);
                        mListView.setAdapter(adapterD);

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
                });
    }

    public void populateListSessions() {
        fgboys
//                    .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThan("edate", date2)
                .whereEqualTo("program",spinPrograms)
                .whereEqualTo("category",spinCategories)
                .orderBy("edate")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        Log.d("Details", "onEvent: Out");
                        String data = "";
                        ArrayList<Note> details = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);

                            if (note.getUrl() == null) {
                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                            } else {
                                url = note.getUrl();
                            }

                            Log.d("Color", "onEvent: Event"+note.area + note.fg);

                            if ((note.getProbability() >= lower) && (note.getProbability() <= higher)) {
                                details.add(note);
                            }
                        }
                        final ColorsAdapter adapterD = new ColorsAdapter(TurnoutDetails.this,R.layout.details_layout,details);
                        mListView.setAdapter(adapterD);

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
                });
    }

    public void populateList() {
        fgboys
//                    .whereEqualTo("zzdate",date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThan("edate", date2)
                .whereEqualTo("program",spinPrograms)
                .whereEqualTo("session",spinSessions)
                .whereEqualTo("category",spinCategories)
                .orderBy("edate")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        Log.d("Details", "onEvent: Out");
                        String data = "";
                        ArrayList<Note> details = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);

                            if (note.getUrl() == null) {
                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                            } else {
                                url = note.getUrl();
                            }

                            Log.d("Color", "onEvent: Event"+note.area + note.fg);

                            if ((note.getProbability() >= lower) && (note.getProbability() <= higher)) {
                                details.add(note);
                            }
                        }
                        final ColorsAdapter adapterD = new ColorsAdapter(TurnoutDetails.this,R.layout.details_layout,details);
                        mListView.setAdapter(adapterD);

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
                });
    }

    public void select1(View v) {
        Intent intent = new Intent(TurnoutDetails.this, DateSelector.class);
        startActivity(intent);
    }
}

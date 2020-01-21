package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "", fid = "";
    private static final String TAG = "AreaActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private int total1 = 0, total2 = 0;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String spinnerPrograms = "";
    private String spinnerCategories = "";
    List<String> programsList;
    List<String> categoriesList;
    ArrayList<String> finalPrograms = new ArrayList<String>();
    ArrayList<String> finalCategories = new ArrayList<String>();
    Button btn;
    ArrayAdapter<String> adapterPrograms;
    ArrayAdapter<String> adapterCategories;
    String spinProgram = "";
    String[] programs = new String[]{"ALL"};
    final String[] categories = new String[]{"ALL"};
    Spinner program, category;
    int posProgram, posCategory;
    private String url = "";

    public void detailsFinal(ArrayList<Note> details) {
        final HistoryAdapter adapterD = new HistoryAdapter(HistoryActivity.this, R.layout.history_layout, details);
        mListView.setAdapter(adapterD);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapterD.getItem(position).getName();

                Intent intent = new Intent(HistoryActivity.this, DetailsFinal.class);
                Bundle bundle = new Bundle();
                bundle.putLong("Date1",date1);
                bundle.putLong("Date2",date2);
                bundle.putString("URL",adapterD.getItem(position).getUrl());
                bundle.putString("Name",name);
                bundle.putString("Phone",adapterD.getItem(position).getZmob());
                bundle.putString("FG",adapterD.getItem(position).getFg());
                bundle.putString("Program",adapterD.getItem(position).getProgram());
                bundle.putString("Time",adapterD.getItem(position).getTime());
                bundle.putString("Date",adapterD.getItem(position).getZzdate());
                bundle.putString("Japa",adapterD.getItem(position).getJapa());
                bundle.putString("Reading",adapterD.getItem(position).getZread());
                bundle.putString("Area",adapterD.getItem(position).getArea());
                bundle.putString("Session",adapterD.getItem(position).getSession());
                bundle.putString("URL",adapterD.getItem(position).getUrl());
                bundle.putString("Source",adapterD.getItem(position).getSource());
                bundle.putString("College",adapterD.getItem(position).getCollege());
                bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                bundle.putString("Branch",adapterD.getItem(position).getBranch());
                bundle.putString("Zone",adapterD.getItem(position).getZzone());
                bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                bundle.putString("TL",adapterD.getItem(position).getZtl());
                bundle.putString("Level",adapterD.getItem(position).getZfl());
                bundle.putString("Category",adapterD.getItem(position).getCategory());
                bundle.putString("FID",adapterD.getItem(position).getFid());
                bundle.putString("Collection",collection);

                if (adapterD.getItem(position).getRes_interest() != null)
                    bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                else
                    bundle.putString("Res","NA");
                if (adapterD.getItem(position).getOrigin() != null)
                    bundle.putLong("Origin",adapterD.getItem(position).getOrigin());
                else
                    bundle.putLong("Origin",0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void regFinal(ArrayList<Note> details) {
        final HistoryAdapter adapterD = new HistoryAdapter(HistoryActivity.this, R.layout.history_layout, details);
        mListView.setAdapter(adapterD);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapterD.getItem(position).getName();

                Intent intent = new Intent(HistoryActivity.this, RegFinalDetails.class);
                Bundle bundle = new Bundle();
                bundle.putLong("Date1",date1);
                bundle.putLong("Date2",date2);
                bundle.putString("URL",adapterD.getItem(position).getUrl());
                bundle.putString("Name",name);
                bundle.putString("Phone",adapterD.getItem(position).getZmob());
                bundle.putString("FG",adapterD.getItem(position).getFg());
                bundle.putString("Program",adapterD.getItem(position).getProgram());
                bundle.putString("Time",adapterD.getItem(position).getTime());
                bundle.putString("Date",adapterD.getItem(position).getZzdate());
                bundle.putString("Japa",adapterD.getItem(position).getJapa());
                bundle.putString("Reading",adapterD.getItem(position).getZread());
                bundle.putString("Area",adapterD.getItem(position).getArea());
                bundle.putString("Session",adapterD.getItem(position).getSession());
                bundle.putString("URL",adapterD.getItem(position).getUrl());
                bundle.putString("Source",adapterD.getItem(position).getSource());
                bundle.putString("College",adapterD.getItem(position).getCollege());
                bundle.putString("Occupation",adapterD.getItem(position).getOccupation());
                bundle.putString("Branch",adapterD.getItem(position).getBranch());
                bundle.putString("Zone",adapterD.getItem(position).getZzone());
                bundle.putString("Organisation",adapterD.getItem(position).getOrganization());
                bundle.putString("TL",adapterD.getItem(position).getZtl());
                bundle.putString("Level",adapterD.getItem(position).getZfl());
                bundle.putString("Category",adapterD.getItem(position).getCategory());
                bundle.putLong("Edate",adapterD.getItem(position).getEdate());
                bundle.putLong("Probability",adapterD.getItem(position).getProbability());
                bundle.putLong("LU",adapterD.getItem(position).getLast_updated());
                bundle.putString("Date",adapterD.getItem(position).getZzdate());
                bundle.putString("Mobile",adapterD.getItem(position).getZmob());
                bundle.putString("FID",adapterD.getItem(position).getFid());
                bundle.putString("Collection",collection);

                if (adapterD.getItem(position).getRes_interest() != null)
                    bundle.putString("Res",adapterD.getItem(position).getRes_interest());
                else
                    bundle.putString("Res","NA");
                if (adapterD.getItem(position).getOrigin() != null)
                    bundle.putLong("Origin",adapterD.getItem(position).getOrigin());
                else
                    bundle.putLong("Origin",0);
                if (adapterD.getItem(position).getFg_call() != null)
                    bundle.putString("fg_call",adapterD.getItem(position).getFg_call());
                else
                    bundle.putString("fg_call","No");
                if (adapterD.getItem(position).getLeave_agreed() != null)
                    bundle.putString("leave_agreed",adapterD.getItem(position).getLeave_agreed());
                else
                    bundle.putString("leave_agreed","No");
                if (adapterD.getItem(position).getMsg_confirm() != null)
                    bundle.putString("msg_confirm",adapterD.getItem(position).getMsg_confirm());
                else
                    bundle.putString("msg_confirm","No");
                if (adapterD.getItem(position).getStatus() != null)
                    bundle.putString("status",adapterD.getItem(position).getStatus());
                else
                    bundle.putString("status","No");
                if (adapterD.getItem(position).getComment() != null)
                    bundle.putString("Comment",adapterD.getItem(position).getComment());
                else
                    bundle.putString("Comment","No");
//                if (adapterD.getItem(position).getLast_updated() != null)
//                    bundle.putString("LU",adapterD.getItem(position).getLast_updated());
//                else
//                    bundle.putString("LU","No");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        collection = getIntent().getStringExtra("Collection");
        fid = getIntent().getStringExtra("FID");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        program = findViewById(R.id.program);
        category = findViewById(R.id.category);

        programsList = Arrays.asList(programs);
        finalPrograms.addAll(programsList);
        programs = new String[]{};

        adapterPrograms = new ArrayAdapter<String>(
                this, R.layout.spinner_item, finalPrograms);
        adapterPrograms.setDropDownViewResource(R.layout.spinner_item);
        program.setAdapter(adapterPrograms);

        fgboys
//                .whereEqualTo("zzdate", date)
                .whereEqualTo("fid",fid)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        TreeMap<String, Integer> count = new TreeMap<>();

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            if ((finalPrograms.contains(note.getProgram())) || (note.getProgram() == null)) {
                                continue;
                            } else {
                                finalPrograms.add(note.getProgram());
                                adapterPrograms.notifyDataSetChanged();
                            }
                        }
                    }
                });

        program.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPrograms = parent.getItemAtPosition(position).toString();
                posProgram = position;

                if (spinnerPrograms.equals("ALL")) {
                    spinProgram = "1";
                    populateCategories(spinProgram, spinnerPrograms);
                } else {
                    spinProgram = "2";
                    populateCategories(spinProgram, spinnerPrograms);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void populateCategories(String spinProgram, final String spinnerPrograms) {
        final String spinPrograms = spinnerPrograms;
        final String[] categories = new String[]{"ALL"};

        if (spinProgram.equals("1")) {
            finalCategories = new ArrayList<String>();
            categoriesList = Arrays.asList(categories);
            finalCategories.addAll(categoriesList);

            adapterCategories = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalCategories);
            total2 = 0;

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereEqualTo("fid",fid)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);
                                if ((finalCategories.contains(note.getCategory())) || (note.getCategory() == null)) {
                                    continue;
                                } else {
                                    finalCategories.add(note.getCategory());
                                    adapterCategories.notifyDataSetChanged();
                                }

                            }
                        }
                    });

            adapterCategories.setDropDownViewResource(R.layout.spinner_item);
            category.setAdapter(adapterCategories);

            category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerCategories = parent.getItemAtPosition(position).toString();
                    total2 = 0;
                    posCategory = position;

                    if (spinnerCategories.equals("ALL")) {
                        fgboys
                                .whereEqualTo("fid",fid)
                                .orderBy("edate")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        ArrayList<Note> details = new ArrayList<>();

                                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getUrl() == null) {
                                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                            } else {
                                                url = note.getUrl();
                                            }

                                            Log.d("Details", "onEvent: Event" + note.area + note.fg);
                                            details.add(note);
                                        }

                                        if (collection.equals("AttendanceDemo")) {
                                            detailsFinal(details);
                                        } else if (collection.equals("RegistrationDemo")){
                                            regFinal(details);
                                        }
                                    }
                                });
                    } else {
                        fgboys
                                .whereEqualTo("fid",fid)
                                .whereEqualTo("category",spinnerCategories)
                                .orderBy("edate")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        ArrayList<Note> details = new ArrayList<>();

                                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getUrl() == null) {
                                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                            } else {
                                                url = note.getUrl();
                                            }

                                            Log.d("Details", "onEvent: Event" + note.area + note.fg);
                                            details.add(note);
                                        }

                                        if (collection.equals("AttendanceDemo")) {
                                            detailsFinal(details);
                                        } else if (collection.equals("RegistrationDemo")){
                                            regFinal(details);
                                        }
                                    }
                                });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            finalCategories = new ArrayList<String>();
            categoriesList = Arrays.asList(categories);
            finalCategories.addAll(categoriesList);

            adapterCategories = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalCategories);
            total2 = 0;

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereEqualTo("fid",fid)
                    .whereEqualTo("program",spinnerPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);
                                if ((finalCategories.contains(note.getCategory())) || (note.getCategory() == null)) {
                                    continue;
                                } else {
                                    finalCategories.add(note.getCategory());
                                    adapterCategories.notifyDataSetChanged();
                                }

                            }
                        }
                    });

            adapterCategories.setDropDownViewResource(R.layout.spinner_item);
            category.setAdapter(adapterCategories);

            category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerCategories = parent.getItemAtPosition(position).toString();
                    total2 = 0;
                    posCategory = position;

                    if (spinnerCategories.equals("ALL")) {
                        fgboys
                                .whereEqualTo("fid",fid)
                                .whereEqualTo("program",spinnerPrograms)
                                .orderBy("edate")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        ArrayList<Note> details = new ArrayList<>();

                                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getUrl() == null) {
                                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                            } else {
                                                url = note.getUrl();
                                            }

                                            Log.d("Details", "onEvent: Event" + note.area + note.fg);
                                            details.add(note);
                                        }

                                        if (collection.equals("AttendanceDemo")) {
                                            detailsFinal(details);
                                        } else if (collection.equals("RegistrationDemo")){
                                            regFinal(details);
                                        }
                                    }
                                });
                    } else {
                        fgboys
                                .whereEqualTo("fid",fid)
                                .whereEqualTo("category",spinnerCategories)
                                .whereEqualTo("program",spinnerPrograms)
                                .orderBy("edate")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        ArrayList<Note> details = new ArrayList<>();

                                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            if (note.getUrl() == null) {
                                                note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                                url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                            } else {
                                                url = note.getUrl();
                                            }

                                            Log.d("Details", "onEvent: Event" + note.area + note.fg);
                                            details.add(note);
                                        }

                                        if (collection.equals("AttendanceDemo")) {
                                            detailsFinal(details);
                                        } else if (collection.equals("RegistrationDemo")){
                                            regFinal(details);
                                        }
                                    }
                                });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}

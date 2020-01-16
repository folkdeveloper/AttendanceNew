package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class TurnoutActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference fgboys = db.collection("RegistrationDemo");
    private static final String TAG = "MainActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private int total = 0;
    private int number0=0, number10=0, number20=0, number30=0, number40=0, number50=0;
    private double total0=0.0, total10=0.0, total20=0.0, total30=0.0, total40=0.0, total50=0.0;
    private double per0=0.0, per10=0.0, per20=0.0, per30=0.0, per40=0.0, per50=0.0;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String spinnerPrograms = "";
    private String spinnerCategories = "";
    private String spinnerSessions = "";
    List<String> programsList;
    List<String> categoriesList;
    List<String> sessionsList;
    final ArrayList<String> finalPrograms = new ArrayList<String>();
    ArrayList<String> finalCategories = new ArrayList<String>();
    ArrayList<String> finalSessions = new ArrayList<String>();
    Button btn;
    ArrayAdapter<String> adapterPrograms;
    ArrayAdapter<String> adapterCategories;
    ArrayAdapter<String> adapterSessions;
    String spinProgram = "";
    String spinCategory = "";
    final String[] categories = new String[]{"ALL"};
    final String[] sessions = new String[]{"ALL"};
    String[] programs = new String[]{"ALL"};
    Spinner program, category, session;
    int posProgram, posCategory, posSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnout);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1", date1);
        date2 = getIntent().getLongExtra("Date2", date2);

        programsList = Arrays.asList(programs);
        finalPrograms.addAll(programsList);

        program = findViewById(R.id.program);
        category = findViewById(R.id.category);
        session = findViewById(R.id.session);

        adapterPrograms = new ArrayAdapter<String>(
                this,R.layout.spinner_item,finalPrograms);
        adapterPrograms.setDropDownViewResource(R.layout.spinner_item);
        program.setAdapter(adapterPrograms);

        fgboys
//                .whereEqualTo("zzdate", date)
                .whereGreaterThanOrEqualTo("edate", date1)
                .whereLessThanOrEqualTo("edate", date2)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        TreeMap<String, Integer> count = new TreeMap<>();

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);

                            if (note.getProgram() == null)
                                continue;

                            if (finalPrograms.contains(note.getProgram())) {
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
                    populateCategories(spinProgram);
                } else {
                    spinProgram = "2";
                    populateCategories(spinProgram);
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

    public void populateCategories(String spinProgram) {
        final String spinPrograms = spinnerPrograms;

        if (spinProgram.equals("1")) {
            finalCategories = new ArrayList<String>();
            categoriesList = Arrays.asList(categories);
            finalCategories.addAll(categoriesList);

            adapterCategories = new ArrayAdapter<String>
                    (this,R.layout.spinner_item,finalCategories);

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getCategory() == null)
                                    continue;

                                if (finalCategories.contains(note.getCategory())) {
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
                    posCategory = position;

                    if (spinnerCategories.equals("ALL")) {
                        spinCategory = "1";
                        populateSessionsPrograms(spinCategory,spinPrograms);
                    } else {
                        spinCategory = "2";
                        populateSessionsPrograms(spinCategory,spinPrograms);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else {
            finalCategories = new ArrayList<String>();
            categoriesList = Arrays.asList(categories);
            finalCategories.addAll(categoriesList);

            adapterCategories = new ArrayAdapter<String>
                    (this,R.layout.spinner_item,finalCategories);

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getCategory() == null)
                                    continue;

                                if (finalCategories.contains(note.getCategory())) {
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
                    posCategory = position;

                    if (spinnerCategories.equals("ALL")) {
                        spinCategory = "1";
                        populateSessions(spinCategory,spinPrograms);
                    } else {
                        spinCategory = "2";
                        populateSessions(spinCategory,spinPrograms);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void populateSessionsPrograms(String spinCategory, String spinProgram) {
        final String spinCategories = spinCategory;
        final String spinPrograms = spinProgram;

        if (spinCategory.equals("1")) {
            finalSessions = new ArrayList<String>();
            sessionsList = Arrays.asList(sessions);
            finalSessions.addAll(sessionsList);

            adapterSessions = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalSessions);

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getSession() == null)
                                    continue;

                                if (finalSessions.contains(note.getSession())) {
                                    continue;
                                } else {
                                    finalSessions.add(note.getSession());
                                    adapterSessions.notifyDataSetChanged();
                                }

                            }
                        }
                    });
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    posSession = position;

                    if (spinnerSessions.equals("ALL")) {
                        total=0;
                        number0=0; number10=0; number20=0; number30=0; number40=0; number50=0;
                        total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate",date1)
                                .whereLessThan("edate",date2)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getFg());

                                            double turnout = note.getProbability();
                                            total++;

                                            if ((turnout >= 0) && (turnout <= 10)) {
                                                number0++;
                                                total0 += turnout;
                                            } else if ((turnout >= 10) && (turnout <= 20)) {
                                                number10++;
                                                total10 += turnout;
                                            } else if ((turnout >= 20) && (turnout <= 30)) {
                                                number20++;
                                                total20 += turnout;
                                            } else if ((turnout >= 30) && (turnout <= 40)) {
                                                number30++;
                                                total30 += turnout;
                                            } else if ((turnout >= 40) && (turnout <= 50)) {
                                                number40++;
                                                total40 += turnout;
                                            } else {
                                                number50++;
                                                total50 += turnout;
                                            }
                                        }

                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(total0 / number0));
                                        per10 = Double.parseDouble(df.format(total10 / number10));
                                        per20 = Double.parseDouble(df.format(total20 / number20));
                                        per30 = Double.parseDouble(df.format(total30 / number30));
                                        per40 = Double.parseDouble(df.format(total40 / number40));
                                        per50 = Double.parseDouble(df.format(total50 / number50));

                                        if (number0 > 0) {
                                            count.put("0-10", new JapaClass(number0, per0));
                                        }

                                        if (number10 > 0) {
                                            count.put("10-20", new JapaClass(number10, per10));
                                        }

                                        if (number20 > 0) {
                                            count.put("20-30", new JapaClass(number20, per20));
                                        }

                                        if (number30 > 0) {
                                            count.put("30-40", new JapaClass(number30, per40));
                                        }

                                        if (number40 > 0) {
                                            count.put("40-50", new JapaClass(number40, per40));
                                        }

                                        if (number50 > 0) {
                                            count.put("50-60", new JapaClass(number50, per50));
                                        }
                                        per50 = Double.parseDouble(df.format(total50 / number50));
                                        count.put("ALL", new JapaClass(total,
                                                Double.parseDouble(df.format((total0+total10+total20+total30+total40+total50)/
                                                        (number0+number10+number20+number30+number40+number50)))));

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String turnout = adapter.getItem(position).getKey();
                                                int lower = 0, higher = 0;
                                                switch (turnout) {
                                                    case "0-10":
                                                        lower = 0;
                                                        higher = 10;
                                                        break;
                                                    case "10-20":
                                                        lower = 10;
                                                        higher = 20;
                                                        break;
                                                    case "20-30":
                                                        lower = 20;
                                                        higher = 30;
                                                        break;
                                                    case "30-40":
                                                        lower = 30;
                                                        higher = 40;
                                                        break;
                                                    case "40-50":
                                                        lower = 40;
                                                        higher = 50;
                                                        break;
                                                    case "50-60":
                                                        lower = 50;
                                                        higher = 60;
                                                        break;
                                                    case "ALL":
                                                        lower = 0;
                                                        higher = 100;
                                                }
                                                Intent intent = new Intent(TurnoutActivity.this, TurnoutDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("Lower", lower);
                                                bundle.putInt("Higher", higher);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    } else {
                        total=0;
                        number0=0; number10=0; number20=0; number30=0; number40=0; number50=0;
                        total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate",date1)
                                .whereLessThan("edate",date2)
                                .whereEqualTo("session",spinnerSessions)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getFg());

                                            double turnout = note.getProbability();
                                            total++;

                                            if ((turnout >= 0) && (turnout <= 10)) {
                                                number0++;
                                                total0 += turnout;
                                            } else if ((turnout >= 10) && (turnout <= 20)) {
                                                number10++;
                                                total10 += turnout;
                                            } else if ((turnout >= 20) && (turnout <= 30)) {
                                                number20++;
                                                total20 += turnout;
                                            } else if ((turnout >= 30) && (turnout <= 40)) {
                                                number30++;
                                                total30 += turnout;
                                            } else if ((turnout >= 40) && (turnout <= 50)) {
                                                number40++;
                                                total40 += turnout;
                                            } else {
                                                number50++;
                                                total50 += turnout;
                                            }
                                        }

                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(total0 / number0));
                                        per10 = Double.parseDouble(df.format(total10 / number10));
                                        per20 = Double.parseDouble(df.format(total20 / number20));
                                        per30 = Double.parseDouble(df.format(total30 / number30));
                                        per40 = Double.parseDouble(df.format(total40 / number40));
                                        per50 = Double.parseDouble(df.format(total50 / number50));

                                        if (number0 > 0) {
                                            count.put("0-10", new JapaClass(number0, per0));
                                        }

                                        if (number10 > 0) {
                                            count.put("10-20", new JapaClass(number10, per10));
                                        }

                                        if (number20 > 0) {
                                            count.put("20-30", new JapaClass(number20, per20));
                                        }

                                        if (number30 > 0) {
                                            count.put("30-40", new JapaClass(number30, per40));
                                        }

                                        if (number40 > 0) {
                                            count.put("40-50", new JapaClass(number40, per40));
                                        }

                                        if (number50 > 0) {
                                            count.put("50-60", new JapaClass(number50, per50));
                                        }

                                        count.put("ALL", new JapaClass(total, 100));

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String turnout = adapter.getItem(position).getKey();
                                                int lower = 0, higher = 0;
                                                switch (turnout) {
                                                    case "0-10":
                                                        lower = 0;
                                                        higher = 10;
                                                        break;
                                                    case "10-20":
                                                        lower = 10;
                                                        higher = 20;
                                                        break;
                                                    case "20-30":
                                                        lower = 20;
                                                        higher = 30;
                                                        break;
                                                    case "30-40":
                                                        lower = 30;
                                                        higher = 40;
                                                        break;
                                                    case "40-50":
                                                        lower = 40;
                                                        higher = 50;
                                                        break;
                                                    case "50-60":
                                                        lower = 50;
                                                        higher = 60;
                                                        break;
                                                    case "ALL":
                                                        lower = 0;
                                                        higher = 100;
                                                }
                                                Intent intent = new Intent(TurnoutActivity.this, TurnoutDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("Lower", lower);
                                                bundle.putInt("Higher", higher);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            finalSessions = new ArrayList<String>();
            sessionsList = Arrays.asList(sessions);
            finalSessions.addAll(sessionsList);

            adapterSessions = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalSessions);

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("category",spinCategories)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getSession() == null)
                                    continue;

                                if (finalSessions.contains(note.getSession())) {
                                    continue;
                                } else {
                                    finalSessions.add(note.getSession());
                                    adapterSessions.notifyDataSetChanged();
                                }

                            }
                        }
                    });
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    posSession = position;

                    if (spinnerSessions.equals("ALL")) {
                        total=0;
                        number0=0; number10=0; number20=0; number30=0; number40=0; number50=0;
                        total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate",date1)
                                .whereLessThan("edate",date2)
                                .whereEqualTo("category",spinCategories)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getFg());

                                            double turnout = note.getProbability();
                                            total++;

                                            if ((turnout >= 0) && (turnout <= 10)) {
                                                number0++;
                                                total0 += turnout;
                                            } else if ((turnout >= 10) && (turnout <= 20)) {
                                                number10++;
                                                total10 += turnout;
                                            } else if ((turnout >= 20) && (turnout <= 30)) {
                                                number20++;
                                                total20 += turnout;
                                            } else if ((turnout >= 30) && (turnout <= 40)) {
                                                number30++;
                                                total30 += turnout;
                                            } else if ((turnout >= 40) && (turnout <= 50)) {
                                                number40++;
                                                total40 += turnout;
                                            } else {
                                                number50++;
                                                total50 += turnout;
                                            }
                                        }

                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(total0 / number0));
                                        per10 = Double.parseDouble(df.format(total10 / number10));
                                        per20 = Double.parseDouble(df.format(total20 / number20));
                                        per30 = Double.parseDouble(df.format(total30 / number30));
                                        per40 = Double.parseDouble(df.format(total40 / number40));
                                        per50 = Double.parseDouble(df.format(total50 / number50));

                                        if (number0 > 0) {
                                            count.put("0-10", new JapaClass(number0, per0));
                                        }

                                        if (number10 > 0) {
                                            count.put("10-20", new JapaClass(number10, per10));
                                        }

                                        if (number20 > 0) {
                                            count.put("20-30", new JapaClass(number20, per20));
                                        }

                                        if (number30 > 0) {
                                            count.put("30-40", new JapaClass(number30, per40));
                                        }

                                        if (number40 > 0) {
                                            count.put("40-50", new JapaClass(number40, per40));
                                        }

                                        if (number50 > 0) {
                                            count.put("50-60", new JapaClass(number50, per50));
                                        }

                                        count.put("ALL", new JapaClass(total, 100));

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String turnout = adapter.getItem(position).getKey();
                                                int lower = 0, higher = 0;
                                                switch (turnout) {
                                                    case "0-10":
                                                        lower = 0;
                                                        higher = 10;
                                                        break;
                                                    case "10-20":
                                                        lower = 10;
                                                        higher = 20;
                                                        break;
                                                    case "20-30":
                                                        lower = 20;
                                                        higher = 30;
                                                        break;
                                                    case "30-40":
                                                        lower = 30;
                                                        higher = 40;
                                                        break;
                                                    case "40-50":
                                                        lower = 40;
                                                        higher = 50;
                                                        break;
                                                    case "50-60":
                                                        lower = 50;
                                                        higher = 60;
                                                        break;
                                                    case "ALL":
                                                        lower = 0;
                                                        higher = 100;
                                                }
                                                Intent intent = new Intent(TurnoutActivity.this, TurnoutDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("Lower", lower);
                                                bundle.putInt("Higher", higher);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    } else {
                        total=0;
                        number0=0; number10=0; number20=0; number30=0; number40=0; number50=0;
                        total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate",date1)
                                .whereLessThan("edate",date2)
                                .whereEqualTo("category",spinCategories)
                                .whereEqualTo("session",spinnerSessions)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getFg());

                                            double turnout = note.getProbability();
                                            total++;

                                            if ((turnout >= 0) && (turnout <= 10)) {
                                                number0++;
                                                total0 += turnout;
                                            } else if ((turnout >= 10) && (turnout <= 20)) {
                                                number10++;
                                                total10 += turnout;
                                            } else if ((turnout >= 20) && (turnout <= 30)) {
                                                number20++;
                                                total20 += turnout;
                                            } else if ((turnout >= 30) && (turnout <= 40)) {
                                                number30++;
                                                total30 += turnout;
                                            } else if ((turnout >= 40) && (turnout <= 50)) {
                                                number40++;
                                                total40 += turnout;
                                            } else {
                                                number50++;
                                                total50 += turnout;
                                            }
                                        }

                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(total0 / number0));
                                        per10 = Double.parseDouble(df.format(total10 / number10));
                                        per20 = Double.parseDouble(df.format(total20 / number20));
                                        per30 = Double.parseDouble(df.format(total30 / number30));
                                        per40 = Double.parseDouble(df.format(total40 / number40));
                                        per50 = Double.parseDouble(df.format(total50 / number50));


                                        if (number0 > 0) {
                                            count.put("0-10", new JapaClass(number0, per0));
                                        }

                                        if (number10 > 0) {
                                            count.put("10-20", new JapaClass(number10, per10));
                                        }

                                        if (number20 > 0) {
                                            count.put("20-30", new JapaClass(number20, per20));
                                        }

                                        if (number30 > 0) {
                                            count.put("30-40", new JapaClass(number30, per40));
                                        }

                                        if (number40 > 0) {
                                            count.put("40-50", new JapaClass(number40, per40));
                                        }

                                        if (number50 > 0) {
                                            count.put("50-60", new JapaClass(number50, per50));
                                        }

                                        count.put("ALL", new JapaClass(total, 100));

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String turnout = adapter.getItem(position).getKey();
                                                int lower = 0, higher = 0;
                                                switch (turnout) {
                                                    case "0-10":
                                                        lower = 0;
                                                        higher = 10;
                                                        break;
                                                    case "10-20":
                                                        lower = 10;
                                                        higher = 20;
                                                        break;
                                                    case "20-30":
                                                        lower = 20;
                                                        higher = 30;
                                                        break;
                                                    case "30-40":
                                                        lower = 30;
                                                        higher = 40;
                                                        break;
                                                    case "40-50":
                                                        lower = 40;
                                                        higher = 50;
                                                        break;
                                                    case "50-60":
                                                        lower = 50;
                                                        higher = 60;
                                                        break;
                                                    case "ALL":
                                                        lower = 0;
                                                        higher = 100;
                                                }
                                                Intent intent = new Intent(TurnoutActivity.this, TurnoutDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("Lower", lower);
                                                bundle.putInt("Higher", higher);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
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

    public void populateSessions(String spinCategory, final String spinProgram) {
        final String spinCategories = spinCategory;
        final String spinPrograms = spinProgram;

        if (spinCategory.equals("1")) {
            finalSessions = new ArrayList<String>();
            sessionsList = Arrays.asList(sessions);
            finalSessions.addAll(sessionsList);

            adapterSessions = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalSessions);
            total = 0;

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program", spinnerPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getSession() == null)
                                    continue;

                                if (finalSessions.contains(note.getSession())) {
                                    continue;
                                } else {
                                    finalSessions.add(note.getSession());
                                    adapterSessions.notifyDataSetChanged();
                                }

                            }
                        }
                    });
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    posSession = position;

                    if (spinnerSessions.equals("ALL")) {
                        total = 0;
                        number0 = 0;
                        number10 = 0;
                        number20 = 0;
                        number30 = 0;
                        number40 = 0;
                        number50 = 0;
                        total0 = 0.0;
                        total10 = 0.0;
                        total20 = 0.0;
                        total30 = 0.0;
                        total40 = 0.0;
                        total50 = 0.0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThan("edate", date2)
                                .whereEqualTo("program", spinPrograms)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getFg());

                                            double turnout = note.getProbability();
                                            total++;

                                            if ((turnout >= 0) && (turnout <= 10)) {
                                                number0++;
                                                total0 += turnout;
                                            } else if ((turnout >= 10) && (turnout <= 20)) {
                                                number10++;
                                                total10 += turnout;
                                            } else if ((turnout >= 20) && (turnout <= 30)) {
                                                number20++;
                                                total20 += turnout;
                                            } else if ((turnout >= 30) && (turnout <= 40)) {
                                                number30++;
                                                total30 += turnout;
                                            } else if ((turnout >= 40) && (turnout <= 50)) {
                                                number40++;
                                                total40 += turnout;
                                            } else {
                                                number50++;
                                                total50 += turnout;
                                            }
                                        }

                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(total0 / number0));
                                        per10 = Double.parseDouble(df.format(total10 / number10));
                                        per20 = Double.parseDouble(df.format(total20 / number20));
                                        per30 = Double.parseDouble(df.format(total30 / number30));
                                        per40 = Double.parseDouble(df.format(total40 / number40));
                                        per50 = Double.parseDouble(df.format(total50 / number50));


                                        if (number0 > 0) {
                                            count.put("0-10", new JapaClass(number0, per0));
                                        }

                                        if (number10 > 0) {
                                            count.put("10-20", new JapaClass(number10, per10));
                                        }

                                        if (number20 > 0) {
                                            count.put("20-30", new JapaClass(number20, per20));
                                        }

                                        if (number30 > 0) {
                                            count.put("30-40", new JapaClass(number30, per40));
                                        }

                                        if (number40 > 0) {
                                            count.put("40-50", new JapaClass(number40, per40));
                                        }

                                        if (number50 > 0) {
                                            count.put("50-60", new JapaClass(number50, per50));
                                        }

                                        count.put("ALL", new JapaClass(total, 100));

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String turnout = adapter.getItem(position).getKey();
                                                int lower = 0, higher = 0;
                                                switch (turnout) {
                                                    case "0-10":
                                                        lower = 0;
                                                        higher = 10;
                                                        break;
                                                    case "10-20":
                                                        lower = 10;
                                                        higher = 20;
                                                        break;
                                                    case "20-30":
                                                        lower = 20;
                                                        higher = 30;
                                                        break;
                                                    case "30-40":
                                                        lower = 30;
                                                        higher = 40;
                                                        break;
                                                    case "40-50":
                                                        lower = 40;
                                                        higher = 50;
                                                        break;
                                                    case "50-60":
                                                        lower = 50;
                                                        higher = 60;
                                                        break;
                                                    case "ALL":
                                                        lower = 0;
                                                        higher = 100;
                                                }
                                                Intent intent = new Intent(TurnoutActivity.this, TurnoutDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("Lower", lower);
                                                bundle.putInt("Higher", higher);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    } else {
                        total = 0;
                        number0 = 0;
                        number10 = 0;
                        number20 = 0;
                        number30 = 0;
                        number40 = 0;
                        number50 = 0;
                        total0 = 0.0;
                        total10 = 0.0;
                        total20 = 0.0;
                        total30 = 0.0;
                        total40 = 0.0;
                        total50 = 0.0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThan("edate", date2)
                                .whereEqualTo("program", spinPrograms)
                                .whereEqualTo("session", spinnerSessions)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getFg());

                                            double turnout = note.getProbability();
                                            total++;

                                            if ((turnout >= 0) && (turnout <= 10)) {
                                                number0++;
                                                total0 += turnout;
                                            } else if ((turnout >= 10) && (turnout <= 20)) {
                                                number10++;
                                                total10 += turnout;
                                            } else if ((turnout >= 20) && (turnout <= 30)) {
                                                number20++;
                                                total20 += turnout;
                                            } else if ((turnout >= 30) && (turnout <= 40)) {
                                                number30++;
                                                total30 += turnout;
                                            } else if ((turnout >= 40) && (turnout <= 50)) {
                                                number40++;
                                                total40 += turnout;
                                            } else {
                                                number50++;
                                                total50 += turnout;
                                            }
                                        }

                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(total0 / number0));
                                        per10 = Double.parseDouble(df.format(total10 / number10));
                                        per20 = Double.parseDouble(df.format(total20 / number20));
                                        per30 = Double.parseDouble(df.format(total30 / number30));
                                        per40 = Double.parseDouble(df.format(total40 / number40));
                                        per50 = Double.parseDouble(df.format(total50 / number50));


                                        if (number0 > 0) {
                                            count.put("0-10", new JapaClass(number0, per0));
                                        }

                                        if (number10 > 0) {
                                            count.put("10-20", new JapaClass(number10, per10));
                                        }

                                        if (number20 > 0) {
                                            count.put("20-30", new JapaClass(number20, per20));
                                        }

                                        if (number30 > 0) {
                                            count.put("30-40", new JapaClass(number30, per40));
                                        }

                                        if (number40 > 0) {
                                            count.put("40-50", new JapaClass(number40, per40));
                                        }

                                        if (number50 > 0) {
                                            count.put("50-60", new JapaClass(number50, per50));
                                        }

                                        count.put("ALL", new JapaClass(total, 100));

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String turnout = adapter.getItem(position).getKey();
                                                int lower = 0, higher = 0;
                                                switch (turnout) {
                                                    case "0-10":
                                                        lower = 0;
                                                        higher = 10;
                                                        break;
                                                    case "10-20":
                                                        lower = 10;
                                                        higher = 20;
                                                        break;
                                                    case "20-30":
                                                        lower = 20;
                                                        higher = 30;
                                                        break;
                                                    case "30-40":
                                                        lower = 30;
                                                        higher = 40;
                                                        break;
                                                    case "40-50":
                                                        lower = 40;
                                                        higher = 50;
                                                        break;
                                                    case "50-60":
                                                        lower = 50;
                                                        higher = 60;
                                                        break;
                                                    case "ALL":
                                                        lower = 0;
                                                        higher = 100;
                                                }
                                                Intent intent = new Intent(TurnoutActivity.this, TurnoutDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("Lower", lower);
                                                bundle.putInt("Higher", higher);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            finalSessions = new ArrayList<String>();
            sessionsList = Arrays.asList(sessions);
            finalSessions.addAll(sessionsList);

            adapterSessions = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalSessions);
            total = 0;

            fgboys
//                    .whereEqualTo("zzdate",date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program", spinnerPrograms)
                    .whereEqualTo("category", spinCategories)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getSession() == null)
                                    continue;

                                if (finalSessions.contains(note.getSession())) {
                                    continue;
                                } else {
                                    finalSessions.add(note.getSession());
                                    adapterSessions.notifyDataSetChanged();
                                }

                            }
                        }
                    });
            adapterSessions.setDropDownViewResource(R.layout.spinner_item);
            session.setAdapter(adapterSessions);

            session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    posSession = position;

                    if (spinnerSessions.equals("ALL")) {
                        total = 0;
                        number0 = 0;
                        number10 = 0;
                        number20 = 0;
                        number30 = 0;
                        number40 = 0;
                        number50 = 0;
                        total0 = 0.0;
                        total10 = 0.0;
                        total20 = 0.0;
                        total30 = 0.0;
                        total40 = 0.0;
                        total50 = 0.0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThan("edate", date2)
                                .whereEqualTo("program", spinPrograms)
                                .whereEqualTo("category", spinCategories)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getFg());

                                            double turnout = note.getProbability();
                                            total++;

                                            if ((turnout >= 0) && (turnout <= 10)) {
                                                number0++;
                                                total0 += turnout;
                                            } else if ((turnout >= 10) && (turnout <= 20)) {
                                                number10++;
                                                total10 += turnout;
                                            } else if ((turnout >= 20) && (turnout <= 30)) {
                                                number20++;
                                                total20 += turnout;
                                            } else if ((turnout >= 30) && (turnout <= 40)) {
                                                number30++;
                                                total30 += turnout;
                                            } else if ((turnout >= 40) && (turnout <= 50)) {
                                                number40++;
                                                total40 += turnout;
                                            } else {
                                                number50++;
                                                total50 += turnout;
                                            }
                                        }

                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(total0 / number0));
                                        per10 = Double.parseDouble(df.format(total10 / number10));
                                        per20 = Double.parseDouble(df.format(total20 / number20));
                                        per30 = Double.parseDouble(df.format(total30 / number30));
                                        per40 = Double.parseDouble(df.format(total40 / number40));
                                        per50 = Double.parseDouble(df.format(total50 / number50));


                                        if (number0 > 0) {
                                            count.put("0-10", new JapaClass(number0, per0));
                                        }

                                        if (number10 > 0) {
                                            count.put("10-20", new JapaClass(number10, per10));
                                        }

                                        if (number20 > 0) {
                                            count.put("20-30", new JapaClass(number20, per20));
                                        }

                                        if (number30 > 0) {
                                            count.put("30-40", new JapaClass(number30, per40));
                                        }

                                        if (number40 > 0) {
                                            count.put("40-50", new JapaClass(number40, per40));
                                        }

                                        if (number50 > 0) {
                                            count.put("50-60", new JapaClass(number50, per50));
                                        }

                                        count.put("ALL", new JapaClass(total, 100));

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String turnout = adapter.getItem(position).getKey();
                                                int lower = 0, higher = 0;
                                                switch (turnout) {
                                                    case "0-10":
                                                        lower = 0;
                                                        higher = 10;
                                                        break;
                                                    case "10-20":
                                                        lower = 10;
                                                        higher = 20;
                                                        break;
                                                    case "20-30":
                                                        lower = 20;
                                                        higher = 30;
                                                        break;
                                                    case "30-40":
                                                        lower = 30;
                                                        higher = 40;
                                                        break;
                                                    case "40-50":
                                                        lower = 40;
                                                        higher = 50;
                                                        break;
                                                    case "50-60":
                                                        lower = 50;
                                                        higher = 60;
                                                        break;
                                                    case "ALL":
                                                        lower = 0;
                                                        higher = 100;
                                                }
                                                Intent intent = new Intent(TurnoutActivity.this, TurnoutDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("Lower", lower);
                                                bundle.putInt("Higher", higher);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    } else {
                        total = 0;
                        number0 = 0;
                        number10 = 0;
                        number20 = 0;
                        number30 = 0;
                        number40 = 0;
                        number50 = 0;
                        total0 = 0.0;
                        total10 = 0.0;
                        total20 = 0.0;
                        total30 = 0.0;
                        total40 = 0.0;
                        total50 = 0.0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThan("edate", date2)
                                .whereEqualTo("program", spinPrograms)
                                .whereEqualTo("session", spinnerSessions)
                                .whereEqualTo("category", spinCategories)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        TreeMap<String, JapaClass> count = new TreeMap<>();
                                        String data = "";
                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);

                                            Log.d(TAG, "onEvent: Area" + note.getFg());

                                            double turnout = note.getProbability();
                                            total++;

                                            if ((turnout >= 0) && (turnout <= 10)) {
                                                number0++;
                                                total0 += turnout;
                                            } else if ((turnout >= 10) && (turnout <= 20)) {
                                                number10++;
                                                total10 += turnout;
                                            } else if ((turnout >= 20) && (turnout <= 30)) {
                                                number20++;
                                                total20 += turnout;
                                            } else if ((turnout >= 30) && (turnout <= 40)) {
                                                number30++;
                                                total30 += turnout;
                                            } else if ((turnout >= 40) && (turnout <= 50)) {
                                                number40++;
                                                total40 += turnout;
                                            } else {
                                                number50++;
                                                total50 += turnout;
                                            }
                                        }

                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        per0 = Double.parseDouble(df.format(total0 / number0));
                                        per10 = Double.parseDouble(df.format(total10 / number10));
                                        per20 = Double.parseDouble(df.format(total20 / number20));
                                        per30 = Double.parseDouble(df.format(total30 / number30));
                                        per40 = Double.parseDouble(df.format(total40 / number40));
                                        per50 = Double.parseDouble(df.format(total50 / number50));


                                        if (number0 > 0) {
                                            count.put("0-10", new JapaClass(number0, per0));
                                        }

                                        if (number10 > 0) {
                                            count.put("10-20", new JapaClass(number10, per10));
                                        }

                                        if (number20 > 0) {
                                            count.put("20-30", new JapaClass(number20, per20));
                                        }

                                        if (number30 > 0) {
                                            count.put("30-40", new JapaClass(number30, per40));
                                        }

                                        if (number40 > 0) {
                                            count.put("40-50", new JapaClass(number40, per40));
                                        }

                                        if (number50 > 0) {
                                            count.put("50-60", new JapaClass(number50, per50));
                                        }

                                        count.put("ALL", new JapaClass(total, 100));

                                        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String turnout = adapter.getItem(position).getKey();
                                                int lower = 0, higher = 0;
                                                switch (turnout) {
                                                    case "0-10":
                                                        lower = 0;
                                                        higher = 10;
                                                        break;
                                                    case "10-20":
                                                        lower = 10;
                                                        higher = 20;
                                                        break;
                                                    case "20-30":
                                                        lower = 20;
                                                        higher = 30;
                                                        break;
                                                    case "30-40":
                                                        lower = 30;
                                                        higher = 40;
                                                        break;
                                                    case "40-50":
                                                        lower = 40;
                                                        higher = 50;
                                                        break;
                                                    case "50-60":
                                                        lower = 50;
                                                        higher = 60;
                                                        break;
                                                    case "ALL":
                                                        lower = 0;
                                                        higher = 100;
                                                }
                                                Intent intent = new Intent(TurnoutActivity.this, TurnoutDetails.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("Lower", lower);
                                                bundle.putInt("Higher", higher);
                                                bundle.putString("SpinPrograms", spinPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
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

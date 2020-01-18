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
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class TurnoutFGList extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "MainActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private int total = 0;
    private int number0=0, number10=0, number20=0, number30=0, number40=0, number50=0, number60=0, number70=0,
            number80=0, number90=0;
    private double total0=0.0, total10=0.0, total20=0.0, total30=0.0, total40=0.0, total50=0.0, total60=0.0,
            total70=0.0, total80=0.0, total90=0.0;
    private double per0=0.0, per10=0.0, per20=0.0, per30=0.0, per40=0.0, per50=0.0, per60=0.0, per70=0.0,
            per80=0.0, per90=0.0;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public static String spinPrograms = "";
    public static String spinCategories = "";
    public static String spinSessions = "";
    public static String fg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnout_fglist);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        fg = getIntent().getStringExtra("FG");
        spinPrograms= getIntent().getStringExtra("SpinPrograms");
        spinCategories= getIntent().getStringExtra("SpinCategories");
        spinSessions= getIntent().getStringExtra("SpinSessions");
    }

    public void populateTurnoutsAdapter(TreeMap<String, JapaClass> count) {
        DecimalFormat df = new DecimalFormat("##.#");
        df.setRoundingMode(RoundingMode.DOWN);

        per0 = Double.parseDouble(df.format(total0 / number0));
        per10 = Double.parseDouble(df.format(total10 / number10));
        per20 = Double.parseDouble(df.format(total20 / number20));
        per30 = Double.parseDouble(df.format(total30 / number30));
        per40 = Double.parseDouble(df.format(total40 / number40));
        per50 = Double.parseDouble(df.format(total50 / number50));
        per60 = Double.parseDouble(df.format(total60 / number60));
        per70 = Double.parseDouble(df.format(total70 / number70));
        per80 = Double.parseDouble(df.format(total80 / number80));
        per90 = Double.parseDouble(df.format(total90 / number90));

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

        if (number60 > 0) {
            count.put("60-70", new JapaClass(number60, per60));
        }

        if (number70 > 0) {
            count.put("70-80", new JapaClass(number70, per70));
        }

        if (number80 > 0) {
            count.put("80-90", new JapaClass(number80, per80));
        }

        if (number90 > 0) {
            count.put("90-100", new JapaClass(number90, per90));
        }

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
                    case "60-70":
                        lower = 60;
                        higher = 70;
                        break;
                    case "70-80":
                        lower = 70;
                        higher = 80;
                        break;
                    case "80-90":
                        lower = 80;
                        higher = 90;
                        break;
                    case "90-100":
                        lower = 90;
                        higher = 100;
                        break;
                    case "ALL":
                        lower = 0;
                        higher = 100;
                }
                Intent intent = new Intent(TurnoutFGList.this, TurnoutFGDetails.class);
                Bundle bundle = new Bundle();
                bundle.putLong("Date1", date1);
                bundle.putLong("Date2", date2);
                bundle.putString("FG",fg);
                bundle.putInt("Lower", lower);
                bundle.putInt("Higher", higher);
                bundle.putString("SpinPrograms", spinPrograms);
                bundle.putString("SpinCategories", spinCategories);
                bundle.putString("SpinSessions", spinSessions);
                bundle.putString("Collection",collection);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
            populateListCategoriesAndSessions();
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
        if (fg.equals("ALL")) {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        } else {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("fg",fg)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        }
    }

    public void populateListProgramsAndCategories() {
        if (fg.equals("ALL")) {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("session",spinSessions)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        } else {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("fg",fg)
                    .whereEqualTo("session",spinSessions)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        }
    }

    public void populateListProgramsAndSessions() {
        if (fg.equals("ALL")) {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        } else {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("fg",fg)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        }
    }

    public void populateListCategoriesAndSessions() {
        if (fg.equals("ALL")) {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("program",spinPrograms)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        } else {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("fg",fg)
                    .whereEqualTo("program",spinPrograms)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        }
    }

    public void populateListPrograms() {
        if (fg.equals("ALL")) {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("session",spinSessions)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        } else {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("fg",fg)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("session",spinSessions)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        }
    }

    public void populateListCategories() {
        if (fg.equals("ALL")) {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("session",spinSessions)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        } else {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("fg",fg)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("session",spinSessions)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        }
    }

    public void populateListSessions() {
        if (fg.equals("ALL")) {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("program",spinPrograms)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        } else {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("fg",fg)
                    .whereEqualTo("program",spinPrograms)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        }
    }

    public void populateList() {
        if (fg.equals("ALL")) {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("session",spinSessions)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        } else {
            total=0;
            number0=0; number10=0; number20=0; number30=0; number40=0; number50=0; number60=0;
            number70=0; number80=0; number90=0;
            total0=0.0; total10=0.0; total20=0.0; total30=0.0; total40=0.0; total50=0.0;
            total60=0.0; total70=0.0; total80=0.0; total90=0.0;

            fgboys
                    .whereGreaterThanOrEqualTo("edate",date1)
                    .whereLessThan("edate",date2)
                    .whereEqualTo("fg",fg)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("session",spinSessions)
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
                                } else if ((turnout >= 50) && (turnout <= 60)) {
                                    number50++;
                                    total50 += turnout;
                                } else if ((turnout >= 60) && (turnout <= 70)) {
                                    number60++;
                                    total60 += turnout;
                                } else if ((turnout >= 70) && (turnout <= 80)) {
                                    number70++;
                                    total70 += turnout;
                                } else if ((turnout >= 80) && (turnout <= 90)) {
                                    number80++;
                                    total80 += turnout;
                                }
                                else if ((turnout >= 90) && (turnout <= 100)) {
                                    number90++;
                                    total90 += turnout;
                                }
                            }
                            populateTurnoutsAdapter(count);
                        }
                    });
        }
    }
}

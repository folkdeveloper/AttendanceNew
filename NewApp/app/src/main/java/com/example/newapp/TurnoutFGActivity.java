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

public class TurnoutFGActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "MainActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private int total = 0;
    private double totalProbability = 0.0;
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
    DecimalFormat df;

    public void populateTurnoutsAdapter(TreeMap<String,JapaClass> count) {
        final JapaActivityAdapter adapter = new JapaActivityAdapter(count);
        mListView.setAdapter((ListAdapter) adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fg = adapter.getItem(position).getKey();
                Intent intent = new Intent(TurnoutFGActivity.this, TurnoutFGList.class);
                Bundle bundle = new Bundle();
                bundle.putLong("Date1", date1);
                bundle.putLong("Date2", date2);
                bundle.putString("FG", fg);
                bundle.putString("SpinPrograms", spinnerPrograms);
                bundle.putString("SpinCategories", spinnerCategories);
                bundle.putString("SpinSessions", spinnerSessions);
                bundle.putString("Collection", collection);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnout_fg);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1", date1);
        date2 = getIntent().getLongExtra("Date2", date2);
        df = new DecimalFormat("##.#");
        df.setRoundingMode(RoundingMode.DOWN);

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
                        totalProbability=0.0;

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
                                            totalProbability += turnout;

                                            if (count.containsKey(note.getFg())) {
                                                JapaClass number = count.get(note.getFg());
                                                int students = number.getStudents();
                                                int  total = (int) (students * number.getPercentage());
                                                students++;
                                                total += turnout;
                                                count.put(note.getFg(), new JapaClass(students,
                                                        Double.parseDouble(df.format(total / students))));
                                            } else {
                                                count.put(note.getFg(), new JapaClass(1, turnout));
                                            }
                                        }

                                        count.put("ALL",new JapaClass(total,Double.parseDouble(df.format(totalProbability / total))));
                                        populateTurnoutsAdapter(count);
                                    }
                                });
                    } else {
                        total=0;
                        totalProbability=0.0;

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
                                            totalProbability += turnout;

                                            if (count.containsKey(note.getFg())) {
                                                JapaClass number = count.get(note.getFg());
                                                int students = number.getStudents();
                                                int  total = (int) (students * number.getPercentage());
                                                students++;
                                                total += turnout;
                                                count.put(note.getFg(), new JapaClass(students,
                                                        Double.parseDouble(df.format(total / students))));
                                            } else {
                                                count.put(note.getFg(), new JapaClass(1, turnout));
                                            }
                                        }

                                        count.put("ALL",new JapaClass(total,Double.parseDouble(df.format(totalProbability / total))));
                                        populateTurnoutsAdapter(count);
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
                        totalProbability=0.0;
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
                                            totalProbability += turnout;

                                            if (count.containsKey(note.getFg())) {
                                                JapaClass number = count.get(note.getFg());
                                                int students = number.getStudents();
                                                int  total = (int) (students * number.getPercentage());
                                                students++;
                                                total += turnout;
                                                count.put(note.getFg(), new JapaClass(students,
                                                        Double.parseDouble(df.format(total / students))));
                                            } else {
                                                count.put(note.getFg(), new JapaClass(1, turnout));
                                            }
                                        }

                                        count.put("ALL",new JapaClass(total,Double.parseDouble(df.format(totalProbability / total))));
                                        populateTurnoutsAdapter(count);
                                    }
                                });
                    } else {
                        total=0;
                        totalProbability=0.0;

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
                                            totalProbability += turnout;

                                            if (count.containsKey(note.getFg())) {
                                                JapaClass number = count.get(note.getFg());
                                                int students = number.getStudents();
                                                int  total = (int) (students * number.getPercentage());
                                                students++;
                                                total += turnout;
                                                count.put(note.getFg(), new JapaClass(students,
                                                        Double.parseDouble(df.format(total / students))));
                                            } else {
                                                count.put(note.getFg(), new JapaClass(1, turnout));
                                            }
                                        }

                                        count.put("ALL",new JapaClass(total,Double.parseDouble(df.format(totalProbability / total))));
                                        populateTurnoutsAdapter(count);
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

    public void populateSessions(String spinCategory, String spinProgram) {
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
                    .whereEqualTo("program",spinnerPrograms)
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
                        totalProbability=0.0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate",date1)
                                .whereLessThan("edate",date2)
                                .whereEqualTo("program",spinnerPrograms)
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
                                            totalProbability += turnout;

                                            if (count.containsKey(note.getFg())) {
                                                JapaClass number = count.get(note.getFg());
                                                int students = number.getStudents();
                                                int  total = (int) (students * number.getPercentage());
                                                students++;
                                                total += turnout;
                                                count.put(note.getFg(), new JapaClass(students,
                                                        Double.parseDouble(df.format(total / students))));
                                            } else {
                                                count.put(note.getFg(), new JapaClass(1, turnout));
                                            }
                                        }

                                        count.put("ALL",new JapaClass(total,Double.parseDouble(df.format(totalProbability / total))));
                                        populateTurnoutsAdapter(count);
                                    }
                                });
                    } else {
                        total=0;
                        totalProbability=0.0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate",date1)
                                .whereLessThan("edate",date2)
                                .whereEqualTo("session",spinnerSessions)
                                .whereEqualTo("program",spinnerPrograms)
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
                                            totalProbability += turnout;

                                            if (count.containsKey(note.getFg())) {
                                                JapaClass number = count.get(note.getFg());
                                                int students = number.getStudents();
                                                int  total = (int) (students * number.getPercentage());
                                                students++;
                                                total += turnout;
                                                count.put(note.getFg(), new JapaClass(students,
                                                        Double.parseDouble(df.format(total / students))));
                                            } else {
                                                count.put(note.getFg(), new JapaClass(1, turnout));
                                            }
                                        }

                                        count.put("ALL",new JapaClass(total,Double.parseDouble(df.format(totalProbability / total))));
                                        populateTurnoutsAdapter(count);
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
                    .whereEqualTo("program",spinnerPrograms)
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
                        totalProbability=0.0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate",date1)
                                .whereLessThan("edate",date2)
                                .whereEqualTo("category",spinCategories)
                                .whereEqualTo("program",spinnerPrograms)
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
                                            totalProbability += turnout;

                                            if (count.containsKey(note.getFg())) {
                                                JapaClass number = count.get(note.getFg());
                                                int students = number.getStudents();
                                                int  total = (int) (students * number.getPercentage());
                                                students++;
                                                total += turnout;
                                                count.put(note.getFg(), new JapaClass(students,
                                                        Double.parseDouble(df.format(total / students))));
                                            } else {
                                                count.put(note.getFg(), new JapaClass(1, turnout));
                                            }
                                        }

                                        count.put("ALL",new JapaClass(total,Double.parseDouble(df.format(totalProbability / total))));
                                        populateTurnoutsAdapter(count);
                                    }
                                });
                    } else {
                        total=0;
                        totalProbability=0.0;

                        fgboys
                                .whereGreaterThanOrEqualTo("edate",date1)
                                .whereLessThan("edate",date2)
                                .whereEqualTo("category",spinCategories)
                                .whereEqualTo("session",spinnerSessions)
                                .whereEqualTo("program",spinnerPrograms)
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
                                            totalProbability += turnout;

                                            if (count.containsKey(note.getFg())) {
                                                JapaClass number = count.get(note.getFg());
                                                int students = number.getStudents();
                                                int  total = (int) (students * number.getPercentage());
                                                students++;
                                                total += turnout;
                                                count.put(note.getFg(), new JapaClass(students,
                                                        Double.parseDouble(df.format(total / students))));
                                            } else {
                                                count.put(note.getFg(), new JapaClass(1, turnout));
                                            }
                                        }

                                        count.put("ALL",new JapaClass(total,Double.parseDouble(df.format(totalProbability / total))));
                                        populateTurnoutsAdapter(count);
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

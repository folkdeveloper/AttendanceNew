package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class MatchRegistrationsGetProgram extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "RegistrationsPrograms";
    ListView mListView;
    private TextView mTextView, mTextView1;
    private long date1 = 0, date2 = 0;
    private int total = 0, total2 = 0;
    String url = "";
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
    final String[] categories = new String[]{};
    final String[] sessions = new String[]{};
    String[] programs = new String[]{};
    Spinner program, category, session;
    int posProgram, posCategory, posSession;
    private TextView mDisplayDate;
    private String date = "";
    private long epoch = 0;
    private String fg = "";
    ArrayList<String> fids = new ArrayList<>();
    ArrayList<String> fidsReg = new ArrayList<>();
    String documentID = "";
    int reg = 0, notReg = 0, att = 0;
    private String spinPrograms = "", spinCategories = "", spinSessions = "";
    private ArrayList<String> fid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Create");
        setContentView(R.layout.activity_match_registrations_get_program);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection("ProgramDetails");
        mTextView = findViewById(R.id.textView2);
        mTextView1 = findViewById(R.id.textView3);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1", date1);
        date2 = getIntent().getLongExtra("Date2", date2);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        fg = getIntent().getStringExtra("FG");
        spinPrograms = getIntent().getStringExtra("SpinPrograms");
        spinCategories = getIntent().getStringExtra("SpinCategoris");
        spinSessions = getIntent().getStringExtra("SpinSessions");
        fid = (ArrayList<String>) getIntent().getSerializableExtra("FID");
//        mTextView.setText(String.valueOf(fid.size()));

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MatchRegistrationsGetProgram.this,
//                        R.style.Theme_AppCompat_DayNight_Dialog,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
                cal.add(Calendar.MONTH, -1);
                long minDate = cal.getTime().getTime();
//                dialog.getDatePicker().setMinDate(minDate);
//                dialog.getDatePicker().setMaxDate((System.currentTimeMillis()));
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String mon = "";

                if (month <= 9) {
                    mon = "0" + month;

                    String dayOf = "";
                    if (day < 10) {
                        dayOf = "0" + day;
                        date = year + "-" + mon + "-" + dayOf;
                    } else {
                        date = year + "-" + mon + "-" + day;
                    }
                } else {
                    String dayOf = "";
                    if (day < 10) {
                        dayOf = "0" + day;
                        date = year + "-" + month + "-" + dayOf;
                    } else {
                        date = year + "-" + month + "-" + day;
                    }
                }
                mDisplayDate.setText(date);

                mTextView.setText(date);

                mTextView.setText(spinnerPrograms + " " + spinnerCategories + " " + spinnerSessions);
            }
        };
    }

    public void select(View v) {
        populatePrograms();
    }

    public void populatePrograms() {
        programsList = Arrays.asList(programs);
        finalPrograms.addAll(programsList);

        program = findViewById(R.id.program);
        category = findViewById(R.id.category);
        session = findViewById(R.id.session);

        adapterPrograms = new ArrayAdapter<String>(
                this, R.layout.spinner_item, finalPrograms);
        adapterPrograms.setDropDownViewResource(R.layout.spinner_item);
        program.setAdapter(adapterPrograms);

        fgboys
                .whereEqualTo("date", date)
//                .whereEqualTo("program",spinPrograms)
//                .whereEqualTo("category",spinCategories)
//                .whereEqualTo("session",spinSessions)
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

                populateCategories();
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

    public void populateCategories() {
        final String spinPrograms = spinnerPrograms;

            finalCategories = new ArrayList<String>();
            categoriesList = Arrays.asList(categories);
            finalCategories.addAll(categoriesList);

            adapterCategories = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalCategories);

            fgboys
                    .whereEqualTo("date", date)
                    .whereEqualTo("program",spinnerPrograms)
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

                    populateSessions();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
    }

    public void populateSessions() {
        final String spinCategories = spinCategory;
        final String spinPrograms = spinProgram;
        reg=0;notReg=0;att=0;

        finalSessions = new ArrayList<String>();
        sessionsList = Arrays.asList(sessions);
        finalSessions.addAll(sessionsList);

        adapterSessions = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalSessions);

            fgboys
                    .whereEqualTo("date", date)
                    .whereEqualTo("program",spinnerPrograms)
                    .whereEqualTo("category",spinnerCategories)
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

//                                Log.d(TAG, "onEvent: In");

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
                    fgboys
                            .whereEqualTo("date",date)
                            .whereEqualTo("program",spinnerPrograms)
                            .whereEqualTo("category",spinnerCategories)
                            .whereEqualTo("session",spinnerSessions)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    if (e != null)
                                        return;

                                    String data = "";

                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        Note note = documentSnapshot.toObject(Note.class);

                                        note.setDocumentId(documentSnapshot.getId());

//                                        Log.d(TAG, "onEvent: InSecond");

                                        documentID = note.getDocumentId();

                                        data += "Document ID: " + documentID + "\n\n";

                                    }
//                                    mTextView.setText(documentID);
                                }
                            });

                    db.collection("AttendanceDemo")
                            .whereGreaterThanOrEqualTo("edate",date1)
                            .whereLessThan("edate",date2)
                            .whereEqualTo("fg",fg)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    if (e != null)
                                        return;

                                    String data = "";

                                    ArrayList<String> countName = new ArrayList<>();

                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        Note note = documentSnapshot.toObject(Note.class);

                                        if (fid.contains(note.getFid())) {
                                            if (countName.contains(note.getFid())) {
                                            continue;
                                        } else {
                                            countName.add(note.getFid());
                                            att++;
                                            Log.d(TAG, "onEvent: In"+" ; " + note.getZmob() + " ; " +note.getSession());
                                            fids.add(note.getFid());
                                            data += "FOLK ID: " + note.getFid() + "\n";
                                        }
                                        }
                                    }
//                                    mTextView.setText(data);
                                }
                            });

                    db.collection("RegistrationDemo")
                            .whereEqualTo("pid",documentID)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    if (e != null)
                                        return;
                                    TreeMap<String, Integer> count = new TreeMap<>();

                                    for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                                        Note note = documentSnapshot.toObject(Note.class);

                                        if (note.getFid() == null)
                                            continue;

                                        if (note.getPid() == null)
                                            continue;

//                                        Log.d(TAG, "onEvent: Reg" + note.getFid());
                                        if (fids.contains(note.getFid())) {
                                            reg++;
                                            fidsReg.add(note.getFid());
                                        }
                                    }
                                    notReg = att - reg;
                                    count.put("Registered",reg);
                                    count.put("Not registered",notReg);
//                                    mTextView1.setText(String.valueOf(reg));

                                    final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                                    mListView.setAdapter((ListAdapter) adapter);

                                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            //                final String fg_name = mListView.getItemAtPosition(i).toString();
                                            //                TextView fgName = (TextView) findViewById(R.id.text_view_1);
                                            String status = adapter.getItem(i).getKey();
                                            Intent intent = new Intent(MatchRegistrationsGetProgram.this, MatchRegistrationsThird.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putLong("Date1",date1);
                                            bundle.putLong("Date2",date2);
                                            bundle.putString("FG",fg);
                                            bundle.putString("Reg", status);
//                                            Log.d("TLActivity", "onItemClick: Main: " + spinPrograms);
                                            bundle.putString("SpinPrograms", spinPrograms);
                                            bundle.putString("SpinCategories", spinnerCategories);
                                            bundle.putString("SpinSessions", spinnerSessions);
                                            bundle.putString("Collection",collection);
                                            intent.putExtra("FIDReg",fidsReg);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
    }
}
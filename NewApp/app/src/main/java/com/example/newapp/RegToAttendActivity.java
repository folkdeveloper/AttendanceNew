package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class RegToAttendActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "OccupationActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public int totalReg, totalCom, totalNotCom, totalAtt, totalANU, totalNA, totalCNA, total;
    public int totalNextReg, totalNextCom, totalNextNotCom, totalNextAtt;
    public double p1, p2, p3 ,p4;
    TreeMap<String,RegToAttend> count = null;
    View headerView = null;
    TextView mTextView6, mTextView7, mTextView8 , mTextView9, mTextView10, mTextView12, mTextView13,
            mTextView14, mTextView15;
    private int total1 = 0, total2 = 0;
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
    String spinSession = "";
    final String[] categories = new String[]{"ALL"};
    String[] programs = new String[]{"ALL"};
    final String[] sessions = new String[]{"ALL"};
    Spinner program, category, session;
    int posProgram, posCategory, posSession;
    Button totalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_to_attend);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        totalReg = 0;
        totalCom = 0;
        totalNotCom = 0;
        totalAtt = 0;
        total = 0;
        p1 = 0.0; p2 = 0.0; p3 = 0.0; p4 = 0.0;
        totalButton = findViewById(R.id.button18);
        mTextView6 = (TextView) findViewById(R.id.text6);
        mTextView7 = (TextView) findViewById(R.id.text7);
        mTextView8 = (TextView) findViewById(R.id.text8);
        mTextView9 = (TextView) findViewById(R.id.text9);
        mTextView10 = (TextView) findViewById(R.id.text10);
        mTextView12 = (TextView) findViewById(R.id.text12);
        mTextView13 = (TextView) findViewById(R.id.text13);
        mTextView14 = (TextView) findViewById(R.id.text14);
        mTextView15 = (TextView) findViewById(R.id.text15);
        program = findViewById(R.id.program);
        category = findViewById(R.id.category);
        session = findViewById(R.id.session);

        count = new TreeMap<>();
        count.clear();
        mListView.setAdapter(null);

        programsList = Arrays.asList(programs);
        finalPrograms.addAll(programsList);

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
                                continue;;

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

    @Override
    protected void onResume() {
        super.onResume();
        programs = new String[]{};
        total=0;totalAtt=0;
        totalNotCom=0; totalReg=0; totalCom=0;

        program.setSelection(posProgram);
        category.setSelection(posCategory);
        session.setSelection(posSession);
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
                                    continue;;

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
                                    continue;;

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
//                        populateSessions(spinCategory,spinPrograms);
                    } else {
                        spinCategory = "2";
//                        populateSessions(spinCategory,spinPrograms);
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
            totalAtt = 0;
            totalNotCom = 0;
            totalReg = 0;
            totalCom = 0;

            fgboys
//                    .whereEqualTo("zzdate", date)
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
                                ;

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
                public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    posSession = position;
                    totalNotCom = 0;
                    totalReg = 0;
                    totalCom = 0;
                    totalAtt = 0;

                    if (spinnerSessions.equals("ALL")) {
                        total = 0;
                        totalAtt = 0;
                        totalNotCom = 0;
                        totalReg = 0;
                        totalCom = 0;
                        totalCNA = 0;
                        fgboys
//                                .whereEqualTo("zzdate", date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("program",spinnerPrograms)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        String data = "";
//                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);
                                            total++;
//                                            Log.d(TAG, "onEvent: Occupation"
//                                                    + note.getOccupation() + " " + note.getZtl());

                                            if (note.getOccupation() == null) {
                                                note.setOccupation("Not updated");
                                            }

                                            if (count.containsKey(note.getFg())) {
                                                RegToAttend regToAttend = count.get(note.getFg());
                                                int reg = regToAttend.getReg();
                                                int com = regToAttend.getCom();
                                                int notcom = regToAttend.getNotcom();
                                                int att = regToAttend.getAtt();
                                                int anu = regToAttend.getAnu();
                                                int na = regToAttend.getNa();
                                                int cna = regToAttend.getCna();

                                                reg++;
                                                totalReg++;
                                                count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    com++;att++;
                                                    totalCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++;na++; cna++;
                                                    totalCom++;com++; totalCNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu,na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    notcom++;na++;
                                                    totalNotCom++;totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    notcom++;att++;
                                                    totalNotCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    anu++;att++;
                                                    totalANU++; totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                } else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    na++; totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }
                                            } else {
                                                int reg = 1;

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 1, 0 ,0, 0));
                                                }
                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++; totalCNA++;
                                                    totalCom++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 0, 0,1, 1));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    totalNotCom++;
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 0, 0,1,0));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalNotCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 1, 0,0,0));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    totalANU++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 1, 1,0,0));
                                                }
                                                else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 0, 0,1,0));
                                                }
                                            }
                                        }

                                        final RegToAttendAdapter adapter = new RegToAttendAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        Double p1 = ((double) totalReg / total) * 100;
                                        Double p2 = ((double) totalCom / total) * 100;
                                        Double p3 = ((double) totalNotCom / total) * 100;
                                        Double p4 = ((double) totalAtt / total) * 100;
                                        Double p5 = Double.parseDouble(df.format(p1));
                                        Double p6 = Double.parseDouble(df.format(p2));
                                        Double p7 = Double.parseDouble(df.format(p3));
                                        Double p8 = Double.parseDouble(df.format(p4));

                                        mTextView7.setText(String.valueOf(total));
                                        mTextView8.setText(String.valueOf(totalCom));
                                        mTextView9.setText(String.valueOf(totalNotCom));
                                        mTextView10.setText(String.valueOf(totalAtt));
                                        mTextView12.setText("100");
                                        mTextView13.setText(p6.toString());
                                        mTextView14.setText(p7.toString());
                                        mTextView15.setText(p8.toString());

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String fg = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putString("FG", fg);
                                                bundle.putInt("REG",adapter.getItem(i).getValue().getReg());
                                                bundle.putInt("COM",adapter.getItem(i).getValue().getCom());
                                                bundle.putInt("NOTCOM",adapter.getItem(i).getValue().getNotcom());
                                                bundle.putInt("ATT",adapter.getItem(i).getValue().getAtt());
                                                bundle.putInt("ANU",adapter.getItem(i).getValue().getAnu());
                                                bundle.putInt("NA",adapter.getItem(i).getValue().getNa());
                                                bundle.putInt("CNA",adapter.getItem(i).getValue().getCna());
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        totalNextReg = total;
                                        totalNextCom = totalCom;
                                        totalNextNotCom = totalNotCom;
                                        totalNextAtt = totalAtt;

                                        totalButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("REG",totalNextReg);
                                                bundle.putInt("COM",totalNextCom);
                                                bundle.putInt("NOTCOM",totalNextNotCom);
                                                bundle.putInt("ATT",totalNextAtt);
                                                bundle.putInt("ANU",totalANU);
                                                bundle.putInt("NA",totalNA);
                                                bundle.putInt("CNA",totalCNA);
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                bundle.putString("ClickedFirst","ALL");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        count.clear();
                                    }
                                });
                    } else {
                        total = 0;
                        totalAtt = 0;
                        totalNotCom = 0;
                        totalReg = 0;
                        totalCom = 0;
                        totalCNA = 0;

                        fgboys
//                                .whereEqualTo("zzdate", date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("program",spinnerPrograms)
                                .whereEqualTo("session", spinnerSessions)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        String data = "";

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);
                                            total++;
//                                            Log.d(TAG, "onEvent: Occupation"
//                                                    + note.getOccupation() + " " + note.getZtl());

                                            if (note.getOccupation() == null) {
                                                note.setOccupation("Not updated");
                                            }

                                            if (count.containsKey(note.getFg())) {
                                                RegToAttend regToAttend = count.get(note.getFg());
                                                int reg = regToAttend.getReg();
                                                int com = regToAttend.getCom();
                                                int notcom = regToAttend.getNotcom();
                                                int att = regToAttend.getAtt();
                                                int anu = regToAttend.getAnu();
                                                int na = regToAttend.getNa();
                                                int cna = regToAttend.getCna();

                                                reg++;
                                                totalReg++;
                                                count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    com++;att++;
                                                    totalCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++;na++; cna++;
                                                    totalCom++;com++; totalCNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu,na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    notcom++;na++;
                                                    totalNotCom++;totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    notcom++;att++;
                                                    totalNotCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    anu++;att++;
                                                    totalANU++; totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                } else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    na++; totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }
                                            } else {
                                                int reg = 1;

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 1, 0 ,0, 0));
                                                }
                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++; totalCNA++;
                                                    totalCom++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 0, 0,1, 1));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    totalNotCom++;
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 0, 0,1,0));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalNotCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 1, 0,0,0));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    totalANU++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 1, 1,0,0));
                                                }
                                                else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 0, 0,1,0));
                                                }
                                            }
                                        }

                                        final RegToAttendAdapter adapter = new RegToAttendAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        Double p1 = ((double) totalReg / total) * 100;
                                        Double p2 = ((double) totalCom / total) * 100;
                                        Double p3 = ((double) totalNotCom / total) * 100;
                                        Double p4 = ((double) totalAtt / total) * 100;
                                        Double p5 = Double.parseDouble(df.format(p1));
                                        Double p6 = Double.parseDouble(df.format(p2));
                                        Double p7 = Double.parseDouble(df.format(p3));
                                        Double p8 = Double.parseDouble(df.format(p4));

                                        mTextView7.setText(String.valueOf(total));
                                        mTextView8.setText(String.valueOf(totalCom));
                                        mTextView9.setText(String.valueOf(totalNotCom));
                                        mTextView10.setText(String.valueOf(totalAtt));
                                        mTextView12.setText("100");
                                        mTextView13.setText(p6.toString());
                                        mTextView14.setText(p7.toString());
                                        mTextView15.setText(p8.toString());

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String fg = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putString("FG", fg);
                                                bundle.putInt("REG",adapter.getItem(i).getValue().getReg());
                                                bundle.putInt("COM",adapter.getItem(i).getValue().getCom());
                                                bundle.putInt("NOTCOM",adapter.getItem(i).getValue().getNotcom());
                                                bundle.putInt("ATT",adapter.getItem(i).getValue().getAtt());
                                                bundle.putInt("ANU",adapter.getItem(i).getValue().getAnu());
                                                bundle.putInt("NA",adapter.getItem(i).getValue().getNa());
                                                bundle.putInt("CNA",adapter.getItem(i).getValue().getCna());
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        totalNextReg = total;
                                        totalNextCom = totalCom;
                                        totalNextNotCom = totalNotCom;
                                        totalNextAtt = totalAtt;

                                        totalButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("REG",totalNextReg);
                                                bundle.putInt("COM",totalNextCom);
                                                bundle.putInt("NOTCOM",totalNextNotCom);
                                                bundle.putInt("ATT",totalNextAtt);
                                                bundle.putInt("ANU",totalANU);
                                                bundle.putInt("NA",totalNA);
                                                bundle.putInt("CNA",totalCNA);
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                bundle.putString("ClickedFirst","ALL");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        count.clear();
//                        mListView.setAdapter(null);
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
            totalAtt = 0;
            totalNotCom = 0;
            totalReg = 0;
            totalCom = 0;
            totalCNA = 0;

            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
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
                                ;

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
                    totalNotCom = 0;
                    totalReg = 0;
                    totalCom = 0;
                    totalAtt = 0;
                    totalCNA = 0;

                    if (spinnerSessions.equals("ALL")) {
                        total = 0;
                        totalAtt = 0;
                        totalNotCom = 0;
                        totalReg = 0;
                        totalCom = 0;
                        totalCNA = 0;

                        fgboys
//                                .whereEqualTo("zzdate", date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("program",spinnerPrograms)
                                .whereEqualTo("category",spinnerCategories)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        String data = "";
//                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);
                                            total++;
//                                            Log.d(TAG, "onEvent: Occupation"
//                                                    + note.getOccupation() + " " + note.getZtl());

                                            if (note.getOccupation() == null) {
                                                note.setOccupation("Not updated");
                                            }

                                            if (count.containsKey(note.getFg())) {
                                                RegToAttend regToAttend = count.get(note.getFg());
                                                int reg = regToAttend.getReg();
                                                int com = regToAttend.getCom();
                                                int notcom = regToAttend.getNotcom();
                                                int att = regToAttend.getAtt();
                                                int anu = regToAttend.getAnu();
                                                int na = regToAttend.getNa();
                                                int cna = regToAttend.getCna();

                                                reg++;
                                                totalReg++;
                                                count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    com++;att++;
                                                    totalCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++;na++; cna++;
                                                    totalCom++;com++; totalCNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu,na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    notcom++;na++;
                                                    totalNotCom++;totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    notcom++;att++;
                                                    totalNotCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    anu++;att++;
                                                    totalANU++; totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                } else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    na++; totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }
                                            } else {
                                                int reg = 1;

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 1, 0 ,0, 0));
                                                }
                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++; totalCNA++;
                                                    totalCom++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 0, 0,1, 1));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    totalNotCom++;
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 0, 0,1,0));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalNotCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 1, 0,0,0));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    totalANU++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 1, 1,0,0));
                                                }
                                                else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 0, 0,1,0));
                                                }
                                            }
                                        }

                                        final RegToAttendAdapter adapter = new RegToAttendAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        Double p1 = ((double) totalReg / total) * 100;
                                        Double p2 = ((double) totalCom / total) * 100;
                                        Double p3 = ((double) totalNotCom / total) * 100;
                                        Double p4 = ((double) totalAtt / total) * 100;
                                        Double p5 = Double.parseDouble(df.format(p1));
                                        Double p6 = Double.parseDouble(df.format(p2));
                                        Double p7 = Double.parseDouble(df.format(p3));
                                        Double p8 = Double.parseDouble(df.format(p4));

                                        mTextView7.setText(String.valueOf(total));
                                        mTextView8.setText(String.valueOf(totalCom));
                                        mTextView9.setText(String.valueOf(totalNotCom));
                                        mTextView10.setText(String.valueOf(totalAtt));
                                        mTextView12.setText("100");
                                        mTextView13.setText(p6.toString());
                                        mTextView14.setText(p7.toString());
                                        mTextView15.setText(p8.toString());

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String fg = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putString("FG", fg);
                                                bundle.putInt("REG",adapter.getItem(i).getValue().getReg());
                                                bundle.putInt("COM",adapter.getItem(i).getValue().getCom());
                                                bundle.putInt("NOTCOM",adapter.getItem(i).getValue().getNotcom());
                                                bundle.putInt("ATT",adapter.getItem(i).getValue().getAtt());
                                                bundle.putInt("ANU",adapter.getItem(i).getValue().getAnu());
                                                bundle.putInt("NA",adapter.getItem(i).getValue().getNa());
                                                bundle.putInt("CNA",adapter.getItem(i).getValue().getCna());
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        totalNextReg = total;
                                        totalNextCom = totalCom;
                                        totalNextNotCom = totalNotCom;
                                        totalNextAtt = totalAtt;

                                        totalButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("REG",totalNextReg);
                                                bundle.putInt("COM",totalNextCom);
                                                bundle.putInt("NOTCOM",totalNextNotCom);
                                                bundle.putInt("ATT",totalNextAtt);
                                                bundle.putInt("ANU",totalANU);
                                                bundle.putInt("NA",totalNA);
                                                bundle.putInt("CNA",totalCNA);
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                bundle.putString("ClickedFirst","ALL");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        count.clear();
                                    }
                                });
                    } else {
                        total = 0;
                        totalAtt = 0;
                        totalNotCom = 0;
                        totalReg = 0;
                        totalCom = 0;
                        totalCNA = 0;
                        fgboys
//                                .whereEqualTo("zzdate", date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("program",spinnerPrograms)
                                .whereEqualTo("category",spinnerCategories)
                                .whereEqualTo("session", spinnerSessions)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        String data = "";

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);
                                            total++;
//                                            Log.d(TAG, "onEvent: Occupation"
//                                                    + note.getOccupation() + " " + note.getZtl());

                                            if (note.getOccupation() == null) {
                                                note.setOccupation("Not updated");
                                            }

                                            if (count.containsKey(note.getFg())) {
                                                RegToAttend regToAttend = count.get(note.getFg());
                                                int reg = regToAttend.getReg();
                                                int com = regToAttend.getCom();
                                                int notcom = regToAttend.getNotcom();
                                                int att = regToAttend.getAtt();
                                                int anu = regToAttend.getAnu();
                                                int na = regToAttend.getNa();
                                                int cna = regToAttend.getCna();

                                                reg++;
                                                totalReg++;
                                                count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    com++;att++;
                                                    totalCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++;na++; cna++;
                                                    totalCom++;com++; totalCNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu,na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    notcom++;na++;
                                                    totalNotCom++;totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    notcom++;att++;
                                                    totalNotCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    anu++;att++;
                                                    totalANU++; totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                } else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    na++; totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }
                                            } else {
                                                int reg = 1;

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 1, 0 ,0, 0));
                                                }
                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++; totalCNA++;
                                                    totalCom++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 0, 0,1, 1));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    totalNotCom++;
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 0, 0,1,0));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalNotCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 1, 0,0,0));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    totalANU++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 1, 1,0,0));
                                                }
                                                else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 0, 0,1,0));
                                                }
                                            }
                                        }

                                        final RegToAttendAdapter adapter = new RegToAttendAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        Double p1 = ((double) totalReg / total) * 100;
                                        Double p2 = ((double) totalCom / total) * 100;
                                        Double p3 = ((double) totalNotCom / total) * 100;
                                        Double p4 = ((double) totalAtt / total) * 100;
                                        Double p5 = Double.parseDouble(df.format(p1));
                                        Double p6 = Double.parseDouble(df.format(p2));
                                        Double p7 = Double.parseDouble(df.format(p3));
                                        Double p8 = Double.parseDouble(df.format(p4));

                                        mTextView7.setText(String.valueOf(total));
                                        mTextView8.setText(String.valueOf(totalCom));
                                        mTextView9.setText(String.valueOf(totalNotCom));
                                        mTextView10.setText(String.valueOf(totalAtt));
                                        mTextView12.setText("100");
                                        mTextView13.setText(p6.toString());
                                        mTextView14.setText(p7.toString());
                                        mTextView15.setText(p8.toString());

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String fg = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putString("FG", fg);
                                                bundle.putInt("REG",adapter.getItem(i).getValue().getReg());
                                                bundle.putInt("COM",adapter.getItem(i).getValue().getCom());
                                                bundle.putInt("NOTCOM",adapter.getItem(i).getValue().getNotcom());
                                                bundle.putInt("ATT",adapter.getItem(i).getValue().getAtt());
                                                bundle.putInt("ANU",adapter.getItem(i).getValue().getAnu());
                                                bundle.putInt("NA",adapter.getItem(i).getValue().getNa());
                                                bundle.putInt("CNA",adapter.getItem(i).getValue().getCna());
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        totalNextReg = total;
                                        totalNextCom = totalCom;
                                        totalNextNotCom = totalNotCom;
                                        totalNextAtt = totalAtt;

                                        totalButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("REG",totalNextReg);
                                                bundle.putInt("COM",totalNextCom);
                                                bundle.putInt("NOTCOM",totalNextNotCom);
                                                bundle.putInt("ATT",totalNextAtt);
                                                bundle.putInt("ANU",totalANU);
                                                bundle.putInt("NA",totalNA);
                                                bundle.putInt("CNA",totalCNA);
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                bundle.putString("ClickedFirst","ALL");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        count.clear();
//                        mListView.setAdapter(null);
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

    public void populateSessionsPrograms(String spinCategory, final String spinProgram) {
        final String spinCategories = spinCategory;
        final String spinPrograms = spinProgram;

        if (spinCategory.equals("1")) {
            finalSessions = new ArrayList<String>();
            sessionsList = Arrays.asList(sessions);
            finalSessions.addAll(sessionsList);

            adapterSessions = new ArrayAdapter<String>
                    (this, R.layout.spinner_item, finalSessions);

            fgboys
//                    .whereEqualTo("zzdate", date)
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
                                ;

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
                public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                    spinnerSessions = parent.getItemAtPosition(position).toString();
                    posSession = position;

                    if (spinnerSessions.equals("ALL")) {
                        total = 0;
                        totalAtt = 0;
                        totalNotCom = 0;
                        totalReg = 0;
                        totalCom = 0;
                        totalANU = 0;
                        totalNA = 0;
                        totalCNA = 0;

                        fgboys
//                                .whereEqualTo("zzdate", date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        String data = "";
//                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);
                                            total++;
//                                            Log.d(TAG, "onEvent: Occupation"
//                                                    + note.getOccupation() + " " + note.getZtl());

                                            if (note.getOccupation() == null) {
                                                note.setOccupation("Not updated");
                                            }

                                            if (count.containsKey(note.getFg())) {
                                                RegToAttend regToAttend = count.get(note.getFg());
                                                int reg = regToAttend.getReg();
                                                int com = regToAttend.getCom();
                                                int notcom = regToAttend.getNotcom();
                                                int att = regToAttend.getAtt();
                                                int anu = regToAttend.getAnu();
                                                int na = regToAttend.getNa();
                                                int cna = regToAttend.getCna();

                                                reg++;
                                                totalReg++;
                                                count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    com++;att++;
                                                    totalCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++;na++; cna++;
                                                    totalCom++;com++; totalCNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu,na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    notcom++;na++;
                                                    totalNotCom++;totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    notcom++;att++;
                                                    totalNotCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    anu++;att++;
                                                    totalANU++; totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                } else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    na++; totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }
                                            } else {
                                                int reg = 1;

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 1, 0 ,0, 0));
                                                }
                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++; totalCNA++;
                                                    totalCom++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 0, 0,1, 1));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    totalNotCom++;
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 0, 0,1,0));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalNotCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 1, 0,0,0));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    totalANU++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 1, 1,0,0));
                                                }
                                                else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 0, 0,1,0));
                                                }
                                            }
                                        }

                                        final RegToAttendAdapter adapter = new RegToAttendAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        Double p1 = ((double) totalReg / total) * 100;
                                        Double p2 = ((double) totalCom / total) * 100;
                                        Double p3 = ((double) totalNotCom / total) * 100;
                                        Double p4 = ((double) totalAtt / total) * 100;
                                        Double p5 = Double.parseDouble(df.format(p1));
                                        Double p6 = Double.parseDouble(df.format(p2));
                                        Double p7 = Double.parseDouble(df.format(p3));
                                        Double p8 = Double.parseDouble(df.format(p4));

                                        mTextView7.setText(String.valueOf(total));
                                        mTextView8.setText(String.valueOf(totalCom));
                                        mTextView9.setText(String.valueOf(totalNotCom));
                                        mTextView10.setText(String.valueOf(totalAtt));
                                        mTextView12.setText("100");
                                        mTextView13.setText(p6.toString());
                                        mTextView14.setText(p7.toString());
                                        mTextView15.setText(p8.toString());

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String fg = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putString("FG", fg);
                                                bundle.putInt("REG",adapter.getItem(i).getValue().getReg());
                                                bundle.putInt("COM",adapter.getItem(i).getValue().getCom());
                                                bundle.putInt("NOTCOM",adapter.getItem(i).getValue().getNotcom());
                                                bundle.putInt("ATT",adapter.getItem(i).getValue().getAtt());
                                                bundle.putInt("ANU",adapter.getItem(i).getValue().getAnu());
                                                bundle.putInt("NA",adapter.getItem(i).getValue().getNa());
                                                bundle.putInt("CNA",adapter.getItem(i).getValue().getCna());
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        totalNextReg = total;
                                        totalNextCom = totalCom;
                                        totalNextNotCom = totalNotCom;
                                        totalNextAtt = totalAtt;

                                        totalButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("REG",totalNextReg);
                                                bundle.putInt("COM",totalNextCom);
                                                bundle.putInt("NOTCOM",totalNextNotCom);
                                                bundle.putInt("ATT",totalNextAtt);
                                                bundle.putInt("ANU",totalANU);
                                                bundle.putInt("NA",totalNA);
                                                bundle.putInt("CNA",totalCNA);
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                bundle.putString("ClickedFirst","ALL");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        count.clear();
                                    }
                                });
                    } else {
                        total = 0;
                        totalAtt = 0;
                        totalNotCom = 0;
                        totalReg = 0;
                        totalCom = 0;
                        totalCNA = 0;

                        fgboys
//                                .whereEqualTo("zzdate", date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("session", spinnerSessions)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        String data = "";

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);
                                            total++;
//                                            Log.d(TAG, "onEvent: Occupation"
//                                                    + note.getOccupation() + " " + note.getZtl());

                                            if (note.getOccupation() == null) {
                                                note.setOccupation("Not updated");
                                            }

                                            if (count.containsKey(note.getFg())) {
                                                RegToAttend regToAttend = count.get(note.getFg());
                                                int reg = regToAttend.getReg();
                                                int com = regToAttend.getCom();
                                                int notcom = regToAttend.getNotcom();
                                                int att = regToAttend.getAtt();
                                                int anu = regToAttend.getAnu();
                                                int na = regToAttend.getNa();
                                                int cna = regToAttend.getCna();

                                                reg++;
                                                totalReg++;
                                                count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    com++;att++;
                                                    totalCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++;na++; cna++;
                                                    totalCom++;com++; totalCNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu,na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    notcom++;na++;
                                                    totalNotCom++;totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    notcom++;att++;
                                                    totalNotCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    anu++;att++;
                                                    totalANU++; totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                } else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    na++; totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }
                                            } else {
                                                int reg = 1;

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 1, 0 ,0, 0));
                                                }
                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++; totalCNA++;
                                                    totalCom++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 0, 0,1, 1));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    totalNotCom++;
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 0, 0,1,0));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalNotCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 1, 0,0,0));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    totalANU++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 1, 1,0,0));
                                                }
                                                else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 0, 0,1,0));
                                                }
                                            }
                                        }

                                        final RegToAttendAdapter adapter = new RegToAttendAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        Double p1 = ((double) totalReg / total) * 100;
                                        Double p2 = ((double) totalCom / total) * 100;
                                        Double p3 = ((double) totalNotCom / total) * 100;
                                        Double p4 = ((double) totalAtt / total) * 100;
                                        Double p5 = Double.parseDouble(df.format(p1));
                                        Double p6 = Double.parseDouble(df.format(p2));
                                        Double p7 = Double.parseDouble(df.format(p3));
                                        Double p8 = Double.parseDouble(df.format(p4));

                                        mTextView7.setText(String.valueOf(total));
                                        mTextView8.setText(String.valueOf(totalCom));
                                        mTextView9.setText(String.valueOf(totalNotCom));
                                        mTextView10.setText(String.valueOf(totalAtt));
                                        mTextView12.setText("100");
                                        mTextView13.setText(p6.toString());
                                        mTextView14.setText(p7.toString());
                                        mTextView15.setText(p8.toString());

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String fg = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putString("FG", fg);
                                                bundle.putInt("REG",adapter.getItem(i).getValue().getReg());
                                                bundle.putInt("COM",adapter.getItem(i).getValue().getCom());
                                                bundle.putInt("NOTCOM",adapter.getItem(i).getValue().getNotcom());
                                                bundle.putInt("ATT",adapter.getItem(i).getValue().getAtt());
                                                bundle.putInt("ANU",adapter.getItem(i).getValue().getAnu());
                                                bundle.putInt("NA",adapter.getItem(i).getValue().getNa());
                                                bundle.putInt("CNA",adapter.getItem(i).getValue().getCna());
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        totalNextReg = total;
                                        totalNextCom = totalCom;
                                        totalNextNotCom = totalNotCom;
                                        totalNextAtt = totalAtt;

                                        totalButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("REG",totalNextReg);
                                                bundle.putInt("COM",totalNextCom);
                                                bundle.putInt("NOTCOM",totalNextNotCom);
                                                bundle.putInt("ATT",totalNextAtt);
                                                bundle.putInt("ANU",totalANU);
                                                bundle.putInt("NA",totalNA);
                                                bundle.putInt("CNA",totalCNA);
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                bundle.putString("ClickedFirst","ALL");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                        count.clear();
//                        mListView.setAdapter(null);
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
            totalAtt = 0;
            totalNotCom = 0;
            totalReg = 0;
            totalCom = 0;
            totalCNA = 0;

            fgboys
//                    .whereEqualTo("zzdate", date)
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
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
                                ;

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
                    totalNotCom = 0;
                    totalReg = 0;
                    totalCom = 0;
                    totalAtt = 0;

                    if (spinnerSessions.equals("ALL")) {
                        total = 0;
                        totalAtt = 0;
                        totalNotCom = 0;
                        totalReg = 0;
                        totalCom = 0;
                        totalCNA = 0;
                        fgboys
//                                .whereEqualTo("zzdate", date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("category",spinnerCategories)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        String data = "";
//                                        Log.d(TAG, "onEvent: Out");

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);
                                            total++;
//                                            Log.d(TAG, "onEvent: Occupation"
//                                                    + note.getOccupation() + " " + note.getZtl());

                                            if (note.getOccupation() == null) {
                                                note.setOccupation("Not updated");
                                            }

                                            if (count.containsKey(note.getFg())) {
                                                RegToAttend regToAttend = count.get(note.getFg());
                                                int reg = regToAttend.getReg();
                                                int com = regToAttend.getCom();
                                                int notcom = regToAttend.getNotcom();
                                                int att = regToAttend.getAtt();
                                                int anu = regToAttend.getAnu();
                                                int na = regToAttend.getNa();
                                                int cna = regToAttend.getCna();

                                                reg++;
                                                totalReg++;
                                                count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    com++;att++;
                                                    totalCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++;na++; cna++;
                                                    totalCom++;com++; totalCNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu,na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    notcom++;na++;
                                                    totalNotCom++;totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    notcom++;att++;
                                                    totalNotCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    anu++;att++;
                                                    totalANU++; totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                } else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    na++; totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }
                                            } else {
                                                int reg = 1;

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 1, 0 ,0, 0));
                                                }
                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++; totalCNA++;
                                                    totalCom++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 0, 0,1, 1));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    totalNotCom++;
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 0, 0,1,0));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalNotCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 1, 0,0,0));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    totalANU++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 1, 1,0,0));
                                                }
                                                else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 0, 0,1,0));
                                                }
                                            }
                                        }

                                        final RegToAttendAdapter adapter = new RegToAttendAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        Double p1 = ((double) totalReg / total) * 100;
                                        Double p2 = ((double) totalCom / total) * 100;
                                        Double p3 = ((double) totalNotCom / total) * 100;
                                        Double p4 = ((double) totalAtt / total) * 100;
                                        Double p5 = Double.parseDouble(df.format(p1));
                                        Double p6 = Double.parseDouble(df.format(p2));
                                        Double p7 = Double.parseDouble(df.format(p3));
                                        Double p8 = Double.parseDouble(df.format(p4));

                                        mTextView7.setText(String.valueOf(total));
                                        mTextView8.setText(String.valueOf(totalCom));
                                        mTextView9.setText(String.valueOf(totalNotCom));
                                        mTextView10.setText(String.valueOf(totalAtt));
                                        mTextView12.setText("100");
                                        mTextView13.setText(p6.toString());
                                        mTextView14.setText(p7.toString());
                                        mTextView15.setText(p8.toString());

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String fg = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putString("FG", fg);
                                                bundle.putInt("REG",adapter.getItem(i).getValue().getReg());
                                                bundle.putInt("COM",adapter.getItem(i).getValue().getCom());
                                                bundle.putInt("NOTCOM",adapter.getItem(i).getValue().getNotcom());
                                                bundle.putInt("ATT",adapter.getItem(i).getValue().getAtt());
                                                bundle.putInt("ANU",adapter.getItem(i).getValue().getAnu());
                                                bundle.putInt("NA",adapter.getItem(i).getValue().getNa());
                                                bundle.putInt("CNA",adapter.getItem(i).getValue().getCna());
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        totalNextReg = total;
                                        totalNextCom = totalCom;
                                        totalNextNotCom = totalNotCom;
                                        totalNextAtt = totalAtt;

                                        totalButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("REG",totalNextReg);
                                                bundle.putInt("COM",totalNextCom);
                                                bundle.putInt("NOTCOM",totalNextNotCom);
                                                bundle.putInt("ATT",totalNextAtt);
                                                bundle.putInt("ANU",totalANU);
                                                bundle.putInt("NA",totalNA);
                                                bundle.putInt("CNA",totalCNA);
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                bundle.putString("ClickedFirst","ALL");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        count.clear();
                                    }
                                });
                    } else {
                        total = 0;
                        totalAtt = 0;
                        totalNotCom = 0;
                        totalReg = 0;
                        totalCom = 0;
                        totalCNA = 0;
                        fgboys
//                                .whereEqualTo("zzdate", date)
                                .whereGreaterThanOrEqualTo("edate", date1)
                                .whereLessThanOrEqualTo("edate", date2)
                                .whereEqualTo("category",spinnerCategories)
                                .whereEqualTo("session", spinnerSessions)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            return;
                                        }

                                        String data = "";

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Note note = documentSnapshot.toObject(Note.class);
                                            total++;
//                                            Log.d(TAG, "onEvent: Occupation"
//                                                    + note.getOccupation() + " " + note.getZtl());

                                            if (note.getOccupation() == null) {
                                                note.setOccupation("Not updated");
                                            }

                                            if (count.containsKey(note.getFg())) {
                                                RegToAttend regToAttend = count.get(note.getFg());
                                                int reg = regToAttend.getReg();
                                                int com = regToAttend.getCom();
                                                int notcom = regToAttend.getNotcom();
                                                int att = regToAttend.getAtt();
                                                int anu = regToAttend.getAnu();
                                                int na = regToAttend.getNa();
                                                int cna = regToAttend.getCna();

                                                reg++;
                                                totalReg++;
                                                count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    com++;att++;
                                                    totalCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++;na++; cna++;
                                                    totalCom++;com++; totalCNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu,na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    notcom++;na++;
                                                    totalNotCom++;totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    notcom++;att++;
                                                    totalNotCom++;totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    anu++;att++;
                                                    totalANU++; totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                } else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    na++; totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, com, notcom, att, anu, na, cna));
                                                }
                                            } else {
                                                int reg = 1;

                                                if ((note.getStatus().equals("Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 1, 0 ,0, 0));
                                                }
                                                else if ((note.getStatus().equals("Coming") && (note.getAttended().equals("No")))) {
                                                    totalNA++; totalCNA++;
                                                    totalCom++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 1, 0, 0, 0,1, 1));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("No"))) {
                                                    totalNotCom++;
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 0, 0,1,0));
                                                }
                                                else if ((note.getStatus().equals("Not Coming")) && (note.getAttended().equals("Yes"))) {
                                                    totalNotCom++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 1, 1, 0,0,0));
                                                }

                                                if ((note.getAttended().equals("Yes")) && (note.getStatus().equals("unknown"))) {
                                                    totalANU++;
                                                    totalAtt++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 1, 1,0,0));
                                                }
                                                else if ((note.getAttended().equals("No")) && (note.getStatus().equals("unknown"))) {
                                                    totalNA++;
                                                    count.put(note.getFg(), new RegToAttend(reg, 0, 0, 0, 0,1,0));
                                                }
                                            }
                                        }

                                        final RegToAttendAdapter adapter = new RegToAttendAdapter(count);
                                        mListView.setAdapter((ListAdapter) adapter);
                                        DecimalFormat df = new DecimalFormat("##.#");
                                        df.setRoundingMode(RoundingMode.DOWN);

                                        Double p1 = ((double) totalReg / total) * 100;
                                        Double p2 = ((double) totalCom / total) * 100;
                                        Double p3 = ((double) totalNotCom / total) * 100;
                                        Double p4 = ((double) totalAtt / total) * 100;
                                        Double p5 = Double.parseDouble(df.format(p1));
                                        Double p6 = Double.parseDouble(df.format(p2));
                                        Double p7 = Double.parseDouble(df.format(p3));
                                        Double p8 = Double.parseDouble(df.format(p4));

                                        mTextView7.setText(String.valueOf(total));
                                        mTextView8.setText(String.valueOf(totalCom));
                                        mTextView9.setText(String.valueOf(totalNotCom));
                                        mTextView10.setText(String.valueOf(totalAtt));
                                        mTextView12.setText("100");
                                        mTextView13.setText(p6.toString());
                                        mTextView14.setText(p7.toString());
                                        mTextView15.setText(p8.toString());

                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String fg = adapter.getItem(i).getKey();
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putString("FG", fg);
                                                bundle.putInt("REG",adapter.getItem(i).getValue().getReg());
                                                bundle.putInt("COM",adapter.getItem(i).getValue().getCom());
                                                bundle.putInt("NOTCOM",adapter.getItem(i).getValue().getNotcom());
                                                bundle.putInt("ATT",adapter.getItem(i).getValue().getAtt());
                                                bundle.putInt("ANU",adapter.getItem(i).getValue().getAnu());
                                                bundle.putInt("NA",adapter.getItem(i).getValue().getNa());
                                                bundle.putInt("CNA",adapter.getItem(i).getValue().getCna());
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        totalNextReg = total;
                                        totalNextCom = totalCom;
                                        totalNextNotCom = totalNotCom;
                                        totalNextAtt = totalAtt;

                                        totalButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RegToAttendActivity.this, RegToAttendSecond.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putLong("Date1", date1);
                                                bundle.putLong("Date2", date2);
                                                bundle.putInt("REG",totalNextReg);
                                                bundle.putInt("COM",totalNextCom);
                                                bundle.putInt("NOTCOM",totalNextNotCom);
                                                bundle.putInt("ATT",totalNextAtt);
                                                bundle.putInt("ANU",totalANU);
                                                bundle.putInt("NA",totalNA);
                                                bundle.putInt("CNA",totalCNA);
                                                bundle.putString("SpinPrograms", spinnerPrograms);
                                                bundle.putString("SpinCategories", spinnerCategories);
                                                bundle.putString("SpinSessions", spinnerSessions);
                                                bundle.putString("Collection", collection);
                                                bundle.putString("ClickedFirst","ALL");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });

                                        count.clear();
//                        mListView.setAdapter(null);
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
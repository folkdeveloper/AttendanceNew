package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.TreeMap;

import javax.annotation.Nullable;

public class RegVsAttendedActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys1, fgboys2;
    private String collection1 = "AttendanceDemo";
    private String collection2 = "RegistrationDemo";
    private static final String TAG = "MainActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    TreeMap<String,Integer> count = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_vs_attended);
        db = FirebaseFirestore.getInstance();
        fgboys1 = db.collection(collection1);
        fgboys2 = db.collection(collection2);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);

        fgboys1
                .whereGreaterThanOrEqualTo("edate",date1)
                .whereLessThan("edate",date2)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);

                            if (count.containsKey(note.getSession())) {
                                int num = count.get(note.getSession());
                                num++;
                                count.put(note.getSession(),num);
                            } else {
                                count.put(note.getSession(),1);
                            }
                        }
                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                        mListView.setAdapter((ListAdapter) adapter);

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String session = adapter.getItem(i).getKey();
                                Intent intent = new Intent(RegVsAttendedActivity.this, RegVsAttendedSecond.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("Date1",date1);
                                bundle.putLong("Date2",date2);
                                bundle.putString("Session", session);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        });
                    }
                });

//        fgboys1
//                .whereGreaterThanOrEqualTo("edate",date1)
//                .whereLessThan("edate",date2)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            return;
//                        }
//
//                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
//                            Note note = documentSnapshot.toObject(Note.class);
//
//                            if (count.containsKey(note.getFg())) {
//                                int att = count.get(note.getFg()).getAtt();
//                                int reg = count.get(note.getFg()).getReg();
//                                att++;
//                                count.put(note.getFg(),new RegVsAttended(reg,att));
//                            } else {
//                                count.put(note.getFg(),new RegVsAttended(0,1));
//                            }
//                        }
//                    }
//                });
//
//        fgboys2
//                .whereGreaterThanOrEqualTo("edate",date1)
//                .whereLessThan("edate",date2)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            return;
//                        }
//
//                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
//                            Note note = documentSnapshot.toObject(Note.class);
//
//                            if (count.containsKey(note.getFg())) {
//                                int reg = count.get(note.getFg()).getReg();
//                                int att = count.get(note.getFg()).getAtt();
//                                reg++;
//                                count.put(note.getFg(),new RegVsAttended(reg,att));
//                            } else {
//                                count.put(note.getFg(),new RegVsAttended(1,0));
//                            }
//                        }
//
//                        final RegVsAttendedAdapter adapter = new RegVsAttendedAdapter(count);
//                        mListView.setAdapter((ListAdapter) adapter);
//                    }
//                });
    }
}
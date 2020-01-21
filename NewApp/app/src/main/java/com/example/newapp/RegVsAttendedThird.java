package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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

public class RegVsAttendedThird extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys1, fgboys2;
    private String collection1 = "AttendanceDemo";
    private String collection2 = "RegistrationDemo";
    private static final String TAG = "MainActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private String session = "";
    private String date = "";
    TreeMap<String,RegVsAttended> count = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_vs_attended_third);
        db = FirebaseFirestore.getInstance();
        fgboys1 = db.collection(collection1);
        fgboys2 = db.collection(collection2);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        session = getIntent().getStringExtra("Session");
        date = getIntent().getStringExtra("Date");

        fgboys2
                .whereGreaterThanOrEqualTo("edate",date1)
                .whereLessThan("edate",date2)
                .whereEqualTo("session",session)
                .whereEqualTo("zzdate",date)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);

                            if (count.containsKey(note.getFg())) {
                                int att = count.get(note.getFg()).getAtt();
                                int reg = Integer.parseInt(count.get(note.getFg()).getReg());
                                reg++;
                                count.put(note.getFg(),new RegVsAttended(String.valueOf(reg),att));
                            } else {
                                count.put(note.getFg(), new RegVsAttended("1",0));
                            }
                        }
                    }
                });

        fgboys1
                .whereGreaterThanOrEqualTo("edate",date1)
                .whereLessThan("edate",date2)
                .whereEqualTo("session",session)
                .whereEqualTo("zzdate",date)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                             return;
                        }

                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);

                            if (count.containsKey(note.getFg())) {
                                int att = count.get(note.getFg()).getAtt();
                                String reg = count.get(note.getFg()).getReg();
                                att++;
                                count.put(note.getFg(),new RegVsAttended(reg,att));
                            } else {
                                count.put(note.getFg(), new RegVsAttended("NA",1));
                            }
                        }

                        final RegVsAttendedAdapter adapter = new RegVsAttendedAdapter(count);
                        mListView.setAdapter((ListAdapter) adapter);

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String fg = adapter.getItem(i).getKey();
                                Intent intent = new Intent(RegVsAttendedThird.this, RegVsAttendedDetails.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("Date1",date1);
                                bundle.putLong("Date2",date2);
                                bundle.putString("Session", session);
                                bundle.putString("Date",date);
                                bundle.putString("FG",fg);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }
}
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

public class RegVsAttendedSecond extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys1, fgboys2;
    private String collection1 = "AttendanceDemo";
    private String collection2 = "RegistrationDemo";
    private static final String TAG = "MainActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private String session = "";
    TreeMap<String,Integer> count = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_vs_attended_second);
        db = FirebaseFirestore.getInstance();
        fgboys1 = db.collection(collection1);
        fgboys2 = db.collection(collection2);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        session = getIntent().getStringExtra("Session");

        fgboys1
                .whereGreaterThanOrEqualTo("edate",date1)
                .whereLessThan("edate",date2)
                .whereEqualTo("session",session)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null){
                            return;
                        }

                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);

                            if (count.containsKey(note.getZzdate())) {
                                continue;
                            } else {
                                count.put(note.getZzdate(),1);
                            }
                        }

                        final AmountsNumberAdapter adapter = new AmountsNumberAdapter(count);
                        mListView.setAdapter((ListAdapter) adapter);

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String date = adapter.getItem(i).getKey();
                                Intent intent = new Intent(RegVsAttendedSecond.this, RegVsAttendedThird.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("Date1",date1);
                                bundle.putLong("Date2",date2);
                                bundle.putString("Session", session);
                                bundle.putString("Date",date);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }
}
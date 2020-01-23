package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class RegToAttendSecond extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "OccupationActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private int reg = 0, com = 0, notcom = 0, att = 0, nu = 0, na = 0, anu = 0, cna = 0;
    public static String fg = "";
    public static String spinPrograms = "";
    public static String spinCategories = "";
    public static String spinSessions = "";
    public static String clickedFirst = "";
    public static String clicked = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_to_attend_second);
        mListView = findViewById(R.id.list_item);
        mTextView = findViewById(R.id.textView2);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        reg = getIntent().getIntExtra("REG",0);
        com = getIntent().getIntExtra("COM",0);
        notcom = getIntent().getIntExtra("NOTCOM",0);
        att = getIntent().getIntExtra("ATT",0);
        anu = getIntent().getIntExtra("ANU",0);
        na = getIntent().getIntExtra("NA",0);
        cna = getIntent().getIntExtra("CNA",0);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        fg = getIntent().getStringExtra("FG");
        spinPrograms= getIntent().getStringExtra("SpinPrograms");
        spinCategories= getIntent().getStringExtra("SpinCategories");
        spinSessions= getIntent().getStringExtra("SpinSessions");
        clickedFirst = getIntent().getStringExtra("ClickedFirst");
        clicked = getIntent().getStringExtra("Clicked");

        nu = reg - (com + notcom);
//        na = com - att;

        TreeMap<String,Integer> count = new TreeMap<>();

        count.put("Registered",reg);
        count.put("Coming",com);
        count.put("Not Coming",notcom);
        count.put("Attended",att);
        count.put("Not Updated",nu);
        count.put("Not Attended",na);
        count.put("Attended but Not Updated",anu);
        count.put("Coming but not Attended",cna);

        final RegToAttendSecondAdapter adapter = new RegToAttendSecondAdapter(count);
        mListView.setAdapter((ListAdapter) adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String clicked = adapter.getItem(i).getKey();
                Intent intent = new Intent(RegToAttendSecond.this, RegToAttendDetails.class);
                Bundle bundle = new Bundle();
                bundle.putLong("Date1", date1);
                bundle.putLong("Date2", date2);
                bundle.putString("FG", fg);
                bundle.putString("SpinPrograms", spinPrograms);
                bundle.putString("SpinCategories", spinCategories);
                bundle.putString("SpinSessions", spinSessions);
                bundle.putString("Collection", collection);
                bundle.putString("ClickedFirst",clickedFirst);
                bundle.putString("Clicked",clicked);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
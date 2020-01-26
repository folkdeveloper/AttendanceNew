package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

import java.util.ArrayList;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class TLViewFG extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference fgboys;
    private String collection = "";
    private static final String TAG = "MainActivity";
    ListView mListView;
    private TextView mTextView;
    private long date1 = 0, date2 = 0;
    private String tl = "";
    private String url = "";
    private int total = 0;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public static String spinPrograms = "";
    public static String spinCategories = "";
    public static String spinSessions = "";
    private ArrayList<String> fid;
    ArrayList<String> countName = new ArrayList<>();
    TreeMap<String, ArrayList<String>> count = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tlviewfg);
        collection = getIntent().getStringExtra("Collection");
        db = FirebaseFirestore.getInstance();
        fgboys = db.collection(collection);
        mTextView = findViewById(R.id.textView2);
        mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mListView = findViewById(R.id.list_item);
        date1 = getIntent().getLongExtra("Date1",date1);
        date2 = getIntent().getLongExtra("Date2",date2);
        tl = getIntent().getStringExtra("TL");
        spinPrograms= getIntent().getStringExtra("SpinPrograms");
        spinCategories= getIntent().getStringExtra("SpinCategories");
        spinSessions= getIntent().getStringExtra("SpinSessions");
        fid = (ArrayList<String>) getIntent().getSerializableExtra("FID");
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
        if (tl.equals("ALL")) {
            total=0;count.clear();countName.clear();
            fgboys
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

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(tl));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
        else {
            total=0;count.clear();countName.clear();
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("ztl",tl)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListProgramsAndCategories() {
        if (tl.equals("ALL")) {
            total=0;count.clear();countName.clear();
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("session",spinSessions)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }


                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
        else {
            total=0;count.clear();countName.clear();
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("ztl",tl)
                    .whereEqualTo("session",spinSessions)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListProgramsAndSessions() {
        if (tl.equals("ALL")) {
            total=0;count.clear();countName.clear();
            fgboys
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

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
        else {
            total=0;count.clear();countName.clear();
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("ztl",tl)
                    .whereEqualTo("category",spinCategories)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListCategoriesAndSessions() {
        if (tl.equals("ALL")) {
            total=0;count.clear();countName.clear();
            fgboys
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

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
        else {
            total=0;count.clear();countName.clear();
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("ztl",tl)
                    .whereEqualTo("program",spinPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListPrograms() {
        if (tl.equals("ALL")) {
            total=0;count.clear();countName.clear();
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("session",spinSessions)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
        else {
            total=0;count.clear();countName.clear();
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("ztl",tl)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("session",spinSessions)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListCategories() {
        if (tl.equals("ALL")) {
            total=0;count.clear();countName.clear();
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("session",spinSessions)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
        else {
            total=0;count.clear();countName.clear();
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("ztl",tl)
                    .whereEqualTo("program",spinPrograms)
                    .whereEqualTo("session",spinSessions)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateListSessions() {
        if (tl.equals("ALL")) {
            total=0;count.clear();countName.clear();
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("program",spinPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
        else {
            total=0;count.clear();countName.clear();
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("ztl",tl)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("program",spinPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }

    public void populateList() {
        if (tl.equals("ALL")) {
            total=0;count.clear();countName.clear();
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("session",spinSessions)
                    .whereEqualTo("program",spinPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
        else {
            total=0;count.clear();countName.clear();
            fgboys
                    .whereGreaterThanOrEqualTo("edate", date1)
                    .whereLessThanOrEqualTo("edate", date2)
                    .whereEqualTo("ztl",tl)
                    .whereEqualTo("category",spinCategories)
                    .whereEqualTo("session",spinSessions)
                    .whereEqualTo("program",spinPrograms)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);

                                if (note.getName() == null) {
                                    continue;
                                }

                                if (note.getUrl() == null) {
                                    note.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr");
                                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
                                }

                                if (countName.contains(note.getFid())) {
                                    continue;
                                }

                                countName.add(note.getFid());
//                                count.put("ALL",total);

                                if (count.containsKey(note.getFg())) {
                                    ArrayList<String> list = count.get(note.getFg());
//                                                    ArrayList<String> listAll = count.get("ALL");
                                    list.add(note.getFid());
//                                                    listAll.add(note.getFid());
                                    count.put(note.getFg(), list);
//                                                    count.put("ALL", listAll);
                                } else {
                                    ArrayList<String> newList = new ArrayList<>();
                                    newList.add(note.getFid());
                                    count.put(note.getFg(), newList);
//                                                    count.put("ALL", newList);
//                                }
                                }
                            }

                            final MatchRegistrationsAdapter adapter = new MatchRegistrationsAdapter(count);
                            mListView.setAdapter((ListAdapter) adapter);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fg = adapter.getItem(i).getKey();
                                    Intent intent = new Intent(TLViewFG.this, TLViewDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("Date1", date1);
                                    bundle.putLong("Date2", date2);
                                    bundle.putString("TL", tl);
                                    bundle.putString("FG", fg);
                                    bundle.putString("SpinPrograms", spinPrograms);
                                    bundle.putString("SpinCategories", spinCategories);
                                    bundle.putString("SpinSessions", spinSessions);
                                    bundle.putString("Collection", collection);
                                    intent.putExtras(bundle);
                                    intent.putExtra("FID",count.get(fg));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }
    }
}

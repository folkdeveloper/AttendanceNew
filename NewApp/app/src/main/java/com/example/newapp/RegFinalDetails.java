package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegFinalDetails extends AppCompatActivity {

    private String name = "";
    private String phone = "";
    private String fg = "";
    private String program = "";
    private String time = "";
    private String date = "";
    private String japa = "";
    private String reading = "";
    private String area = "";
    private String session = "";
    private String url = "";
    private String source = "";
    private String college = "";
    private String occupation = "";
    private String branch = "";
    private String zone = "";
    private String organisation = "";
    private String tl = "";
    private String level = "";
    private String category = "";
    private String res_interest = "";
    private String fg_call;
    private String leave_agreed;
    private String msg_confirm;
    private String status;
    private String comment;
    private String zzdate, zmob;
    private long edate;
    private long probability;
    private long origin = 0;
    private long last_updated;
    private ImageButton callButton;
    private Button buttonfg_CallYes, buttonfg_CallNo;
    private Button buttonLeave_Yes, buttonLeave_No;
    private Button buttonmsg_Yes, buttonmsg_No;
    private Button buttonstatus_com, buttonstatus_notcom;
    private Button update;

    private TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5, mTextView6,
            mTextView7, mTextView8, mTextView9, mTextView10, mTextView11, mTextView12, mTextView13,
            mTextView14, mTextView15, mTextView16, mTextView17, mTextView18, mTextView19, mTextView20,
            mTextView21, mTextView22, mTextView33, mTextView34;

    private EditText mTextView36;

    private ImageView mImageView;

    private JSONObject postData = new JSONObject();
    private JSONObject postDataFinal = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_final_details);
        name = getIntent().getStringExtra("Name");
        phone = getIntent().getStringExtra("Phone");
        fg = getIntent().getStringExtra("FG");
        program = getIntent().getStringExtra("Program");
        time = getIntent().getStringExtra("Time");
        date = getIntent().getStringExtra("Date");
        japa = getIntent().getStringExtra("Japa");
        reading = getIntent().getStringExtra("Reading");
        area = getIntent().getStringExtra("Area");
        session = getIntent().getStringExtra("Session");
        url = getIntent().getStringExtra("URL");
        source = getIntent().getStringExtra("Source");
        college = getIntent().getStringExtra("College");
        occupation = getIntent().getStringExtra("Occupation");
        branch = getIntent().getStringExtra("Branch");
        zone = getIntent().getStringExtra("Zone");
        organisation = getIntent().getStringExtra("Organisation");
        tl = getIntent().getStringExtra("TL");
        level = getIntent().getStringExtra("Level");
        category = getIntent().getStringExtra("Category");
        res_interest = getIntent().getStringExtra("Res");
        origin = getIntent().getLongExtra("Origin",origin);
        fg_call = getIntent().getStringExtra("fg_call");
        leave_agreed = getIntent().getStringExtra("leave_agreed");
        msg_confirm = getIntent().getStringExtra("msg_confirm");
        status = getIntent().getStringExtra("status");
        comment = getIntent().getStringExtra("Comment");
        probability = getIntent().getLongExtra("Probability",probability);
        edate = getIntent().getLongExtra("Edate",edate);
        last_updated = getIntent().getLongExtra("LU",last_updated);
        zzdate = getIntent().getStringExtra("Date");
        zmob = getIntent().getStringExtra("Mobile");

        mTextView1 = findViewById(R.id.textView1);
        mTextView2 = findViewById(R.id.textView2);
        mTextView3 = findViewById(R.id.textView3);
        mTextView4 = findViewById(R.id.textView4);
        mTextView5 = findViewById(R.id.textView5);
        mTextView6 = findViewById(R.id.textView6);
        mTextView7 = findViewById(R.id.textView7);
        mTextView8 = findViewById(R.id.textView8);
        mTextView9 = findViewById(R.id.textView9);
        mTextView10 = findViewById(R.id.textView10);
        mTextView11 = findViewById(R.id.textView11);
        mTextView12 = findViewById(R.id.textView12);
        mTextView13 = findViewById(R.id.textView13);
        mTextView14 = findViewById(R.id.textView14);
        mTextView15 = findViewById(R.id.textView15);
        mTextView16 = findViewById(R.id.textView16);
        mTextView17 = findViewById(R.id.textView17);
        mTextView18 = findViewById(R.id.textView18);
        mTextView19 = findViewById(R.id.textView19);
        mTextView20 = findViewById(R.id.textView20);
        mTextView21 = findViewById(R.id.textView21);
        mTextView22 = findViewById(R.id.textView22);
        mTextView33 = findViewById(R.id.textView33);
        mTextView34 = findViewById(R.id.textView34);
        mTextView36 = findViewById(R.id.textView36);

        callButton = findViewById(R.id.call);
        mImageView = findViewById(R.id.imageView);
        buttonfg_CallYes = findViewById(R.id.button1);
        buttonfg_CallNo = findViewById(R.id.button2);
        buttonLeave_Yes = findViewById(R.id.button3);
        buttonLeave_No = findViewById(R.id.button4);
        buttonmsg_Yes = findViewById(R.id.button5);
        buttonmsg_No = findViewById(R.id.button6);
        buttonstatus_com = findViewById(R.id.button7);
        buttonstatus_notcom = findViewById(R.id.button8);
        update = findViewById(R.id.button15);

        mTextView1.setText("Name: " + name);
        mTextView2.setText("Phone: " + phone);
        mTextView3.setText("FG: " + fg);
        mTextView4.setText("Program: " + program);
        mTextView5.setText("Time: " + time);
        mTextView6.setText("Date: " + date);
        mTextView7.setText("Japa: " + japa);
        mTextView8.setText("Reading: " + reading);
        mTextView9.setText("Area: " + area);
        mTextView10.setText("Session: " + session);
        mTextView11.setText("Source: " + source);
        mTextView12.setText("Branch: " + branch);
        mTextView13.setText("College: " + college);
        mTextView14.setText("Occupation: " + occupation);
        mTextView15.setText("Zone: " + zone);
        mTextView16.setText("TL: " + tl);
        mTextView17.setText("Level: " + level);
        mTextView18.setText("Organisation: " + organisation);
        mTextView19.setText("Category: " + category);
        mTextView20.setText("Residency Interest: " + res_interest);
        if (res_interest.equals("Yes")) {
            mTextView20.setBackgroundResource(R.color.colorPrimary);
        }

        mTextView33.setText("Probability: " + String.valueOf(probability));

        String originValue = String.valueOf(origin);
        String edateValue = String.valueOf(edate);
        String luValue = String.valueOf(last_updated);

        long originFinal = Long.valueOf(originValue) * 1000;
        long edateFinal = Long.valueOf(edateValue) * 1000;
        long luFinal = Long.valueOf(edateValue) * 1000;

        Date date1 = new Date(Long.valueOf(originFinal));
        Date date2 = new Date(Long.valueOf(edateFinal));
        Date date3 = new Date(Long.valueOf(luFinal));
        DateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
//        format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        String formatted1 = format.format(date1);
        String formatted2 = format.format(date2);
        String formatted3 = format.format(date3);

        mTextView21.setText("Origin: " + formatted1);
        mTextView34.setText(formatted2);
        mTextView22.setText(formatted3);

        mTextView36.setText(comment);

        if (fg_call.equals("Yes")) {
            buttonfg_CallYes.setBackgroundResource(R.color.colorPrimary);
        } else {
            buttonfg_CallNo.setBackgroundResource(R.color.colorPrimary);
        }

        if (leave_agreed.equals("Yes")) {
            buttonLeave_Yes.setBackgroundResource(R.color.colorPrimary);
        } else {
            buttonLeave_No.setBackgroundResource(R.color.colorPrimary);
        }

        if (msg_confirm.equals("Yes")) {
            buttonmsg_Yes.setBackgroundResource(R.color.colorPrimary);
        } else {
            buttonmsg_No.setBackgroundResource(R.color.colorPrimary);
        }

        if (status.equals("Coming")) {
            buttonstatus_com.setBackgroundResource(R.color.colorPrimary);
        } else if (status.equals("Not Coming")){
            buttonstatus_notcom.setBackgroundResource(R.color.colorPrimary);
        } else {

        }

        try {
            postDataFinal.put("zzdate",zzdate);
            postDataFinal.put("session",session);
            postDataFinal.put("zmob",zmob);

        }catch(JSONException e) {
            e.printStackTrace();
        }

        buttonfg_CallYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonfg_CallYes.setBackgroundResource(R.color.colorPrimary);
                buttonfg_CallNo.setBackgroundResource(android.R.drawable.btn_default);
                try {
                    postData.put("fg_call", "Yes");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonfg_CallNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonfg_CallNo.setBackgroundResource(R.color.colorPrimary);
                buttonfg_CallYes.setBackgroundResource(android.R.drawable.btn_default);
                try {
                    postData.put("fg_call", "No");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonLeave_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLeave_Yes.setBackgroundResource(R.color.colorPrimary);
                buttonLeave_No.setBackgroundResource(android.R.drawable.btn_default);
                try {
                    postData.put("leave_agreed", "Yes");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonLeave_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLeave_No.setBackgroundResource(R.color.colorPrimary);
                buttonLeave_Yes.setBackgroundResource(android.R.drawable.btn_default);
                try {
                    postData.put("leave_agreed", "No");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonmsg_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonmsg_Yes.setBackgroundResource(R.color.colorPrimary);
                buttonmsg_No.setBackgroundResource(android.R.drawable.btn_default);
                try {
                    postData.put("msg_confirm", "Yes");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonmsg_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonmsg_No.setBackgroundResource(R.color.colorPrimary);
                buttonmsg_Yes.setBackgroundResource(android.R.drawable.btn_default);
                try {
                    postData.put("msg_confirm", "no");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonstatus_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonstatus_com.setBackgroundResource(R.color.colorPrimary);
                buttonstatus_notcom.setBackgroundResource(android.R.drawable.btn_default);
                try {
                    postData.put("status", "Yes");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonstatus_notcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonstatus_notcom.setBackgroundResource(R.color.colorPrimary);
                buttonstatus_com.setBackgroundResource(android.R.drawable.btn_default);
                try {
                    postData.put("status", "No");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postData.put("comment", mTextView36.getText().toString());
                    postDataFinal.put("comment", mTextView36.getText().toString());
                    new SendDeviceDetails().execute("https://us-central1-folk-demo.cloudfunctions.net/fgInputToRegistrations", postDataFinal.toString());
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

//        mTextView36.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                try {
//                    postData.put("comment", mTextView36.getText().toString());
//                }catch(JSONException e) {
//                    e.printStackTrace();
//                }
//                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        if(url.equals("")) {
            url = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQkqmIlxctkcE0ACfSg3aZUNRG8cAj1cYi2TvyT72FH55BTTMEr";
        }

        Glide.with(this).load(url).crossFade().into(mImageView);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegFinalDetails.this, DialogPhoto.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                bundle.putString("URL", url);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+phone.trim()));
                startActivity(callIntent);
            }
        });
    }
}
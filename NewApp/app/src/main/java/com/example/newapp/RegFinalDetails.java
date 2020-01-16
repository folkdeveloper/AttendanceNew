package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
    private long edate;
    private long probability;
    private long origin = 0;
    private ImageButton callButton;
    private Button buttonfg_CallYes, buttonfg_CallNo;
    private Button buttonLeave_Yes, buttonLeave_No;
    private Button buttonmsg_Yes, buttonmsg_No;
    private Button buttonstatus_com, buttonstatus_notcom;

    private TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5, mTextView6,
            mTextView7, mTextView8, mTextView9, mTextView10, mTextView11, mTextView12, mTextView13,
            mTextView14, mTextView15, mTextView16, mTextView17, mTextView18, mTextView19, mTextView20,
            mTextView21, mTextView33, mTextView34;

    private EditText mTextView36;

    private ImageView mImageView;

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
        long originFinal = Long.valueOf(originValue) * 1000;
        long edateFinal = Long.valueOf(edateValue) * 1000;
        Date date1 = new Date(Long.valueOf(originFinal));
        Date date2 = new Date(Long.valueOf(edateFinal));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        String formatted1 = format.format(date1);
        String formatted2 = format.format(date2);
        mTextView21.setText(formatted1);
        mTextView34.setText(formatted2);

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

        if (status.equals("Yes")) {
            buttonstatus_com.setBackgroundResource(R.color.colorPrimary);
        } else {
            buttonstatus_notcom.setBackgroundResource(R.color.colorPrimary);
        }

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
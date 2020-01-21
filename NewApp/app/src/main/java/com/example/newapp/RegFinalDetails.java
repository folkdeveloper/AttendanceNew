package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
    private String zzdate, zmob, attended;
    private String collection = "", fid = "";
    private long edate;
    private long probability;
    private long origin = 0;
    private long last_updated;
    private ImageButton callButton, historyButton;
    private Button buttonfg_CallYes, buttonfg_CallNo;
    private Button buttonLeave_Yes, buttonLeave_No;
    private Button buttonmsg_Yes, buttonmsg_No;
    private Button buttonstatus_com, buttonstatus_notcom;
    private Button update;

    private RequestQueue requestQueue;

    private TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5, mTextView6,
            mTextView7, mTextView8, mTextView9, mTextView10, mTextView11, mTextView12, mTextView13,
            mTextView14, mTextView15, mTextView16, mTextView17, mTextView18, mTextView19, mTextView20,
            mTextView21, mTextView22, mTextView33, mTextView34, mTextView39, mTextView40;

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
        collection = getIntent().getStringExtra("Collection");
        fid = getIntent().getStringExtra("FID");
        attended = getIntent().getStringExtra("Attended");

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
        mTextView39 = findViewById(R.id.textView39);
        mTextView40 = findViewById(R.id.textView40);

        callButton = findViewById(R.id.call);
        historyButton = findViewById(R.id.history);
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
        mTextView39.setText("FID: " + fid);
        mTextView40.setText("Attended: " + attended);
        if (res_interest.equals("Yes")) {
            mTextView20.setBackgroundResource(R.color.colorPrimary);
        }

        mTextView33.setText("Probability: " + String.valueOf(probability) + "%");

        String originValue = String.valueOf(origin);
        String edateValue = String.valueOf(edate);
        String luValue = String.valueOf(last_updated);

        long originFinal = Long.valueOf(originValue) * 1000;
        long edateFinal = Long.valueOf(edateValue) * 1000;
        long luFinal = Long.valueOf(luValue) * 1000;

        Date date1 = new Date(Long.valueOf(originFinal));
        Date date2 = new Date(Long.valueOf(edateFinal));
        Date date3 = new Date(Long.valueOf(luFinal));
        DateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
//        format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        String formatted1 = format.format(date1);
        String formatted2 = format.format(date2);
        String formatted3 = format.format(date3);

        mTextView21.setText("Origin: " + formatted1);
        mTextView34.setText(formatted2);
        mTextView22.setText(formatted3);

        mTextView36.setText(comment);

        if (fg_call.equals("Yes")) {
            buttonfg_CallYes.setBackgroundResource(R.drawable.button_selected);
        } else {
            buttonfg_CallNo.setBackgroundResource(R.drawable.button_selected);
        }

        if (leave_agreed.equals("Yes")) {
            buttonLeave_Yes.setBackgroundResource(R.drawable.button_selected);
        } else {
            buttonLeave_No.setBackgroundResource(R.drawable.button_selected);
        }

        if (msg_confirm.equals("Yes")) {
            buttonmsg_Yes.setBackgroundResource(R.drawable.button_selected);
        } else {
            buttonmsg_No.setBackgroundResource(R.drawable.button_selected);
        }

        if (status.equals("Coming")) {
            buttonstatus_com.setBackgroundResource(R.drawable.button_selected);
        } else if (status.equals("Not Coming")){
            buttonstatus_notcom.setBackgroundResource(R.drawable.button_selected);
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
                buttonfg_CallYes.setBackgroundResource(R.drawable.button_selected);
                buttonfg_CallNo.setBackgroundResource(R.drawable.button_notselected);
                try {
                    postData.put("fg_call", "Yes");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                fg_call = "Yes";
//                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonfg_CallNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonfg_CallNo.setBackgroundResource(R.drawable.button_selected);
                buttonfg_CallYes.setBackgroundResource(R.drawable.button_notselected);
                try {
                    postData.put("fg_call", "No");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                fg_call = "No";
//                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonLeave_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLeave_Yes.setBackgroundResource(R.drawable.button_selected);
                buttonLeave_No.setBackgroundResource(R.drawable.button_notselected);
                try {
                    postData.put("leave_agreed", "Yes");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                leave_agreed = "Yes";
//                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonLeave_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLeave_No.setBackgroundResource(R.drawable.button_selected);
                buttonLeave_Yes.setBackgroundResource(R.drawable.button_notselected);
                try {
                    postData.put("leave_agreed", "No");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                leave_agreed = "No";
//                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonmsg_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonmsg_Yes.setBackgroundResource(R.drawable.button_selected);
                buttonmsg_No.setBackgroundResource(R.drawable.button_notselected);
                try {
                    postData.put("msg_confirm", "Yes");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                msg_confirm = "Yes";
//                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonmsg_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonmsg_No.setBackgroundResource(R.drawable.button_selected);
                buttonmsg_Yes.setBackgroundResource(R.drawable.button_notselected);
                try {
                    postData.put("msg_confirm", "no");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                msg_confirm = "No";
//                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonstatus_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonstatus_com.setBackgroundResource(R.drawable.button_selected);
                buttonstatus_notcom.setBackgroundResource(R.drawable.button_notselected);
                try {
                    postData.put("status", "Yes");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                status = "Coming";
//                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonstatus_notcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonstatus_notcom.setBackgroundResource(R.drawable.button_selected);
                buttonstatus_com.setBackgroundResource(R.drawable.button_notselected);
                try {
                    postData.put("status", "No");
                    postDataFinal.put("fg_call", "Yes");
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                status = "Not Coming";
//                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

                mTextView36.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    postData.put("comment", mTextView36.getText().toString());
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                comment = mTextView36.getText().toString();
//                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postData.put("comment", mTextView36.getText().toString());
                    postDataFinal.put("comment", mTextView36.getText().toString());
//                    new SendDeviceDetails().execute("https://us-central1-folk-demo.cloudfunctions.net/fgInputToRegistrations", postDataFinal.toString());
                    String data = "{"+
                            "\"zzdate\"" + ":" + "\"" + date + "\","+
                            "\"session\"" + ":" + "\"" + session + "\","+
                            "\"zmob\"" + ":" + "\"" + phone + "\","+
                            "\"leave_agreed\"" + ":" + "\"" + leave_agreed + "\","+
                            "\"fg_call\"" + ":" + "\"" + fg_call + "\","+
                            "\"msg_confirm\"" + ":" + "\"" + msg_confirm + "\","+
                            "\"comment\"" + ":" + "\"" + comment + "\","+
                            "\"status\"" + ":" + "\"" + status + "\""+
                            "}";
                    Submit(data);
                }catch(JSONException e) {
                    e.printStackTrace();

                }
//                Toast.makeText(RegFinalDetails.this, "" + postData.toString(), Toast.LENGTH_SHORT).show();
            }
        });

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

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(RegFinalDetails.this,HistoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Collection",collection);
                bundle.putString("FID",fid);
                historyIntent.putExtras(bundle);
                startActivity(historyIntent);
            }
        });
    }

    private void Submit(String data)
    {
        final String savedata= data;
        String URL="https://us-central1-folk-demo.cloudfunctions.net/fgInputToRegistrations";

        Toast.makeText(this, "" + data, Toast.LENGTH_SHORT).show();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres=new JSONObject(response);
                    Toast.makeText(getApplicationContext(),objres.toString(),Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();

                }
                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                //Log.v("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    //Log.v("Unsupported Encoding while trying to get the bytes", data);
                    return null;
                }
            }

        };
        requestQueue.add(stringRequest);
    }
}
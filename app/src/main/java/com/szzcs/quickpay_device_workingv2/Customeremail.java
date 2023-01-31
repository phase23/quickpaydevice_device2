package com.szzcs.quickpay_device_workingv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Customeremail extends AppCompatActivity {
    String postaction;
    RadioButton isgmail;
    RadioButton ishotmail;
    RadioButton isyahoo;
    RadioButton isother;

    EditText getemailpart;
    String thisemailpart;
    TextView  thisfullemail;

    String getcarddetails;
    String getcardno;
    String getexpiry;
    TextView cardname;
    TextView  cardno;
    TextView  thisstrorecheck;
    TextView  previousemail;
    Button dochargecard;


    TextView  shownicecharge;
    String fname;
    String lname;
    String chargethis;
    String tip;
    ImageView checkmark;
    EditText samount;
    String scvv;
    String thisamount;
    Handler handler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customeremail);


        final String thismydevice = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        handler2 = new Handler(Looper.getMainLooper());


        getcarddetails = getIntent().getExtras().getString("carddetails");
        scvv = getIntent().getExtras().getString("ccv");

        String[] pieces = getcarddetails.split(Pattern.quote("^"));
        String cardpiece = pieces[0].trim();
        getcardno = cardpiece.substring(1,17);
        String last8Digits = getcardno.substring(getcardno.length() - 8);

        String namepiece = pieces[1].trim();
        String[] nameoutput = namepiece.split(Pattern.quote("/"));
        fname = nameoutput[0].trim();
        lname = nameoutput[1];

        String expiece = pieces[2].trim();
        getexpiry =  expiece.substring(0,4);

        dochargecard = (Button)findViewById(R.id.chargecard);
        isgmail = (RadioButton)findViewById(R.id.gmail);
        ishotmail = (RadioButton)findViewById(R.id.hotmail);
        isyahoo = (RadioButton)findViewById(R.id.yahoo);
        isother = (RadioButton)findViewById(R.id.other);
        getemailpart = (EditText)findViewById(R.id.emailpart);
        thisfullemail = (TextView)findViewById(R.id.fullemail);
        shownicecharge = (TextView)findViewById(R.id.nicercharge);
        thisstrorecheck = (TextView)findViewById(R.id.storecheck);
        previousemail = (TextView)findViewById(R.id.previous_alert);


        checkmark = (ImageView) findViewById(R.id.checks);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        chargethis = preferences.getString("chargetotal", "");
        tip = preferences.getString("incltip", "");

        double createTotal = 0;
        try {
            createTotal = Float.parseFloat(chargethis);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        samount = (EditText)findViewById(R.id.sendtocharge);

        String COUNTRY = "US";
        String LANGUAGE = "en";
        String nicecharge = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(createTotal);
        samount.setText(chargethis);
        shownicecharge.setText(nicecharge);




        dochargecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                thisamount = samount.getText().toString();


                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                } else {
                    connected = false;
                }

                float myNum = 0;
                try {
                    myNum = Float.parseFloat(thisamount);
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }

                if(myNum < 1) {
                    Toast.makeText(getApplicationContext(), "The charge amount cannot be less than $1.00", Toast.LENGTH_SHORT).show();
                    return;

                }


                if(!connected) {
                    Toast.makeText(getApplicationContext(), "Check Internet & Restart App", Toast.LENGTH_LONG).show();
                    Intent nointernet = new Intent(Customeremail.this, Nointernet.class);
                    startActivity(nointernet);

                }else {

                    try {

                        dochargecard.setText("Please wait...");
                        dochargecard.setEnabled(false);

                        String sendemail = thisfullemail.getText().toString();

                        sendemail = sendemail.replaceAll("\\s","");

                        Log.i("[print]", "https://quickpay.ai/api_chargecard.php?deviceid=" + thismydevice + "&ccnumber=" + getcardno + "&cvv=" + scvv + "&charge=" + thisamount + "&expr=" + getexpiry + "&ccname=" + fname + ' ' + lname + "&tip=" + tip + "&getemail="+sendemail) ;
                        doGetRequest("https://quickpay.ai/api_chargecard.php?deviceid=" + thismydevice + "&ccnumber=" + getcardno + "&cvv=" + scvv + "&charge=" + thisamount + "&expr=" + getexpiry + "&ccname=" + fname + ' ' + lname + "&tip=" + tip + "&getemail="+sendemail);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }





            }

        });




        getemailpart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                String emaildomain = thisstrorecheck.getText().toString();
                thisfullemail.setText(getemailpart.getText().toString() + emaildomain);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        isother.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Do something when the radio button is checked
                    thisemailpart = getemailpart.getText().toString();

                    thisfullemail.setText(thisemailpart);
                    thisstrorecheck.setText("");
                    getemailpart.setVisibility(View.VISIBLE);

                } else {
                    // Do something when the radio button is not checked
                }
            }
        });

        isgmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Do something when the radio button is checked
                    thisemailpart = getemailpart.getText().toString();
                    String newemail = thisemailpart + "@gmail.com";
                    thisfullemail.setText(newemail);
                    thisstrorecheck.setText("@gmail.com");
                    getemailpart.setVisibility(View.VISIBLE);

                } else {
                    // Do something when the radio button is not checked
                }
            }
        });

        ishotmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Do something when the radio button is checked
                    thisemailpart = getemailpart.getText().toString();
                    String newemail = thisemailpart + "@hotmail.com";
                    thisstrorecheck.setText("@hotmail.com");
                    thisfullemail.setText(newemail);
                    getemailpart.setVisibility(View.VISIBLE);

                } else {
                    // Do something when the radio button is not checked
                }
            }
        });


        isyahoo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Do something when the radio button is checked
                    thisemailpart = getemailpart.getText().toString();
                    String newemail = thisemailpart + "@yahoo.com";
                    thisstrorecheck.setText("@yahoo.com");
                    thisfullemail.setText(newemail);
                    getemailpart.setVisibility(View.VISIBLE);

                } else {
                    // Do something when the radio button is not checked
                }
            }
        });



        try {

            Log.i("[print]", "https://quickpay.ai/api_emailfetch.php?expr=" + getexpiry + "&lastd=" + last8Digits ) ;
            doEmailCheck("https://quickpay.ai/api_emailfetch.php?expr=" + getexpiry + "&lastd=" + last8Digits );
        } catch (IOException e) {
            e.printStackTrace();
        }





    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        View focusedView = this.getCurrentFocus();

        if (focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        return true;
    }




    void doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // For the example, you can show an error dialog or a toast
                                // on the main UI thread
                                Log.i("[print]","error" );
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        postaction = response.body().string();
                        Log.i("assyn url",postaction);
                        // Do something with the response


                        Log.i("[print]",postaction);
                        postaction = postaction.trim();
                        String[] separated = postaction.split("~");
                        String thisresult = separated[0].trim();
                        Log.i("[print]",thisresult);

                        if(thisresult.equals("nocharge")) {
                            final String thisdescipt = separated[3];


                            handler2.post(new Runnable() {
                                @Override
                                public void run() {
                                    //setstatus.setText(postaction);
                                    // dochargecard.setVisibility(View.INVISIBLE);
                                    samount.setVisibility(View.INVISIBLE);
                                    //scvv.setVisibility(View.INVISIBLE);

                                    //blinkback.setText("Try again");
                                    //cardresulttxt.setVisibility(View.VISIBLE);
                                   // cardresulttxt.setText(thisdescipt);

                                    //dochargecard.setEnabled(true);
                                    //dochargecard.setText("Charge Card");

                                }
                            });

                        }else if(thisresult.equals("charge")) {
                            String thisdescipt = separated[3];

                            Intent intent = new Intent(Customeremail.this, CardSuccess.class);
                            intent.putExtra("transid",thisdescipt);
                            intent.putExtra("puttall",postaction);
                            startActivity(intent);



                        }else {

                        }



                    }
                });
    }







    void doEmailCheck(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // For the example, you can show an error dialog or a toast
                                // on the main UI thread
                                Log.i("[print]","error" );
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        postaction = response.body().string();
                        Log.i("assyn url",postaction);
                        // Do something with the response


                        Log.i("[print]",postaction);
                        postaction = postaction.trim();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(postaction.equals("")){


                                }else {
                                    thisfullemail.setText(postaction);
                                    getemailpart.setVisibility(View.INVISIBLE);
                                    checkmark.setVisibility(View.VISIBLE);
                                    previousemail.setVisibility(View.VISIBLE);

                                }

                            }
                        });


                    }
                });
    }

}
package com.szzcs.quickpay_device_workingv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class Carddetails extends AppCompatActivity {
    String getcarddetails;
    String getcardno;
    String getexpiry;
    TextView cardname;
    TextView  cardno;
    TextView  thisfinal;

    TextView  cardresulttxt;
    Button trybtn;
    Button docontunieemail;
    Button blinkback;
    EditText scvv;
    EditText samount;


    ImageView checkmark;
    String thisamount;
    String thiscvv;
    String postaction;
    Handler handler2;
    String fname;
    String lname;
    String chargethis;
    String tip;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carddetails);

        getcarddetails = getIntent().getExtras().getString("carddetails");
// B4658601305603021^JNO-BAPTISTE       /P W.MR^2206201000000000000000698000000
// B4658596116066014^JNO-BAPTISTE       /P W.MR^2206221000000000000000218000000
// %B4658596116066014^JNO-BAPTISTE       /P W.MR^2206221000000000000000218000000?;4658596116066014=22062212180000000001?
// %B4100750001509794^JNO-BAPTISTE/PHILMON.^2402101926200010000000598000000?;4100750001509794=24021019262059800100?



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






        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        chargethis = preferences.getString("chargetotal", "");
        tip = preferences.getString("incltip", "");

        double createTotal = 0;
        try {
            createTotal = Float.parseFloat(chargethis);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }


        cardname = (TextView)findViewById(R.id.trck1);
        cardno = (TextView)findViewById(R.id.trck2);
        cardresulttxt = (TextView)findViewById(R.id.cardresult);
        thisfinal = (TextView)findViewById(R.id.finalcharge);

        checkmark = (ImageView) findViewById(R.id.checks);











        samount = (EditText)findViewById(R.id.amount);
        scvv = (EditText)findViewById(R.id.cvv);




        String COUNTRY = "US";
        String LANGUAGE = "en";
        String nicecharge = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(createTotal);
        samount.setText(chargethis);
        thisfinal.setText(nicecharge);


        docontunieemail = (Button)findViewById(R.id.continuetoemail);
        blinkback = (Button)findViewById(R.id.linkback);
        trybtn = (Button)findViewById(R.id.tryagainbtn);





        cardname.setText(fname + ' ' + lname );
        cardname.setBackgroundColor(Color.parseColor("#000000"));
        cardname.setTextColor(Color.parseColor("#FFFFFF"));

        Log.i("[print]",  "scan details" + getcardno);
        String newcardno = getcardno.substring(0,8);
        cardno.setText(newcardno + "**** ****");
        cardno.setTextColor(Color.parseColor("#FFFFFF"));
        cardno.setBackgroundColor(Color.parseColor("#000000"));




        trybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Carddetails.this,MainActivity.class);
                startActivity(i);

            }

        });




        blinkback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Carddetails.this, MainActivity.class);

                startActivity(intent);

            }

        });


        docontunieemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                thiscvv = scvv.getText().toString();
                if(thiscvv.length() != 3  ) {
                    Toast.makeText(getApplicationContext(), "The CVV must be three characters", Toast.LENGTH_SHORT).show();
                    return;

                }





                Intent intent = new Intent(Carddetails.this, Customeremail.class);
                intent.putExtra("carddetails",getcarddetails);
                intent.putExtra("ccv",thiscvv);
                startActivity(intent);


            }

        });


         /*

        */






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


}
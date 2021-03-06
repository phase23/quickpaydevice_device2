package com.szzcs.quickpay_device_workingv2.qr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.szzcs.quickpay_device_workingv2.R;
import com.szzcs.quickpay_device_workingv2.base.BaseActivity;
import com.zcs.sdk.DriverManager;
import com.zcs.sdk.HQrsanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class QRTestActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_create, btn_scanner,btn_qrscanner;
    private ImageView imageView;
    private EditText et;
    private String time;
    private File file = null;
    private TextView tv;
    private EditText qrrest;
    android.support.v7.app.ActionBar actionBar;
    private static final int MSG_ACTIVE = 5;
    private DriverManager mDriverManager = DriverManager.getInstance();

    private HQrsanner mhqscanner;


    //    private int[] mBarcodeList = {CodeID.CODEEAN13, CodeID.CODEEAN8, CodeID.CODE128,
    //            CodeID.CODE39, CodeID.QR, CodeID.PDF417, CodeID.DM};
    ////    private TextView mToast;
    ////    private Button mEnter;
    //    private  PermissionsManager mPermissionsManager;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ACTIVE:
                    // mToast.setText((String) msg.obj);
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrtest);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.pref_scan));
        }
        btn_create = (Button) findViewById(R.id.btn_create);
        btn_scanner = (Button) findViewById(R.id.btn_scanner);
        btn_qrscanner = (Button) findViewById(R.id.btn_qrscanner);
        imageView = (ImageView) findViewById(R.id.image);
        et = (EditText) findViewById(R.id.editText);

        qrrest = (EditText) findViewById(R.id.editText1);


        String res = getIntent().getStringExtra("QRCODE");
        et.setText(res == null ? "" : res);
        tv = (TextView) findViewById(R.id.tv_tips);
        btn_create.setOnClickListener(this);
        btn_scanner.setOnClickListener(this);
        btn_qrscanner.setOnClickListener(this);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                saveCurrentImage();
                return true;
            }
        });



        mDriverManager = DriverManager.getInstance();
        mhqscanner = mDriverManager.getHQrsannerDriver();



    /*    // ?????????????????????
        mPermissionsManager = new PermissionsManager(this) {
            @Override
            public void authorized(int requestCode) {
                activeBarcode();
            }

            @Override
            public void noAuthorization(int requestCode, String[] lacksPermissions) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QRTestActivity.this);
                builder.setTitle("??????");
                builder.setMessage("?????????????????????");
                builder.setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionsManager.startAppSettings(getApplicationContext());
                    }
                });
                builder.create().show();
            }

            @Override
            public void ignore() {
                activeBarcode();
            }
        };*/
    }

    @Override
    protected void onStop() {

//        mhqscanner.QRScanerPowerCtrl((byte) 0);
//
//        mhqscanner.QRScanerCtrl((byte)0);
//        mhqscanner.QRScanerCtrl((byte)0);

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

       /* // ??????????????????
        String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_PHONE_STATE};
        // ????????????
        mPermissionsManager.checkPermissions(0, PERMISSIONS);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                String msg = et.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(QRTestActivity.this, "please input", Toast.LENGTH_LONG).show();
                    return;
                }
                //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                // Generate a two-dimensional code picture, the first parameter is the content of two-dimensional code, the second parameter is the square picture side length, the unit is pixels
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapUtil.create2DCoderBitmap(msg, 400, 400);
                    //bitmap = BitmapUtil.CreateOneDCode("0123456789012");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmap);
                tv.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_scanner:
                Intent mIntent = new Intent(QRTestActivity.this, CaptureActivity.class);
                // mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //  mIntent.setClass(getApplicationContext(), CaptureActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_qrscanner:


                qrrest.setText("");
              //  qrrest.setFocusable(true);
             //   qrrest.setFocusableInTouchMode(true);
                qrrest.requestFocus();
                mhqscanner.QRScanerPowerCtrl((byte) 1);
                mhqscanner.QRScanerCtrl((byte)1);
                mhqscanner.QRScanerCtrl((byte)0);

                break;
            default:
                break;
        }
    }

    //???????????????????????????????????????????????????????????????
    //This method status bar is blank, can not display the status bar information
    private void saveCurrentImage() {
        //???????????????????????????
        //Gets the size of the current screen
        int width = getWindow().getDecorView().getRootView().getWidth();
        int height = getWindow().getDecorView().getRootView().getHeight();
        //???????????????????????????
        //Generate the same size of the picture
        Bitmap temBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        View view = getWindow().getDecorView().getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //???????????????????????????????????????,????????????DrawingCache??????????????????DrawingCache???????????????????????????????????????
        //Get the current screen image from the cache and create a copy of the DrawingCache because the bitmap that DrawingCache gets is disabled after being disabled
        temBitmap = view.getDrawingCache();
        SimpleDateFormat df = new SimpleDateFormat("yyyymmddhhmmss");
        time = df.format(new Date());
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/screen", time + ".png");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                temBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/screen/" + time + ".png";
                    final Result result = parseQRcodeBitmap(path);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(QRTestActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).start();
            //??????DrawingCahce????????????????????? ,?????????????????????????????????????????????????????????????????????????????????
            //Disable DrawingCahce otherwise it will affect the performance, and does not prohibit each capture will lead to the first screenshot to save the cache bitmap
            view.setDrawingCacheEnabled(false);
        }
    }

    //?????????????????????,?????????????????????Result?????????
    //The QRcode is parsed and the result is wrapped in a Result object
    private Result parseQRcodeBitmap(String bitmapPath) {
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath, options);
        options.inSampleSize = options.outHeight / 400;
        if (options.inSampleSize <= 0) {
            options.inSampleSize = 1;
        }
        /**
         * ???????????????????????? Auxiliary saves memory settings
         *
         * options.inPreferredConfig = Bitmap.Config.ARGB_4444;    
         * options.inPurgeable = true;
         * options.inInputShareable = true;
         */
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(bitmapPath, options);
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(bitmap);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        QRCodeReader reader = new QRCodeReader();
        Result result = null;
        try {
            result = reader.decode(binaryBitmap, hints);
        } catch (Exception e) {
        }
        return result;
    }


    //    //Active the Barcode before use it to scan and decode.
    //    //Decode can only return correct result when it's activated.
    //
    //    private void activeBarcode() {
    //        //set a readable and writeable path to save the download license file.
    //        //path need to be end with a "/"
    //        CodeUtils.setLicPathName("/mnt/sdcard/apklic/", "test");
    //        CodeUtils.enableDebug(true);
    //        CodeUtils mUtils = new CodeUtils(getApplicationContext());
    //
    //        if (mUtils.isBarcodeActivated() == false) {
    //            mUtils.tryActivateBarcode(new IActivateListener() {
    //                //this function will be called during active process, and return the process messages
    //                @Override
    //                public void onActivateProcess(String msg) {
    //                    // post the processing message
    //                    mHandler.obtainMessage(MSG_ACTIVE,
    //                            msg
    //                    ).sendToTarget();
    //                }
    //
    //                // this function will be called after the active process.
    //                //result_code: CodeUtils.RESULT_SUCCESS means active success, others means fail
    //                // error: return the fail cause message.
    //                @Override
    //                public void onActivateResult(int result_code, String error) {
    //                    // post the result message
    //                    mHandler.obtainMessage(MSG_ACTIVE,
    ////							result_code+""
    //                            error
    //                    ).sendToTarget();
    //                }
    //
    //                //Current Active state when calling active function.
    //                //if it's unactive state, this function will be returned when active process is done.
    //                @Override
    //                public void onActivateState(boolean bActivated) {
    //                    if (bActivated) // barcode is in activated state
    //                    {
    //                        //config the barcode
    //                        configBarcode();
    //                        // show the jump button
    //                       // mEnter.setVisibility(View.VISIBLE);
    //                    }
    //                }
    //            });
    //        } else {
    //            configBarcode();
    //            // show the jump button
    //          //  mEnter.setVisibility(View.VISIBLE);
    //        }
    //
    //    }
    //
    //    // config the supporting barcode types.
    //    //all the supported types are listed in CodeID.
    //    private void configBarcode() {
    //        CodeUtils mUtils = new CodeUtils(getApplicationContext());
    //
    //        //clear old configs
    //        mUtils.enableAllFormats(false);
    //        //set new config, just enable the type in the list
    //        for (int i = 0; i < mBarcodeList.length; i++)
    //            mUtils.enableCodeFormat(mBarcodeList[i]);
    //
    //
    //    }
    //
    //    @Override
    //    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //        mPermissionsManager.recheckPermissions(requestCode, permissions, grantResults);
    //    }

}

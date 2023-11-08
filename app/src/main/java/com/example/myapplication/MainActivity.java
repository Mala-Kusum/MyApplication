package com.example.myapplication;

import static com.example.myapplication.MyUtils.getSHA256;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static String Debug_Mode_Signature = "3FA59585A9365ECC45EC0D667F40B144DEAFE5CA36D7E91563F79CCD31462995";
    private static String Release_Mode_Signature = "3FA59585A9365ECC45EC0D667F40B144DEAFE5CA36D7E91563F79CCD31462995";

    public void showAlertDialogAndExitApp(String message) {
        AlertDialog alertDialog = new
                AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyUtils myUtils = new MyUtils();
        if(!myUtils.validSignature(getApplicationContext(),
                Debug_Mode_Signature,Release_Mode_Signature)){
            showAlertDialogAndExitApp("Application Signature is not matched. You can not use this app.");
        }
        try {
            PackageInfo packageInfo =
                    getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                final String currentSignature = getSHA256(signature.toByteArray());
                Log.e("Signature Key",currentSignature);
                Toast.makeText(getApplicationContext(),currentSignature,Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
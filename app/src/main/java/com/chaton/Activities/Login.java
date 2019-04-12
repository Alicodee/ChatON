package com.chaton.Activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaton.Classes.GlobalClass;
import com.chaton.Classes.Users;
import com.chaton.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    EditText email;
    EditText pass;
    Button logIn;
    ProgressDialog dialog;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView1 = (TextView)findViewById(R.id.loginText2);
        textView2 = (TextView) findViewById(R.id.forgetText);
        textView3 = (TextView) findViewById(R.id.signupText);
        email = (EditText) findViewById(R.id.loginName);
        pass = (EditText) findViewById(R.id.loginPass);
        logIn =(Button)findViewById(R.id.loginButton);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();



        Typeface bold = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/HelveticaNeuBold.ttf"); //use this.getAssets if you are calling from an Activity
        final Typeface light = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/HNThin.ttf"); //use this.getAssets if you are calling from an Activity
        Typeface regular = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/HelveticaNeuBold.ttf"); //use this.getAssets if you are calling from an Activity
        Typeface italic = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "HNI.ttf"); //use this.getAssets if you are calling from an Activity

        textView1.setTypeface(bold);
        logIn.setTypeface(bold);
        email.setTypeface(regular);
        pass.setTypeface(regular);
        textView3.setTypeface(light);
        textView2.setTypeface(light);
    }

    public void goBackToSignUp(View view) {
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation1,R.anim.animation2).toBundle();
        startActivity(new Intent(this,SignUp.class),bndlanimation);
    }

    public void logMeIn(View view) {
        final String emailString = email.getText().toString();
        String passwordString = pass.getText().toString();

        if(emailString.length() ==0 || passwordString.length() ==0){
            showDialgue("sorry","Empty fields! Enter all the feilds");
        }else if(passwordString.length() <6){
            showDialgue("Sorry","Password must be six characters");
        }else {
            showProgressDialog("Loging in....");
            mFirebaseAuth.signInWithEmailAndPassword(emailString,passwordString)
                    .addOnCompleteListener(this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isComplete()){
                                        dialog.dismiss();
                                        GlobalClass.emailNode = generateEmailNode(emailString);
                                        Toast.makeText(getApplicationContext(),"Login successfully",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        finish();


                                    }else {
                                        dialog.dismiss();
                                        showDialgue("Sorry","Login failed. Try again");
                                    }
                                }
                            });
        }

    }

    private void showDialgue(String title, String msg){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }
    private void showProgressDialog(String msg) {
        dialog = ProgressDialog.show(this, null,
                msg, true);
        dialog.setCancelable(false);
        dialog.show();
    }
    private String  generateEmailNode(String email) {

            String[] id = email.split("@");
            String final_id = id[0];
            if(final_id.contains(".")){
                final_id = final_id.replace(".","@");
            }

        return final_id;
    }

    public void goToSplash(View view) {
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation3,R.anim.animation4).toBundle();
        Intent intent = new Intent(this,WellcomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent,bndlanimation);
    }
}


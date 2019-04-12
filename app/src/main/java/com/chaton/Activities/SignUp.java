package com.chaton.Activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaton.Classes.Users;
import com.chaton.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    TextView textView1;
    TextView textView2;
    EditText name;
    EditText email;
    EditText pass;
    Button signUp;

    ProgressDialog dialog;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        textView1 = (TextView)findViewById(R.id.create_account_text);
        textView2 = (TextView) findViewById(R.id.loginText);
        name = (EditText) findViewById(R.id.signUpName);
        email = (EditText) findViewById(R.id.signUpEmail);
        pass = (EditText) findViewById(R.id.signUpPass);
        signUp =(Button)findViewById(R.id.signUp);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");

        Typeface bold = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/HelveticaNeuBold.ttf"); //use this.getAssets if you are calling from an Activity
        final Typeface light = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/HNThin.ttf"); //use this.getAssets if you are calling from an Activity
        Typeface regular = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/HelveticaNeuBold.ttf"); //use this.getAssets if you are calling from an Activity
        Typeface italic = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "HNI.ttf"); //use this.getAssets if you are calling from an Activity
        textView1.setTypeface(bold);
        name.setTypeface(regular);
        email.setTypeface(regular);
        pass.setTypeface(regular);
        signUp.setTypeface(regular);
        textView2.setTypeface(light);
    }

    public void goBackToMain(View view) {
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation3,R.anim.animation4).toBundle();
        startActivity(new Intent(this,Login.class),bndlanimation);
    }

    public void createAccount(View view) {
        final String nameString = name.getText().toString();
        final String emailString = email.getText().toString();
        String passwordString = pass.getText().toString();

        if(nameString.length() == 0 || emailString.length() == 0 || passwordString.length() == 0){
           showDialgue("Sorry","Empty fields! Please enter all the fields");
        }else if (pass.length() < 6) {
          showDialgue("Sorry","Password must be at least 6 characters");

        }else {
            showProgressDialog("Creating account..");
            mFirebaseAuth.createUserWithEmailAndPassword(emailString,passwordString)
                    .addOnCompleteListener(this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        dialog.dismiss();
                                        showDialgue("Sorry","Account creation failed. Try again");
                                    }else {
                                        sendUserDataIntoSever(nameString,emailString);
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext()," Account created successfully",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),Login.class));
                                        finish();
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
    private void sendUserDataIntoSever(String name, String email) {

        try{
            Users userData = new Users(email,name,null);
            String[] id = userData.getEmail().split("@");
            String final_id = id[0];
            if(final_id.contains(".")){
                final_id = final_id.replace(".","@");
            }
            DatabaseReference reference = mUsersDatabaseReference.child(final_id);
            reference.setValue(userData);
        }catch (Exception e){
            Log.d("firebase", "onAuthStateChanged: firebase error "+ e.getMessage());
        }
    }

    private void showProgressDialog(String msg) {
        dialog = ProgressDialog.show(SignUp.this, "",
                msg, true);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void goToSplash(View view) {
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation3,R.anim.animation4).toBundle();
        Intent intent = new Intent(this,WellcomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent,bndlanimation);
    }
}

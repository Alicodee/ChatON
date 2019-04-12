package com.chaton.Activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaton.Classes.GlobalClass;
import com.chaton.Classes.Users;
import com.chaton.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class WellcomeScreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView6;
    TextView textView5;

    ProgressBar progressBar;

    Button signUp;
    Button logIn;
    LinearLayout signInWithGoogle;

    private static final String TAG = "chat";
    private static final int RC_SIGN_IN = 1001;

    private static String name ;
    private static String email;
    private static Uri photUri;
    private static String emailNode;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome_screen);
        textView1 = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);
        textView3 = (TextView) findViewById(R.id.text3);
        textView4 = (TextView) findViewById(R.id.text4);
        textView6 = (TextView) findViewById(R.id.text6);
        textView5 = (TextView) findViewById(R.id.text5);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        signUp = (Button) findViewById(R.id.signUp);
        logIn = (Button) findViewById(R.id.logIn);
        signInWithGoogle =(LinearLayout) findViewById(R.id.signInWithGoogle);

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
        textView2.setTypeface(light);
        textView3.setTypeface(light);
        textView4.setTypeface(italic);
        textView5.setTypeface(light);
        textView6.setTypeface(regular);
        logIn.setTypeface(regular);
        signUp.setTypeface(regular);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                checkAndVeriftyAuthUser();
                mFirebaseAuth.addAuthStateListener(mAuthStateListener);
                progressBar.setVisibility(View.INVISIBLE);


            }
        },3000);

    }

    private void checkAndVeriftyAuthUser() {

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    GlobalClass.IsSigned = true;
                    for (UserInfo profile : user.getProviderData()) {
                        // Name, email address, and profile photo Url
                        name = profile.getDisplayName();
                        email = profile.getEmail();
                        emailNode = generateEmailNode(email);
                        photUri = profile.getPhotoUrl();
                        GlobalClass.name = name;
                        GlobalClass.email= email;
                        GlobalClass.emailNode = emailNode;
                        GlobalClass.photUri =photUri;

                    };

                    sendUserDataIntoSever(name,email,photUri);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();


                } else {
                    // User is signed out
                    // onSignedOutCleanup();
                    startAnimation();
                    GoogleSignInOptions signInOptions =
                            new GoogleSignInOptions.Builder(
                                    GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(getString(R.string.default_web_client_id))
                                    .requestEmail()
                                    .requestProfile()
                                    .build();
                    googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                            .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                            .build();

                    GlobalClass.IsSigned = false;


                }
            }
        };
    }

    private void startAnimation() {
        TranslateAnimation animateLeft = new TranslateAnimation(-signUp.getWidth(),0 , 0, 0);
        animateLeft.setDuration(500);
        animateLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                signUp.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
        TranslateAnimation animateRight = new TranslateAnimation(logIn.getWidth(),0 , 0, 0);
        animateRight.setDuration(700);
        animateRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logIn.setVisibility(View.VISIBLE);

                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(2000);
                textView3.startAnimation(fadeIn);
                signInWithGoogle.startAnimation(fadeIn);
                textView5.startAnimation(fadeIn);
                textView3.setVisibility(View.VISIBLE);
                signInWithGoogle.setVisibility(View.VISIBLE);
                textView5.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
        signUp.startAnimation(animateLeft);
        logIn.startAnimation(animateRight);
    }

    private void sendUserDataIntoSever(String name, String email, Uri photUri) {

        try{
            String uri = photUri.toString();
            Users userData = new Users(email,name,uri);
            String[] id = userData.getEmail().split("@");
            String final_id = id[0];
            if(final_id.contains(".")){
                final_id = final_id.replace(".","@");
            }
            DatabaseReference reference = mUsersDatabaseReference.child(final_id);
            reference.setValue(userData);
        }catch (Exception e){
            Log.d(TAG, "onAuthStateChanged: firebase error "+ e.getMessage());
        }
    }


    public void signUp(View view) {
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation1,R.anim.animation2).toBundle();
        startActivity(new Intent(this,SignUp.class),bndlanimation);
      //  overridePendingTransition(R.anim.);

    }
    public void logIn(View view) {
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation1,R.anim.animation2).toBundle();
        startActivity(new Intent(this,Login.class),bndlanimation);
        //  overridePendingTransition(R.anim.);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

        //  mMessageAdapter.clear();
//        if (mAuthStateListener != null) {
//            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    private String  generateEmailNode(String email) {

        String[] id = email.split("@");
        String final_id = id[0];
        if(final_id.contains(".")){
            final_id = final_id.replace(".","@");
        }

        return final_id;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Google Play Services failure.",Toast.LENGTH_SHORT).show();
    }

    public void signInWithGoogle(View view) {
        Intent signInIntent =
                Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        progressBar.setVisibility(View.VISIBLE);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(View.INVISIBLE);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result =
                    Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null)
                    progressBar.setVisibility(View.VISIBLE);
                    authWithFirebase(account);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this,"Google sign-in failed.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void authWithFirebase(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(!task.isSuccessful()){


                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Firebase Authentication failed",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

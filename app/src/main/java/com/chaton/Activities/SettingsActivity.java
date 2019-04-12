package com.chaton.Activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaton.Classes.CustomDialog;
import com.chaton.Classes.GlobalClass;
import com.chaton.Classes.Users;
import com.chaton.Fragments.Chat_Fragment;
import com.chaton.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "signed out";
    private static final int PICK_IMAGE_REQUEST = 555;
    private de.hdodenhof.circleimageview.CircleImageView imgProfile;
    TextView activityName;
    TextView nametitle;
    TextView noteTitle;
    TextView emailTitle;

    public static TextView name ;
    public static TextView about;
    public static int ID = 1;
    TextView email;
    ProgressBar bar;
    ImageView logout,backArrow;
    ImageView editName,editAbout;
    ProgressDialog progressDialog;

    SharedPreferences preferences;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String About = "aboutKey";
    private Bitmap bitmap;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         preferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

         nametitle = (TextView) findViewById(R.id.nameTitle);
         noteTitle =(TextView)findViewById(R.id.noteTitle);
         emailTitle=(TextView)findViewById(R.id.emailTitle);
         activityName=(TextView) findViewById(R.id.activtiyname);
        imgProfile = findViewById(R.id.profile_image);
        backArrow = findViewById(R.id.back);
        name= (TextView)findViewById(R.id.name);
        about =(TextView)findViewById(R.id.about);
        email = (TextView)findViewById(R.id.email);
        bar = (ProgressBar) findViewById(R.id.pbImageLoading);
        logout = (ImageView)findViewById(R.id.logout_img);
        editAbout = (ImageView) findViewById(R.id.editAbout);
        editName = (ImageView) findViewById(R.id.editName);
        name.setText(GlobalClass.name);

        name.setTypeface(GlobalClass.getMed(getApplicationContext()));
        activityName.setTypeface(GlobalClass.getMed(getApplicationContext()));
        about.setTypeface(GlobalClass.getMed(getApplicationContext()));
        email.setTypeface(GlobalClass.getMed(getApplicationContext()));
        nametitle.setTypeface(GlobalClass.getMed(getApplicationContext()));
        noteTitle.setTypeface(GlobalClass.getMed(getApplicationContext()));
        emailTitle.setTypeface(GlobalClass.getMed(getApplicationContext()));

        String namePref =MainActivity.getDefaults(Name,this);
        String aboutPref =MainActivity.getDefaults(About,getContext());

        if(namePref != null) {
           // Log.d("values", "onCreate: "+ MainActivity.getDefaults(Name,getContext()));

            name.setText(namePref);
        }
        if (aboutPref != null){
           // Log.d("values", "onCreate: "+ MainActivity.getDefaults(About,getContext()));

            about.setText(aboutPref);
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateIntent(imgProfile,MainActivity.class);
            }
        });


        editAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog dialog = new CustomDialog();
                 dialog.show(SettingsActivity.this,2,getContext());


            }
        });
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           CustomDialog dialog = new CustomDialog();
                dialog.show(SettingsActivity.this,ID,getContext());

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(getApplicationContext()).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           MainActivity.setDefaults(GlobalClass.isFirstTimeKey,null,getContext());
                           MainActivity.setDefaults(Name,null,getContext());
                           MainActivity.setDefaults(About,null,getContext());
                           MainActivity.setDefaults("img",null,getContext());
                           GlobalClass.clearData();
                           Bundle bndlanimation =
                                   ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation3,R.anim.animation4).toBundle();

                           startActivity(new Intent(getApplicationContext(),WellcomeScreen.class),bndlanimation);
                           Toast.makeText(SettingsActivity.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                           finish();
                       }else {
                           Toast.makeText(SettingsActivity.this,"Failed to signed out",Toast.LENGTH_SHORT).show();
                       }

                    }
                });
            }
        });

          loadProfileImage();

        email.setText(GlobalClass.email);


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),ProfileImageScreen.class));
                //animateIntent(imgProfile,ProfileImageScreen.class);
                animateIntent(imgProfile,FullScreenActivity.class);
                Log.d("firebasetree", "onClick: set"+ GlobalClass.emailNode);
            }
        });

    }

    private void loadProfileImage() {

        Picasso.get().load(GlobalClass.photUri).placeholder(R.drawable.ic_circle)
                .error(R.drawable.ic_circle)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imgProfile.setImageBitmap(bitmap);
                        bar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        imgProfile.setImageDrawable(errorDrawable);
                        bar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        imgProfile.setImageDrawable(placeHolderDrawable);
                        bar.setVisibility(View.VISIBLE);

                    }
                });


    }


    public Context getContext(){
        return SettingsActivity.this;
    }

    public void goback(View view) {
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation3,R.anim.animation4).toBundle();

        startActivity(new Intent(this,MainActivity.class),bndlanimation);


    }
    public void animateIntent(View view,Class name) {
        Intent intent =new Intent(getApplicationContext(),name);
        String transitionName = getString(R.string.transition_string);

        ActivityOptionsCompat options =

                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        view,   // Starting view
                        transitionName    // The String
                );

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    private List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }
    public void pickImage(View view) {
        Intent chooserIntent = null;
        List<Intent> intentList = new ArrayList<>();
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        intentList = addIntentsToList(this, intentList, pickIntent);
        intentList = addIntentsToList(this, intentList, takePhotoIntent);
        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }
        startActivityForResult(Intent.createChooser(chooserIntent, "Select Image"), PICK_IMAGE_REQUEST);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data.getData() == null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 50, out);
                bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                GlobalClass.photUri = getImageUri(this,bitmap);
                imgProfile.setImageBitmap(bitmap);
                Log.d("imagecheck", "onActivityResult: "+ getEncoded64ImageStringFromBitmap(bitmap));
               // convertIntoUriAndUpload();

            } else {
                Uri filePath = data.getData();
                Cursor cursor = this.getContentResolver().query(filePath, null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
             //   String image_pah = .compressImage(cursor.getString(idx));
                File imgFile = new File(cursor.getString(idx));
                if (imgFile.exists()) {
                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    GlobalClass.photUri = getImageUri(this,bitmap);
                    imgProfile.setImageBitmap(bitmap);
                    Log.d("imagecheck", "onActivityResult: "+ getEncoded64ImageStringFromBitmap(bitmap));

                    //  convertIntoUriAndUpload();;

                }
            }
        }
    }

    private void convertIntoUriAndUpload() {




        new AsyncTask<Void,Void,Uri>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getContext(), null, "Updating...", false, false);
            }

            @Override
            protected Uri doInBackground(Void... voids) {
                Uri uri = getImageUri(getContext(),bitmap);
                String s = BitMapToString(bitmap);
                GlobalClass.photUri= uri;
                DatabaseReference reference = mUsersDatabaseReference.child(GlobalClass.emailNode);
                Users users = new Users(GlobalClass.email,GlobalClass.name,GlobalClass.photUri.toString());
                reference.setValue(users);
                return uri;
            }

            @Override
            protected void onPostExecute(Uri uri) {
                super.onPostExecute(uri);
                progressDialog.dismiss();
            }
        }.execute();
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp1=Base64.encodeToString(b, Base64.DEFAULT);
        return temp1;
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap1=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap1;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

}

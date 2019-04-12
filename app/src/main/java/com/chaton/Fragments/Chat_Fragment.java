package com.chaton.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.chaton.Activities.WellcomeScreen;
import com.chaton.Classes.FriendlyMessage;
import com.chaton.Activities.MainActivity;
import com.chaton.Adapters.MessageAdapter;
import com.chaton.Classes.GlobalClass;
import com.chaton.Classes.Users;
import com.chaton.Database.DBHelper;
import com.chaton.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.chaton.Activities.SettingsActivity.Name;


/*
 */
public class Chat_Fragment extends Fragment {
    public static final String ANONYMOUS = "anonymous";
    public static String name,email ;
    public static  Uri photUri;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_PHOTO_PICKER = 2;

    private RecyclerView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mCameraPickerButton;
    private ImageButton mGalleryPickerButton;
    private EditText mMessageEditText;
    private ImageButton mSendButton;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;


    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    private String mUsername;
    private ChildEventListener mChildEventListener;

    public static final int GALLERY = 555;
    public static final int CAMERA = 777;

    SQLiteDatabase mDB;
    Cursor cursor;

    public Chat_Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long milis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        String time= simpleDateFormat.format(milis);
        Log.d("timet", "onComplete: "+ time);

        // Initialize references to views
        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        mMessageListView = (RecyclerView) getView().findViewById(R.id.messageListView);
        mCameraPickerButton = (ImageButton) getView().findViewById(R.id.cameraPickerButton);
        mGalleryPickerButton = (ImageButton) getView().findViewById(R.id.galleryPickerButton);
        mMessageEditText = (EditText) getView().findViewById(R.id.messageEditText);
     //   mMessageEditText.setTypeface(GlobalClass.getLight(getContext()));
        mSendButton = (ImageButton) getView().findViewById(R.id.sendButton);
        mSendButton.setClickable(false);
        mSendButton.setEnabled(false);


        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mFirebaseStorage = FirebaseStorage.getInstance();

        mDatabaseReference = mFirebaseDatabase.getReference().child("global");
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");
        Log.d("firebasetree", "onViewCreated: "+GlobalClass.emailNode);
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users").child(GlobalClass.emailNode);

        DBHelper helper = new DBHelper(getContext());
        mDB = helper.getWritableDatabase();

        mCameraPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoFromCamera();
            }
        });
        mGalleryPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
            }
        });


       // SharedPreferences preferences =

        // Initialize message ListView and its adapter
        final List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter( friendlyMessages,getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        manager.setStackFromEnd(true);

        mMessageListView.setLayoutManager(manager);
        mMessageListView.setItemAnimator(new DefaultItemAnimator());
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                    mSendButton.setClickable(true);
                } else {
                    mSendButton.setEnabled(false);
                    mSendButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click

                FriendlyMessage message = new FriendlyMessage(getTime(),mMessageEditText.getText().toString(), mUsername, null, GlobalClass.emailNode,GlobalClass.photUri.toString());
                mDatabaseReference.push().setValue(message);

                //  mMessageAdapter.add(message);
                // Clear input box
                mMessageEditText.setText("");
            }
        });



       loadData();
        setupCredentials();
    }

    private String getTime() {
        long milis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        String time= simpleDateFormat.format(milis);
        return time;
    }

    private void loadData() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    Log.d("TAG", ""+ name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mDatabaseReference.addValueEventListener(eventListener);

        ValueEventListener eventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               if(MainActivity.getDefaults(GlobalClass.isFirstTimeKey,getContext()) == null ){

                   Object name = dataSnapshot.child("name").getValue();
                   Object email = dataSnapshot.child("email").getValue();
                   Object urlPhoto = dataSnapshot.child("urlPhoto").getValue();

                   if(name != null){
                       mUsername =name.toString();
                       GlobalClass.name = mUsername  ;
                       Log.d("firebasevalue", "onDataChange: "+GlobalClass.name);
                       MainActivity.setDefaults(Name,mUsername,getContext());

                   }
                   if(email != null){
                       GlobalClass.email = email.toString();
                       Log.d("firebasevalue", "onDataChange: "+GlobalClass.email);
                   }
                   if(urlPhoto != null){
                       GlobalClass.photUri =  Uri.parse(urlPhoto.toString());
                       Log.d("firebasevalue", "onDataChange: " +GlobalClass.photUri);

                   }
                   MainActivity.setDefaults(GlobalClass.isFirstTimeKey,"false",getContext());
                   GlobalClass.isFirstTime =false;
               }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mUsersDatabaseReference.addValueEventListener(eventListener1);
    }

    private void setupCredentials() {
      //  mUsername = SplashScreen.name;
        if(!GlobalClass.IsSigned){
            onSignedOutCleanup();
            attachDatabaseReadListener();
        }else {
            attachDatabaseReadListener();
        }
        if(MainActivity.getDefaults(Name,getContext()) != null){
            mUsername = MainActivity.getDefaults(Name,getContext());
            Log.d("realuser", "setupCredentials: "+ mUsername);
        }
        email = GlobalClass.email;
        photUri = GlobalClass.photUri;
        Picasso.get().load(photUri).placeholder(R.drawable.ic_circle)
                .error(R.drawable.ic_circle)
                .into(MainActivity.imgDocotor);

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        detachDatabaseReadListener();

    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        mMessageAdapter.onDetachedFromRecyclerView(mMessageListView);
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    mMessageAdapter.add(friendlyMessage);
                    mMessageListView.smoothScrollToPosition(mMessageAdapter.getItemCount());
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
//                    mMessageAdapter.add(friendlyMessage);
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }
    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    public void choosePhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            // Get a reference to store file at chat_photos/<FILENAME>
            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // When the image has successfully uploaded, we get its download URL
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            // Set the download URL to the message box, so that the user can send it to the database
                            FriendlyMessage friendlyMessage = new FriendlyMessage(getTime(),null, mUsername, downloadUrl.toString(),GlobalClass.emailNode,GlobalClass.photUri.toString());
                            mDatabaseReference.push().setValue(friendlyMessage);
                        }
                    });
        }else if(requestCode == CAMERA && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();

            // Get a reference to store file at chat_photos/<FILENAME>
            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // When the image has successfully uploaded, we get its download URL
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            // Set the download URL to the message box, so that the user can send it to the database
                            FriendlyMessage friendlyMessage = new FriendlyMessage(getTime(),null, mUsername, downloadUrl.toString(),GlobalClass.emailNode,GlobalClass.photUri.toString());
                            mDatabaseReference.push().setValue(friendlyMessage);
                        }
                    });
        }
    }

}

/**
 * Copyright Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chaton.Activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chaton.Adapters.PageAdapter;
import com.chaton.Classes.GlobalClass;
import com.chaton.Fragments.Chat_Fragment;
import com.chaton.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static de.hdodenhof.circleimageview.CircleImageView imgDocotor;
    TextView   title;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgDocotor =findViewById(R.id.profile_img);
        title =(TextView)findViewById(R.id.title);
        setSupportActionBar(toolbar);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        checkAndVeriftyAuthUser();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);


        Typeface bold = Typeface.createFromAsset(this.getAssets(),
                "fonts/HelveticaNeuBold.ttf"); //use this.getAssets if you are calling from an Activity

        title.setTypeface(bold);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Camera"));
        tabLayout.addTab(tabLayout.newTab().setText("Chat"));
        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);




        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PageAdapter adapter = new PageAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Picasso.get().load(GlobalClass.photUri).placeholder(R.drawable.ic_circle)
                .error(R.drawable.ic_circle)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imgDocotor.setImageBitmap(bitmap);
                       // bar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        imgDocotor.setImageDrawable(errorDrawable);
                        //bar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        imgDocotor.setImageDrawable(placeHolderDrawable);
                        //bar.setVisibility(View.VISIBLE);

                    }
                });

        imgDocotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bndlanimation =
//                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation1,R.anim.animation2).toBundle();
//
//                startActivity(new Intent(getApplicationContext(),SettingsActivity.class),bndlanimation);

                animateIntent();

            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
       // inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);

                return true;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
               // Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public void animateIntent() {
        Intent intent =new Intent(getApplicationContext(),SettingsActivity.class);
        String transitionName = getString(R.string.transition_string);

        ActivityOptionsCompat options =

                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        imgDocotor,   // Starting view
                        transitionName    // The String
                );

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    public void gotoSettings(View view) {
        Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation1,R.anim.animation2).toBundle();

                startActivity(new Intent(getApplicationContext(),SettingsActivity.class),bndlanimation);

    }

    private void checkAndVeriftyAuthUser() {

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is signed in
                    finish();
                }
            }
        };
    }

}

package com.chaton.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.chaton.Classes.GlobalClass;
import com.chaton.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class FullScreenActivity extends AppCompatActivity {


    ImageView profileImage;
    ImageView back;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        profileImage = (ImageView) findViewById(R.id.full_imageview);
        back =(ImageView) findViewById(R.id.back);
        bar =(ProgressBar)findViewById(R.id.progressBar);


        if(GlobalClass.photUri != null){
            Picasso.get().load(GlobalClass.photUri).placeholder(R.drawable.ic_circle)
                    .error(R.drawable.ic_circle)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            profileImage.setImageBitmap(bitmap);
                            bar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            profileImage.setImageDrawable(errorDrawable);
                            bar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            profileImage.setImageDrawable(placeHolderDrawable);
                            bar.setVisibility(View.VISIBLE);

                        }
                    });

          //  loadProfileImage(profileImage);


        }
    }
    public void animateIntent() {
        Intent intent =new Intent(FullScreenActivity.this,SettingsActivity.class);
        String transitionName = getString(R.string.transition_string);

        ActivityOptionsCompat options =

                ActivityOptionsCompat.makeSceneTransitionAnimation(FullScreenActivity.this,
                        profileImage,   // Starting view
                        transitionName    // The String
                );

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

}

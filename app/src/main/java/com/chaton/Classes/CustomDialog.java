package com.chaton.Classes;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chaton.Activities.MainActivity;
import com.chaton.Activities.SettingsActivity;
import com.chaton.R;

import static com.chaton.Activities.SettingsActivity.About;
import static com.chaton.Activities.SettingsActivity.Name;

public class CustomDialog extends DialogFragment {
    Dialog dialog;
    SharedPreferences preferences;

    public CustomDialog(){

    }

    public void show(Activity activity,  final int id, final Context context){
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog);

        final EditText editText = (EditText)dialog.findViewById(R.id.editText);
        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button save = (Button) dialog.findViewById(R.id.save);


        if(id ==1){
            editText.setText(MainActivity.getDefaults(Name,context));
        }else {
            editText.setText(MainActivity.getDefaults(About,context));
        }

      //  dialog.setTitle(message);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id == 1){
                    String name = editText.getText().toString();
                    MainActivity.setDefaults(Name,name,context);
                    Log.d("realuser", "onClick: "+name);
                    name =MainActivity.getDefaults(Name,context);
                    SettingsActivity.name.setText(name);
                    Log.d("realuser", "onClick1: "+name);

                }
                else {
                    SettingsActivity.about.setText(MainActivity.getDefaults(About,context));
                    MainActivity.setDefaults(About,editText.getText().toString(),context);

                    }
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}

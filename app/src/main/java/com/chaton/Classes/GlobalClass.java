package com.chaton.Classes;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;

public class GlobalClass {
    public static boolean isFirstTime =true;
    public  static boolean IsSigned=false;
    public static String name=null ;
    public static String email = null;
    public static Uri photUri = null;
    public static String emailNode =null;
    public static String isFirstTimeKey = "isFirstTimeKey";


    public static void clearData(){
        IsSigned =false;
        name=null;
        emailNode =null;
        photUri = null;
        email= null;
        emailNode =null;
        isFirstTime = true;
    }
    public static Typeface getBold(Context context){
        Typeface bold = Typeface.createFromAsset(context.getAssets(),
                "fonts/HelveticaNeuBold.ttf"); //use this.getAssets if you are calling from an Activity
        return bold;
    }
    public static Typeface getRegular(Context context){
        Typeface regular = Typeface.createFromAsset(context.getAssets(),
                "fonts/HelveticaNeue.ttf");//use this.getAssets if you are calling from an Activity
        return regular;
    }
    public static Typeface getThin(Context context){
        Typeface thin =  Typeface.createFromAsset(context.getAssets(),
                "HNThin.ttf");//use this.getAssets if you are calling from an Activity
        return thin;
    }
    public static Typeface getLight(Context context){
        Typeface light =Typeface.createFromAsset(context.getAssets(),
                "fonts/HelveticaNeue Light.ttf"); //use this.getAssets if you are calling from an Activity
        return light;
    }
    public static Typeface getMed(Context context){
        Typeface med =Typeface.createFromAsset(context.getAssets(),
                "fonts/HelveticaNeueMed.ttf"); //use this.getAssets if you are calling from an Activity
        return med;
    }
}


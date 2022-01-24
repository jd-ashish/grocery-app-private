package com.apps.onlinegroceriesworld.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.View;

import com.apps.onlinegroceriesworld.activity.MainActivity;
import com.apps.onlinegroceriesworld.models.AppSettings;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import q.rorbin.badgeview.QBadgeView;


public class Util {
    static Context context;
    public static AppSettings appSettings;
    public Util(Context context) {
        this.context = context;
    }

    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Uri getNotificationSound(){
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return alarmSound;
    }
    public static void playNotofication(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String StringModify(String str){
        str = str.replaceAll("[-+^@!#$0-9]*", " ");
        str = str.replaceAll("_"," ");
        //str=str.replaceAll("\\W", " ")    //we can also use this regular expression
        return nullString(capitalStr(str));
    }
    public static String nullString(String text){
        if(text!=null){
            return text;
        }else {
            return "";
        }
    }
    public static boolean isEmail(String email){
        String regex = "^(.+)@(.+)$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);
        //Create instance of matcher
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidMobileNo(String str)
    {
        //(0/91): number starts with (0/91)
        //[7-9]: starting of the number may contain a digit between 0 to 9
        //[0-9]: then contains digits 0 to 9
        Pattern ptrn = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        //the matcher() method creates a matcher that will match the given input against this pattern
        Matcher match = ptrn.matcher(str);
        //returns a boolean value
        return (match.find() && match.group().equals(str));
    }
    public static String convertPrice(Context context, Double price) {
        appSettings = new UserPrefs(context).getAppSettingsPreferenceObjectJson().getData().get(0);
        return appSettings.getCurrency().getSymbol()+ " " + new DecimalFormat("#,###.00").format(Double.parseDouble(String.valueOf(price*appSettings.getCurrency().getExchangeRate())));
    }
    public static List<Integer> reverseArray(List<Integer> a) {
        // Write your code here
        List<Integer> revInt = new ArrayList<>();
        for(int i=0; i<a.size(); i++){
            revInt.add(a.get(a.size()-i-1));
        }

        return revInt;
    }
    public static AppSettings getAppSettings(Context context){
        return new UserPrefs(context).getAppSettingsPreferenceObjectJson().getData().get(0);
    }
//    public static String convertPrice(Context context, Double price) {
////        appSettings = new UserPrefs(context).getAppSettingsPreferenceObjectJson("app_settings_response").getData().get(0);
//        return "RS. " + new DecimalFormat("#,###.00").format(Double.parseDouble(String.valueOf(price*1)));
//    }
//    public static AppSettings getAppSettings(Context context){
//        return new UserPrefs(context).getAppSettingsPreferenceObjectJson("app_settings_response").getData().get(0);
//    }
    public static int convertDpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static String capitalStr(String string) {
        if (string == null || string.equals("")) {
            return "";
        } else if (string.length() < 2) {
            return string.toUpperCase();
        } else return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String lowerFirstStr(String string) {
        if (string == null || string.equals("")) {
            return "";
        }
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    public static ArrayList<String> capitalStrList(List<String> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        for (int i = 0; i < list.size(); i++) {
            list.set(i, capitalStr(list.get(i)));
        }
        return (ArrayList<String>) list;
    }

    public static ArrayList<String> lowerStrList(List<String> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).toLowerCase());
        }
        return (ArrayList<String>) list;
    }

    public void QBadgeView(String count){
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) MainActivity.binding.bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2); // number of menu from left
        new QBadgeView(context).bindTarget(v).setBadgeText(count).setShowShadow(false);
    }

}

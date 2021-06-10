package com.test.ecommerceft.room;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.test.ecommerceft.BuildConfig;

public class Utils {

    public static Drawable setMyImage(String imageName, Context context){
        String uri = "@drawable/"+imageName;  // where myresource (without the extension) is the file

        int imageResource = context.getResources().getIdentifier(uri, null, BuildConfig.APPLICATION_ID);

        Drawable res = context.getResources().getDrawable(imageResource);

        return res;

    }
}

package com.example.testecarrefour.model;

import android.util.Base64;

public class Base64Custom {

    public static String codeBase64(String text){
        return Base64.encodeToString(text.getBytes(),Base64.DEFAULT).replaceAll("(\\n|\\r )", "");
    }

    public static String uncodeBase64(String textCode){
        return new String(Base64.decode(textCode, Base64.DEFAULT));
    }
}

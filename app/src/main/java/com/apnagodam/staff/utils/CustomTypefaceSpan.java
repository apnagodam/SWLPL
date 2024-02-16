package com.apnagodam.staff.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.Pair;
import androidx.annotation.NonNull;

import com.apnagodam.staff.ApnaGodamApp;
import com.apnagodam.staff.R;


public class CustomTypefaceSpan extends MetricAffectingSpan {
    int color = Color.BLACK;
    Typeface typeface;

    float size;

    public CustomTypefaceSpan(int color) {
        this.color = color;
    //    this.typeface = Typeface.createFromAsset(ApnaGodamApp.getApp().getAssets(), "fonts/GothamBold.ttf");
        this.typeface = Typeface.createFromAsset(ApnaGodamApp.getApp().getAssets(), "fonts/RobotoLight.ttf");
        size = ApnaGodamApp.getApp().getResources().getDimensionPixelSize(R.dimen._14ssp);
    }

    public CustomTypefaceSpan(int color, float size) {
        this.color = color;
   //     this.typeface = Typeface.createFromAsset(ApnaGodamApp.getApp().getAssets(), "fonts/GothamBold.ttf");
        this.typeface = Typeface.createFromAsset(ApnaGodamApp.getApp().getAssets(), "fonts/RobotoLight.ttf");
        this.size = size * 2;
    }

    public CustomTypefaceSpan() {
 //       this.typeface = Typeface.createFromAsset(ApnaGodamApp.getApp().getAssets(), "fonts/GothamBold.ttf");
        this.typeface = Typeface.createFromAsset(ApnaGodamApp.getApp().getAssets(), "fonts/RobotoLight.ttf");
        size = ApnaGodamApp.getApp().getResources().getDimensionPixelSize(R.dimen._14ssp);
    }

    public static SpannableStringBuilder getSpannableString(String string, String part) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
        int indexStart = string.indexOf(part);
        int indexEnd = indexStart + part.length();
        spannableStringBuilder.setSpan(new CustomTypefaceSpan(), indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }


    public static SpannableStringBuilder getSpannableString(String string, String part, String color) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
        int indexStart = string.indexOf(part);
        int indexEnd = indexStart + part.length();
        spannableStringBuilder.setSpan(new CustomTypefaceSpan(Color.parseColor(color)), indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    public static SpannableStringBuilder getSpannableString(String string, String part, String color, float size) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
        int indexStart = string.indexOf(part);
        int indexEnd = indexStart + part.length();
        spannableStringBuilder.setSpan(new CustomTypefaceSpan(Color.parseColor(color), size), indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    public static SpannableStringBuilder getSpannableString(String string, Pair<String, String>... pairs) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
        for (Pair<String, String> pair : pairs) {
            int indexStart = string.indexOf(pair.first);
            int indexEnd = indexStart + pair.first.length();
            spannableStringBuilder.setSpan(new CustomTypefaceSpan(Color.parseColor(pair.second)), indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        return spannableStringBuilder;
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint textPaint) {
        textPaint.setTypeface(typeface);
        textPaint.setColor(color);
        textPaint.setTextSize(size);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(typeface);
        tp.setColor(color);
        tp.setTextSize(size);
    }
}


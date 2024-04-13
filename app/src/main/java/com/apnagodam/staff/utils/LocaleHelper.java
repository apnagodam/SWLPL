package com.apnagodam.staff.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.Pair;

import com.apnagodam.staff.db.SharedPreferencesRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    private static final String DEFAULT_LANGUAGE = "en";

    static List<Pair<String, String>> languageList = Arrays.asList(
            Pair.create("en", "English"),
            Pair.create("hi", "हिन्दी (Hindi)"));


    public static List<Pair<String, String>> getLanguageList() {
        return languageList;
    }

    public static String getLanguageName(Context context) {
        for (Pair<String, String> stringStringPair : languageList) {
            if (stringStringPair.first.equalsIgnoreCase(getLanguage(context))) {
                return stringStringPair.second;
            }
        }
        return languageList.get(0).second;
    }


    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, DEFAULT_LANGUAGE);
        return setLocale(context, lang);

    }

    public static Context onAttach(Context context, String defaultLanguage) {
        String lang = getPersistedData(context, defaultLanguage);

        return setLocale(context, lang);
    }

    public static String getLanguage(Context context) {

        //  return getPersistedData(context, Locale.getDefault().getLanguage());
        return getPersistedData(context, DEFAULT_LANGUAGE);

    }

    public static Context setLocale(Context context, String language) {
        persist(context, language);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }
        return updateResourcesLegacy(context, language);
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        return new SharedPreferencesRepository().getSelectedLanguage();
    }

    private static void persist(Context context, String language) {
        new SharedPreferencesRepository().setSelectedLanguage(language);
    }

    public static Locale getLocal(String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        return locale;

    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        configuration.setLayoutDirection(locale);

//        Configuration config = new Configuration();
//        config.locale = locale;
//
//        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

}

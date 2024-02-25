package com.apnagodam.staff.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.apnagodam.staff.ApnaGodamApp;
import com.apnagodam.staff.module.AllUserPermissionsResultListResponse;
import com.apnagodam.staff.module.Bank;
import com.apnagodam.staff.module.CommudityResponse;
import com.apnagodam.staff.module.UserDetails;
import com.apnagodam.staff.utils.Constants;
import com.apnagodam.staff.utils.Tags;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SharedPreferencesRepository implements Tags {
    private static SharedPreferences loginSharedPreferences;
    private static SharedPreferences.Editor loginPrefEditor;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor sharedPrefEditor;
    private static SharedPreferencesRepository dataManagerInstance;

    public static SharedPreferencesRepository getDataManagerInstance() {
        if (dataManagerInstance == null) {
            return dataManagerInstance = new SharedPreferencesRepository();
        } else {
            return dataManagerInstance;
        }
    }

    public void clear() {
        loginPrefEditor.clear().commit();
        loginPrefEditor.putBoolean(LOGIN_PREF_Profile, true).commit();
    }

    public SharedPreferencesRepository() {
        //static {
        loginSharedPreferences = ApnaGodamApp.app.getSharedPreferences(LANG, Context.MODE_PRIVATE);
        loginPrefEditor = loginSharedPreferences.edit();
        sharedPreferences = ApnaGodamApp.app.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        sharedPrefEditor = sharedPreferences.edit();
    }

    public void setLanguageSelected(boolean isSelected) {
        loginPrefEditor.putBoolean(IS_LANGUAGE_SELECTED, isSelected).commit();
    }

    public boolean isLanguageSelected() {
        return loginSharedPreferences.getBoolean(IS_LANGUAGE_SELECTED, false);
    }

    public boolean isLoggedIn() {
        return loginSharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void setIsLoggedIn(boolean isFirstTime) {
        loginPrefEditor.putBoolean(IS_LOGGED_IN, isFirstTime).commit();
    }

    public static boolean isUSerName() {
        return loginSharedPreferences.getBoolean(LOGIN_PREF_Profile, false);
    }

    public static void setIsUserName(boolean isProfileComplete) {
        loginPrefEditor.putBoolean(LOGIN_PREF_Profile, isProfileComplete).commit();
    }

    public static void clearLoginPref() {
        loginPrefEditor.clear().commit();
    }


    public static void saveSessionToken(String sessionToken) {
        loginPrefEditor.putString(SESSION_TOKEN, sessionToken).commit();
    }

    public static void logout(){
        SharedPreferencesRepository.getDataManagerInstance().clear();
        SharedPreferencesRepository.setIsUserName(false);
        SharedPreferencesRepository.saveSessionToken("");

    }
    public static String getSessionToken() {
        String value = loginSharedPreferences.getString(SESSION_TOKEN, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

    public static String getSelectedKNo() {
        String value = loginSharedPreferences.getString(SELECTED_KNO, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

    public static void setSelectedKNo(String selectedKNo) {
        loginPrefEditor.putString(SELECTED_KNO, selectedKNo).commit();
    }


    public void saveUserData(UserDetails user) {
        String data = new Gson().toJson(user, UserDetails.class);
        loginPrefEditor.putString(USER_NAME, data).commit();
    }

    public UserDetails getUser() {
        String userData = loginSharedPreferences.getString(USER_NAME, null);
        return userData != null ? new Gson().fromJson(userData, UserDetails.class) : null;
    }

    // login prefs
    public String getSelectedLanguage() {
        return loginSharedPreferences.getString(SELECTED_LANGUAGE, "en");
    }

    public void setSelectedLanguage(String language) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                loginPrefEditor.putString(SELECTED_LANGUAGE, language).commit();
            }
        });
    }

    public static String getLocale() {
        String language = sharedPreferences.getString(Locale_KeyValue, Constants.ENGLISH_LOCALE);
        return language;
    }

    //Save locale method in preferences
    public static void setLocale(String locale) {
        sharedPrefEditor.putString(Locale_KeyValue, locale);
        sharedPrefEditor.commit();
    }

    /*  public static void setBank(List<OTPvarifedResponse.Bank> bankNameResponses) {
          String data = new Gson().toJson(bankNameResponses, OTPvarifedResponse.Bank.class);
          sharedPrefEditor.putString(BANK_NAME, data).commit();
      }

      public static List<OTPvarifedResponse.Bank> getBank() {
          String bankData = sharedPreferences.getString(BANK_NAME, null);
          Gson gson = new Gson();
          Type userListType = new TypeToken<ArrayList<OTPvarifedResponse.Bank>>(){}.getType();
          ArrayList<OTPvarifedResponse.Bank> userArray = gson.fromJson(bankData, userListType);
          return userArray;
      }
  */
    public void saveUserPermissionData(List<AllUserPermissionsResultListResponse.UserPermissionsResult> user) {
        String data = new Gson().toJson(user);
        sharedPrefEditor.putString(Permission, data).commit();

    }

    public List<AllUserPermissionsResultListResponse.UserPermissionsResult> getUserPermission() {
        String userMonthlyLedger = sharedPreferences.getString(Permission, null);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<AllUserPermissionsResultListResponse.UserPermissionsResult>>() {
        }.getType();
        ArrayList<AllUserPermissionsResultListResponse.UserPermissionsResult> userArray= gson.fromJson(userMonthlyLedger, userListType);

        return userArray;
    }

    public void setEmployee(List<CommudityResponse.Employee> datumMonthlyLedger) {
        String data = new Gson().toJson(datumMonthlyLedger);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                sharedPrefEditor.putString(Employee, data).commit();
            }
        });
    }

    public List<CommudityResponse.Employee> getEmployee() {
        String userMonthlyLedger = sharedPreferences.getString(Employee, null);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<CommudityResponse.Employee>>() {
        }.getType();
        ArrayList<CommudityResponse.Employee> userArray = gson.fromJson(userMonthlyLedger, userListType);
        return userArray;
    }

    public void setContractor(List<CommudityResponse.Labour> datumMonthlyLedger) {
        String data = new Gson().toJson(datumMonthlyLedger);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                sharedPrefEditor.putString(Contractor, data).commit();
            }
        });
    }

    public List<CommudityResponse.Contractor> getContractorList() {
        String userMonthlyLedger = sharedPreferences.getString(Contractor, null);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<CommudityResponse.Contractor>>() {
        }.getType();
        ArrayList<CommudityResponse.Contractor> userArray = gson.fromJson(userMonthlyLedger, userListType);
        return userArray;
    }

    public void setUser(List<CommudityResponse.User> datumMonthlyLedger) {
        String data = new Gson().toJson(datumMonthlyLedger);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                sharedPrefEditor.putString(User, data).commit();
            }
        });
    }

    public List<CommudityResponse.User> getuserlist() {
        String userMonthlyLedger = sharedPreferences.getString(User, null);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<CommudityResponse.User>>() {
        }.getType();
        ArrayList<CommudityResponse.User> userArray = gson.fromJson(userMonthlyLedger, userListType);
        return userArray;
    }

    public void setCommdity(List<CommudityResponse.Category> datumMonthlyLedger) {
        String data = new Gson().toJson(datumMonthlyLedger);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                sharedPrefEditor.putString(Commudity, data).commit();
            }
        });
    }

    public List<CommudityResponse.Category> getCommudity() {
        String userMonthlyLedger = sharedPreferences.getString(Commudity, null);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<CommudityResponse.Category>>() {
        }.getType();
        ArrayList<CommudityResponse.Category> userArray = gson.fromJson(userMonthlyLedger, userListType);
        return userArray;
    }

    public void SETTerminal(List<CommudityResponse.Terminals> datumMonthlyLedger) {
        String data = new Gson().toJson(datumMonthlyLedger);
        sharedPrefEditor.putString(Terminal, data).commit();
    }

    public ArrayList<CommudityResponse.Terminals> GetTerminal() {
        String userMonthlyLedger = sharedPreferences.getString(Terminal, null);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<CommudityResponse.Terminals>>() {
        }.getType();
        ArrayList<CommudityResponse.Terminals> userArray = gson.fromJson(userMonthlyLedger, userListType);
        return userArray;
    }

    public void setBankNameList(List<Bank> bankNameResponses) {
        String data = new Gson().toJson(bankNameResponses);
        sharedPrefEditor.putString(BANK_NAME, data).commit();
    }

    public List<Bank> getBankNameList() {
        String bankNameList = sharedPreferences.getString(BANK_NAME, null);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<Bank>>() {
        }.getType();
        ArrayList<Bank> userArray = gson.fromJson(bankNameList, userListType);
        return userArray;
    }

    public void savelat(String sessionToken) {
        loginPrefEditor.putString(lat, sessionToken).commit();
    }

    public String getlat() {
        String value = loginSharedPreferences.getString(lat, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

    public void savelong(String sessionToken) {
        loginPrefEditor.putString(Long, sessionToken).commit();
    }

    public String getlong() {
        String value = loginSharedPreferences.getString(Long, null);
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }
    // temp save dataa
    public static boolean isReleaseSave() {
        return loginSharedPreferences.getBoolean(ACCOUNTS, false);
    }

    public static void setisReleaseSave(boolean isProfileComplete) {
        loginPrefEditor.putBoolean(ACCOUNTS, isProfileComplete).commit();
    }
}

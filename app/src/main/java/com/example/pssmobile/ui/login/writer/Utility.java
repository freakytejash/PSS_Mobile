package com.example.pssmobile.ui.login.writer;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pssmobile.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utility {
    
    public static void hideSoftKeyboard(Context mContext, View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Context mContext, View v) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static final String[] PERMISSIONS = Arrays.asList(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION).toArray(new String[0]);
    public static final int PERMISSION_ALL = 800;

    public static Boolean hasPermissions(Context mContext, String[] permissions) {
        if (mContext != null && permissions != null) {
            for (int permission = 0; permission < permissions.length; permission++) {
                if (ActivityCompat.checkSelfPermission(mContext, permissions[permission]) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Bitmap getBitmapFromBase64(String image) {
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static String encodeImageToBase64(String path) {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;
    }

    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        } catch (MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }

/*    public static String convertErrorCode(Response<Object> response) {
        Gson gson = new GsonBuilder().create();
        ErrorPojoClass mError = new ErrorPojoClass();
        try {
            mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mError.message;
    }*/

    public static boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }

    public static boolean isPasswordValid(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return !matcher.matches();

    }

    public static String calculateStrength(String passwordText, AppCompatTextView tvPasswordStrength) {
        int upperChars = 0, lowerChars = 0, numbers = 0,
                specialChars = 0, otherChars = 0, strengthPoints = 0;
        char c;

        int passwordLength = passwordText.length();

        if (passwordLength == 0) {
            //tvPasswordStrength.setText("Invalid Password");
            //tvPasswordStrength.setBackgroundColor(Color.RED);
            tvPasswordStrength.setText("");
            return "Invalid Password";
        }

        //If password length is <= 5 set strengthPoints=1
        if (passwordLength <= 5) {
            strengthPoints = 1;
        }
        //If password length is >5 and <= 10 set strengthPoints=2
        else if (passwordLength <= 10) {
            strengthPoints = 2;
        }
        //If password length is >10 set strengthPoints=3
        else
            strengthPoints = 3;
        // Loop through the characters of the password
        for (int i = 0; i < passwordLength; i++) {
            c = passwordText.charAt(i);
            // If password contains lowercase letters
            // then increase strengthPoints by 1
            if (c >= 'a' && c <= 'z') {
                if (lowerChars == 0) strengthPoints++;
                lowerChars = 1;
            }
            // If password contains uppercase letters
            // then increase strengthPoints by 1
            else if (c >= 'A' && c <= 'Z') {
                if (upperChars == 0) strengthPoints++;
                upperChars = 1;
            }
            // If password contains numbers
            // then increase strengthPoints by 1
            else if (c >= '0' && c <= '9') {
                if (numbers == 0) strengthPoints++;
                numbers = 1;
            }
            // If password contains _ or @
            // then increase strengthPoints by 1
            else if (c == '_' || c == '@') {
                if (specialChars == 0) strengthPoints += 1;
                specialChars = 1;
            }
            // If password contains any other special chars
            // then increase strengthPoints by 1
            else {
                if (otherChars == 0) strengthPoints += 2;
                otherChars = 1;
            }
        }

        if (strengthPoints <= 3) {
            tvPasswordStrength.setText("LOW");
            tvPasswordStrength.setBackgroundColor(Color.RED);
        } else if (strengthPoints <= 6) {
            tvPasswordStrength.setText("MEDIUM");
            tvPasswordStrength.setBackgroundColor(Color.YELLOW);
        } else if (strengthPoints <= 9) {
            tvPasswordStrength.setText("HIGH");
            tvPasswordStrength.setBackgroundColor(Color.GREEN);
        }

        return tvPasswordStrength.getText().toString();
    }
/*
    public static int getCurrentCountryCode(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryIso = telephonyManager.getSimCountryIso().toUpperCase();
        return PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryIso);
    }*/

    private static Dialog loaderDialog;
    public static void showLoader(Context activityContext, Boolean isCancel) {
        try {
            // Dialog instance for loader;
            loaderDialog = new Dialog(activityContext);
            loaderDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(activityContext , R.color.color_transparent));
            loaderDialog.setCancelable(isCancel);
            loaderDialog.setCanceledOnTouchOutside(isCancel);
            loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
           // loaderDialog.setContentView(R.layout.progress_layout);
            loaderDialog.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void hideLoader() {
        if (loaderDialog != null) {
            if (loaderDialog.isShowing()) {
                loaderDialog.cancel();
                loaderDialog.dismiss();
            }
        }
    }

    void showProgressHUD(Context context) {
        if (context != null) {
            showLoader(context, true);
        }
    }

    void showProgress(Context context , Boolean isCancel) {
        if (context != null) {
            showLoader(context, isCancel);
        }
    }

    void hideProgressHud() {
        hideLoader();
    }

   /* public static String GetCountryZipCode(Context mContext) {
        String CountryID;
        String CountryZipCode = "";
        TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = mContext.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }

    public static String getAddress(Context context, double latitude, double longitude) {
        String result = "";
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result = address.getCountryName();
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        return result;
    }
*/

    /*--------------------------------------------------------------------------------------------*/

   /* public static void customDialogBoxAuthenticating(Context mContext, Boolean isShow) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext, R.style.MyAlertDialogStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        if (isShow) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }*/

   /* public static void customDialog(Context mContext, Boolean isShow, String message) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext, R.style.MyAlertDialogStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);

        if (isShow) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
            progressDialog.hide();
        }
    }
*/
   /* public static void customDialog(Context mContext, String title, String message, String buttonText, LayoutInflater inflater, ButtonAction buttonAction) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        View dialogView = inflater.inflate(R.layout.dialog_custom, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        MaterialTextView tvHeader = dialogView.findViewById(R.id.tvHeader);
        MaterialTextView tvMessage = dialogView.findViewById(R.id.tvMessage);
        MaterialTextView btnOk = dialogView.findViewById(R.id.btnOk);
        MaterialTextView btnCancel = dialogView.findViewById(R.id.btnCancel);

        tvHeader.setText(title);
        tvMessage.setText(message);
        btnOk.setText(buttonText);

        btnCancel.setOnClickListener(view -> alertDialog.dismiss());

        btnOk.setOnClickListener(view -> {
            alertDialog.dismiss();
            buttonAction.buttonClicked();
        });

        alertDialog.show();
    }
*/
    public static void customDialogBoxTextWithSingle(Context mContext, String title, String message) {
        new MaterialAlertDialogBuilder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    @SuppressLint("DefaultLocale")
    public static String withSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp-1));
    }


}

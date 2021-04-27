package com.example.pssmobile.ui.login.writer.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class GeoCodeLocation {
    private static final String TAG = "GeoCodeLocation";
    public static void getAddressFromLocation(final String
                                                      locationAddress,
                                              final Context
                                                      context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context,
                        Locale.getDefault());
                String result = null;
                String latitude = null;
                String longitude = null;
                String postCode = null;
                try {
                    List addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = (Address)
                                addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        sb.append(address.getLatitude()).append("\n");
                        sb.append(address.getLongitude()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        latitude = String.valueOf(address.getLatitude());
                        longitude =String.valueOf(address.getLongitude());
                        postCode = String.valueOf(address.getPostalCode());
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address: " + locationAddress + "\n\nLatitude and Longitude:\n" + result;
                        bundle.putString("address", result);
                        bundle.putString("latitude",latitude);
                        bundle.putString("longitude",longitude);
                        bundle.putString("postCode",postCode);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address: " + locationAddress +
                                "\n Unable to get Latitude and Longitude for this address location.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}

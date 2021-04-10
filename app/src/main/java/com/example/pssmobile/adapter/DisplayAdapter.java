package com.example.pssmobile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.pssmobile.R;

import java.util.ArrayList;

/* renamed from: au.com.perthsecurityservices.patrolscanner.Database.DisplayAdapter */
public class DisplayAdapter extends BaseAdapter {
    private ArrayList<String> checkpoin_id;
    private ArrayList<String> date_id;

    /* renamed from: id */
    private ArrayList<String> f38id;
    private Context mContext;

    /* renamed from: au.com.perthsecurityservices.patrolscanner.Database.DisplayAdapter$Holder */
    public class Holder {
        TextView DateID;
        TextView checkpointID;

        public Holder() {
        }
    }

    public DisplayAdapter(Context c, ArrayList<String> id, ArrayList<String> chck_id, ArrayList<String> d_id) {
        this.mContext = c;
        this.f38id = id;
        this.checkpoin_id = chck_id;
        this.date_id = d_id;
    }

    public int getCount() {
        return this.f38id.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("WrongConstant")
    public View getView(int pos, View child, ViewGroup parent) {
        Holder mHolder;
        if (child == null) {
            child = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.list_cell, null);
            mHolder = new Holder();
            mHolder.checkpointID = (TextView) child.findViewById(R.id.chckID);
            mHolder.DateID = (TextView) child.findViewById(R.id.DateTime);
            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }
        Holder mHolder2 = mHolder;
        mHolder2.checkpointID.setText((CharSequence) this.checkpoin_id.get(pos));
        mHolder2.DateID.setText((CharSequence) this.date_id.get(pos));
        return child;
    }
}

package com.example.pssmobile.ui.login.writer.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pssmobile.R;
import com.example.pssmobile.data.model.Getters;
import com.example.pssmobile.ui.login.writer.NFCWriter;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements Filterable {
    /* access modifiers changed from: private */
    public ArrayList<Getters> mArrayList;
    /* access modifiers changed from: private */
    public ArrayList<Getters> mFilteredList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public TextView tv_ID;
        /* access modifiers changed from: private */
        public TextView tv_address;
        /* access modifiers changed from: private */
        public TextView tv_bureau;
        /* access modifiers changed from: private */
        public TextView tv_description;
        /* access modifiers changed from: private */
        public TextView tv_name;

        public ViewHolder(View view) {
            super(view);
            this.tv_ID = (TextView) view.findViewById(R.id.tv_ID);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
            this.tv_address = (TextView) view.findViewById(R.id.tv_address);
            this.tv_bureau = (TextView) view.findViewById(R.id.tv_bureau);
            this.tv_description = (TextView) view.findViewById(R.id.tv_description);
        }
    }

    public ListAdapter(ArrayList<Getters> arrayList) {
        this.mArrayList = arrayList;
        this.mFilteredList = arrayList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.tv_name.setText(((Getters) this.mFilteredList.get(i)).getCheckpoint_Name());
        viewHolder.tv_ID.setText(((Getters) this.mFilteredList.get(i)).getID());
        viewHolder.tv_address.setText(((Getters) this.mFilteredList.get(i)).getAddress());
        viewHolder.tv_bureau.setText(((Getters) this.mFilteredList.get(i)).getBureau());
        if (((Getters) this.mFilteredList.get(i)).getCheckpoint_description().length() > 0) {
        viewHolder.tv_description.setText(((Getters) this.mFilteredList.get(i)).getCheckpoint_description());
    } else {
        viewHolder.tv_description.setText("None");
    }
        viewHolder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NFCWriter.class);
                intent.putExtra("getIDs", ((Getters) ListAdapter.this.mFilteredList.get(i)).getID());
                intent.putExtra("getCheckpoint_Name", ((Getters) ListAdapter.this.mFilteredList.get(i)).getCheckpoint_Name());
                intent.putExtra("getAddress", ((Getters) ListAdapter.this.mFilteredList.get(i)).getAddress());
                intent.putExtra("getBureau", ((Getters) ListAdapter.this.mFilteredList.get(i)).getBureau());
                intent.putExtra("getCheckpoint_description", ((Getters) ListAdapter.this.mFilteredList.get(i)).getCheckpoint_description());
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.mFilteredList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            /* access modifiers changed from: protected */
            public FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    ListAdapter.this.mFilteredList = ListAdapter.this.mArrayList;
                } else {
                    ArrayList<Getters> filteredList = new ArrayList<>();
                    Iterator it = ListAdapter.this.mArrayList.iterator();
                    while (it.hasNext()) {
                        Getters androidVersion = (Getters) it.next();
                        if (androidVersion.getCheckpoint_Name().toLowerCase().contains(charString) || androidVersion.getAddress().toLowerCase().contains(charString) || androidVersion.getBureau().toLowerCase().contains(charString)) {
                            filteredList.add(androidVersion);
                        }
                    }
                    ListAdapter.this.mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = ListAdapter.this.mFilteredList;
                return filterResults;
            }

            /* access modifiers changed from: protected */
            public void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ListAdapter.this.mFilteredList = (ArrayList) filterResults.values;
                ListAdapter.this.notifyDataSetChanged();
            }
        };
    }
}

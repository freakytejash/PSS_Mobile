package com.example.pssmobile.ui.login.writer;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pssmobile.R;
import com.example.pssmobile.data.model.AddSiteRequest;
import com.example.pssmobile.databinding.ItemFilePathUploadBinding;


import java.util.ArrayList;
import java.util.List;

public class AddFileDetailAdapter extends RecyclerView.Adapter<AddFileDetailAdapter.MyViewHolder> {

    ArrayList<AddSiteRequest.DocumentsAttached> attachedList;
    private final Context ctx;
    private OnItemClickListener onItemClickListener;

    public AddFileDetailAdapter(Context ctx, ArrayList<AddSiteRequest.DocumentsAttached> attachedList,
                                OnItemClickListener onItemClickListener) {
        this.ctx = ctx;
        this.attachedList = attachedList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

  /*      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_path_upload, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;*/

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFilePathUploadBinding filePathBinding = ItemFilePathUploadBinding.inflate(layoutInflater, parent, false);
        return new MyViewHolder(filePathBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(attachedList.get(position), ctx, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return attachedList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemFilePathUploadBinding binding;
       // public TextView filePath;
       public MyViewHolder(ItemFilePathUploadBinding itemFilePathUploadBinding) {
           super(itemFilePathUploadBinding.getRoot());
           this.binding = itemFilePathUploadBinding;
       }
        public void bind(AddSiteRequest.DocumentsAttached attachmentList, Context mContext, OnItemClickListener onItemClickListener) {

            binding.tvFilePath.setText( attachmentList.fileName);
            binding.etFileNameItem.setText(attachmentList.documentName);
            if (!attachmentList.documentName.equalsIgnoreCase("")){
                binding.etFileNameItem.setEnabled(false);
                binding.etFileNameItem.setFocusable(false);
                binding.btnAddDocName.setEnabled(false);
            }
            binding.etFileNameItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onEditTextClickListener(getAdapterPosition());
                }
            });

            binding.tvFilePath.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    onItemClickListener.onDeletePositionListener(getAdapterPosition());


                }
            });

            binding.btnAddDocName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String docName =binding.etFileNameItem.getText().toString().trim();
                    onItemClickListener.onAddButtonClickListener(getAdapterPosition(), docName);
                    binding.etFileNameItem.setEnabled(false);
                    binding.etFileNameItem.setFocusable(false);
                    binding.btnAddDocName.setEnabled(false);
                }
            });


        }
    }
    public interface OnItemClickListener {
        void onEditTextClickListener(int position);

        void onDeletePositionListener(int position);

        void onAddButtonClickListener(int position, String docName);
    }

}
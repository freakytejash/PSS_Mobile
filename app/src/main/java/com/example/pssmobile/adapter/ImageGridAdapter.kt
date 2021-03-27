package com.example.pssmobile.adapter

import com.example.pssmobile.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File


class ImageGridAdapter(val context: Context, imageList: ArrayList<File>) : RecyclerView.Adapter<ImageGridAdapter.ListViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var mImageList: ArrayList<File> = imageList

    fun updateImageList(updatedList: ArrayList<File>){
        mImageList.clear()
        mImageList.addAll(updatedList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mImageList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(inflater.inflate(R.layout.selected_image_item, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
       val image = mImageList[position]
        Glide.with(context.applicationContext).load(image).apply(
            RequestOptions().centerCrop()
                .placeholder(R.drawable.ic_photo)
        ).into(holder.iv_selectedImage)
    }

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_selectedImage:ImageView = view.findViewById(R.id.iv_itemselectedImage)
    }
}
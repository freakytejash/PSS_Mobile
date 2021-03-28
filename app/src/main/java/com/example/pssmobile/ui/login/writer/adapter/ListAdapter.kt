package com.example.pssmobile.ui.login.writer.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pssmobile.R
import com.example.pssmobile.data.model.Getters
import com.example.pssmobile.ui.login.writer.NFCWriter
import java.util.*

class ListAdapter(arrayList: ArrayList<Getters>) : RecyclerView.Adapter<ListAdapter.ViewHolder>(), Filterable {
    /* access modifiers changed from: private */
    var mArrayList: ArrayList<Getters>

    /* access modifiers changed from: private */
    var mFilteredList: ArrayList<Getters>

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /* access modifiers changed from: private */
        var tv_ID: TextView

        /* access modifiers changed from: private */
        var tv_address: TextView

        /* access modifiers changed from: private */
        var tv_bureau: TextView

        /* access modifiers changed from: private */
        var tv_description: TextView

        /* access modifiers changed from: private */
        var tv_name: TextView

        init {
            tv_ID = view.findViewById<View>(R.id.tv_ID) as TextView
            tv_name = view.findViewById<View>(R.id.tv_name) as TextView
            tv_address = view.findViewById<View>(R.id.tv_address) as TextView
            tv_bureau = view.findViewById<View>(R.id.tv_bureau) as TextView
            tv_description = view.findViewById<View>(R.id.tv_description) as TextView
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.card_row, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.tv_name.setText((mFilteredList[i] as Getters).checkpoint_Name)
        viewHolder.tv_ID.setText((mFilteredList[i] as Getters).checkpoint_ID)
        viewHolder.tv_address.setText((mFilteredList[i] as Getters).address)
        viewHolder.tv_bureau.setText((mFilteredList[i] as Getters).bureau)
        if ((mFilteredList[i] as Getters).checkpoint_description!!.length> 0) {
            viewHolder.tv_description.setText((mFilteredList[i] as Getters).checkpoint_description)
        } else {
            viewHolder.tv_description.text = "None"
        }
        viewHolder.itemView.setOnClickListener { v ->
            val intent = Intent(v.context, NFCWriter::class.java)
            intent.putExtra("getIDs", (mFilteredList[i] as Getters).iD)
            intent.putExtra("getCheckpoint_Name", (mFilteredList[i] as Getters).checkpoint_Name)
            intent.putExtra("getAddress", (mFilteredList[i] as Getters).address)
            intent.putExtra("getBureau", (mFilteredList[i] as Getters).bureau)
            intent.putExtra("getCheckpoint_description", (mFilteredList[i] as Getters).checkpoint_description)
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mFilteredList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            /* access modifiers changed from: protected */
            public override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    mFilteredList = mArrayList
                } else {
                    val filteredList: ArrayList<Getters> = ArrayList<Getters>()
                    val it: Iterator<*> = mArrayList.iterator()
                    while (it.hasNext()) {
                        val androidVersion: Getters = it.next() as Getters
                        if (androidVersion.checkpoint_Name?.toLowerCase()?.contains(charString)!! || androidVersion.address?.toLowerCase()?.contains(charString)!! || androidVersion.bureau!!.toLowerCase().contains(charString)) {
                            filteredList.add(androidVersion)
                        }
                    }
                    mFilteredList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = mFilteredList
                return filterResults
            }

            /* access modifiers changed from: protected */
            public override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mFilteredList = filterResults.values as ArrayList<Getters>
                notifyDataSetChanged()
            }
        }
    }

    init {
        mArrayList = arrayList
        mFilteredList = arrayList
    }
}

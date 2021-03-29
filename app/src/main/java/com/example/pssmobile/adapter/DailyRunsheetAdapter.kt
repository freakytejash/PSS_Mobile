package com.example.pssmobile.adapter

import Data
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.pssmobile.R
import com.example.pssmobile.ui.login.reader.PatrolRunsheetFragmentDirections
import java.util.ArrayList

class DailyRunsheetAdapter(
        val context: Context,
        val dataList: ArrayList<Data>,
) :
        RecyclerView.Adapter<DailyRunsheetAdapter.ListViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(inflater.inflate(R.layout.item_dailyrunsheet, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val model = dataList[position]
        holder.bind(model)
        holder.itemLayout.setOnClickListener {
            val action:NavDirections = PatrolRunsheetFragmentDirections.actionPatrolRunsheetFragmentToPatrolRunsheetDetailsFragment(model)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
       return dataList.size
    }

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemLayout: ConstraintLayout = view.findViewById(R.id.item_dailyRunsheet)
        val tv_Location: TextView = view.findViewById(R.id.tv_location)
        val tv_jobType: TextView = view.findViewById(R.id.tv_jobType)
        val tv_jobDate: TextView = view.findViewById(R.id.tv_jobDate)

        fun bind(data: Data){
            tv_Location.text = data.location1
            tv_jobType.text = data.job_type
            tv_jobDate.text = data.job_date
        }
    }
}
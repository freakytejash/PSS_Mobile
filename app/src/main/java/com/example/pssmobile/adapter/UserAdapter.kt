package com.example.pssmobile.adapter

import UsersDetails
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

class UserAdapter(
    val context: Context,
    val dataList: ArrayList<UsersDetails>,
) :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(inflater.inflate(R.layout.list_users, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val model = dataList[position]
        holder.bind(model)
        holder.itemLayout.setOnClickListener {
           /* val action: NavDirections = PatrolRunsheetFragmentDirections.actionPatrolRunsheetFragmentToPatrolRunsheetDetailsFragment(model)
            Navigation.findNavController(it).navigate(action)*/
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemLayout: ConstraintLayout = view.findViewById(R.id.list_user_id)
        val user_name: TextView = view.findViewById(R.id.user_name)
        val phone_no: TextView = view.findViewById(R.id.phone_no)
        val zoho_id: TextView = view.findViewById(R.id.zoho_id)

        fun bind(user: UsersDetails){
            user_name.text = user.name
            //phone_no.text = user.phone
            zoho_id.text = user.zohoCreatorUserName
        }
    }
}
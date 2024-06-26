package com.example.srwa.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.srwa.Model.Jackets
import com.example.srwa.R
import com.example.srwa.activity.EditJacketsActivity
import com.google.firebase.database.FirebaseDatabase

class JacketsAdapter(private val jacketList: ArrayList<Jackets>, private val context: Context) : RecyclerView.Adapter<JacketsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.textViewId)
        val type: TextView = itemView.findViewById(R.id.textViewType)
        val color: TextView = itemView.findViewById(R.id.textViewColor)
        val quantity: TextView = itemView.findViewById(R.id.textViewQuantity)
        val editButton: Button = itemView.findViewById(R.id.buttonEdit)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.jackets_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jacket = jacketList[position]
        holder.id.text = jacket.id
        holder.type.text = jacket.type
        holder.color.text = jacket.color
        holder.quantity.text = jacket.qty.toString()

        holder.deleteButton.setOnClickListener {
            deleteJacket(jacket)
        }

        holder.editButton.setOnClickListener {
            val intent = Intent(context, EditJacketsActivity::class.java).apply {
                putExtra("jacketId", jacket.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return jacketList.size
    }

    private fun deleteJacket(jacket: Jackets) {
        val database = FirebaseDatabase.getInstance().reference.child("jackets").child(jacket.id)
        database.removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Jacket deleted successfully", Toast.LENGTH_SHORT).show()
                jacketList.remove(jacket)
                notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to delete jacket", Toast.LENGTH_SHORT).show()
            }
    }
}

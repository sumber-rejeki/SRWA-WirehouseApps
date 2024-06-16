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
import com.example.srwa.Model.Yarn
import com.example.srwa.R
import com.example.srwa.activity.EditYarnActivity
import com.google.firebase.database.FirebaseDatabase

class YarnAdapter(private val yarntList: ArrayList<Yarn>, private val context: Context) : RecyclerView.Adapter<YarnAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.textViewId)
        val type: TextView = itemView.findViewById(R.id.textViewType)
        val color: TextView = itemView.findViewById(R.id.textViewColor)
        val quantity: TextView = itemView.findViewById(R.id.textViewQuantity)
        val editButton: Button = itemView.findViewById(R.id.buttonEdit)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.yarn_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val yarnfragment = yarntList[position]
        holder.id.text = yarnfragment.id
        holder.type.text = yarnfragment.type
        holder.color.text = yarnfragment.color
        holder.quantity.text = yarnfragment.qty.toString()

        holder.deleteButton.setOnClickListener {
            deleteYarnFragment(yarnfragment)
        }

        holder.editButton.setOnClickListener {
            val intent = Intent(context, EditYarnActivity::class.java).apply {
                putExtra("yarnfragmentId", yarnfragment.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return yarntList.size
    }

    private fun deleteYarnFragment(yarn: Yarn) {
        val database = FirebaseDatabase.getInstance().reference.child("yarnfragments").child(yarn.id)
        database.removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "YarnFragment deleted successfully", Toast.LENGTH_SHORT).show()
                yarntList.remove(yarn)
                notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to delete YarnFragment", Toast.LENGTH_SHORT).show()
            }
    }
}

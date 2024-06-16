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
import com.example.srwa.Model.Chino
import com.example.srwa.R
import com.example.srwa.activity.EditChinoActivity
import com.google.firebase.database.FirebaseDatabase

class ChinoAdapter(private val chinoList: ArrayList<Chino>, private val context: Context) : RecyclerView.Adapter<ChinoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.textViewId)
        val type: TextView = itemView.findViewById(R.id.textViewType)
        val color: TextView = itemView.findViewById(R.id.textViewColor)
        val quantity: TextView = itemView.findViewById(R.id.textViewQuantity)
        val editButton: Button = itemView.findViewById(R.id.buttonEdit)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chino_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chino = chinoList[position]
        holder.id.text = chino.id
        holder.type.text = chino.type
        holder.color.text = chino.color
        holder.quantity.text = chino.qty.toString()

        holder.deleteButton.setOnClickListener {
            deleteChino(chino)
        }

        holder.editButton.setOnClickListener {
            val intent = Intent(context, EditChinoActivity::class.java).apply {
                putExtra("chinoId", chino.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return chinoList.size
    }

    private fun deleteChino(chino: Chino) {
        val database = FirebaseDatabase.getInstance().reference.child("chino").child(chino.id)
        database.removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Chino deleted successfully", Toast.LENGTH_SHORT).show()
                chinoList.remove(chino)
                notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to delete chino", Toast.LENGTH_SHORT).show()
            }
    }
}

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
import com.example.srwa.Model.Chemical
import com.example.srwa.R
import com.example.srwa.activity.EditChemicalActivity
import com.google.firebase.database.FirebaseDatabase

class ChemicalAdapter(private val chemicalList: ArrayList<Chemical>, private val context: Context) : RecyclerView.Adapter<ChemicalAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.textViewId)
        val type: TextView = itemView.findViewById(R.id.textViewType)
        val color: TextView = itemView.findViewById(R.id.textViewColor)
        val quantity: TextView = itemView.findViewById(R.id.textViewQuantity)
        val editButton: Button = itemView.findViewById(R.id.buttonEdit)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chemical_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chemical = chemicalList[position]
        holder.id.text = chemical.id
        holder.type.text = chemical.type
        holder.color.text = chemical.color
        holder.quantity.text = chemical.qty.toString()

        holder.deleteButton.setOnClickListener {
            deleteChemical(chemical)
        }

        holder.editButton.setOnClickListener {
            val intent = Intent(context, EditChemicalActivity::class.java).apply {
                putExtra("chemicalId", chemical.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return chemicalList.size
    }

    private fun deleteChemical(chemical: Chemical) {
        val database = FirebaseDatabase.getInstance().reference.child("chemicals").child(chemical.id)
        database.removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Chemical deleted successfully", Toast.LENGTH_SHORT).show()
                chemicalList.remove(chemical)
                notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to delete chemical", Toast.LENGTH_SHORT).show()
            }
    }
}

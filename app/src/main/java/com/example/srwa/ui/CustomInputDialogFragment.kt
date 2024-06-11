package com.example.srwa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.srwa.R
import com.example.srwa.Model.Fabric
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.android.material.textfield.TextInputEditText

class CustomInputDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_QR_DATA = "qr_data"

        fun newInstance(qrData: String): CustomInputDialogFragment {
            val fragment = CustomInputDialogFragment()
            val args = Bundle()
            args.putString(ARG_QR_DATA, qrData)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.custom_input_box, container, false)

        val qrData = arguments?.getString(ARG_QR_DATA)
        qrData?.let { fetchDataAndPopulateUI(it, view) }

        // Existing code for autoCompleteTextView setup
        val items = listOf("Input", "Production", "Output")
        val autoComplete: AutoCompleteTextView = view.findViewById(R.id.auto_complete)
        val adapter = ArrayAdapter(requireContext(), R.layout.item_list_items, items)
        autoComplete.setAdapter(adapter)
        autoComplete.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val itemSelected = adapterView.getItemAtPosition(i)
            Toast.makeText(requireContext(), "Item: $itemSelected", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun fetchDataAndPopulateUI(qrData: String, view: View) {
        val database = FirebaseDatabase.getInstance().reference.child("fabrics").child(qrData)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fabric = snapshot.getValue(Fabric::class.java)
                fabric?.let {
                    val idEditText: TextInputEditText = view.findViewById(R.id.id_item_qr)
                    val typeEditText: TextInputEditText = view.findViewById(R.id.type_qr)
                    val colorEditText: TextInputEditText = view.findViewById(R.id.color_qr)
                    val qtyEditText: TextInputEditText = view.findViewById(R.id.qty_qr)

                    idEditText.setText(it.id)
                    typeEditText.setText(it.type)
                    colorEditText.setText(it.color)
                    qtyEditText.setText(it.qty.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setView(onCreateView(layoutInflater, null, savedInstanceState))
            .setPositiveButton("Save") { dialog, id ->
                // Handle the save action
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.dismiss()
            }
            .create()
    }
}

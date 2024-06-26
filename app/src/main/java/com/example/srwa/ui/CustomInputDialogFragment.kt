package com.example.srwa.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.srwa.Model.Fabric
import com.example.srwa.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CustomInputDialogFragment : DialogFragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var selectedCategory: String
    private var qrData: String? = null

    companion object {
        private const val ARG_QR_DATA = "qr_data"
        private const val TAG = "CustomInputDialogFragment"

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

        qrData = arguments?.getString(ARG_QR_DATA)
        Log.d(TAG, "QR Data: $qrData")
        qrData?.let { fetchDataAndPopulateUI(it, view) }

        // Existing code for autoCompleteTextView setup
        val items = listOf("Input", "Production", "Output")
        val autoComplete: AutoCompleteTextView = view.findViewById(R.id.auto_complete)
        val adapter = ArrayAdapter(requireContext(), R.layout.item_list_items, items)
        autoComplete.setAdapter(adapter)

        // Set up the category list
        val categoryAutoComplete: AutoCompleteTextView = view.findViewById(R.id.categories)
        autoComplete.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val itemSelected = adapterView.getItemAtPosition(position).toString()
            Toast.makeText(requireContext(), "Item: $itemSelected", Toast.LENGTH_SHORT).show()
            updateCategoryList(itemSelected, categoryAutoComplete)
        }

        // Set up the category selection
        categoryAutoComplete.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            selectedCategory = adapterView.getItemAtPosition(position).toString()
            Toast.makeText(requireContext(), "Category: $selectedCategory", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun updateCategoryList(selectedItem: String, categoryAutoComplete: AutoCompleteTextView) {
        val categories = when (selectedItem) {
            "Input" -> listOf("Fabric", "Yarn", "Chino", "Chemicals")
            "Production" -> listOf("Jackets", "Shirts", "Pants", "Bags")
            "Output" -> listOf("Shipping", "Product", "Materials")
            else -> emptyList()
        }

        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.item_list_items, categories)
        categoryAutoComplete.setAdapter(categoryAdapter)
        categoryAutoComplete.setText("")  // Clear previous selection
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
                Log.e(TAG, "Failed to load data: ${error.message}")
            }
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setView(onCreateView(layoutInflater, null, savedInstanceState))
            .setPositiveButton("Save") { dialog, id ->
                saveDataToFirebase()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.dismiss()
            }
            .create()
    }

    private fun saveDataToFirebase() {
        // Get values from UI
        val idEditText: TextInputEditText? = view?.findViewById(R.id.id_item_qr)
        val typeEditText: TextInputEditText? = view?.findViewById(R.id.type_qr)
        val colorEditText: TextInputEditText? = view?.findViewById(R.id.color_qr)
        val qtyEditText: TextInputEditText? = view?.findViewById(R.id.qty_qr)

        val idValue = idEditText?.text.toString()
        val typeValue = typeEditText?.text.toString()
        val colorValue = colorEditText?.text.toString()
        val qtyValue = qtyEditText?.text.toString().toIntOrNull() ?: 0

        if (selectedCategory.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT).show()
            return
        }

        // Update database reference based on selected category
        databaseReference = FirebaseDatabase.getInstance().reference.child(selectedCategory.toLowerCase())
        Log.d(TAG, "Saving data to category: $selectedCategory")

        // Save data to Firebase
        val fabricData = Fabric(idValue, typeValue, colorValue, qtyValue)
        databaseReference.child(idValue).setValue(fabricData)
            .addOnSuccessListener {
                // Check if fragment is still attached before showing Toast or deleting original data
                if (isAdded) {
                    Toast.makeText(requireContext(), "Data saved successfully", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Data saved successfully to $selectedCategory")
                    deleteOriginalData()
                }
            }
            .addOnFailureListener {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Failed to save data", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Failed to save data: ${it.message}")
                }
            }
    }

    private fun deleteOriginalData() {
        qrData?.let { qrCode ->
            Log.d(TAG, "Attempting to delete original data with QR Code: $qrCode")
            val originalRef = FirebaseDatabase.getInstance().reference.child("fabrics").child(qrCode)
            originalRef.removeValue()
                .addOnSuccessListener {
                    if (isAdded) {
                        Toast.makeText(requireContext(), "Original data deleted successfully", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Original data deleted successfully with QR Code: $qrCode")
                    }
                }
                .addOnFailureListener { error ->
                    if (isAdded) {
                        Toast.makeText(requireContext(), "Failed to delete original data", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Failed to delete original data: ${error.message}")
                    }
                }
        } ?: run {
            if (isAdded) {
                Toast.makeText(requireContext(), "No original data found to delete", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "No QR Code found to delete original data")
            }
        }
    }
}

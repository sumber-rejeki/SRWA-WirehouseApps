package com.example.srwa.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.srwa.Adapter.ChemicalAdapter
import com.example.srwa.Model.Chemical
import com.example.srwa.activity.AddChemicalActivity
import com.example.srwa.databinding.FragmentChemicalBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChemicalFragment : Fragment() {

    private lateinit var binding: FragmentChemicalBinding
    private lateinit var chemicalList: ArrayList<Chemical>
    private lateinit var chemicalAdapter: ChemicalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChemicalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chemicalList = arrayListOf()
        chemicalAdapter = ChemicalAdapter(chemicalList, requireContext()) // Menggunakan requireContext()
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = chemicalAdapter

        binding.buttonAdd.setOnClickListener {
            startActivity(Intent(requireContext(), AddChemicalActivity::class.java))
        }

        loadItemsFromDatabase()
    }

    private fun loadItemsFromDatabase() {
        val database = FirebaseDatabase.getInstance().reference.child("chemicals")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chemicalList.clear()
                for (dataSnapshot in snapshot.children) {
                    val chemical = dataSnapshot.getValue(Chemical::class.java)
                    chemical?.let {
                        chemicalList.add(it)
                    }
                }
                chemicalAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

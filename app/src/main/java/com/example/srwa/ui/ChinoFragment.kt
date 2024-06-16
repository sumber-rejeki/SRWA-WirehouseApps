package com.example.srwa.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.srwa.Adapter.ChinoAdapter
import com.example.srwa.Model.Chino
import com.example.srwa.activity.AddChinoActivity
import com.example.srwa.databinding.FragmentChinoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChinoFragment : Fragment() {

    private lateinit var binding: FragmentChinoBinding
    private lateinit var chinoList: ArrayList<Chino>
    private lateinit var chinoAdapter: ChinoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChinoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chinoList = arrayListOf()
        chinoAdapter = ChinoAdapter(chinoList, requireContext()) // Menggunakan requireContext()
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = chinoAdapter

        binding.buttonAdd.setOnClickListener {
            startActivity(Intent(requireContext(), AddChinoActivity::class.java))
        }

        loadItemsFromDatabase()
    }

    private fun loadItemsFromDatabase() {
        val database = FirebaseDatabase.getInstance().reference.child("chinos")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chinoList.clear()
                for (dataSnapshot in snapshot.children) {
                    val chino = dataSnapshot.getValue(Chino::class.java)
                    chino?.let {
                        chinoList.add(it)
                    }
                }
                chinoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

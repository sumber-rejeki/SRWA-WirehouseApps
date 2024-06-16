package com.example.srwa.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.srwa.Adapter.YarnAdapter
import com.example.srwa.Model.Yarn
import com.example.srwa.activity.AddYarnActivity
import com.example.srwa.databinding.FragmentYarnBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class YarnFragment : Fragment() {

    private lateinit var binding: FragmentYarnBinding
    private lateinit var yarnList: ArrayList<Yarn>
    private lateinit var yarnAdapter: YarnAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYarnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        yarnList = arrayListOf()
        yarnAdapter = YarnAdapter(yarnList, requireContext()) // Menggunakan requireContext()
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = yarnAdapter

        binding.buttonAdd.setOnClickListener {
            startActivity(Intent(requireContext(), AddYarnActivity::class.java))
        }

        loadItemsFromDatabase()
    }

    private fun loadItemsFromDatabase() {
        val database = FirebaseDatabase.getInstance().reference.child("yarns")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                yarnList.clear()
                for (dataSnapshot in snapshot.children) {
                    val yarn = dataSnapshot.getValue(Yarn::class.java)
                    yarn?.let {
                        yarnList.add(it)
                    }
                }
                yarnAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
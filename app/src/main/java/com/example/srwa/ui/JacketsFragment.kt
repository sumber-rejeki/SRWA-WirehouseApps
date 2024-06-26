package com.example.srwa.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.srwa.Adapter.JacketsAdapter
import com.example.srwa.Model.Jackets
import com.example.srwa.activity.AddJacketsActivity
import com.example.srwa.databinding.FragmentJacketsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class JacketsFragment : Fragment() {

    private lateinit var binding: FragmentJacketsBinding
    private lateinit var jacketList: ArrayList<Jackets>
    private lateinit var jacketsAdapter: JacketsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJacketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jacketList = arrayListOf()
        jacketsAdapter = JacketsAdapter(jacketList, requireContext())
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = jacketsAdapter

        binding.buttonAdd.setOnClickListener {
            startActivity(Intent(requireContext(), AddJacketsActivity::class.java))
        }

        loadItemsFromDatabase()
    }

    private fun loadItemsFromDatabase() {
        val database = FirebaseDatabase.getInstance().reference.child("jackets")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                jacketList.clear()
                for (dataSnapshot in snapshot.children) {
                    val jacket = dataSnapshot.getValue(Jackets::class.java)
                    jacket?.let {
                        jacketList.add(it)
                    }
                }
                jacketsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

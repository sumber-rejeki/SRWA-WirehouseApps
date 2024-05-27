package com.example.srwa.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.srwa.Adapter.Item
import com.example.srwa.Adapter.TableAdapter
import com.example.srwa.R


class JacketsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_jackets, container, false)

        // Inisialisasi RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TableAdapter(generateDummyMaterials())

        return view
    }

    private fun generateDummyMaterials(): List<Item> {
        return listOf(
            Item("J01", "Hoodie", "Black", 24),
            Item("J02", "Hoodie", "Black", 24),
            Item("J03", "Hoodie", "Black", 24),
            Item("J04", "Hoodie", "Indigo Blue", 20),
            Item("J05", "Hoodie", "Indigo Blue", 25),
            Item("J06", "Hoodie", "Indigo Blue", 30),
            Item("J07", "Denim", "Black", 24),
            Item("J08", "Denim", "Black", 26),
            Item("J09", "Denim", "Black", 40),
            Item("J10", "Denim", "Light Blue", 23),
            Item("J11", "Denim", "Light Blue", 54),
            Item("J12", "Denim", "Light Blue", 36)
        )
    }
}
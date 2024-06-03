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


class PantsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pants, container, false)

        // Inisialisasi RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view2)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TableAdapter(generateDummyMaterials())

        return view
    }

    private fun generateDummyMaterials(): List<Item> {
        return listOf(
            Item("C01", "Chinos", "Black", 24),
            Item("C02", "Chinos", "Grey", 24),
            Item("C03", "Cargo", "Black", 24),
            Item("C04", "Cargo", "Indigo Blue", 20),
            Item("C05", "Cargo", "Blue", 25),
            Item("C06", "Cargo", "Balck", 30),
            Item("C07", "Jeans", "Black", 24),
            Item("C08", "Jeans", "Navy", 26),
            Item("C09", "Jeans", "Light Blue", 40),
            Item("C10", "Jeans", "Grey", 23),
            Item("C11", "Cargo", "Navy", 54),
            Item("C12", "Cargo", "Green", 36)
        )
    }
}
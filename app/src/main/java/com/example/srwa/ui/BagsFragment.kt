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


class BagsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bags, container, false)

        // Inisialisasi RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TableAdapter(generateDummyMaterials())

        return view
    }

    private fun generateDummyMaterials(): List<Item> {
        return listOf(
            Item("B01", "Handbag", "Black", 24),
            Item("B02", "Handbag", "Grey", 24),
            Item("B03", "BackPack", "Black", 24),
            Item("B04", "Handbag", "Indigo Blue", 20),
            Item("B05", "Tote Bag", "Blue", 25),
            Item("B06", "Tote Bag", "Black", 30),
            Item("B07", "Waist Bag", "Black", 24),
            Item("B08", "Waist Bag", "Navy", 26),
            Item("B09", "Waist Bag", "Light Blue", 40),
            Item("B10", "Waist Bag", "Grey", 23),
            Item("B11", "Shoulder Bag", "Navy", 54),
            Item("B12", "Shoulder Bag", "Green", 36)
        )
    }
}
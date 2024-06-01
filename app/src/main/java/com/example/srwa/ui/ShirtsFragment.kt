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


class ShirtsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shirts, container, false)

        // Inisialisasi RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TableAdapter(generateDummyMaterials())

        return view
    }

    private fun generateDummyMaterials(): List<Item> {
        return listOf(
            Item("S01", "Kaos", "Black", 24),
            Item("S02", "Kaos", "Grey", 24),
            Item("S03", "Kaos", "Black", 24),
            Item("S04", "Kemeja", "Indigo Blue", 20),
            Item("S05", "Kemeja", "Blue", 25),
            Item("S06", "Kemeja", "Balck", 30),
            Item("S07", "Jeans", "Black", 24),
            Item("S08", "Jeans", "Navy", 26),
            Item("S09", "Jeans", "Light Blue", 40),
            Item("S10", "Jeans", "Grey", 23),
            Item("S11", "Jas", "Navy", 54),
            Item("S12", "Jas", "Green", 36)
        )
    }
}
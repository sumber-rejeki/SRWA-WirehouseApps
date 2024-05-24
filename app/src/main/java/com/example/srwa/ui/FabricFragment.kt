package com.example.srwa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.srwa.R
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.srwa.Adapter.Material
import com.example.srwa.Adapter.MaterialAdapter



class FabricFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fabric, container, false)

        // Inisialisasi RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MaterialAdapter(generateDummyMaterials())

        return view
    }

    private fun generateDummyMaterials(): List<Material> {
        return listOf(
            Material("F01", "Cotton", "White", 20),
            Material("F02", "Cotton", "Beige", 25),
            Material("F03", "Cotton", "Navy", 30),
            Material("F04", "Cotton", "Black", 35),
            Material("F05", "Denim", "Indigo Blue", 40),
            Material("F06", "Denim", "Black", 40),
            Material("F07", "Denim", "Light Blue", 45),
            Material("F08", "Polyester", "Dark Green", 50),
            Material("F09", "Polyester", "Black", 55),
            Material("F10", "Polyester", "Grey", 60),
            Material("F11", "Polyester", "Blue", 65),
            Material("F12", "Polyester", "White", 70)
        )
    }
}
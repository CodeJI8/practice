package com.example.practice.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.practice.R
import com.example.practice.databinding.FragmentAddProductBinding


class Add_productFragment : Fragment() {

private lateinit var binding: FragmentAddProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(layoutInflater)


        binding.floatingActionButton2.setOnClickListener {
            findNavController().navigate(R.id.action_add_productFragment_to_product_Fragment)

        }




        return binding.root
    }


}
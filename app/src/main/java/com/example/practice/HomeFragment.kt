package com.example.practice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.practice.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

binding.Slider.setOnClickListener {
    findNavController().navigate(R.id.action_homeFragment_to_slider_Fragment)

}
        binding.addProductFragment.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_add_productFragment)

        }


        binding.Category.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)

        }
        return binding.root

    }


    }

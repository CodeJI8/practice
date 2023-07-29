package com.example.practice.adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practice.R
import com.example.practice.databinding.ImageItemBinding
import com.example.practice.model.model_product

class adapter_addproduct(val list :ArrayList<Uri>):RecyclerView.Adapter<adapter_addproduct.AddViewholder>() {

    inner class AddViewholder(val binding:ImageItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewholder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,  false)
        return AddViewholder(binding)
    }

    override fun getItemCount()= list.size

    override fun onBindViewHolder(holder: AddViewholder, position: Int) {
       holder.binding.addImageView.setImageURI(list[position])
    }

}





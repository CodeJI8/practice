package com.example.practice.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.practice.R
import com.example.practice.databinding.FragmentSliderBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import com.google.firebase.storage.FirebaseStorage
import java.net.URL
import java.util.UUID


class slider_Fragment : Fragment() {
    lateinit var binding: FragmentSliderBinding
    private var ImageUrl : Uri? = null

    private lateinit var db:FirebaseFirestore





    private var launchGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult ()){

        if(it.resultCode== Activity.RESULT_OK)
            ImageUrl = it.data!!.data
        binding.imageSlider.setImageURI(ImageUrl)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentSliderBinding.inflate(layoutInflater)

        binding.apply {

            imageSlider.setOnClickListener {

                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGallery.launch(intent)

            }

            binding.uploadSlider.setOnClickListener {
                if (ImageUrl != null){
                    uploadImage(ImageUrl!!)

                }

            }
        return binding.root
    }


}

    private fun uploadImage(uri: Uri) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refstorage =
            FirebaseStorage.getInstance().reference.child("slider/$fileName")
        refstorage.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL from the taskSnapshot
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                    storeData(downloadUri.toString()) // Pass the download URL to storeData function
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "SOME THING WENT WRONG", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "SOME THING WENT WRONG", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(image: String) {
        val db = Firebase.firestore
        val data = hashMapOf<Any, String>("img" to image)
        db.collection("slider").document("item").set(data).addOnSuccessListener{
            Toast.makeText(requireContext(), "UPLOADED", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener{
            Toast.makeText(requireContext(), "FAILED UPLOAD", Toast.LENGTH_SHORT).show()

        }

    }

}

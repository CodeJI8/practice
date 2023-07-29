package com.example.practice.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import com.example.practice.R
import com.example.practice.adapter.adapter_addproduct
import com.example.practice.databinding.FragmentProductBinding
import com.example.practice.model.model_product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class Product_Fragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private lateinit var list: ArrayList<Uri>
    private lateinit var listImages: ArrayList<String>
    private lateinit var adapter: adapter_addproduct
    private  var coverImage: Uri? = null
    private var ImageUrl : Uri? = null
    private lateinit var dialog: Dialog
    private var coverImgUrl:String? = ""
    private lateinit var  categoryList: ArrayList<String>
    private var i = 0



    private var launchGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult ()){

        if(it.resultCode== Activity.RESULT_OK)
            ImageUrl = it.data!!.data
        binding.coverImage.setImageURI(ImageUrl)
        binding.coverImage.visibility = VISIBLE


    }

    private var launchPRODUCTGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult ()){

        if(it.resultCode== Activity.RESULT_OK)
            coverImage = it.data!!.data
        binding.productImage.setImageURI(coverImage)
        binding.productImage.visibility = VISIBLE



    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(layoutInflater)


        list = ArrayList()
        listImages = ArrayList()



        binding.apply {

            binding.coverImageBtn.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGallery.launch(intent)


            }

            binding.productBtn.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchPRODUCTGallery.launch(intent)


            }

            binding.addProductBtn.setOnClickListener {
           validate()
            }

        }





        return binding.root
    }

    private fun validate() {
       if (binding.productNameEdt.text.toString().isEmpty()){
           binding.productNameEdt.requestFocus()
           binding.productNameEdt.error="Empty"
       }


        else if(binding.productSpEdt.text.toString().isEmpty()){
            binding.productSpEdt.requestFocus()
            binding.productSpEdt.error="Empty"
        }
        else if (coverImage == null)
            Toast.makeText(requireContext(), "Select Cover Images", Toast.LENGTH_SHORT).show()
       else if (listImages.size>1)
           Toast.makeText(requireContext(), "Select Cover Images", Toast.LENGTH_SHORT).show()
else{
           uploadImage()
           
       }

    }

    private fun uploadImage() {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refstorage =
            FirebaseStorage.getInstance().reference.child("products/$fileName")
        refstorage.putFile(coverImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {image ->
                    coverImgUrl = image.toString()
                    uploadProductImage()
                }

                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "SOMETHING WENT WRONG",Toast.LENGTH_SHORT).show()
                    }

            }
    }

    private fun  setProductCategory(){
        categoryList = ArrayList()
        Firebase.firestore.collection("category").get().addOnSuccessListener {
            categoryList.clear()
            for (doc in it.documents){
                val data = doc.toObject(model_product::class.java)
                categoryList.add(data!!.category!!)

            }
        }

    }

    private fun uploadProductImage() {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refstorage = FirebaseStorage.getInstance().reference.child("products/$fileName")
        refstorage.putFile(list[i])
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { image ->
                    listImages.add(image.toString())
                    if (list.size == listImages.size) {
                        storedata()
                    } else {
                        i++
                        uploadProductImage()
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show()
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




    private fun storedata() {
        val db = Firebase.firestore.collection("products")
        val key = db.id
        val data = model_product(
            binding.productNameEdt.text.toString(),
            binding.productDescriptionEdt.text.toString(),
       coverImgUrl.toString(),
            categoryList[binding.dropCategoryBtn.selectedItemPosition],
            key,
            binding.productMrpEdt.text.toString(),
                    binding.productSpEdt.text.toString(),
            listImages

        )
db.document(key).set(data).addOnSuccessListener {
    Toast.makeText(requireContext(), "SUCCESS UPLOAD", Toast.LENGTH_SHORT).show()
    binding.productNameEdt.text = null
}
    .addOnFailureListener {
        Toast.makeText(requireContext(), "FAILED UPLOAD", Toast.LENGTH_SHORT).show()
    }

    }

    }

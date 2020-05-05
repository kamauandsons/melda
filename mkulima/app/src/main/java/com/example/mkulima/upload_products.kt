package com.example.mkulima


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.mkulima.databinding.UploadProductBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class upload_products : Fragment() {
    private lateinit var binding: UploadProductBinding
    private lateinit var mStorage : FirebaseStorage
    private lateinit var md: FirebaseFirestore

    private val REQUEST_IMAGE_GET = 1

    private lateinit var selectedImageUri : Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.upload_product,
            container,
            false
        )

        mStorage = FirebaseStorage.getInstance()
        md= FirebaseFirestore.getInstance()
        binding.choose.setOnClickListener {
            selectImage()
        }
        binding.uprod.setOnClickListener {
            uploadProductImage()
        }

        return binding.root
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {

            val fullPhotoUri: Uri = data!!.data!!
            selectedImageUri = fullPhotoUri


            Glide.with(requireContext().applicationContext)
                .load(selectedImageUri)
                .into(binding.img)

        }
    }

    private fun uploadProductImage() {
        //getting the image name that will be used to store it. eg farm_1.jpeg
       // val imageName = binding.name.text


        //storage location of image with the image name
        val productImageRef = mStorage.reference.
            child("products/"+binding.name.text +"@" + UUID.randomUUID().toString())


        //task to upload the selected image url to the specific location
        val uploadTask = productImageRef.putFile(selectedImageUri)

        //task to get the imageUrl of the uploaded image
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {

                task.exception?.let {
                    throw it

                }
            }
            productImageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result

                saveImageToFirestore(downloadUri)

                Toast.makeText(requireContext().applicationContext,"Upload Succesful", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun saveImageToFirestore(downloadUri: Uri?) {
        val imageName = binding.name.text.toString()
        val phone=binding.phone.text.toString()


        val uploadedImage = pinfo(
            imageName,
            downloadUri.toString(),
            phone
        )


        val imageRef = md.collection("images")
        imageRef.add(uploadedImage).addOnSuccessListener { documentReference ->
            Log.i("chei","added success")
            Toast.makeText(requireContext().applicationContext, "Added Successfully", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener { e ->
                Log.i("chei","added error")
                Toast.makeText(requireContext().applicationContext, "Storage to database Failed:", Toast.LENGTH_SHORT).show()
            }

    }




}

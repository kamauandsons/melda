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
import com.example.mkulima.databinding.UploadBookBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.upload_book.*
import java.util.*

class upload_books : Fragment() {
    private lateinit var binding: UploadBookBinding
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
            R.layout.upload_book,
            container,
            false
        )

        mStorage = FirebaseStorage.getInstance()
        md= FirebaseFirestore.getInstance()

        binding.choose.setOnClickListener {
            selectbook()
        }
        binding.uprod.setOnClickListener {
            uploadbook()
        }

        return binding.root
    }

    private fun selectbook() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
    }

    private fun showPdfFromUri(uri: Uri?) {
        pdfView.fromUri(uri)
            .defaultPage(0)
            .spacing(10)
            .load()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {

            val fullPhotoUri: Uri = data!!.data!!
            selectedImageUri = fullPhotoUri
            showPdfFromUri(selectedImageUri)



        }
    }

    private fun uploadbook() {
        //getting the image name that will be used to store it. eg farm_1.jpeg
       // val imageName = selectedImageUri.lastPathSegment


        //storage location of image with the image name
        val productImageRef = mStorage.reference.
            child("pdfs/"+binding.name.text +"@" + UUID.randomUUID().toString()+".pdf")


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

        val uploadedImage = binfo(
            imageName,
            downloadUri.toString()
        )

        //reference to store the image to firestore
        //val imageRef = mdb.getReference("uplands").child("images")
        val imageRef = md.collection("pdfs")
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


package com.example.mkulima


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mkulima.databinding.BookBinding

class book : Fragment() {
    private lateinit var binding: BookBinding
    private lateinit var selectedImageUri : Uri
    val jc=10
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.book,
            container,
            false)
        var args=bookArgs.fromBundle(requireArguments())
        var ur=args.url
        var tt=args.title
        val pd=Uri.parse(ur)
        binding.title.text=tt

        binding.uprod.setOnClickListener {
            try {
                show(pd)
            }
            catch (e: RuntimeException){
                Toast.makeText(requireContext(), "There's no PDF Reader found in your device", Toast.LENGTH_SHORT).show();
            }
        }

        return binding.root
    }


    private fun show(pdu:Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            type = "application/pdf"
            data=pdu
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, jc)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == jc && resultCode == Activity.RESULT_OK) {

            val fullPhotoUri: Uri = data!!.data!!
            selectedImageUri = fullPhotoUri


        }
    }


}

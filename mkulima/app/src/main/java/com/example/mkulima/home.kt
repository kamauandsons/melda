package com.example.mkulima
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.mkulima.databinding.HomeBinding

class home : Fragment() {
    private lateinit var binding: HomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home,
            container,
            false
        )
        binding.book.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_home_to_upload_books)
        )
        binding.lib.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_home_to_library)
        )
        binding.uprod.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_home_to_upload_products)
        )
        binding.vprod.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_home_to_view_products)
        )


        return binding.root
    }


}


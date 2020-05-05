package com.example.mkulima


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mkulima.databinding.ViewProductBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class view_products : Fragment() {
    private lateinit var binding: ViewProductBinding
    private lateinit var md: FirebaseFirestore
    private var mAdapter: FirebaseRecyclerAdapter<pinfo, viewh>? = null
    private var adapter: ProductFirestoreRecyclerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.view_product,
            container,
            false
        )
        md= FirebaseFirestore.getInstance()
        val rcvListImg = binding.prodRecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = false
        rcvListImg.setHasFixedSize(true)
        rcvListImg.layoutManager = layoutManager

        val k = md.collection("images").orderBy("image_name", Query.Direction.ASCENDING)

        val query =k

        val options = FirestoreRecyclerOptions.Builder<pinfo>().setQuery(query, pinfo::class.java).build()

        adapter = ProductFirestoreRecyclerAdapter(options)
        binding.prodRecyclerView.adapter = adapter
        return binding.root
    }

    private inner class ProductFirestoreRecyclerAdapter internal constructor(options: FirestoreRecyclerOptions<pinfo>) : FirestoreRecyclerAdapter<pinfo, ProductViewHolder>(options) {
        override fun onBindViewHolder(productViewHolder: ProductViewHolder, position: Int, productModel: pinfo) {
            productViewHolder.setProductName(productModel.image_name,productModel.image_url,productModel.phone)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.pitem, parent, false)
            return ProductViewHolder(view)
        }
    }

    private inner class ProductViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view) {
        internal fun setProductName(productName: String,url:String,phonee:String) {
            val textView = view.findViewById<TextView>(R.id.label)
            val img=view.findViewById<ImageView>(R.id.recipe)
            val phone=view.findViewById<TextView>(R.id.phone)
            //view.setOnClickListener ( Navigation.createNavigateOnClickListener(viewDirections.actionViewToKitabu(url,productName)))
            textView.text = productName
            phone.text=phonee
            Glide.with(requireContext())
                .load(url)
                .error(R.drawable.common_google_signin_btn_icon_dark)
                .into(img)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()

        if (adapter != null) {
            adapter!!.stopListening()
        }
    }

}

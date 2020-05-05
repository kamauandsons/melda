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
import com.example.mkulima.databinding.LibraryBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class library : Fragment() {
    private lateinit var binding: LibraryBinding
    private lateinit var md: FirebaseFirestore
    private var mAdapter: FirebaseRecyclerAdapter<binfo, viewh>? = null
    private var adapter: ProductFirestoreRecyclerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.library,
            container,
            false
        )
        md= FirebaseFirestore.getInstance()
        val rcvListImg = binding.bookRecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = false
        rcvListImg.setHasFixedSize(true)
        rcvListImg.layoutManager = layoutManager

        val k = md.collection("pdfs").orderBy("image_name", Query.Direction.ASCENDING)

        val query =k

        val options = FirestoreRecyclerOptions.Builder<binfo>().setQuery(query, binfo::class.java).build()

        adapter = ProductFirestoreRecyclerAdapter(options)
        binding.bookRecyclerView.adapter = adapter

        return binding.root
    }

    private inner class ProductFirestoreRecyclerAdapter internal constructor(options: FirestoreRecyclerOptions<binfo>) : FirestoreRecyclerAdapter<binfo, ProductViewHolder>(options) {
        override fun onBindViewHolder(productViewHolder: ProductViewHolder, position: Int, productModel: binfo) {
            productViewHolder.setProductName(productModel.image_name,productModel.image_url)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.bitem, parent, false)
            return ProductViewHolder(view)
        }
    }

    private inner class ProductViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view) {
        internal fun setProductName(productName: String,url:String) {
            val textView = view.findViewById<TextView>(R.id.label)
            val img=view.findViewById<ImageView>(R.id.recipe)
           // val phone=view.findViewById<TextView>(R.id.phone)
            view.setOnClickListener ( Navigation.createNavigateOnClickListener(libraryDirections.actionLibraryToBook(productName,url)))
            textView.text = productName

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


package com.nads.shopping.pagingsource

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nads.shopping.R
import com.nads.shopping.databinding.FullproductpagingsourceitemBinding
import com.nads.shopping.datamodels.Product
import com.nads.shopping.viewmodels.ProductsViewModel
import kotlinx.android.synthetic.main.fullproductpagingsourceitem.view.*
import kotlinx.android.synthetic.main.fullproductpagingsourceitem.view.textView4
import kotlinx.android.synthetic.main.fullproductpagingsourceitem.view.textView5
import kotlinx.android.synthetic.main.fullproductpagingsourceitem.view.textView6
import kotlinx.android.synthetic.main.fullproductpagingsourceitem.view.textView7
import java.util.*


class BasicPagingSourceAdapter(diffCallback: DiffUtil.ItemCallback<Product>, context: Context,productsViewModel: ProductsViewModel) :
    PagingDataAdapter<Product, BasicPagingSourceAdapter.ViewHolder>(diffCallback){
    private var context1: Context? = context


    private var productsViewModel: ProductsViewModel? = productsViewModel

    class ViewHolder(view: FullproductpagingsourceitemBinding) : RecyclerView.ViewHolder(view.root){
       val cardView:ConstraintLayout = view.root.findViewById<ConstraintLayout>(R.id.paginsourcebox)

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

  //      productsViewModel = ViewModelProviders.of(((context1 as FragmentActivity?)!!)).get(ProductsViewModel::class.java)

        val locale: Locale  = Locale.getDefault()
        if (locale.language == "ar"){
            holder.cardView.textView4.text = item?.brandNameAr
            holder.cardView.textView5.text = item?.nameAr
        }else{
            holder.cardView.textView5.text = item?.name
            holder.cardView.textView4.text = item?.brandName
        }

          holder.cardView.textView6.text = "QAR ${item?.price}"
          holder.cardView.textView7.isVisible = !item?.previousPrice
              .equals("0") && item?.previousPrice != null
        holder.cardView.textView7.text = item?.previousPrice
        holder.cardView.fullproductview.isVisible =!item?.previousPrice
            .equals("0") && item?.previousPrice != null
        val context =holder.cardView.context
        val img = holder.cardView.photoproduct
        Glide.with(context).load(item?.photo).fitCenter().into(img)

       holder.cardView.setOnClickListener{
               productsViewModel!!.productsId.value = item?.productsId
          Log.e("Clicked", productsViewModel!!.productsId.value.toString())
       }

    }
    fun refreshView(position: Int) {
        notifyItemChanged(position)
        refresh()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.getContext())
        val fullproductpagingsourceitemBinding = FullproductpagingsourceitemBinding.inflate(view, parent, false)
        return ViewHolder(fullproductpagingsourceitemBinding)
    }




    object UserComparator : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            // Id is unique.
            return oldItem.productsId == newItem.productsId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }



}
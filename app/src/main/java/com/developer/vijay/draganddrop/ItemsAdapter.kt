package com.developer.vijay.draganddrop

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developer.vijay.draganddrop.databinding.ItemVegetableBinding

class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.VegetablesViewHolder>() {

    private var vegetableList = arrayListOf<Int>()
    private var longClickListener: LongClickListener? = null

    inner class VegetablesViewHolder(val mBinding: ItemVegetableBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VegetablesViewHolder {
        return VegetablesViewHolder(
            ItemVegetableBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VegetablesViewHolder, position: Int) {
        holder.mBinding.apply {
            Glide.with(root.context).load(vegetableList[position]).into(ivVegetable)
            ivVegetable.setOnLongClickListener {
                it.tag = "NOT Added Item"
                longClickListener?.onLongClick(position, it)

                val clipText = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_PLAIN, "")
                val shadowBuilder = View.DragShadowBuilder(it)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    it.startDragAndDrop(clipText, shadowBuilder, it, 0)
                else
                    it.startDrag(clipText, shadowBuilder, it, 0)

                true
            }
        }
    }

    override fun getItemCount(): Int {
        return vegetableList.size
    }

    fun setData(vegetableList: ArrayList<Int>, longClickListener: LongClickListener) {
        this.vegetableList = vegetableList
        this.longClickListener = longClickListener
        notifyDataSetChanged()
    }
}
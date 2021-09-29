package com.developer.vijay.draganddrop

import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.vijay.draganddrop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LongClickListener {

    companion object {
        const val TAG = "mydata"
    }

    private lateinit var mBinding: ActivityMainBinding
    private val itemsAdapter by lazy { ItemsAdapter() }
    private val droppedItemsAdapter by lazy { DroppedItemsAdapter(this) }
    private val droppedItemList = arrayListOf<Int>()
    private val vegetableList = arrayListOf(
        R.drawable.veg_1,
        R.drawable.veg_2,
        R.drawable.veg_3,
        R.drawable.veg_4,
        R.drawable.veg_5,
        R.drawable.veg_6,
        R.drawable.veg_7,
    )
    private var itemDragPosition = -1
    private var addedItemDragPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        itemsAdapter.setData(vegetableList, object : LongClickListener {
            override fun onLongClick(position: Int, view: View) {
                Log.d(TAG, "Position: $position")
                itemDragPosition = position
            }
        })


        mBinding.rvItems.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = itemsAdapter
        }


        mBinding.rvAddedItems.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = droppedItemsAdapter
        }


        mBinding.clRoot.setOnDragListener { view, dragEvent ->

            val selectedView = dragEvent.localState as View

            if (selectedView.tag.equals("Added Item")) {
                when (dragEvent.action) {
                    DragEvent.ACTION_DRAG_ENTERED -> {
                        view.setBackgroundColor(
                            ContextCompat.getColor(
                                applicationContext,
                                android.R.color.holo_red_light
                            )
                        )
                        true
                    }
                    DragEvent.ACTION_DRAG_EXITED -> {
                        view.setBackgroundColor(
                            ContextCompat.getColor(
                                applicationContext,
                                android.R.color.transparent
                            )
                        )
                        true
                    }
                    DragEvent.ACTION_DROP -> {

                        showToast("Item REMOVED from the Bowl.")
                        view.setBackgroundColor(
                            ContextCompat.getColor(
                                applicationContext,
                                android.R.color.transparent
                            )
                        )

                        droppedItemList.removeAt(addedItemDragPosition)
                        droppedItemsAdapter.setData(droppedItemList)
                        true
                    }
                    else -> true
                }
            } else
                true
        }

        mBinding.ivBowl.setOnDragListener { view, dragEvent ->

            val selectedView = dragEvent.localState as View

            if (selectedView.tag.equals("NOT Added Item")) {
                when (dragEvent.action) {
                    DragEvent.ACTION_DRAG_ENTERED -> {
                        DrawableCompat.setTint(
                            DrawableCompat.wrap(mBinding.ivBowl.drawable),
                            ContextCompat.getColor(applicationContext, android.R.color.darker_gray)
                        )
                        true
                    }
                    DragEvent.ACTION_DRAG_EXITED -> {
                        DrawableCompat.setTint(
                            DrawableCompat.wrap(mBinding.ivBowl.drawable),
                            ContextCompat.getColor(applicationContext, android.R.color.black)
                        )
                        true
                    }
                    DragEvent.ACTION_DROP -> {
                        showToast("Item ADDED in the Bowl.")
                        droppedItemList.add(vegetableList[itemDragPosition])
                        droppedItemsAdapter.setData(droppedItemList)
                        DrawableCompat.setTint(
                            DrawableCompat.wrap(mBinding.ivBowl.drawable),
                            ContextCompat.getColor(
                                applicationContext,
                                android.R.color.holo_red_dark
                            )
                        )
                        true
                    }
                    else -> true
                }
            } else
                true
        }

    }

    private fun showToast(message: String) =
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    override fun onLongClick(position: Int, view: View) {
        Log.d(TAG, "Added Position: $position")
        addedItemDragPosition = position
    }
}
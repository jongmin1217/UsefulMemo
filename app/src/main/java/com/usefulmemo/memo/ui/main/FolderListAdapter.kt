package com.usefulmemo.memo.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.usefulmemo.memo.databinding.ItemFolderListBinding
import com.usefulmemo.memo.domain.model.Folder
import timber.log.Timber

class FolderListAdapter(private val viewModel : MainViewModel) : RecyclerView.Adapter<FolderListAdapter.ListHolder>() {

    var items = ArrayList<Folder>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    private var swipeLayout : SwipeRevealLayout? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val binding = ItemFolderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ListHolder(val binding: ItemFolderListBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(folder : Folder){
            binding.model = folder
            binding.vm = viewModel

            if(adapterPosition == itemCount-1) binding.line.visibility = View.INVISIBLE

            if(folder.id < 1) binding.swipeLayout.setLockDrag(true)

            binding.swipeLayout.setSwipeListener(
                object : SwipeRevealLayout.SwipeListener{
                    override fun onClosed(view: SwipeRevealLayout?) { }
                    override fun onOpened(view: SwipeRevealLayout?) {
                        swipeLayout = if(swipeLayout == null) view
                        else{
                            if(swipeLayout != view) swipeLayout?.close(true)
                            view
                        }
                    }
                    override fun onSlide(view: SwipeRevealLayout?, slideOffset: Float) { }
                }
            )

            binding.ibDeleteFolder.setOnClickListener {
                binding.swipeLayout.close(true)
                viewModel.deleteFolder(folder)
            }

            binding.ibEditFolder.setOnClickListener {
                binding.swipeLayout.close(true)
                viewModel.editFolder(folder)
            }
        }
    }
}
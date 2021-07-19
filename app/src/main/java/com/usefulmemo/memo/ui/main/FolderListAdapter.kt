package com.usefulmemo.memo.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.usefulmemo.memo.databinding.ItemFolderListBinding
import com.usefulmemo.memo.domain.model.Folder

class FolderListAdapter(private val viewModel : MainViewModel) : RecyclerView.Adapter<FolderListAdapter.ListHolder>() {

    var items = ArrayList<Folder>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

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
        }
    }
}
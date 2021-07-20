package com.usefulmemo.memo.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.usefulmemo.memo.databinding.ItemFolderListBinding
import com.usefulmemo.memo.databinding.ItemMemoListBinding
import com.usefulmemo.memo.domain.model.Folder
import com.usefulmemo.memo.domain.model.Memo
import timber.log.Timber

class MemoListAdapter (private val viewModel : MainViewModel) : RecyclerView.Adapter<MemoListAdapter.ListHolder>() {

    var items = ArrayList<Memo>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    private var swipeLayout : SwipeRevealLayout? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val binding = ItemMemoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ListHolder(val binding: ItemMemoListBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(memo : Memo){
            binding.model = memo
            binding.vm = viewModel

            if(adapterPosition == itemCount-1) binding.line.visibility = View.INVISIBLE

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

            binding.ibDeleteMemo.setOnClickListener {
                binding.swipeLayout.close(true)
                viewModel.deleteMemo(memo)
            }
        }
    }
}
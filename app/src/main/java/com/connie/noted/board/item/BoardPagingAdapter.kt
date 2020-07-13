package com.connie.noted.board.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.connie.noted.data.Board
import com.connie.noted.databinding.ItemBoardBinding

class BoardPagingAdapter :
    PagedListAdapter<Board, BoardPagingAdapter.BoardViewHolder>(DiffCallback) {


    class BoardViewHolder(private var binding: ItemBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(board: Board) {
            binding.board = board
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Board>() {
        override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        return BoardViewHolder(
            ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val board = getItem(position)

        board?.let {
            holder.bind(it)
        }

    }


}
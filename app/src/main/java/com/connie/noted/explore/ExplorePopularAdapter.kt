package com.connie.noted.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.connie.noted.data.Board
import com.connie.noted.databinding.ItemBoardPopularBinding

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * [Board], including computing diffs between lists.
 * @param onClickListener a lambda that takes the
 */
class ExplorePopularAdapter(
    private val onClickListener: OnClickListener,
    val viewModel: ExploreViewModel
) :
    ListAdapter<Board, ExplorePopularAdapter.BoardViewHolder>(DiffCallback) {

    class BoardViewHolder(private var binding: ItemBoardPopularBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(board: Board) {

            binding.board = board

            val i = board.images

            binding.imagesList = when (i.size) {
                0 -> arrayListOf("", "", "", "", "")
                1 -> arrayListOf(i[0], "", "", "", "")
                2 -> arrayListOf(i[0], i[1], "", "", "")
                3 -> arrayListOf(i[0], i[1], i[2], "", "")
                4 -> arrayListOf(i[0], i[1], i[2], i[3], "")
                else -> arrayListOf(i[0], i[1], i[2], i[3], i[4])
            }

            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Board]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Board>() {
        override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BoardViewHolder {
        return BoardViewHolder(
            ItemBoardPopularBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val board = getItem(position)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(board)
        }
        holder.bind(board)

    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Board]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Board]
     */
    class OnClickListener(val clickListener: (board: Board) -> Unit) {
        fun onClick(board: Board) = clickListener(board)
    }

}

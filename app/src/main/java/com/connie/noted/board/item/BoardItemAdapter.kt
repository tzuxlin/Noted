package com.connie.noted.board.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.connie.noted.data.Board
import com.connie.noted.databinding.ItemBoardBinding

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * [Board], including computing diffs between lists.
 * @param onClickListener a lambda that takes the
 */
class BoardItemAdapter(
    private val onClickListener: OnClickListener,
    val viewModel: BoardItemViewModel
) :
    ListAdapter<Board, BoardItemAdapter.BoardViewHolder>(DiffCallback) {

    class BoardViewHolder(private var binding: ItemBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(board: Board) {
            binding.board = board
//            binding.imageString = board.images[0]

            binding.imagesList = ArrayList<String>()

            val i = board.images

            when (i.size) {
                0 -> binding.imagesList = arrayListOf("", "", "", "", "")
                1 -> binding.imagesList = arrayListOf(i[0], "", "", "", "")
                2 -> binding.imagesList = arrayListOf(i[0], i[1], "", "", "")
                3 -> binding.imagesList = arrayListOf(i[0], i[1], i[2], "", "")
                4 -> binding.imagesList = arrayListOf(i[0], i[1], i[2], i[3], "")
                5 -> binding.imagesList = arrayListOf(i[0], i[1], i[2], i[3], i[4])
                else -> binding.imagesList = arrayListOf(i[0], i[1], i[2], i[3], i[4])

            }


            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
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
            ItemBoardBinding.inflate(
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

        board?.let {
            holder.bind(it)
        }

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

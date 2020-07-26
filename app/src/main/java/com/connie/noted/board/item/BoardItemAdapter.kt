package com.connie.noted.board.item

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.connie.noted.data.Board
import com.connie.noted.databinding.ItemBoardGridBinding
import com.connie.noted.databinding.ItemBoardLinearBinding
import com.connie.noted.util.Util.setUpThinTags

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * [Board], including computing diffs between lists.
 * @param onClickListener a lambda that takes the
 */
class BoardItemAdapter(
    private val onClickListener: OnClickListener,
    val viewModel: BoardItemViewModel
) :
    ListAdapter<Board, RecyclerView.ViewHolder>(DiffCallback) {

    override fun getItemViewType(position: Int): Int {
        return viewModel.viewType.value ?: 0
    }

    class BoardLinearViewHolder(private var binding: ItemBoardLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(board: Board, viewModel: BoardItemViewModel) {

            binding.board = board

            val i = board.images

            when (i.size) {
                0 -> binding.imagesList = arrayListOf("", "", "", "", "")
                1 -> binding.imagesList = arrayListOf(i[0], "", "", "", "")
                2 -> binding.imagesList = arrayListOf(i[0], i[1], "", "", "")
                3 -> binding.imagesList = arrayListOf(i[0], i[1], i[2], "", "")
                4 -> binding.imagesList = arrayListOf(i[0], i[1], i[2], i[3], "")
                else -> binding.imagesList = arrayListOf(i[0], i[1], i[2], i[3], i[4])

            }

            // Binding chipGroup

            if (!board.tags.isNullOrEmpty()) {
                val chipGroup = binding.noteTagGroup
                setUpThinTags(board.tags, chipGroup, OnTagClickListener {
                    viewModel.tagClicked(it)
                    Log.i("Connie", "OnTagClickListener, it=$it")
                })
            }


            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }

        class OnTagClickListener(val tagClickListener: (tag: String) -> Unit) {
            fun onTagClick(tag: String) = tagClickListener(tag)
        }

    }

    class BoardGridViewHolder(private var binding: ItemBoardGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(board: Board, viewModel: BoardItemViewModel) {

            binding.board = board


            // Binding imageList
            val i = board.images

            binding.imagesList = when (i.size) {
                0 -> arrayListOf("", "", "", "", "")
                1 -> arrayListOf(i[0], "", "", "", "")
                2 -> arrayListOf(i[0], i[1], "", "", "")
                3 -> arrayListOf(i[0], i[1], i[2], "", "")
                4 -> arrayListOf(i[0], i[1], i[2], i[3], "")
                else -> arrayListOf(i[0], i[1], i[2], i[3], i[4])
            }

            // Binding chipGroup

            if (!board.tags.isNullOrEmpty()) {
                val chipGroup = binding.noteTagGroup
                setUpThinTags(board.tags, chipGroup, BoardLinearViewHolder.OnTagClickListener {
                    viewModel.tagClicked(it)
                    Log.i("Connie", "OnTagClickListener, it=$it")
                })
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
    ): RecyclerView.ViewHolder {
        return when (viewType) {

            0 -> {
                BoardGridViewHolder(
                    ItemBoardGridBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                BoardLinearViewHolder(
                    ItemBoardLinearBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

        }
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val board = getItem(position)

        when (holder) {

            is BoardGridViewHolder -> {

                holder.itemView.setOnClickListener {
                    onClickListener.onClick(board)
                }
                holder.bind(board, viewModel)
            }

            is BoardLinearViewHolder -> {

                holder.itemView.setOnClickListener {
                    onClickListener.onClick(board)
                }
                holder.bind(board, viewModel)
            }

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


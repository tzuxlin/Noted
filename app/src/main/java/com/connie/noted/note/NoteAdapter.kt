package com.connie.noted.note

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.connie.noted.NotedApplication
import com.connie.noted.R
import com.connie.noted.data.Note
import com.connie.noted.databinding.ItemNoteGridBinding
import com.connie.noted.databinding.ItemNoteLinearBinding
import com.connie.noted.util.Logger


/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * [Note], including computing diffs between lists.
 */

class NoteAdapter(
    private val onClickListener: OnClickListener,
    private val viewModel: NoteViewModel
) :
    ListAdapter<Note, RecyclerView.ViewHolder>(DiffCallback) {

    override fun getItemViewType(position: Int): Int {
        return viewModel.viewType.value ?: 0
    }


    class NoteLinearViewHolder(private var binding: ItemNoteLinearBinding) :
        RecyclerView.ViewHolder(binding.root), LifecycleOwner {


        fun bind(
            note: Note,
            viewModel: NoteViewModel
        ) {

            binding.lifecycleOwner = this
            binding.viewModel = viewModel
            binding.note = note

            binding.imageString =
                if (note.images.isNotEmpty()) note.images[0]
                else NotedApplication.instance.applicationContext.getString(R.string.url_place_holder)

            binding.iconNoteLiked.setOnClickListener {
                viewModel.likeButtonClicked(note)
            }

            binding.textLongClickAdd.setOnClickListener {
                viewModel.toAddNote(note)

                binding.iconLongClickAdd.setBackgroundResource(if (note.isSelected) R.drawable.icon_add_to_board_active else R.drawable.icon_add_to_board_inactive)
                binding.textLongClickAdd.text = if (note.isSelected) "　已選擇　" else "加入分類板"
                binding.layoutLongClick.backgroundTintList =
                    if (note.isSelected) ColorStateList.valueOf(NotedApplication.instance.getColor(R.color.olive_green_transparent_90)) else ColorStateList.valueOf(
                        NotedApplication.instance.getColor(R.color.olive_green_transparent_60)
                    )
            }


            binding.iconLongClickAdd.setOnClickListener {
                viewModel.toAddNote(note)

                binding.iconLongClickAdd.setBackgroundResource(if (note.isSelected) R.drawable.icon_add_to_board_active else R.drawable.icon_add_to_board_inactive)
                binding.textLongClickAdd.text = if (note.isSelected) "　已選擇　" else "加入分類板"
                binding.layoutLongClick.backgroundTintList =
                    if (note.isSelected) ColorStateList.valueOf(NotedApplication.instance.getColor(R.color.olive_green_transparent_90)) else ColorStateList.valueOf(
                        NotedApplication.instance.getColor(R.color.olive_green_transparent_60)
                    )
            }

            binding.executePendingBindings()
        }

        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }

        fun onAttach() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        fun onDetach() {
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

    }


    class NoteGridViewHolder(private var binding: ItemNoteGridBinding) :
        RecyclerView.ViewHolder(binding.root), LifecycleOwner {


        fun bind(
            note: Note,
            viewModel: NoteViewModel
        ) {


            binding.lifecycleOwner = this
            binding.viewModel = viewModel

            binding.note = note

            binding.imageString =
                if (note.images.isNotEmpty()) note.images[0]
                else NotedApplication.instance.applicationContext.getString(R.string.url_place_holder)


            binding.iconNoteLiked.setOnClickListener {
                viewModel.likeButtonClicked(note)
            }

            binding.textLongClickAdd.setOnClickListener {
                viewModel.toAddNote(note)

                binding.iconLongClickAdd.setBackgroundResource(if (note.isSelected) R.drawable.icon_add_to_board_active else R.drawable.icon_add_to_board_inactive)
                binding.textLongClickAdd.text = if (note.isSelected) "　已選擇　" else "加入分類板"
                binding.layoutLongClick.backgroundTintList
                if (note.isSelected) ColorStateList.valueOf(NotedApplication.instance.getColor(R.color.olive_green_transparent_90)) else ColorStateList.valueOf(
                    NotedApplication.instance.getColor(R.color.olive_green_transparent_60)
                )
            }


            binding.iconLongClickAdd.setOnClickListener {
                viewModel.toAddNote(note)

                binding.iconLongClickAdd.setBackgroundResource(if (note.isSelected) R.drawable.icon_add_to_board_active else R.drawable.icon_add_to_board_inactive)
                binding.textLongClickAdd.text = if (note.isSelected) "　已選擇　" else "加入分類板"
                binding.layoutLongClick.backgroundTintList =
                    if (note.isSelected) ColorStateList.valueOf(NotedApplication.instance.getColor(R.color.olive_green_transparent_90)) else ColorStateList.valueOf(
                        NotedApplication.instance.getColor(R.color.olive_green_transparent_60)
                    )
            }

            binding.executePendingBindings()
        }

        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }

        fun onAttach() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        fun onDetach() {
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }
    }


    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Note]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return (oldItem == newItem)
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.isSelected == newItem.isSelected
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            0 -> {
                NoteGridViewHolder(
                    ItemNoteGridBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                NoteLinearViewHolder(
                    ItemNoteLinearBinding.inflate(
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

        val note = getItem(position)

        Logger.i("position: $position, title = ${note.title}")

        when (holder) {

            is NoteGridViewHolder -> {
                holder.bind(getItem(position), viewModel)

                holder.itemView.setOnClickListener {

                    if (viewModel.isEditMode.value == false) {
                        Logger.d("Note Adapter, onClick = $note")
                        onClickListener.onClick(note)
                    }
                }


                holder.itemView.setOnLongClickListener {
                    viewModel.isEditMode.value = viewModel.isEditMode.value != true
                    true
                }

            }

            is NoteLinearViewHolder -> {

                holder.bind(getItem(position), viewModel)


                holder.itemView.setOnClickListener {

                    if (viewModel.isEditMode.value == false) {
                        Logger.d("Note Adapter, onClick = $note")
                        onClickListener.onClick(note)
                    }
                }

                holder.itemView.setOnLongClickListener {
                    viewModel.isEditMode.value = viewModel.isEditMode.value != true
                    true
                }

            }
        }

    }

    class OnClickListener(val clickListener: (note: Note) -> Unit) {
        fun onClick(note: Note) = clickListener(note)
    }


    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)

        when (holder) {
            is NoteGridViewHolder -> holder.onAttach()
            is NoteLinearViewHolder -> holder.onAttach()
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)

        when (holder) {
            is NoteGridViewHolder -> holder.onDetach()
            is NoteLinearViewHolder -> holder.onDetach()
        }
    }

}


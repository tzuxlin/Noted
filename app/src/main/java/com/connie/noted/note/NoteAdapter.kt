package com.connie.noted.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.connie.noted.data.Note
import com.connie.noted.databinding.ItemNoteGridBinding
import com.connie.noted.databinding.ItemNoteLinearBinding
import com.connie.noted.util.Logger


/**
 * Created by Wayne Chen in Jul. 2019.
 *
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

        fun bind(note: Note, viewModel: NoteViewModel) {

            binding.lifecycleOwner = this
            binding.viewModel = viewModel
            binding.note = note

            if (note.images.isNotEmpty()) {
                binding.imageString = note.images[0]
            } else {
                binding.imageNote.visibility = View.GONE
            }

            binding.iconNoteLiked.setOnClickListener {
                viewModel.likeButtonClicked(note)
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

        fun bind(note: Note, viewModel: NoteViewModel) {
            binding.lifecycleOwner = this
            binding.viewModel = viewModel

            binding.note = note

            if (note.images.isNotEmpty()) {
                binding.imageString = note.images[0]
            } else {
                binding.imageNote.visibility = View.GONE
            }

            binding.iconNoteLiked.setOnClickListener {
                viewModel.likeButtonClicked(note)
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

        when (holder) {

            is NoteGridViewHolder -> {
                holder.bind(getItem(position), viewModel)

                holder.itemView.setOnClickListener {

                    if (viewModel.isEditMode.value == false) {
                        Logger.d("Note Adapter, onClick = $note")
                        onClickListener.onClick(note)
                    } else {
                        noteSelected(note)
                        notifyItemChanged(position)
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
                    } else {
                        noteSelected(note)
                        notifyItemChanged(position)
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


//    private fun longClick(): Boolean {
//        Toast.makeText(NotedApplication.instance, "Long Clicked", Toast.LENGTH_SHORT).show()
//        viewModel.isEditMode.value = !viewModel.isEditMode.value!!
//        return true
//    }

//    private fun setTag(tagList: MutableList<String>) {
//        val chipGroup: ChipGroup = findViewById(R.id.tag_group)
//        for (index in tagList.indices) {
//            val tagName = tagList[index]
//            val chip = Chip(this)
//            val paddingDp = TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP, 10f,
//                getResources().getDisplayMetrics()
//            ).toInt()
//            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
//            chip.text = tagName
//            chip.setCloseIconResource(R.drawable.ic_action_navigation_close)
//            chip.isCloseIconEnabled = true
//            //Added click listener on close icon to remove tag from ChipGroup
//            chip.setOnCloseIconClickListener(object : OnClickListener() {
//                fun onClick(v: View?) {
//                    tagList.remove(tagName)
//                    chipGroup.removeView(chip)
//                }
//            })
//            chipGroup.addView(chip)
//        }
//    }


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

    private fun noteSelected(note: Note) {

        viewModel.notes.value?.let { notes ->

            val list = mutableListOf<Note>()
            for (i in notes.indices) {

                if (notes[i].id == note.id) {
                    notes[i].isSelected = !notes[i].isSelected

                    viewModel.notes.value = notes

                    viewModel.noteToAdd.value = notes.filter { note ->
                        note.isSelected
                    }

                    Logger.d("Note to add count: ${viewModel.noteToAdd.value?.size}")
                }

            }
        }
    }
}


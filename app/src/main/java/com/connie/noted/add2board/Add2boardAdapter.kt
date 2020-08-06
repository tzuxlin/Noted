package com.connie.noted.add2board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.connie.noted.data.Note
import com.connie.noted.databinding.ItemNoteLinearSmallBinding


/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * [Note], including computing diffs between lists.
 */

class Add2boardAdapter :
    ListAdapter<Note, Add2boardAdapter.NoteLinearViewHolder>(DiffCallback) {


    class NoteLinearViewHolder(private var binding: ItemNoteLinearSmallBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {

            if (note.images.isNotEmpty()) {
                binding.imageString = note.images[0]
            } else {
                binding.imageNote.visibility = View.GONE
            }

            binding.textNoteTitle.text = note.title

            binding.executePendingBindings()
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteLinearViewHolder {
        return NoteLinearViewHolder(
            ItemNoteLinearSmallBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: NoteLinearViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }


}

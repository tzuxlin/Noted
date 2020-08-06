package com.connie.noted.boardpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.connie.noted.data.Note
import com.connie.noted.databinding.ItemNoteLinearBinding
import com.connie.noted.util.Logger


class BoardNotesAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Note, BoardNotesAdapter.NoteLinearViewHolder>(DiffCallback) {

    class NoteLinearViewHolder(private var binding: ItemNoteLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {

            binding.note = note

            if (note.images.isNotEmpty()) {
                binding.imageString = note.images[0]
            } else {
                binding.imageNote.visibility = View.GONE
            }

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
            return oldItem == newItem
        }
    }


    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteLinearViewHolder {

        return NoteLinearViewHolder(
            ItemNoteLinearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }


    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */

    override fun onBindViewHolder(holder: NoteLinearViewHolder, position: Int) {

        val note = getItem(position)

        holder.bind(note)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(note)
            Logger.i("Note Adapter, onClick = $note")
        }
    }


    class OnClickListener(val clickListener: (note: Note) -> Unit) {
        fun onClick(note: Note) = clickListener(note)
    }

}

package com.connie.noted.note

import android.R
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.connie.noted.data.Note
import com.connie.noted.databinding.ItemNoteBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * [Product], including computing diffs between lists.
 */
class NoteAdapter(private val viewModel: NoteViewModel) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(DiffCallback) {

    class NoteViewHolder(private var binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note, viewModel: NoteViewModel) {

            binding.note = note
            if (note.images.isNotEmpty()){
                binding.imageString = note.images[0]
            } else {
                binding.imageString = "https://i.imgur.com/Iin605C.png"
            }
//            binding.viewModel = viewModel
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }


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


}

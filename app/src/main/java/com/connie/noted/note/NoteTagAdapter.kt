package com.connie.noted.note

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.connie.noted.databinding.ItemNoteTagBinding

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * [Color], including computing diffs between lists.
 * @param viewModel: [Add2cartViewModel]
 */
class NoteTagAdapter(val viewModel: NoteViewModel) : ListAdapter<String, NoteTagAdapter.ColorViewHolder>(DiffCallback) {

    private lateinit var context: Context

    /**
     * Implements [LifecycleOwner] to support Data Binding
     */
    class ColorViewHolder(
        private val binding: ItemNoteTagBinding,
        private val viewModel: NoteViewModel
    ): RecyclerView.ViewHolder(binding.root), LifecycleOwner {

//        val isSelected: LiveData<Boolean> = Transformations.map(viewModel.selectedColorPosition) {
//            it == adapterPosition
//        }

        fun bind(tag: String) {
            binding.lifecycleOwner = this
            binding.tag = tag
//            binding.viewHolder = this
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }

        fun markAttach() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        fun markDetach() {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        context = parent.context
        return ColorViewHolder(
            ItemNoteTagBinding.inflate(LayoutInflater.from(parent.context), parent, false), viewModel)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewAttachedToWindow(holder: ColorViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.markAttach()
    }

    override fun onViewDetachedFromWindow(holder: ColorViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.markDetach()
    }
}

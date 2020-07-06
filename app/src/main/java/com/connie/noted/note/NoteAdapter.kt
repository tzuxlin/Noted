package app.appworks.school.stylish.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.databinding.ItemCartBinding

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * [Product], including computing diffs between lists.
 */
class CartAdapter(val viewModel: CartViewModel) : ListAdapter<Product, CartAdapter.ProductViewHolder>(DiffCallback) {

    class ProductViewHolder(private var binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product, viewModel: CartViewModel) {

            binding.product = product
            binding.viewModel = viewModel
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Product]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return (oldItem.id == newItem.id)
                    && (oldItem.selectedVariant.colorCode == newItem.selectedVariant.colorCode)
                    && (oldItem.selectedVariant.size == newItem.selectedVariant.size)
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.amount == newItem.amount
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }
}

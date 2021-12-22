package com.example.retrofit2practice.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit2practice.data.local.ImageItem
import com.example.retrofit2practice.databinding.GridViewFabItemBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<ImageItem>() {
        override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var fabImages: List<ImageItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    class FavoriteViewHolder(private val binding: GridViewFabItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(imageItem: ImageItem){
            binding.imageItem = imageItem
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(GridViewFabItemBinding.inflate(
            LayoutInflater.from(parent.context)
        ))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val viewItem = fabImages[position]
        holder.bind(viewItem)
    }

    override fun getItemCount(): Int {
        return fabImages.size
    }
}
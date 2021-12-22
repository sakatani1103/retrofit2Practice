package com.example.retrofit2practice.imagePick

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit2practice.data.remote.ImageResult
import com.example.retrofit2practice.databinding.GridViewItemBinding

class ImageAdapter(val clickListener: ImagePickListener) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){

    private val diffCallback = object : DiffUtil.ItemCallback<ImageResult>() {
        override fun areItemsTheSame(oldItem: ImageResult, newItem: ImageResult): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ImageResult, newItem: ImageResult): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private val differ = AsyncListDiffer(this,diffCallback)
    var images: List<ImageResult>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    class ImageViewHolder(private val binding: GridViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(imageResult: ImageResult, clickListener: ImagePickListener){
            binding.image = imageResult
            binding.listener = clickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(GridViewItemBinding.inflate(
            LayoutInflater.from(parent.context)
        ))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val viewItem = images[position]
        holder.bind(viewItem, clickListener)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}

class ImagePickListener(val clickListener: (imgUrl: String) -> Unit) {
    fun onClick(imageResult: ImageResult) = clickListener(imageResult.largeImageURL)
}



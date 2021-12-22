package com.example.retrofit2practice.imagePick

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofit2practice.R
import com.example.retrofit2practice.databinding.FragmentImagePickBinding
import com.example.retrofit2practice.others.EventObserver
import com.example.retrofit2practice.others.Status
import com.google.android.material.snackbar.Snackbar

class ImagePickFragment : Fragment() {
    private lateinit var binding: FragmentImagePickBinding
    private lateinit var viewModel: ImagePickViewModel
    private lateinit var imageAdapter: ImageAdapter

    private val args: ImagePickFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_image_pick, container, false)
        viewModel = ViewModelProvider(requireActivity())[ImagePickViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.start(args.keyword)
        imageAdapter = ImageAdapter(ImagePickListener { imgUrl ->  viewModel.insertImageUrlToDb(imgUrl)})
        setUpRecyclerView()
        subscribeToObserver()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setUpRecyclerView() {
        binding.photoGrid.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun subscribeToObserver() {
        viewModel.imageResponse.observe(viewLifecycleOwner, EventObserver { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    if (result.data?.hits.isNullOrEmpty()){
                        Snackbar.make(binding.root, "キーワードに該当する画像はありません。", Snackbar.LENGTH_LONG)
                            .show()
                    } else {
                        imageAdapter.images = result.data!!.hits
                    }
                }
                Status.LOADING -> Snackbar.make(binding.root, "データをロード中です。", Snackbar.LENGTH_LONG)
                    .show()
                Status.ERROR -> result.message?.let {
                    Snackbar.make(
                        binding.root,
                        it,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        })

        viewModel.insertStatus.observe(viewLifecycleOwner, EventObserver{ result ->
            if(result.status == Status.SUCCESS){
                this.findNavController().popBackStack()
            }
        })
    }
}
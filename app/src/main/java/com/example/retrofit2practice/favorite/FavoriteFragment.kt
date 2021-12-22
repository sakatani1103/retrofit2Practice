package com.example.retrofit2practice.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofit2practice.R
import com.example.retrofit2practice.databinding.FragmentFavoriteBinding
import com.example.retrofit2practice.databinding.KeywordDialogBinding
import com.example.retrofit2practice.others.EventObserver
import com.example.retrofit2practice.others.Status
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val viewModel by viewModels<FavoriteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_favorite, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.addBtn.setOnClickListener {
            createAddDialog()
        }
        binding.deleteBtn.setOnClickListener {
            createDeleteDialog()
        }
        favoriteAdapter = FavoriteAdapter()
        subscribeToFavoriteFragment()
        setUpRecyclerView()
    }

    private fun createAddDialog() {
        val inflater = requireActivity().layoutInflater
        val dialogBinding = DataBindingUtil.inflate<KeywordDialogBinding>(
            inflater, R.layout.keyword_dialog, null, false
        )
        dialogBinding.viewModel = viewModel
        dialogBinding.lifecycleOwner = viewLifecycleOwner
        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.dialog_save) { _, _ -> viewModel.navigateToImagePick() }
            .setNegativeButton(R.string.dialog_cancel) { dialog , _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun createDeleteDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .setTitle("全ての画像を削除しますか。")
            .setPositiveButton(R.string.dialog_ok) { _, _ -> viewModel.deleteItemsFromDb() }
            .setNegativeButton(R.string.dialog_cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun subscribeToFavoriteFragment() {
        viewModel.fabImages.observe(viewLifecycleOwner, {
            favoriteAdapter.fabImages = it
        })

        viewModel.navigateToImagePick.observe(viewLifecycleOwner, EventObserver{
            when(it.status) {
                Status.SUCCESS -> {
                    it.data?.let { keyword ->
                        this.findNavController().navigate(
                        FavoriteFragmentDirections.actionFavoriteFragmentToImagePickFragment(keyword)
                        )
                    }
                }
                else -> it.message?.let { msg -> Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show() }
            }
        })

        viewModel.deleteStatus.observe(viewLifecycleOwner, EventObserver{
            if(it.status == Status.SUCCESS){
                Snackbar.make(binding.root, "削除を完了しました。", Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun setUpRecyclerView() {
        binding.favList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = favoriteAdapter
        }
    }


}
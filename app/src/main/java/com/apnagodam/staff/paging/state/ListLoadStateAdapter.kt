package com.apnagodam.staff.paging.state

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.LayoutCancelCaseIdBinding
import com.apnagodam.staff.databinding.LayoutListLoadBinding

class ListLoadStateAdapter(private val retry:()->Unit) :LoadStateAdapter<ListLoadStateAdapter.ListLoadViewHolder>(){

    class ListLoadViewHolder(private val binding:LayoutListLoadBinding,retry :()->Unit):RecyclerView.ViewHolder(binding.root){

        init {
            binding.btRetry.setOnClickListener {
                retry.invoke()
            }
        }
        fun bind(loadState: LoadState){
            binding.tvError.isVisible = loadState is LoadState.Error

            if (loadState is LoadState.Error) {
                binding.tvError.text = loadState.error.localizedMessage
            }

            binding.loader.isVisible = loadState is LoadState.Loading
            binding.btRetry.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: ListLoadViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ListLoadViewHolder {

        val binding = DataBindingUtil.inflate<LayoutListLoadBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_list_load,
            parent,
            false
        )

        return ListLoadViewHolder(binding,retry)

    }
}
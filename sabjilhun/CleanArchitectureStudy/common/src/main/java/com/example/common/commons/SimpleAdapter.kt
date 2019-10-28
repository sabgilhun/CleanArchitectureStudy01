package com.example.common.commons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

infix fun RecyclerView.setUp(block: RecyclerView.() -> Unit) {
    block()
}

fun <T : RecyclerView.LayoutManager> RecyclerView.withLayoutManager(
    block: RecyclerView.() -> T
) {
    layoutManager = block()
}

fun <E> RecyclerView.adapter(
    @LayoutRes itemLayoutRes: Int,
    block: SimpleAdapter<E>.() -> Unit
) {
    val simpleAdapter = SimpleAdapter<E>(itemLayoutRes)
    block(simpleAdapter)
    adapter = simpleAdapter
}

class SimpleAdapter<E>(
    @LayoutRes val itemLayoutRes: Int
) : RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder>() {

    val item: MutableList<E> = mutableListOf()

    var onBind: ((ViewDataBinding, Int) -> Unit)? = null

    var onItemClickListener: ((E, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder =
        SimpleViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                itemLayoutRes,
                parent,
                false
            )
        ).apply {
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(item[adapterPosition], adapterPosition)
            }
        }

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        onBind?.invoke(holder.binding, position)
    }

    class SimpleViewHolder(
        val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root)
}

fun <E> SimpleAdapter<E>.onBind(onBind: (ViewDataBinding, Int) -> Unit) {
    this.onBind = onBind
}

fun <E> SimpleAdapter<E>.onClickItem(onItemClickListener: (E, Int) -> Unit) {
    this.onItemClickListener = onItemClickListener
}

fun <E> SimpleAdapter<E>.observeLiveData(
    lifecycleOwner: LifecycleOwner,
    block: () -> LiveData<List<E>>
) {
    block().observe(lifecycleOwner, Observer {
        item.clear()
        item.addAll(it)
        notifyDataSetChanged()
    })
}
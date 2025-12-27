package ru.otus.cryptosample.coins.feature.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cryptosample.coins.feature.CoinState
import ru.otus.cryptosample.databinding.ItemCarouselBinding

class HorizontalCoinsRowViewHolder(
    private val binding: ItemCarouselBinding,
    sharedPool: RecyclerView.RecycledViewPool
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter = HorizontalCoinsAdapter()

    init {
        binding.horizontalRecyclerView.apply {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            setRecycledViewPool(sharedPool)
            this.adapter = this@HorizontalCoinsRowViewHolder.adapter
        }
    }

    fun bind(coins: List<CoinState>) {
        adapter.submitData(coins)
    }
}
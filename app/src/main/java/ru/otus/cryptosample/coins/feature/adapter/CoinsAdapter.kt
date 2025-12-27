package ru.otus.cryptosample.coins.feature.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cryptosample.R
import ru.otus.cryptosample.coins.feature.CoinCategoryState
import ru.otus.cryptosample.databinding.ItemCarouselBinding
import ru.otus.cryptosample.databinding.ItemCategoryHeaderBinding
import ru.otus.cryptosample.databinding.ItemCoinBinding

class CoinsAdapter(
    private val sharedPool: RecyclerView.RecycledViewPool
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CoinsAdapterItem>() {
            override fun areItemsTheSame(
                oldItem: CoinsAdapterItem,
                newItem: CoinsAdapterItem
            ): Boolean {
                return when (oldItem) {
                    is CoinsAdapterItem.CategoryHeader if newItem is CoinsAdapterItem.CategoryHeader ->
                        oldItem.categoryName == newItem.categoryName

                    is CoinsAdapterItem.CoinItem if newItem is CoinsAdapterItem.CoinItem ->
                        oldItem.coin.id == newItem.coin.id

                    is CoinsAdapterItem.HorizontalCoinsRow if newItem is CoinsAdapterItem.HorizontalCoinsRow ->
                        oldItem.categoryName == newItem.categoryName

                    else -> false
                }
            }

            override fun areContentsTheSame(
                oldItem: CoinsAdapterItem,
                newItem: CoinsAdapterItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(
                oldItem: CoinsAdapterItem,
                newItem: CoinsAdapterItem
            ): Any? {
                if (oldItem is CoinsAdapterItem.CoinItem && newItem is CoinsAdapterItem.CoinItem) {
                    val oldCoin = oldItem.coin
                    val newCoin = newItem.coin
                    if (oldCoin.highlight != newCoin.highlight &&
                        oldCoin.copy(highlight = newCoin.highlight) == newCoin) {
                        return CoinViewHolder.Payload.HighlightChanged(newCoin.highlight)
                    }
                }
                return null
            }
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    fun setData(categories: List<CoinCategoryState>, showAll: Boolean) {
        val adapterItems = mutableListOf<CoinsAdapterItem>()

        categories.forEach { category ->
            adapterItems.add(CoinsAdapterItem.CategoryHeader(category.name))

            val coins = category.coins
            val shouldUseHorizontalRow = coins.size > 10 && !showAll

            if (shouldUseHorizontalRow) {
                adapterItems.add(
                    CoinsAdapterItem.HorizontalCoinsRow(
                        categoryName = category.name,
                        coins = coins
                    )
                )
            } else {
                coins.forEach { coin ->
                    adapterItems.add(CoinsAdapterItem.CoinItem(coin))
                }
            }
        }

        differ.submitList(adapterItems)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is CoinsAdapterItem.CategoryHeader -> R.layout.item_category_header
            is CoinsAdapterItem.CoinItem -> R.layout.item_coin
            is CoinsAdapterItem.HorizontalCoinsRow -> R.layout.item_carousel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_category_header -> CategoryHeaderViewHolder(
                parent.inflateBinding(ItemCategoryHeaderBinding::inflate)
            )
            R.layout.item_coin -> CoinViewHolder(
                parent.inflateBinding(ItemCoinBinding::inflate)
            )
            R.layout.item_carousel -> HorizontalCoinsRowViewHolder(
                parent.inflateBinding(ItemCarouselBinding::inflate),
                sharedPool
            )
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = differ.currentList[position]) {
            is CoinsAdapterItem.CategoryHeader -> {
                (holder as CategoryHeaderViewHolder).bind(item.categoryName)
            }
            is CoinsAdapterItem.CoinItem -> {
                (holder as CoinViewHolder).bind(item.coin)
            }
            is CoinsAdapterItem.HorizontalCoinsRow -> {
                (holder as HorizontalCoinsRowViewHolder).bind(item.coins)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            when (val item = differ.currentList[position]) {
                is CoinsAdapterItem.CoinItem -> {
                    (holder as CoinViewHolder).bind(item.coin, payloads)
                }
                else -> super.onBindViewHolder(holder, position, payloads)
            }
        }
    }
}
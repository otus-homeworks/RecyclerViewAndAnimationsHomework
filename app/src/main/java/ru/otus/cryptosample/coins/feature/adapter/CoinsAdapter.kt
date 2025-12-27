package ru.otus.cryptosample.coins.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cryptosample.coins.feature.CoinCategoryState
import ru.otus.cryptosample.databinding.ItemCarouselBinding
import ru.otus.cryptosample.databinding.ItemCategoryHeaderBinding
import ru.otus.cryptosample.databinding.ItemCoinBinding

class CoinsAdapter(
    private val sharedPool: RecyclerView.RecycledViewPool
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_COIN = 1
        private const val VIEW_TYPE_HORIZONTAL_ROW = 2

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CoinsAdapterItem>() {
            override fun areItemsTheSame(
                oldItem: CoinsAdapterItem,
                newItem: CoinsAdapterItem
            ): Boolean {
                return when {
                    oldItem is CoinsAdapterItem.CategoryHeader && newItem is CoinsAdapterItem.CategoryHeader ->
                        oldItem.categoryName == newItem.categoryName

                    oldItem is CoinsAdapterItem.CoinItem && newItem is CoinsAdapterItem.CoinItem ->
                        oldItem.coin.id == newItem.coin.id

                    oldItem is CoinsAdapterItem.HorizontalCoinsRow && newItem is CoinsAdapterItem.HorizontalCoinsRow ->
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
            is CoinsAdapterItem.CategoryHeader -> VIEW_TYPE_CATEGORY
            is CoinsAdapterItem.CoinItem -> VIEW_TYPE_COIN
            is CoinsAdapterItem.HorizontalCoinsRow -> VIEW_TYPE_HORIZONTAL_ROW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CATEGORY -> CategoryHeaderViewHolder(
                ItemCategoryHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_COIN -> CoinViewHolder(
                ItemCoinBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_HORIZONTAL_ROW -> HorizontalCoinsRowViewHolder(
                ItemCarouselBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
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
}
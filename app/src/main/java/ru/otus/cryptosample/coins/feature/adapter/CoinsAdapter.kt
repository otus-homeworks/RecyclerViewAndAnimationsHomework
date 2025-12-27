package ru.otus.cryptosample.coins.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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
    }

    private var items = listOf<CoinsAdapterItem>()

//    private var showAll: Boolean = true

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

        items = adapterItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
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
        when (val item = items[position]) {
            is CoinsAdapterItem.CategoryHeader -> {
                (holder as CategoryHeaderViewHolder).bind(item.categoryName)
            }
            is CoinsAdapterItem.CoinItem -> {
                (holder as CoinViewHolder).bind(item.coin)
            }

            is CoinsAdapterItem.HorizontalCoinsRow ->
                (holder as HorizontalCoinsRowViewHolder).bind(item.coins)
        }
    }
}
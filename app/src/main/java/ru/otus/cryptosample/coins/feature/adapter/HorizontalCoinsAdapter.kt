package ru.otus.cryptosample.coins.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt
import ru.otus.cryptosample.coins.feature.CoinState
import ru.otus.cryptosample.databinding.ItemCoinBinding

class HorizontalCoinsAdapter : ListAdapter<CoinState, CoinViewHolder>(DIFF_CALLBACK) {

    companion object {
        private const val VISIBLE_ITEMS_COUNT = 2.25f

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CoinState>() {
            override fun areItemsTheSame(oldItem: CoinState, newItem: CoinState): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CoinState, newItem: CoinState): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding = ItemCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val rv = parent as? RecyclerView
        val parentWidthPx = rv?.width?.takeIf { it > 0 }
            ?: parent.measuredWidth.takeIf { it > 0 }
            ?: parent.resources.displayMetrics.widthPixels

        val paddingStart = rv?.paddingStart ?: 0
        val paddingEnd = rv?.paddingEnd ?: 0
        val availableWidth = (parentWidthPx - paddingStart - paddingEnd).coerceAtLeast(0)

        val itemWidth = (availableWidth / VISIBLE_ITEMS_COUNT).roundToInt().coerceAtLeast(1)

        val lp = (binding.root.layoutParams as? RecyclerView.LayoutParams)
            ?: RecyclerView.LayoutParams(itemWidth, RecyclerView.LayoutParams.WRAP_CONTENT)

        lp.width = itemWidth
        lp.height = RecyclerView.LayoutParams.WRAP_CONTENT
        binding.root.layoutParams = lp

        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
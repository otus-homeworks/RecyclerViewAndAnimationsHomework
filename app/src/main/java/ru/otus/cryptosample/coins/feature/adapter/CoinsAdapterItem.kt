package ru.otus.cryptosample.coins.feature.adapter

import ru.otus.cryptosample.coins.feature.CoinState

sealed interface CoinsAdapterItem {

    data class CategoryHeader(val categoryName: String) : CoinsAdapterItem

    data class CoinItem(val coin: CoinState) : CoinsAdapterItem

    data class HorizontalCoinsRow(
        val categoryName: String,
        val coins: List<CoinState>
    ) : CoinsAdapterItem
}
package ru.otus.cryptosample.coins.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * Extension-функция для упрощения inflate ViewBinding в адаптерах.
 * 
 * Пример использования:
 * ```
 * parent.inflateBinding(ItemCoinBinding::inflate)
 * ```
 */
inline fun <T : ViewBinding> ViewGroup.inflateBinding(
    crossinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T
): T {
    return bindingInflater(LayoutInflater.from(context), this, false)
}

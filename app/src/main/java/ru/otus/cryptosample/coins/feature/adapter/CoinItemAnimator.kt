package ru.otus.cryptosample.coins.feature.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class CoinItemAnimator : DefaultItemAnimator() {

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.alpha = 0f
        holder.itemView.translationX = -holder.itemView.width.toFloat()

        val fadeIn = ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 0f, 1f)
        val slideIn = ObjectAnimator.ofFloat(holder.itemView, View.TRANSLATION_X, -holder.itemView.width.toFloat(), 0f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(fadeIn, slideIn)
        animatorSet.duration = addDuration
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                dispatchAddStarting(holder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchAddFinished(holder)
                holder.itemView.alpha = 1f
                holder.itemView.translationX = 0f
            }

            override fun onAnimationCancel(animation: Animator) {
                holder.itemView.alpha = 1f
                holder.itemView.translationX = 0f
            }
        })
        animatorSet.start()

        return false
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        val fadeOut = ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 1f, 0f)
        val slideOut = ObjectAnimator.ofFloat(holder.itemView, View.TRANSLATION_X, 0f, holder.itemView.width.toFloat())

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(fadeOut, slideOut)
        animatorSet.duration = removeDuration
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                dispatchRemoveStarting(holder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchRemoveFinished(holder)
                holder.itemView.alpha = 1f
                holder.itemView.translationX = 0f
            }

            override fun onAnimationCancel(animation: Animator) {
                holder.itemView.alpha = 1f
                holder.itemView.translationX = 0f
            }
        })
        animatorSet.start()

        return false
    }
}

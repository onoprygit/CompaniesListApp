package com.onopry.lifehackstudiotesttask.app.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.onopry.lifehackstudiotesttask.data.model.CompanyItem

class CompaniesDiffCallback(
    private val newList: List<CompanyItem>,
    private val oldList: List<CompanyItem>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.img == newItem.img && oldItem.name == newItem.name
    }
}
package com.onopry.lifehackstudiotesttask.app.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onopry.lifehackstudiotesttask.app.presentation.utils.NetworkConst
import com.onopry.lifehackstudiotesttask.data.model.CompanyItem
import com.onopry.lifehackstudiotesttask.databinding.ItemCompanyBinding

typealias OnCompanyClickListener = (id: String) -> Unit

class CompaniesPreviewAdapter(private val clickListener: OnCompanyClickListener) :
    RecyclerView.Adapter<CompaniesPreviewAdapter.Holder>() {

    private val companies = mutableListOf<CompanyItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemCompanyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val company = companies[position]
        holder.bind(company, clickListener)
    }

    override fun getItemCount() = companies.size

    fun setList(list: List<CompanyItem>) {
        val diffCallback = CompaniesDiffCallback(list, companies)
        val diffRes = DiffUtil.calculateDiff(diffCallback)
        companies.clear()
        companies.addAll(list)
        diffRes.dispatchUpdatesTo(this)
    }

    class Holder(private val binding: ItemCompanyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(company: CompanyItem, listener: OnCompanyClickListener) {
            with(binding) {
                companyName.text = company.name
                Glide.with(binding.root.context)
                    .load(NetworkConst.COMPANY_API_ENDPOINT + company.img)
                    .into(companyImage)
                root.setOnClickListener { listener.invoke(company.id) }
            }
        }
    }
}


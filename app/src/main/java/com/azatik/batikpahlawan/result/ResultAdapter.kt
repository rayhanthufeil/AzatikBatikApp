package com.azatik.batikpahlawan.result

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azatik.batikpahlawan.databinding.ActivityResultBinding
import com.azatik.batikpahlawan.databinding.ListResultBinding
import com.azatik.batikpahlawan.retrofit.MotifBatik
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ResultAdapter: RecyclerView.Adapter<ResultAdapter.ViewHolder>() {
    private val data = ArrayList<MotifBatik>()
    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    fun setdata(dataBatik: ArrayList<MotifBatik>) {
        data.clear()
        data.addAll(dataBatik)
        notifyDataSetChanged()


    }


    inner class ViewHolder(val binding: ListResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(batik: MotifBatik) {
            binding.root.setOnClickListener {
                onItemClickCallBack?.onItemClicked(batik)
            }
            binding.apply {
                tvItemMotifResult.text = batik.Motif_Batik
                Log.d("bisa", batik.Motif_Batik)
                tvItemPercentageResult.text = batik.persentase
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder((view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size


    interface OnItemClickCallBack {
        fun onItemClicked(data: MotifBatik)

    }
}
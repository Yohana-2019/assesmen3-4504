package org.d3if3118.hitungbmr.ui.lainnya

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3if3118.hitungbmr.R
import org.d3if3118.hitungbmr.databinding.ListItemBinding
import org.d3if3118.hitungbmr.model.Lainnya
import org.d3if3118.hitungbmr.network.DeskripsiApi

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val data = mutableListOf<Lainnya>()
    fun updateData(newData: List<Lainnya>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
    override fun getItemCount(): Int {
        return data.size
    }
    class ViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lainnya: Lainnya) = with(binding) {
            namaTextView.text = lainnya.nama
            latinTextView.text = lainnya.deskripsi
            Glide.with(imageView.context)
                .load(DeskripsiApi.getLainnyaUrl(lainnya.imageId))
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(imageView)

            root.setOnClickListener {
                val message = root.context.getString(R.string.message, lainnya.nama)
                Toast.makeText(root.context, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
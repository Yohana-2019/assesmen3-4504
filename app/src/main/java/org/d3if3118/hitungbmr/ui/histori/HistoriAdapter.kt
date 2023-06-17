package org.d3if3118.hitungbmr.ui.histori

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.d3if3118.hitungbmr.R
import org.d3if3118.hitungbmr.databinding.ItemHistoriBinding
import org.d3if3118.hitungbmr.db.BmrEntity
import org.d3if3118.hitungbmr.model.hitungBmr
import java.text.SimpleDateFormat
import java.util.*

class HistoriAdapter : ListAdapter<BmrEntity, HistoriAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<BmrEntity>() {
                override fun areItemsTheSame(
                    oldData: BmrEntity, newData: BmrEntity
                ): Boolean {
                    return oldData.id == newData.id
                }

                override fun areContentsTheSame(
                    oldData: BmrEntity, newData: BmrEntity
                ): Boolean {
                    return oldData == newData
                }
            }
    }

    class ViewHolder(
        private val binding: ItemHistoriBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormatter = SimpleDateFormat("dd MMMM yyyy",
            Locale("id", "ID")
        )

        fun bind(item: BmrEntity) = with(binding) {
            val hasilBmr = item.hitungBmr()

            val gender = root.context.getString(
                if (item.isMale) R.string.pria else R.string.wanita)

            genderTextView.text = gender.substring(0, 1)
            val colorRes = if (item.isMale) R.color.navy1 else R.color.navy2
            val circleBg = genderTextView.background as GradientDrawable
            circleBg.setColor(ContextCompat.getColor(root.context, colorRes))

            tanggalTextView.text = dateFormatter.format(Date(item.tanggal))

            bmrTextView.text = root.context.getString(R.string.hasil_x,
                hasilBmr.bmr, hasilBmr.bmrAktivitas, hasilBmr.beratIdeal)

            dataTextView.text = root.context.getString(R.string.data_x,
                item.berat, item.tinggi, gender)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoriBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

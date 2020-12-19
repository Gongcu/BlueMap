package com.bluemap.overcom_blue.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluemap.overcom_blue.model.DiagnosisModel
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.databinding.ItemDiagnosisBinding
import kotlinx.android.synthetic.main.item_diagnosis.view.*

class DiagnosisAdapter(val list: ArrayList<DiagnosisModel>) : RecyclerView.Adapter<DiagnosisAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiagnosisBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tag=position
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(val binding: ItemDiagnosisBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(model: DiagnosisModel){
            binding.model=model
            binding.root.so_disagree_btn.setOnClickListener {
                initImage()
                binding.root.so_disagree_btn.setImageResource(R.drawable.diagnosis_btn_clicked)
                list[binding.root.tag as Int].point=0
            }
            binding.root.disagree_btn.setOnClickListener {
                initImage()
                binding.root.disagree_btn.setImageResource(R.drawable.diagnosis_btn_clicked)
                list[binding.root.tag as Int].point=1
            }
            binding.root.agree_btn.setOnClickListener {
                initImage()
                binding.root.agree_btn.setImageResource(R.drawable.diagnosis_btn_clicked)
                list[binding.root.tag as Int].point=2
            }
            binding.root.so_agree_btn.setOnClickListener {
                initImage()
                binding.root.so_agree_btn.setImageResource(R.drawable.diagnosis_btn_clicked)
                list[binding.root.tag as Int].point=3
            }
        }

        private fun initImage(){
            binding.root.so_disagree_btn.setImageResource(R.drawable.diagnosis_btn)
            binding.root.disagree_btn.setImageResource(R.drawable.diagnosis_btn)
            binding.root.agree_btn.setImageResource(R.drawable.diagnosis_btn)
            binding.root.so_agree_btn.setImageResource(R.drawable.diagnosis_btn)
        }
    }


}
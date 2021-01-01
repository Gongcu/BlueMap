package com.bluemap.overcom_blue.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bluemap.overcom_blue.model.DiagnosisModel
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.databinding.ItemCompleteBinding
import com.bluemap.overcom_blue.databinding.ItemDiagnosisBinding
import com.bluemap.overcom_blue.fragment.DiagnosisFragment
import com.bluemap.overcom_blue.fragment.DiagnosisFragmentDirections
import kotlinx.android.synthetic.main.item_diagnosis.view.*

class DiagnosisAdapter(
        val list: ArrayList<DiagnosisModel>,
        private val fragment: DiagnosisFragment)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == R.layout.item_complete){
            val binding = ItemCompleteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            )
            CompleteViewHolder(binding)
        }else{
            val binding = ItemDiagnosisBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            )
            ItemViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.itemView.tag = position
            holder.bind(list[position])
        } else {
            holder.itemView.tag = list.size
        }
    }

    override fun getItemCount(): Int {
        return list.size+1
    }

    override fun getItemViewType(position: Int): Int {
        return if(position==list.size)
            R.layout.item_complete
        else
            position
    }

    inner class ItemViewHolder(val binding: ItemDiagnosisBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init {
            binding.root.so_disagree_btn.setOnClickListener (this)
            binding.root.disagree_btn.setOnClickListener (this)
            binding.root.agree_btn.setOnClickListener (this)
            binding.root.so_agree_btn.setOnClickListener (this)

        }
        fun bind(model: DiagnosisModel){
            binding.model=model
        }

        override fun onClick(v: View) {
            initImage()
            (v as ImageView).setImageResource(R.drawable.diagnosis_btn_clicked)
            when(v.id){
                R.id.so_disagree_btn -> list[binding.root.tag as Int].point=0
                R.id.disagree_btn -> list[binding.root.tag as Int].point=1
                R.id.agree_btn -> list[binding.root.tag as Int].point=2
                R.id.so_agree_btn -> list[binding.root.tag as Int].point=3
            }
        }

        private fun initImage(){
            binding.root.so_disagree_btn.setImageResource(R.drawable.diagnosis_btn)
            binding.root.disagree_btn.setImageResource(R.drawable.diagnosis_btn)
            binding.root.agree_btn.setImageResource(R.drawable.diagnosis_btn)
            binding.root.so_agree_btn.setImageResource(R.drawable.diagnosis_btn)
        }
    }

    inner class CompleteViewHolder(val binding: ItemCompleteBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var sum = 0
            var complete = true
            for(i in list.indices){
                if(list[i].point==-1) {
                    Toast.makeText(fragment.requireContext(), "설문을 모두 완료해주세요.", Toast.LENGTH_LONG).show()
                    complete = false
                    break
                }else{
                    sum+=list[i].point
                }
            }
            if(complete){
                val direction: NavDirections =
                        DiagnosisFragmentDirections.actionDiagnosisFragmentToResultFragment(sum)
                fragment.findNavController().navigate(direction)
            }
        }
    }
}
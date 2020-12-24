package com.bluemap.overcom_blue.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bluemap.overcom_blue.adapter.DiagnosisAdapter
import com.bluemap.overcom_blue.model.DiagnosisModel
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.util.Util
import kotlinx.android.synthetic.main.fragment_diagnosis.*
import kotlinx.android.synthetic.main.fragment_diagnosis.view.*

class DiagnosisFragment : Fragment() {
    val list = ArrayList<DiagnosisModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Util.setDiagnosisPaper(list)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diagnosis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.adapter = DiagnosisAdapter(list)

        view.result_btn.setOnClickListener {
            var sum:Int = 0
            var complete = true
            val resultList = (recycler_view.adapter as DiagnosisAdapter).list
            for(i in resultList.indices){
                if(resultList[i].point==-1) {
                    Toast.makeText(context, "설문을 모두 완료해주세요.", Toast.LENGTH_LONG).show()
                    complete=false
                    break
                }else
                    sum+=resultList[i].point
            }
            //if(complete){
                val direction: NavDirections =
                    DiagnosisFragmentDirections.actionDiagnosisFragmentToResultFragment(sum)
                findNavController().navigate(direction)
            //}
        }
    }
}
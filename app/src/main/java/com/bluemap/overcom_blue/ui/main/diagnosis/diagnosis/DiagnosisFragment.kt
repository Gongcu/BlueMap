package com.bluemap.overcom_blue.ui.main.diagnosis.diagnosis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluemap.overcom_blue.model.DiagnosisModel
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.util.Util
import kotlinx.android.synthetic.main.fragment_diagnosis.*

class DiagnosisFragment : Fragment() {
    private val list = ArrayList<DiagnosisModel>()
    private val adapter: DiagnosisAdapter by lazy{
        DiagnosisAdapter(list, this@DiagnosisFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Util.setDiagnosisPaper(list)
        return inflater.inflate(R.layout.fragment_diagnosis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
    }
}
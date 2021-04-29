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
        setDiagnosisPaper(list)
        return inflater.inflate(R.layout.fragment_diagnosis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
    }

    private fun setDiagnosisPaper(list: ArrayList<DiagnosisModel>){
        //1~10
        list.add(DiagnosisModel("나는 슬픔을 느낀다."))
        list.add(DiagnosisModel("나는 앞날에 대해 낙담한다."))
        list.add(DiagnosisModel("나는 실패자라고 느낀다."))
        list.add(DiagnosisModel("나는 일상생활에 불만족스럽다."))
        list.add(DiagnosisModel("나는 죄책감을 자주 느낀다."))
        list.add(DiagnosisModel("나는 벌을 받고 있다고 느낀다."))
        list.add(DiagnosisModel("나는 내 자신에게 자주 실망한다."))
        list.add(DiagnosisModel("나는 내가 다른 사람보다 못하다고 느낀다."))
        list.add(DiagnosisModel("나는 자살을 생각한다."))
        list.add(DiagnosisModel("나는 자주 운다."))

        //11~21
        list.add(DiagnosisModel("나는 전보다 짜증이 많다."))
        list.add(DiagnosisModel("나는 다른 사람들에게 관심이 없다."))
        list.add(DiagnosisModel("나는 결정을 잘 내리지 못한다."))
        list.add(DiagnosisModel("과어에 비해 내 모습이 나빠졌다고 생각한다."))
        list.add(DiagnosisModel("나는 일을 제대로 수행할 수 없다."))
        list.add(DiagnosisModel("나는 잠을 잘 못잔다."))
        list.add(DiagnosisModel("나는 항상 피곤하다."))
        list.add(DiagnosisModel("요즘 식욕이 거의 없다."))
        list.add(DiagnosisModel("과거에 비해 몸무게가 줄었다."))
        list.add(DiagnosisModel("과거에 비해 건강이 염려되어 아무 일도 할 수가 없다."))
        list.add(DiagnosisModel("과거에 비해 성욕이 줄었다."))

    }
}
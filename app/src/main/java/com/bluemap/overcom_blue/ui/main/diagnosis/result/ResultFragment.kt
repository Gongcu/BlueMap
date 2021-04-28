package com.bluemap.overcom_blue.ui.main.diagnosis.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bluemap.overcom_blue.R
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.android.synthetic.main.fragment_result.view.*

class ResultFragment : Fragment() {
    private val resultArgs by navArgs<ResultFragmentArgs>()
    private val array:Array<String> = arrayOf(
            "검사 결과 현재 우울하지 않은 상태입니다.",
            "검사 결과 현재 가벼운 우울 증세가 있는 상태입니다. 증세가 악화되기 전에 주변 정신 건간 증진 센터에 방문하는 것도 좋은 방법입니다.",
            "검사 결과 현재 중증도의 우울 증세가 있는 상태입니다. 증세가 더 악화되기 전에 주변 정신 건간 증진 센터 방문을 권고드립니다.",
            "검사 결과 현재 심한 우울 증세가 있는 상태입니다. 최대한 빨리 주변 정신 건간 증진 센터에 방문하는 것을 권고드립니다.",
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        total_point_text_view.text=resultArgs.totalPoint.toString()
        when(resultArgs.totalPoint){
            in 0..9 -> result_text_view.text=array[0]
            in 10..15 -> result_text_view.text=array[1]
            in 16..23 -> result_text_view.text=array[2]
            in 24..100 -> result_text_view.text=array[3]
        }
        view.move_map_btn.setOnClickListener {
            val direction: NavDirections =
                ResultFragmentDirections.actionResultFragmentToMapFragment()
            findNavController().navigate(direction)
        }
    }

}
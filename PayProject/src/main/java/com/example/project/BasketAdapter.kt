package com.example.project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.ItemRecyclerBinding

class MyViewHolder(val binding: ItemRecyclerBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(orderData: OrderActivity.OrderData) {
        val item = orderData.item.joinToString(", ")
        val count = orderData.count.toString()
        val price = "%,d원".format(orderData.price)

        binding.itemOrder.text = item
        binding.itemCount.text = count
        binding.itemPrice.text = price
    }
}

class OrderAdapter(val datas: MutableList<OrderActivity.OrderData>, val changePrice: BasketActivity): RecyclerView.Adapter<MyViewHolder>() {

    override fun getItemCount(): Int {
        return datas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
        = MyViewHolder(ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = datas[position]
        holder.bind(data)

        //삭제버튼 누를시 recyclerView의 항목 삭제와 합계 금액 동시에 변경
        holder.binding.btnItemDelete.setOnClickListener {
            datas.remove(data)
            notifyDataSetChanged()
            updataPrice()
        }

        //체크박스 누를시 recyclerView의 항목 체크 및 합계금액 동시에 변경
        holder.binding.itemCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            data.isChecked = isChecked
            uncheckPrice()
        }

        // 변경 된 체크 상태 대로 UI 업데이트
        holder.binding.itemCheck.isChecked = data.isChecked
    }

    //합계 금액 변경 함수
    fun updataPrice():Int {
        val totalPrice = datas.sumOf { it.price }
        changePrice.binding.totalPrice.text = "%,d원".format(totalPrice)
        return totalPrice
    }

    //체크 박스 해제 시 합계 금액 변경 함수
    fun uncheckPrice():Int {
        val totalPrice = datas.filter { it.isChecked }.sumOf { it.price }
        changePrice.binding.totalPrice.text = "%,d원".format(totalPrice)
        return totalPrice
    }
}

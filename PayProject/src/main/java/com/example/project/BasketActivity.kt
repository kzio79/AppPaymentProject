package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.databinding.ActivityBasketBinding
import com.example.project.databinding.CustomDialogBinding

class BasketActivity: AppCompatActivity() {

    lateinit var binding: ActivityBasketBinding
    lateinit var orderAdapter: OrderAdapter
    lateinit var customDialogBinding: CustomDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Adapter를 사용하여 리퀘스트뷰에 목록 활성화
        binding.basketRecycler.layoutManager = LinearLayoutManager(this)
        orderAdapter = OrderAdapter(OrderSaved.orders, this)
        binding.basketRecycler.adapter = orderAdapter
        binding.basketRecycler.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        var totalPrice = OrderSaved.orders.sumOf { it.price }

        binding.totalPrice.text = "%,d원".format(totalPrice)

        //주문화면 버튼 클릭
        binding.order.setOnClickListener {
            intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }

        //결제하기 버튼 클릭
        binding.payment.setOnClickListener {

            customDialogBinding = CustomDialogBinding.inflate(layoutInflater)
            if(totalPrice <= 0){
                dialog()
                return@setOnClickListener
            } else if(orderAdapter.uncheckPrice() <= 0) {
                dialog()
                return@setOnClickListener
            }else {
                totalPrice = OrderAdapter(OrderSaved.orders, this).uncheckPrice()
                intent = Intent(this, AuthActivity::class.java)
                intent.putExtra("price",totalPrice)
                startActivity(intent)
            }
        }
    }

    fun dialog() {

        val alertDialog = AlertDialog.Builder(this)
            .setView(customDialogBinding.customDialog)
            .create()

        val dialogTitle = customDialogBinding.dialogTitle
        val dialogMessage =customDialogBinding.dialogMessage
        val dialogBtn = customDialogBinding.dialogButton

        dialogTitle.text = "주문금액을 확인해 주세요"
        dialogMessage.visibility = View.GONE
        dialogBtn.visibility = View.GONE

        alertDialog.show()
    }

    override fun onBackPressed() {
        intent = Intent(this, OrderActivity::class.java)
        intent.putExtra("selcount",OrderSaved.orders.size)
        startActivity(intent)
    }

    fun onLinearLayoutClicked(view: View) {

        view.setOnClickListener {
            val parentView = view.parent as? ViewGroup
            if (parentView is LinearLayout) {
                var checkBox: CheckBox = parentView.findViewById(R.id.item_check)
                checkBox.isChecked = !checkBox.isChecked
            }
        }
    }
}

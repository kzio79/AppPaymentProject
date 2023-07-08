package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityOrderBinding
import com.example.project.databinding.CustomDialogBinding


class OrderActivity: AppCompatActivity() {

    lateinit var binding: ActivityOrderBinding
    private lateinit var customDialogBinding: CustomDialogBinding

    class OrderData(var item: ArrayList<String>, var count: Int, var price: Int,var isChecked: Boolean = true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //장바구니 모양 클릭
        binding.orderBasket.setOnClickListener {
            val intent = Intent(this, BasketActivity::class.java)
            startActivity(intent)
        }

        //가격 예시
        val itemPrices = mapOf(
            "아이스 커피" to 1800,
            "핫 커피" to 2000,
            "레몬 에이드" to 3000,
            "오렌지 쥬스" to 3500,
            "미네럴 워터" to 2500,
        )
        var selectedItemPrice = 0

        //음료이미지 클릭시 drawerLayout동작
        binding.icecoffee.setOnClickListener {
            selectedItemPrice = itemPrices.getValue("아이스 커피")
            if (!binding.orderDrawer.isDrawerOpen(Gravity.RIGHT)) {
                binding.orderDrawer.openDrawer(Gravity.RIGHT)
            }
            binding.drawerIcecoffee.setImageResource(R.drawable.menu_icecoffee)
            binding.drawerIcecoffeeText.setText("아이스 커피")
        }

        binding.coffee.setOnClickListener {
            selectedItemPrice = itemPrices.getValue("핫 커피")
            if (!binding.orderDrawer.isDrawerOpen(Gravity.RIGHT)) {
                binding.orderDrawer.openDrawer(Gravity.RIGHT)
            }
            binding.drawerIcecoffee.setImageResource(R.drawable.menu_coffee)
            binding.drawerIcecoffeeText.setText("핫 커피")
        }

        binding.lemonade.setOnClickListener {
            selectedItemPrice = itemPrices.getValue("레몬 에이드")
            if (!binding.orderDrawer.isDrawerOpen(Gravity.RIGHT)) {
                binding.orderDrawer.openDrawer(Gravity.RIGHT)
            }
            binding.drawerIcecoffee.setImageResource(R.drawable.menu_lemonade)
            binding.drawerIcecoffeeText.setText("레몬 에이드")
        }

        binding.orange.setOnClickListener {
            selectedItemPrice = itemPrices.getValue("오렌지 쥬스")
            if (!binding.orderDrawer.isDrawerOpen(Gravity.RIGHT)) {
                binding.orderDrawer.openDrawer(Gravity.RIGHT)
            }
            binding.drawerIcecoffee.setImageResource(R.drawable.menu_orange)
            binding.drawerIcecoffeeText.setText("오렌지 쥬스")
        }

        binding.water.setOnClickListener {
            selectedItemPrice = itemPrices.getValue("미네럴 워터")
            if (!binding.orderDrawer.isDrawerOpen(Gravity.RIGHT)) {
                binding.orderDrawer.openDrawer(Gravity.RIGHT)
            }
            binding.drawerIcecoffee.setImageResource(R.drawable.menu_water)
            binding.drawerIcecoffeeText.setText("미네럴 워터")
        }

        //수량 카운터 표시
        var counter: Int = 0
        var counterNum = binding.orderCount
        val priceTotal = binding.orderTotal

        //-버튼 클릭
        binding.orderMinus.setOnClickListener {
            if (counter < 1) {
                return@setOnClickListener
            } else {
                counter-- //숫자는 1감소
                counterNum.text = counter.toString() //상단의 결과 값은 증가된 숫자가 문자로 표시된 것
                var price = selectedItemPrice * counter
                priceTotal.text = "%,d원".format(price)
            }
        }

        //+버튼 클릭
        binding.orderPlus.setOnClickListener {
            counter++ //숫자는 1증가
            counterNum.text = counter.toString() //상단의 결과 값은 증가된 숫자가 문자로 표시된 것
            var price = selectedItemPrice * counter
            priceTotal.text = "%,d원".format(price)
        }

        //주문하기 버튼
        binding.orderItem.setOnClickListener {
            customDialogBinding = CustomDialogBinding.inflate(layoutInflater)
            if (counter <= 0) {
                try {
                    dialog()
                }catch (e:Exception){
                    Log.d("pay","0이하 에러 : $e")
                }
            } else {
                try {
                    val selectedItem =
                        itemPrices.filter { it.value == selectedItemPrice }.keys.firstOrNull()
                    // 선택한 아이템만 리스트에 추가합니다.
                    var item = selectedItem?.let { listOf(it) } ?: emptyList()
                    var count = counter
                    var price = selectedItemPrice * counter

                  //OrderData에 저장

                    if(item != null && price != 0) {
                        var itemExists = false

                        if(OrderSaved.orders.isEmpty()){
                            OrderSaved.orders.add(OrderData(ArrayList<String>(item), count, price))
                        } else {
                            OrderSaved.orders.forEach { order ->
                                if (item.equals(order.item)) {
                                    order.count += count
                                    order.price += price
                                    itemExists = true
                                }
                            }
                            // 동일한 'item'이 없는 경우 새 OrderData 객체를 추가합니다.
                            if (!itemExists) {
                                OrderSaved.orders.add(OrderData(ArrayList<String>(item), count, price))
                            }
                        }
                    }

                    val orderIntent = Intent(this, OrderActivity::class.java)
                    startActivity(orderIntent)
                    Log.d("pay", "넘어가는 값 : item : $item, count : $count, price: $price")

                }catch (e:Exception){
                    Log.d("pay","0이상 에러 : ${e}")
                }
            }
        }

        // 장바구니 카운트
        val selcount = OrderSaved.orders.size
        binding.orderBasketCount.text = selcount.toString()

    }

        fun dialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setView(customDialogBinding.customDialog)
            .create()

        val dialogTitle = customDialogBinding.dialogTitle
        val dialogMessage =customDialogBinding.dialogMessage
        val dialogBtn = customDialogBinding.dialogButton

        dialogTitle.text = "주문을 해주세요"
        dialogMessage.visibility = View.GONE
        dialogBtn.visibility = View.GONE

        alertDialog.show()
    }
}


package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityPayBinding
import com.example.project.databinding.CustomDialogBinding
import com.tosspayments.paymentsdk.PaymentWidget

class PaymentActivity:AppCompatActivity() {
    lateinit var binding: ActivityPayBinding
    private lateinit var paymentWidget: PaymentWidget
    private lateinit var paymentResultLauncher: ActivityResultLauncher<Intent>
    lateinit var customDialogBinding: CustomDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId : String = intent.getStringExtra("orderId")!!
        val price = intent.getIntExtra("price",0)


        Log.d("orderId 확인", "$orderId")

        if(orderId == null || price == 0){
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }

        //clientKey는 고정값 반드시 해당 값으로만 해야 함 (test 용 key)
        // (클라이언트키)test_ck_7DLJOpm5Qrl2Za1wG503PNdxbWnY (시크릿 키)test_sk_oeqRGgYO1r5wbZxzNg23QnN2Eyaz
        //customerKey는 자유

        //위젯에 값전달
        paymentWidget = PaymentWidget(
            activity = this,
            clientKey = "test_ck_7DLJOpm5Qrl2Za1wG503PNdxbWnY",
            customerKey = "juhyun",
        )

        //위젯 생성하기 위해 생성
        val methodWidget = binding.paymentWidget

        paymentWidget.setMethodWidget(methodWidget)

        //위젯 화면에 렌더링


        //가격과 주문번호
        paymentWidget.renderPaymentMethodWidget(
            amount = price,
            orderId = orderId
        )
        paymentResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                //결제 결과 처리 코드
                customDialogBinding = CustomDialogBinding.inflate(layoutInflater)
                if (result.resultCode == 200) {
                    //결제 성공 처리 로직 작성
                    Log.d("result","결제 성공 !! ${result}")

                    dialog("결제가 되었습니다.","결제요청 결과",
                        customDialogBinding.dialogButton.setOnClickListener {
                            OrderSaved.orders.clear()
                            val restartintent = Intent(this, MainActivity::class.java)
                            startActivity(restartintent)

                    })
                   } else {
                    //결제 실패 처리 로직 작성
                    Log.d("result","결제 실패 !! ${result}")
                    dialog("결제에 실패하였습니다.","결제요청 결과",
                        customDialogBinding.dialogButton.setOnClickListener {
                            val restartintent = Intent(this, MainActivity::class.java)
                            startActivity(restartintent)
                    })
                }
            }
        binding.btnpay.setOnClickListener {
            paymentWidget.requestPayment(
                paymentResultLauncher,
                orderId,
                "주현",
                "mjh3365@naver.com",
                "hyun",
            )
        }
    }
    //dialog
    fun dialog(message:String, title:String, OnClickListener: Unit) {


        val alertDialog = AlertDialog.Builder(this)
            .setView(customDialogBinding.customDialog)
            .create()

            customDialogBinding.dialogTitle.text = title
            customDialogBinding.dialogMessage.text = message
            customDialogBinding.dialogButton.text = "확인"
            customDialogBinding.dialogMessage.visibility = View.VISIBLE
            customDialogBinding.dialogButton.visibility = View.VISIBLE

        alertDialog.show()
    }
}
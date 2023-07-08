import android.util.Log
import android.widget.Toast
import com.kakao.sdk.user.UserApiClient
import kotlin.random.Random

//package com.example.project.Auth
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import com.example.project.PaymentActivity
//import com.example.project.databinding.ActivityAuthBinding
//import com.example.project.databinding.CustomDialogBinding
//import com.kakao.sdk.common.KakaoSdk
//import com.kakao.sdk.user.UserApiClient
//import com.kakao.sdk.user.model.User
//import java.util.Timer
//import java.util.TimerTask
//
//class KakaoActivity : AppCompatActivity() {
//
//    lateinit var binding : ActivityAuthBinding
//    lateinit var customDialogBinding: CustomDialogBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAuthBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
        //카카오 인증
//        try {
//            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
//                if (error != null) {
//                    Toast.makeText(this,  "카카오인증을 해주세요", Toast.LENGTH_SHORT).show()
//                    changeVisibility("false")
//                    Log.e("kkang", "카카오로그인 실패", error)
//
//                }
//                else if (token != null) {
//                    changeVisibility("kakao")
//                    Log.d("kkang","카카오 로그인 성공")
//
//                    //사용자 정보 가져오기
//                    UserApiClient.instance.me { user, error ->
//                        if (error != null) {
//                            Log.w("kkang","카카오 이메일1 : ${user?.kakaoAccount?.email}")
//
//                        } else if(user != null){
//                            // 사용자 정보 가져오기 성공
//                            Log.w("kkang","카카오 이메일2 : ${user.kakaoAccount?.email}")
//                            Log.w("kkang","카카오 닉네임2 : ${user?.kakaoAccount?.profile?.nickname}")
//                        }
//                    }
//                }
//            }
//        } catch (e:Exception){
//            Log.d("pay","카카오 에러 : $e")
//        }
//
//    //화면 전환
//    fun changeVisibility(mode: String, timeout:Long = 30_000) {
//        customDialogBinding = CustomDialogBinding.inflate(layoutInflater)
//       if (mode === "kakao") {
//
//            binding.run {
//                autoLogout(timeout)
//                dialog("카카오 메일로 인증되었습니다.",
//                    customDialogBinding.dialogButton.setOnClickListener {
////                        val totalPrice = OrderSaved.orders.sumOf { it.price }
//                        val price = intent.getIntExtra("price",0)
//                        intent = Intent(this@KakaoActivity, PaymentActivity::class.java)
//                        intent.putExtra("price",price)
//                        startActivity(intent)
//                    })
//
//                googleAuth.visibility = View.GONE
//                kakaoAuth.visibility = View.GONE
//                naverAuth.visibility = View.GONE
//
//            }
//        }else if(mode === "false"){
//            binding.run {
//                googleAuth.visibility = View.VISIBLE
//                kakaoAuth.visibility = View.VISIBLE
//                naverAuth.visibility = View.VISIBLE
//            }
//        }
//
//
//    }
//    //인증 초기화 시간설정
//    private fun autoLogout(timeout: Long) {
//        val timer = Timer()
//
//        timer.schedule(object : TimerTask() {
//            override fun run() {
//                // 로그아웃 처리 및 인증 초기화 코드
//                runOnUiThread {
//                    clearAuthData()
//                    changeVisibility("false")
//                }
//            }
//        }, timeout)
//    }
//
//    //프로그램 종료후 설정
//    private fun clearAuthData() {
//
//        // kakao
//        KakaoSdk.init(this, "f4a2b2beb9681f0ab955cab9d6f52d39")
//    }
//
//    //dialog
//    fun dialog(Message:String ,onClickListener: Unit) {
//
//        val alertDialog = AlertDialog.Builder(this)
//            .setView(customDialogBinding.customDialog)
//            .create()
//
//        val dialogTitle = customDialogBinding.dialogTitle
//        val dialogMessage =customDialogBinding.dialogMessage
//        val dialogBtn = customDialogBinding.dialogButton
//
//        dialogTitle.text = "환영합니다"
//        dialogMessage.setText(Message)
//        dialogMessage.visibility = View.VISIBLE
//        dialogBtn.visibility = View.VISIBLE
//
//        alertDialog.show()
//    }
//payment에 들어갈 주문번호
//fun setorderid() : String{
//    var charset = ('0'..'9') + ('a'..'z') + ('A'..'Z')
//    val randomset = List(20) {charset[Random.nextInt(charset.size)]}.joinToString("")
//    return randomset
//}
//}
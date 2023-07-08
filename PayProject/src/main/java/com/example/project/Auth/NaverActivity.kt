import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

//package com.example.project.Auth
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import com.example.project.PaymentActivity
//import com.example.project.databinding.ActivityAuthBinding
//import com.example.project.databinding.CustomDialogBinding
//import com.navercorp.nid.NaverIdLoginSDK
//import com.navercorp.nid.oauth.NidOAuthBehavior
//import java.util.Timer
//import java.util.TimerTask
//
//class NaverActivity : AppCompatActivity() {
//
//    lateinit var binding : ActivityAuthBinding
//    lateinit var customDialogBinding: CustomDialogBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAuthBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//네이버 인증
//        var naverLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//        { result ->
//            try {
//                when (result.resultCode) {
//                    AppCompatActivity.RESULT_OK -> {
//                        // 성공
//                        Log.d("pay", "네이버 로그인 성공")
//                        NaverIdLoginSDK.getAccessToken()
//                        changeVisibility("naver")
//
//
//
//                        NidOAuthLogin().callProfileApi(object :
//                            NidProfileCallback<NidProfileResponse> {
//                            override fun onSuccess(response: NidProfileResponse) {
//                                Log.d("kkang", "네이버 메일 : ${response.profile?.email}")
//                            }
//
//                            override fun onFailure(httpStatus: Int, message: String) {
//                                Log.d("kkang", "네이버 메일 실패")
//                            }
//
//                            override fun onError(errorCode: Int, message: String) {
//                                onFailure(errorCode, message)
//                            }
//                        })
//
//
//                    }
//
//                    AppCompatActivity.RESULT_CANCELED -> {
//                        // 실패 or 에러
//                        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//                        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//                        Toast.makeText(this, "네이버 로그인 실패", Toast.LENGTH_SHORT).show()
//                        Log.d("pay", "네이버 로그인 에러 :  ${errorCode}, $errorDescription")
//                    }
//                }
//            } catch (e: Exception) {
//                Toast.makeText(this, "네이버 회원 인증을 해주세요", Toast.LENGTH_SHORT).show()
//                Log.d("pay", "네이버 인증 오류 : $e")
//                changeVisibility("false")
//            }
//        }
//
//        //네이버 본인 인증
//            // 네이버 로그인 Launcher 실행
//            // Initialize NAVER id login SDK
//        binding.naverAuth.setOnClickListener {
//            NaverIdLoginSDK.apply {
//                showDevelopersLog(true)
//            }
//                NaverIdLoginSDK.behavior = NidOAuthBehavior.DEFAULT
//                NaverIdLoginSDK.authenticate(this, naverLauncher)
//        }
//    }
//
//    //화면 전환
//    fun changeVisibility(mode: String, timeout:Long = 30_000) {
//        customDialogBinding = CustomDialogBinding.inflate(layoutInflater)
//        if (mode === "naver") {
//
//            binding.run {
//                autoLogout(timeout)
//                dialog("네이버 메일로 인증되었습니다.",
//                    customDialogBinding.dialogButton.setOnClickListener {
//                        val price = intent.getIntExtra("price",0)
//                    intent = Intent(this@NaverActivity, PaymentActivity::class.java)
//                    intent.putExtra("price",price)
//                    startActivity(intent)
//                })
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
//        //naver
//        NaverIdLoginSDK.logout()
//        NaverIdLoginSDK.initialize(this, "pfBz7O5_ubi8mAo7VffZ", "nb74wImmkS", "네이버 아이디로 로그인")
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
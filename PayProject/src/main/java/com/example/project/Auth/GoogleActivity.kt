//package com.example.project.Auth
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import com.example.project.MyApplication
//import com.example.project.PaymentActivity
//import com.example.project.R
//import com.example.project.databinding.ActivityAuthBinding
//import com.example.project.databinding.CustomDialogBinding
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.api.ApiException
//import com.google.firebase.FirebaseApp
//import com.google.firebase.auth.GoogleAuthProvider
//import java.util.Timer
//import java.util.TimerTask
//
//class GoogleActivity : AppCompatActivity() {
//
//    lateinit var binding : ActivityAuthBinding
//    lateinit var customDialogBinding: CustomDialogBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAuthBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        if (MyApplication.googleCheckAuth()) {
//            changeVisibility("google")
//        } else {
//            changeVisibility("false")
//        }
//
//        //구글 인증 로그인
//        var googleLauncher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        )
//        {
//            FirebaseApp.initializeApp(this)
//            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
//            try {
//                val googleAccount = task.getResult(ApiException::class.java)
//                val googleCredential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
//                MyApplication.googleAuth.signInWithCredential(googleCredential)
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            MyApplication.googleEmail = googleAccount.email
//                            changeVisibility("google")
//                        } else {
//                            Toast.makeText(this, "구글회원 인증을 해주세요", Toast.LENGTH_SHORT).show()
//                            changeVisibility("false")
//                        }
//                    }
//
//            } catch (e: Exception) {
//                Toast.makeText(this, "인증을 해주세요", Toast.LENGTH_SHORT).show()
//                Log.d("pay", "에러표시 : $e")
//                changeVisibility("false")
//            }
//        }
//
//        //구글 본인 인증
//        binding.googleAuth.setOnClickListener {
////            FirebaseApp.initializeApp(this)
//            val gso = GoogleSignInOptions
//                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build()
//
//            val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
//            googleLauncher.launch(signInIntent)
//        }
//    }
//
//        //화면 전환
//        fun changeVisibility(mode: String, timeout: Long = 30_000) {
//            customDialogBinding = CustomDialogBinding.inflate(layoutInflater)
//            if (mode === "google") {
//                binding.run {
//                    autoLogout(timeout)
//                    dialog("구글 메일로 인증되었습니다.",
//                        customDialogBinding.dialogButton.setOnClickListener {
//
//                            val price = intent.getIntExtra("price", 0)
//                            intent = Intent(this@GoogleActivity, PaymentActivity::class.java)
//                            intent.putExtra("price", price)
//                            startActivity(intent)
//                        })
//
//                    googleAuth.visibility = View.GONE
//                    kakaoAuth.visibility = View.GONE
//                    naverAuth.visibility = View.GONE
//
//                }
//            } else if (mode === "false") {
//                binding.run {
//                    googleAuth.visibility = View.VISIBLE
//                    kakaoAuth.visibility = View.VISIBLE
//                    naverAuth.visibility = View.VISIBLE
//                }
//            }
//        }
//
//
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
//        // google
//        MyApplication.googleAuth.signOut()
//        MyApplication.googleEmail = null
//        FirebaseApp.initializeApp(this)
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
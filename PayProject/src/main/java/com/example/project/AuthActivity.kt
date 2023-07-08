package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.project.MyApplication.Companion.googleAuth
import com.example.project.MyApplication.Companion.googleEmail
import com.example.project.MyApplication.Companion.googleName
import com.example.project.databinding.ActivityAuthBinding
import com.example.project.databinding.CustomDialogBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthBehavior
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import java.util.Timer
import java.util.TimerTask
import kotlin.random.Random

class AuthActivity : AppCompatActivity() {

    lateinit var binding : ActivityAuthBinding
    lateinit var customDialogBinding: CustomDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeVisibility("false")

        //구글 인증 로그인
        var googleLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val googleAccount = task.getResult(ApiException::class.java)
                val googleCredential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
                googleAuth.signInWithCredential(googleCredential)
                    .addOnCompleteListener(this) { task ->
                        if(task.isSuccessful){
                           googleEmail = googleAccount.email
                            changeVisibility("google")
                        }else{
                            Toast.makeText(this,"구글회원 인증을 해주세요",Toast.LENGTH_SHORT).show()
                           changeVisibility("false")
                        }
                    }

            }catch (e:Exception){
                Toast.makeText(this,"인증을 해주세요",Toast.LENGTH_SHORT).show()
                Log.d("pay","에러표시 : $e")
                changeVisibility("false")
            }
        }

        //네이버 인증
        var naverLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
                try {
                    when(result.resultCode) {
                        RESULT_OK -> {
                            // 성공
                            Log.d("pay","네이버 로그인 성공")
                            NaverIdLoginSDK.getAccessToken()
                            changeVisibility("naver")

                            //네이저 사용자 정보 가져오기
                            NidOAuthLogin().callProfileApi(object :
                                NidProfileCallback<NidProfileResponse> {
                                override fun onSuccess(response: NidProfileResponse) {
                                    Log.d("kkang", "네이버 메일 : ${response.profile?.email}")
                                }

                                override fun onFailure(httpStatus: Int, message: String) {
                                    Log.d("kkang", "네이버 메일 실패")
                                }

                                override fun onError(errorCode: Int, message: String) {
                                    onFailure(errorCode, message)
                                }
                            })


                        }
                        RESULT_CANCELED -> {
                            // 실패 or 에러
                            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                            Toast.makeText(this, "네이버 로그인 실패", Toast.LENGTH_SHORT).show()
                            Log.d("pay","네이버 로그인 에러 :  ${errorCode}, $errorDescription")
                        }
                    }
                }catch (e:Exception){
                    Toast.makeText(this,"네이버 회원 인증을 해주세요",Toast.LENGTH_SHORT).show()
                    Log.d("pay","네이버 인증 오류 : $e")
                    changeVisibility("false")
                }
        }

        //구글 본인 인증
        binding.googleAuth.setOnClickListener {
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
            googleLauncher.launch(signInIntent)
            Log.d("kkang","구글 로그인 성공 : $googleEmail")
        }

        //카카오 본인 인증
        binding.kakaoAuth.setOnClickListener {
            try {
                UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                    if (error != null) {
                        Toast.makeText(this,  "카카오인증을 해주세요", Toast.LENGTH_SHORT).show()
                        changeVisibility("false")
                        Log.e("kkang", "카카오로그인 실패", error)

                    }
                    else if (token != null) {
                        changeVisibility("kakao")
                        Log.d("kkang","카카오 로그인 성공")

                        //사용자 정보 가져오기
                        UserApiClient.instance.me { user, error ->
                            var kakaoEmail = user?.kakaoAccount?.email
                            if (error != null) {
                                Log.w("kkang","카카오 이메일1 : ${kakaoEmail}")

                            } else if(user != null){
                                // 사용자 정보 가져오기 성공
                                Log.w("kkang","카카오 이메일2 : ${user.kakaoAccount?.email}")
                                Log.w("kkang","카카오 닉네임2 : ${user?.kakaoAccount?.profile?.nickname}")
                            }
                       }
                    }
                }
            } catch (e:Exception){
                Log.d("pay","카카오 에러 : $e")
            }
        }

        //네이버 본인 인증
            // 네이버 로그인 Launcher 실행
            // Initialize NAVER id login SDK
        binding.naverAuth.setOnClickListener {
            NaverIdLoginSDK.apply {
                showDevelopersLog(true)
            }
                NaverIdLoginSDK.behavior = NidOAuthBehavior.DEFAULT
                NaverIdLoginSDK.authenticate(this, naverLauncher)
            Log.d("kkang","네이버 로그인 성공")
        }
    }

    //화면 전환
    fun changeVisibility(mode: String, timeout:Long = 5_000) {
        customDialogBinding = CustomDialogBinding.inflate(layoutInflater)
        if (mode === "google") {
            binding.run {
                autoLogout(timeout)
                dialog("구글 메일로 인증되었습니다.",
                    customDialogBinding.dialogButton.setOnClickListener {

                        val price = intent.getIntExtra("price",0)
                        intent = Intent(this@AuthActivity, PaymentActivity::class.java)
                        intent.putExtra("price",price)
                        intent.putExtra("orderId", setorderid())
                        startActivity(intent)
                    })

                googleAuth.visibility = View.GONE
                kakaoAuth.visibility = View.GONE
                naverAuth.visibility = View.GONE

            }
        } else if (mode === "kakao") {

            binding.run {
                autoLogout(timeout)
                dialog("카카오 메일로 인증되었습니다.",
                    customDialogBinding.dialogButton.setOnClickListener {
                        val price = intent.getIntExtra("price",0)
                        intent = Intent(this@AuthActivity, PaymentActivity::class.java)
                        intent.putExtra("price",price)
                        intent.putExtra("orderId", setorderid())
                        startActivity(intent)
                    })

                googleAuth.visibility = View.GONE
                kakaoAuth.visibility = View.GONE
                naverAuth.visibility = View.GONE

            }
        }else if (mode === "naver") {

            binding.run {
                autoLogout(timeout)
                dialog("네이버 메일로 인증되었습니다.",
                    customDialogBinding.dialogButton.setOnClickListener {
                        val price = intent.getIntExtra("price",0)
                    intent = Intent(this@AuthActivity, PaymentActivity::class.java)
                    intent.putExtra("orderId", setorderid())
                    intent.putExtra("price",price)
                    startActivity(intent)
                })

                googleAuth.visibility = View.GONE
                kakaoAuth.visibility = View.GONE
                naverAuth.visibility = View.GONE

            }
        }else if(mode === "false"){
            binding.run {
                googleAuth.visibility = View.VISIBLE
                kakaoAuth.visibility = View.VISIBLE
                naverAuth.visibility = View.VISIBLE
            }
        }
    }
    //인증 초기화 시간설정
    private fun autoLogout(timeout: Long) {
        val timer = Timer()

        timer.schedule(object : TimerTask() {
            override fun run() {
                // 로그아웃 처리 및 인증 초기화 코드
                runOnUiThread {
                    clearAuthData()
                    changeVisibility("false")
                }
            }
        }, timeout)
    }

    //프로그램 종료후 설정
    private fun clearAuthData() {
        // google
        googleAuth.signOut()

        // kakao
        UserApiClient.instance.logout {  }

        //naver
        NaverIdLoginSDK.logout()
    }

    //dialog
    fun dialog(Message:String ,onClickListener: Unit) {

        val alertDialog = AlertDialog.Builder(this)
            .setView(customDialogBinding.customDialog)
            .create()

        val dialogTitle = customDialogBinding.dialogTitle
        val dialogMessage =customDialogBinding.dialogMessage
        val dialogBtn = customDialogBinding.dialogButton

        dialogTitle.text = "환영합니다"
        dialogMessage.setText(Message)
        dialogMessage.visibility = View.VISIBLE
        dialogBtn.visibility = View.VISIBLE

        alertDialog.show()
    }

    //payment에 들어갈 주문번호
    fun setorderid() : String{
        var charset = ('0'..'9') + ('a'..'z') + ('A'..'Z')
        val randomset = List(20) {charset[Random.nextInt(charset.size)]}.joinToString("")
        return randomset
    }
}
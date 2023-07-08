package com.example.project


import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK

class MyApplication: MultiDexApplication() {
    //구글 인증
    companion object {
        lateinit var googleAuth: FirebaseAuth

        var googleEmail: String? = null
        var googleName:String? = null
        fun googleCheckAuth(): Boolean {
            val currentUser = googleAuth.currentUser
            return currentUser?.let {
                googleEmail = currentUser.email
                googleName = currentUser.uid
                if (currentUser.isEmailVerified) {
                    true
                } else {
                    false
                }
            } ?: let {
                false
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        googleAuth = Firebase.auth

        // google SDK 초기화
        FirebaseApp.initializeApp(this)

        // kakao SDK 초기화
        KakaoSdk.init(this, getString(R.string.kakao_app_key))

        //naver SDK 초기화
        NaverIdLoginSDK.initialize(this, "pfBz7O5_ubi8mAo7VffZ", "nb74wImmkS", "네이버 아이디로 로그인")
    }
}



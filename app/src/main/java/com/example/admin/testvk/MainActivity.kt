package com.example.admin.testvk

import android.content.Intent
import android.os.Bundle
import com.example.admin.testvk.common.TestApp
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import com.vk.sdk.util.VKUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val friendsIntent by lazy { Intent(this, FriendsActivity::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        authButton.setOnClickListener {
            VKSdk.login(this, "friends", "groups", "photos")
        }

        val fingerprints = VKUtil.getCertificateFingerprint(this, this.packageName)
        System.out.println(fingerprints[0])
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object: VKCallback<VKAccessToken> {
            override fun onResult(res: VKAccessToken?) {
                friendsIntent.putExtra(TestApp.USERIDKEY, res?.userId ?: "")
                startActivity(friendsIntent)
                finish()
            }

            override fun onError(error: VKError?) {
                showError("Error", error.toString())
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}

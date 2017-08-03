package com.example.admin.testvk

import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.MenuItem
import com.example.admin.testvk.common.GlideApp
import com.vk.sdk.api.model.VKApiUserFull
import kotlinx.android.synthetic.main.activity_friends_detail.*

class FriendsDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_detail)
        setSupportActionBar(detail_toolbar)

        // Show the Up button in the action bar.
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val arguments = Bundle()
        val user = intent.getParcelableExtra<VKApiUserFull>(FriendsDetailFragment.ARG_ITEM)
        arguments.putParcelable(FriendsDetailFragment.ARG_ITEM, user)
        toolbar_layout.title = "${user.first_name}  ${user.last_name}"
        GlideApp
                .with(this)
                .load(user?.photo_max_orig)
                .centerCrop()
                .into(profileImage)

        if (savedInstanceState == null) {
            val fragment = FriendsDetailFragment()
            fragment.arguments = arguments
            supportFragmentManager.beginTransaction()
                    .add(R.id.friends_detail_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //


            NavUtils.navigateUpFromSameTask(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

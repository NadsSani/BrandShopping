package com.nads.shopping.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nads.shopping.R
import com.nads.shopping.utils.ContextWrapper
import com.nads.shopping.utils.PREF_KEY_SELECTED_LANGUAGE
import com.nads.shopping.utils.defaultPrefs
import com.nads.shopping.utils.get
import java.util.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun attachBaseContext(newBase: Context?) {
        val language: String? = defaultPrefs(newBase!!)[PREF_KEY_SELECTED_LANGUAGE, "en"]
        super.attachBaseContext(ContextWrapper.wrap(newBase, Locale(language)))
    }
}



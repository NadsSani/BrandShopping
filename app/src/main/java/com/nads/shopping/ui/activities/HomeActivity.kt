package com.nads.shopping.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.nads.shopping.R
import com.nads.shopping.databinding.ActivityHomeBinding
import com.nads.shopping.ui.home.search.SearchDialog
import com.nads.shopping.utils.ContextWrapper.Companion.wrap
import com.nads.shopping.utils.PREF_KEY_SELECTED_LANGUAGE
import com.nads.shopping.utils.defaultPrefs
import com.nads.shopping.utils.get
import com.nads.shopping.viewmodels.HomeActivityViewModel
import com.nads.shopping.viewmodels.ViewModelFactory
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val activityViewModel: HomeActivityViewModel by viewModels { ViewModelFactory.getInstance() }
    private val navController by lazy { findNavController(R.id.homeNavigationHostFragment) }
    lateinit var progress: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.viewModel = activityViewModel
        binding.lifecycleOwner = this

        binding.bottomNav.setupWithNavController(navController)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.viewSearchBar.setOnClickListener {
            SearchDialog().show(supportFragmentManager, "search_dialog")
        }

        binding.ivSearch.setOnClickListener {
            SearchDialog().show(supportFragmentManager, "search_dialog")
        }



//
//        val builder: AlertDialog.Builder= AlertDialog.Builder(this)
//
//        val mView =  R.layout.alert_builder
//
//        builder?.setView(mView)
//        val dialog: AlertDialog? = builder?.create()
//
//        dialog?.show()
//         progress = binding.progressBar
//         progress.isVisible = true






    }

    fun onMenuSelected(item: MenuItem) {
        when (item.itemId) {

        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val language: String? = defaultPrefs(newBase!!)[PREF_KEY_SELECTED_LANGUAGE, "en"]
        super.attachBaseContext(wrap(newBase, Locale(language)))
    }


/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.forEach { fragment ->
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }*/
}
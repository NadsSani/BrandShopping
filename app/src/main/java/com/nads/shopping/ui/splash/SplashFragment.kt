package com.nads.shopping.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nads.shopping.R
import com.nads.shopping.ui.activities.HomeActivity
import com.nads.shopping.utils.Coroutines
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_splash, container, false)
        Coroutines.main {
            for (progress in 0..50) {
                delay(30)
            }
            requireActivity().finish()
//            if (requireActivity().getToken()?.isNullOrEmpty())
//            startActivity(Intent(requireActivity(), LoginActivity::class.java))
//            else
                startActivity(Intent(requireActivity(), HomeActivity::class.java))
        }
        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SplashFragment()
    }
}
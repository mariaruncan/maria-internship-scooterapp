package com.internship.move.feature.splash

import android.content.Context.MODE_PRIVATE
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.util.Constants.SharedPref.KEY_APP_PREFERECES
import com.internship.move.util.Constants.SharedPref.KEY_HAS_VISITED_AUTHENTICATION
import com.internship.move.util.Constants.SharedPref.KEY_HAS_VISITED_ONBOARDING

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val navigationHandler: Handler = Handler(Looper.getMainLooper())
    private val navigationRunnableJob: Runnable = Runnable {
        val sharedPref = requireActivity().getSharedPreferences(KEY_APP_PREFERECES, MODE_PRIVATE)

        when (sharedPref.getBoolean(KEY_HAS_VISITED_ONBOARDING, false)) {
            true -> {
                when (sharedPref.getBoolean(KEY_HAS_VISITED_AUTHENTICATION, false)) {
                    true -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeGraph())
                    false -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToAuthenticationGraph())
                }
            }
            false -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingGraph())
        }
    }

    override fun onPause() {
        super.onPause()

        navigationHandler.removeCallbacks(navigationRunnableJob)
    }

    override fun onResume() {
        super.onResume()

        navigationHandler.postDelayed(navigationRunnableJob, SPLASH_FRAGMENT_DELAY)
    }


    companion object {
        private const val SPLASH_FRAGMENT_DELAY = 2000L
    }
}
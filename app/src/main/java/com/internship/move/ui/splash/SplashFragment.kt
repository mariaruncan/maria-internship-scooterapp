package com.internship.move.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModel()
    private val navigationHandler: Handler = Handler(Looper.getMainLooper())
    private val navigationRunnableJob: Runnable = Runnable {
        val hasVisitedOnboarding = viewModel.getHasViewedOnboarding()
        val isLoggedIn = viewModel.getIsLoggedIn()

        when {
            hasVisitedOnboarding and isLoggedIn -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeGraph())
            hasVisitedOnboarding -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToAuthenticationGraph())
            else -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingGraph())
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
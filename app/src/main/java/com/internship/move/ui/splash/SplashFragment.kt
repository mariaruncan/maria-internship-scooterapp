package com.internship.move.ui.splash

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModel()
    private lateinit var job: Job

    override fun onResume() {
        super.onResume()
        job = CoroutineScope(Dispatchers.Main).launch {
            delay(SPLASH_FRAGMENT_DELAY)
            val hasVisitedOnboarding = viewModel.hasViewedOnboarding
            val isLoggedIn = viewModel.isLoggedIn

            when {
                hasVisitedOnboarding and isLoggedIn -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeGraph())
                hasVisitedOnboarding -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToAuthenticationGraph())
                else -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingGraph())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    companion object {
        private const val SPLASH_FRAGMENT_DELAY = 2000L
    }
}
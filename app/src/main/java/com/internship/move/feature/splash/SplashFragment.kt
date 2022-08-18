package com.internship.move.feature.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.feature.authentication.RegisterFragmentDirections
import com.internship.move.feature.onboarding.OnboardingFragmentDirections

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPref = requireActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
            )
            val all = sharedPref.all
            val hasVisitedOnboarding = sharedPref.getBoolean(
                getString(R.string.has_visited_onboarding_key), false
            )
            if (!hasVisitedOnboarding) {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingGraph())
            } else {
                val hasVisitedAuthentication = sharedPref.getBoolean(
                    getString(R.string.has_visited_authentication_key), false
                )
                if (!hasVisitedAuthentication) {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToAuthenticationGraph())
                } else {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeGraph())
                }
            }
        }, SPLASH_FRAGMENT_DELAY)
    }

    companion object {
        private const val SPLASH_FRAGMENT_DELAY = 1500L
    }
}
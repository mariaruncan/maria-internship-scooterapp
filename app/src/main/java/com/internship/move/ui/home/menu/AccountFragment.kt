package com.internship.move.ui.home.menu

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentAccountBinding
import com.internship.move.ui.home.UserViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.navigation.koinNavGraphViewModel

class AccountFragment : Fragment(R.layout.fragment_account) {

    private val binding by viewBinding(FragmentAccountBinding::bind)
    private val userViewModel: UserViewModel by koinNavGraphViewModel(R.id.home_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = userViewModel.currentUser.value ?: return
        binding.usernameTIET.setText(user.name)
        binding.emailTIET.setText(user.email)
        initListeners()
    }

    private fun initListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.usernameTIET.doOnTextChanged { _, _, _, _ ->
            binding.saveBtn.setIsEnabled(true)
        }

        binding.emailTIET.doOnTextChanged { _, _, _, _ ->
            binding.saveBtn.setIsEnabled(true)
        }

        binding.logOutTV.setOnClickListener {
            userViewModel.clearApp()
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToSplashGraph())
        }

        binding.saveBtn.setOnClickListener {
            // TODO()
        }
    }
}
package com.internship.move.ui.home.menu

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.internship.move.R
import com.internship.move.databinding.FragmentChangePasswordBinding
import com.internship.move.ui.home.UserViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.navigation.koinNavGraphViewModel

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    private val binding by viewBinding(FragmentChangePasswordBinding::bind)
    private val userViewModel: UserViewModel by koinNavGraphViewModel(R.id.home_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.oldPassTIET.doOnTextChanged { _, _, _, _ ->
            binding.saveBtn.setIsEnabled(areFieldsNotEmpty())
        }

        binding.newPassTIET.doOnTextChanged { _, _, _, _ ->
            binding.saveBtn.setIsEnabled(areFieldsNotEmpty())
        }

        binding.newPass2TIET.doOnTextChanged { _, _, _, _ ->
            binding.saveBtn.setIsEnabled(areFieldsNotEmpty())
        }

        binding.saveBtn.setOnClickListener {
            // TODO()
        }
    }

    private fun areFieldsNotEmpty(): Boolean =
        !binding.oldPassTIET.text.isNullOrEmpty() and !binding.newPassTIET.text.isNullOrEmpty() and !binding.newPass2TIET.text.isNullOrEmpty()
}
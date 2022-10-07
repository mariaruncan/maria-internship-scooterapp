package com.internship.move.ui.home.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentTripsHistoryBinding
import com.internship.move.ui.home.TripViewModel
import com.internship.move.ui.home.menu.adapter.TripsAdapter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.navigation.koinNavGraphViewModel

class TripsHistoryFragment : Fragment(R.layout.fragment_trips_history) {

    private val binding by viewBinding(FragmentTripsHistoryBinding::bind)
    private val tripsViewModel by koinNavGraphViewModel<TripViewModel>(R.id.home_graph)
    private val adapter by lazy { TripsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initTripsList()

        tripsViewModel.tripsList.observe(viewLifecycleOwner) { tripsList ->
            adapter.submitList(tripsList)
        }

        tripsViewModel.getUserTrips()
    }

    private fun initListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initTripsList() {
        binding.tripsRV.adapter = adapter
    }
}
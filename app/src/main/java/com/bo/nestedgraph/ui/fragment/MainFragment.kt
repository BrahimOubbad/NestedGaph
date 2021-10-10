package com.bo.nestedgraph.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bo.nestedgraph.R
import com.bo.nestedgraph.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_main_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomMainNav.setupWithNavController(navController)

        binding.ivToolBarBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            binding.tvToolBarTitle.text = destination.label

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
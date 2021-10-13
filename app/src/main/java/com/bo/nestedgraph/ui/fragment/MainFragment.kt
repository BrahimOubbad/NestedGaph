package com.bo.nestedgraph.ui.fragment

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.PointF
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bo.nestedgraph.R
import com.bo.nestedgraph.databinding.FragmentMainBinding
import com.bo.nestedgraph.registerDraggableTouchListener

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

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
            binding.tvBarTitle.text = destination.label
        }

        binding.ivBigRobot.registerDraggableTouchListener(
            initialPosition = { PointF(binding.ivBigRobot.x, binding.ivBigRobot.y) },
            positionListener = { x, y -> setPosition(x, y) },
            onUp = {
                moveToEdges()
            },
            showCloseButton = {
                binding.vClose.isVisible = it
            }
        )

        binding.vClose.setOnClickListener {
            Toast.makeText(requireContext(), "Close", Toast.LENGTH_SHORT).show()
//            robotHomeSpace(binding.ivBigRobot, binding.ivBarSmallRobot)


            binding.ivBigRobot.animate()
                .x(binding.ivBarSmallRobot.x)
                .y(binding.ivBarSmallRobot.y)
                .setDuration(1000)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .withEndAction {
                    //to make sure that it arrives,
                    //but not needed actually these two lines
                    binding.ivBigRobot.x = binding.ivBarSmallRobot.x
                    binding.ivBigRobot.y = binding.ivBarSmallRobot.y
                }
                .start()


        }


    }


    private fun robotHomeSpace(start: View, end: View) {
        ValueAnimator.ofFloat(start.x, end.x).apply {
            duration = 500
            addUpdateListener {
                start.x = it.animatedValue as Float
            }
            start()
        }
        ValueAnimator.ofFloat(start.y, end.y).apply {
            duration = 500
            addUpdateListener {
                start.y = it.animatedValue as Float
            }
            start()
        }

        ValueAnimator.ofFloat(1f, 0f).apply {
            duration = 500
            startDelay = 250
            addUpdateListener {
                start.scaleX = it.animatedValue as Float
            }
            start()
        }

        ValueAnimator.ofFloat(1f, 0f).apply {
            duration = 500
            startDelay = 250
            addUpdateListener {
                start.scaleY = it.animatedValue as Float
            }
            start()
        }

    }


    private fun setPosition(x: Float, y: Float) {

        val bottom = (binding.navHostMainFragment.y +
                binding.navHostMainFragment.height -
                binding.ivBigRobot.height)

        if (y >= bottom) {
            binding.ivBigRobot.y = bottom
        } else {
            binding.ivBigRobot.y = y
        }
        binding.ivBigRobot.x = x

    }

    private fun moveToEdges() {

        val top = binding.navHostMainFragment.y
        val bottom = (binding.navHostMainFragment.y +
                binding.navHostMainFragment.height -
                binding.ivBigRobot.height)

        if (binding.ivBigRobot.x < binding.navHostMainFragment.width / 2) {
            moveWithAnimationX(binding.ivBigRobot.x, 0f)
        } else {
            moveWithAnimationX(
                binding.ivBigRobot.x,
                (binding.navHostMainFragment.width - binding.ivBigRobot.width).toFloat()
            )
        }

        if (binding.ivBigRobot.y < top) {
            moveWithAnimationY(binding.ivBigRobot.y, top)
        } else if (binding.ivBigRobot.y > bottom) {
            moveWithAnimationY(binding.ivBigRobot.y, bottom)
        }

    }

    private fun moveWithAnimationX(start: Float, end: Float) {
        ObjectAnimator.ofFloat(start, end).apply {
            interpolator = OvershootInterpolator()
            duration = 250
            addUpdateListener {
                binding.ivBigRobot.x = it.animatedValue as Float
            }
        }.start()
    }

    private fun moveWithAnimationY(start: Float, end: Float) {
        ObjectAnimator.ofFloat(start, end).apply {
            interpolator = OvershootInterpolator()
            duration = 250
            addUpdateListener {
                binding.ivBigRobot.y = it.animatedValue as Float
            }
        }.start()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
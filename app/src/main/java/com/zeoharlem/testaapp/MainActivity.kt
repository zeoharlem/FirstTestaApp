package com.zeoharlem.testaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.zeoharlem.testaapp.databinding.ActivityMainBinding
import com.zeoharlem.testaapp.extensions.hide
import com.zeoharlem.testaapp.extensions.makeStatusBarTransparent
import com.zeoharlem.testaapp.extensions.show
import com.zeoharlem.testaapp.extensions.toLocalPrice
import com.zeoharlem.testaapp.models.CartMenu
import com.zeoharlem.testaapp.models.Tests
import com.zeoharlem.testaapp.ui.test.TestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: TestViewModel
    private var menuList = emptyList<CartMenu<Tests>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        makeStatusBarTransparent()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_dashboard) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.testFragment,
                R.id.historyFragment,
                R.id.profileFragment,
                R.id.settingsFragment
            ),
        )

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setHomeButtonEnabled(false)
            setDisplayHomeAsUpEnabled(false)
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavView.setupWithNavController(navController)

        //viewModel = ViewModelProvider(this)[TestViewModel::class.java]
        viewModel = ViewModelProvider(this)[TestViewModel::class.java]

        init()
    }

    private fun init() {
        setUpNavigationAction()
        observeCheckOutData()
    }

    private fun observeCheckOutData() {
        viewModel.getCartMenuItems().observe(this@MainActivity) {
            if (it.isNotEmpty()) {
                with(binding) {
                    cartDetails.show()
                    testSelectedTv.text = "${it.size} Test Selected"
                    menuList = it
                }
            } else {
                with(binding) {
                    cartDetails.hide()
                    testSelectedTv.text = "0 Test Selected"
                }
            }
        }
        viewModel.getTotalPrice().observe(this) {
            binding.totalTestAmountTv.text = it.toInt().toString().toLocalPrice()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun navigateToCheckoutDetails() {
        with(binding) {
            checkoutBtn.setOnClickListener {
                navController.navigate(R.id.orderDetailsFragment)
            }
        }
    }

    private fun navigateToPaymentIntegration() {
        with(binding) {
            checkoutBtn.setOnClickListener {
                navController.navigate(R.id.paymentIntegrationFragment)
            }
        }
    }

    private fun setUpNavigationAction() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val bottomNavigation = when (destination.id) {
                R.id.loginFragment -> View.INVISIBLE

                R.id.registerFragment, R.id.orderDetailsFragment,
                R.id.paymentIntegrationFragment, R.id.testListSelectedFragment -> {
                    navigateToPaymentIntegration()
                    View.GONE
                }

                else -> {
                    navigateToCheckoutDetails()
                    binding.bottomNavView.visibility = View.VISIBLE
                    View.VISIBLE
                }
            }

            val toolbar = when (destination.id) {
                R.id.loginFragment -> View.INVISIBLE
                else -> View.VISIBLE
            }

            val cardDetailsDisplay = when (destination.id) {
                R.id.paymentIntegrationFragment, R.id.registerFragment, R.id.loginFragment -> View.GONE
                else -> if (viewModel.getCartMenuItems().value?.isEmpty() == true) View.GONE else View.VISIBLE
            }

            binding.bottomNavView.visibility = bottomNavigation
            binding.cartDetails.visibility = cardDetailsDisplay
            binding.toolbar.visibility = toolbar

            /**
             * use to should any escapes switch check
             */
            //binding.toolbar.isVisible = arguments?.getBoolean("ShowBottomNav", false) == true
        }
    }

    fun setSupportActionBarTitle(title: String) {
        binding.toolbar.title = title
    }

}
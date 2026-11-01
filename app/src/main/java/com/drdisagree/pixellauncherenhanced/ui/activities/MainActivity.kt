package com.drdisagree.pixellauncherenhanced.ui.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.drdisagree.pixellauncherenhanced.R
import com.drdisagree.pixellauncherenhanced.databinding.ActivityMainBinding
import com.drdisagree.pixellauncherenhanced.ui.base.BaseActivity
import com.drdisagree.pixellauncherenhanced.ui.fragments.HomePage
import com.drdisagree.pixellauncherenhanced.utils.ExpressiveDialog
import com.drdisagree.pixellauncherenhanced.utils.RootShell
import com.google.android.material.color.MaterialColors
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        supportFragmentManager.registerFragmentLifecycleCallbacks(sharedElementBinder, false)

        if (savedInstanceState == null) {
            replaceFragment(supportFragmentManager, HomePage())
        }

        if (intent.hasExtra(EXTRA_IS_ROOTED)) {
            if (intent.getBooleanExtra(EXTRA_IS_ROOTED, false)) return
            showRootFailedDialog()
        } else {
            RootShell.checkRoot { isRooted ->
                if (isFinishing) return@checkRoot
                if (!isRooted) showRootFailedDialog()
            }
        }
    }

    private fun showRootFailedDialog() {
        ExpressiveDialog.show(
            context = this,
            icon = R.drawable.ic_error,
            title = getString(R.string.root_connection_failed_title),
            message = getString(R.string.root_connection_failed_desc),
            positive = ExpressiveDialog.Action(getString(R.string.exit)) { exitProcess(0) },
            tone = ExpressiveDialog.Tone.ERROR,
            cancelable = false
        )
    }

    private val sharedElementBinder = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            v.setBackgroundColor(MaterialColors.getColor(v, com.google.android.material.R.attr.colorSurface))
            (v as? ViewGroup)?.isTransitionGroup = true
        }
    }

    companion object {

        const val EXTRA_IS_ROOTED = "isRooted"
        const val ARG_HIGHLIGHT_KEY = "plenhanced_highlight_key"

        fun replaceFragment(fragmentManager: FragmentManager, fragment: Fragment) {
            if (fragmentManager.isStateSaved) return

            try {
                val fragmentTag = fragment.javaClass.simpleName
                var currentFragment = fragmentManager.findFragmentById(R.id.fragmentContainerView)

                if (currentFragment != null &&
                    currentFragment.javaClass.simpleName == fragmentTag
                ) {
                    popCurrentFragment(fragmentManager)
                }

                for (i in 0 until fragmentManager.backStackEntryCount) {
                    if (fragmentManager.getBackStackEntryAt(i).name == fragmentTag) {
                        fragmentManager.popBackStack(
                            fragmentTag,
                            POP_BACK_STACK_INCLUSIVE
                        )
                        break
                    }
                }

                currentFragment = fragmentManager.findFragmentById(R.id.fragmentContainerView)
                listOfNotNull(fragment, currentFragment).forEach { it.clearTransitions() }

                fragmentManager.beginTransaction().apply {
                    setReorderingAllowed(true)
                    setCustomAnimations(
                        R.anim.push_enter,
                        R.anim.push_exit,
                        R.anim.pop_enter,
                        R.anim.pop_exit
                    )
                    replace(R.id.fragmentContainerView, fragment, fragmentTag)

                    when (fragmentTag) {
                        HomePage::class.java.simpleName -> {
                            fragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE)
                        }

                        else -> {
                            addToBackStack(fragmentTag)
                        }
                    }

                    commit()
                }
            } catch (_: IllegalStateException) {
            }
        }

        private fun Fragment.clearTransitions() {
            enterTransition = null
            exitTransition = null
            reenterTransition = null
            returnTransition = null
        }

        fun popCurrentFragment(fragmentManager: FragmentManager) {
            if (fragmentManager.isStateSaved) return

            fragmentManager.popBackStack()
        }
    }
}

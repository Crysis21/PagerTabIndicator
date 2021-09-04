package com.hold1.pagertabsdemo

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.hold1.pagertabsdemo.databinding.ActivityNavigationBinding
import com.hold1.pagertabsindicator.TabViewProvider
import com.hold1.pagertabsindicator.adapters.TabsAdapter

class JetpackNavigationActivity : AppCompatActivity() {

    lateinit var binding: ActivityNavigationBinding
    lateinit var navController: NavController
    var tabViewProvider: TabViewProvider = TextProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host)

//        binding.tabsIndicator.setAdapter(TabsAdapterImpl(this))
    }

    class TextProvider: TabViewProvider.TextResource {
        override fun getTabTitle(position: Int): String {
            return "tab"
        }
    }
//
//    inner class TabsAdapterImpl(context: Context) : TabsAdapter(context) {
//        override fun getCount(): Int {
//            return 5
//        }
//
//        override fun getTabAt(position: Int): View {
//            tabViewProvider
//        }
//    }
}
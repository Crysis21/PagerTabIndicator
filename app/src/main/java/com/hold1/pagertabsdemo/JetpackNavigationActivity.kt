package com.hold1.pagertabsdemo

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.hold1.pagertabsdemo.databinding.ActivityNavigationBinding
import com.hold1.pagertabsdemo.fragments.TabInfo
import com.hold1.pagertabsindicator.PagerTabsIndicator
import com.hold1.pagertabsindicator.TabViewProvider
import com.hold1.pagertabsindicator.adapters.TabsAdapter

class JetpackNavigationActivity : AppCompatActivity() {

    lateinit var binding: ActivityNavigationBinding
    lateinit var navController: NavController

    val tabInfos = listOf(
        TabInfo(R.id.tabsFragment,"Tabs", null, R.drawable.ic_tabs),
        TabInfo(R.id.dividerFragment,"Divider", null, R.drawable.ic_divider),
        TabInfo(R.id.colorsFragment,"Colors", null, R.drawable.ic_colors),
        TabInfo(R.id.adaptersFragment,"Adapters", null, R.drawable.ic_adapter),
        TabInfo(R.id.contactFragment,"Contact", null, R.drawable.ic_contact),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host)

        tabsAdapter.tabViewProvider = viewProvider
        binding.tabsIndicator.setAdapter(tabsAdapter)
        binding.tabsIndicator.onItemSelectedListener = object: PagerTabsIndicator.OnItemSelectedListener {
            override fun onItemSelected(position: Int) {
                navController.navigate(tabInfos[position].id)
            }
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            tabInfos.forEachIndexed { index, info ->
                if (info.id == destination.id) {
                    binding.tabsIndicator.setTabSelected(index)
                }
            }
        }
    }

    val tabsAdapter = object : TabsAdapter() {
        override fun getCount(): Int {
            return 5
        }
    }

    private val imageProvider = object : TabViewProvider.ImageResource {
        override fun getTabIconUri(position: Int): Uri? {
            return null
        }

        override fun getTabIconResId(position: Int): Int {
            return tabInfos[position].imageResource ?: R.drawable.ic_add_shopping_cart_black_24dp
        }
    }

    private val webImageProvider = object : TabViewProvider.ImageResource {
        override fun getTabIconUri(position: Int): Uri? {
            return Uri.parse(
                tabInfos[position].imageUrl
                    ?: "https://icons8.github.io/flat-color-icons/svg/opened_folder.svg"
            )
        }

        override fun getTabIconResId(position: Int): Int {
            return -1
        }
    }


    private val textProvider = object : TabViewProvider.TextResource {

        override fun getTabTitle(position: Int): String {
            return tabInfos[position].name ?: "Tab $position"
        }
    }


    private val viewProvider = object : TabViewProvider.ViewResource {
        override fun getTabView(position: Int): View {
            val tabInfo = tabInfos[position]
            val view = layoutInflater.inflate(R.layout.tab_item, null)

            val title = view.findViewById<TextView>(R.id.tab_name)
            title.text = tabInfo.name

            val icon = view.findViewById<ImageView>(R.id.tab_icon)
            tabInfo.imageResource?.let { icon.setImageResource(it) }
            return view
        }
    }

    private val customAnimationProvider = object : TabViewProvider.ViewResource {

        override fun getTabView(position: Int): View {
            val tabInfo = tabInfos[position]
            return TabItemView(
                applicationContext,
                tabInfo.name,
                tabInfo.imageResource ?: -1,
                -0xc9c9ca,
                -0x10000
            )
        }
    }

}
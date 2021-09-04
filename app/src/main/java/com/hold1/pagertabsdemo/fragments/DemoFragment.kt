package com.hold1.pagertabsdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hold1.pagertabsdemo.R

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */
class DemoFragment : Fragment(), FragmentPresenter {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.demo_fragment, null)
    }

    override val tabName: String
        get() = "Dummy"
    override val tabImage: Int
        get() = R.drawable.ic_add_shopping_cart_black_24dp
    override val tabImageUrl: String?
        get() = null
}
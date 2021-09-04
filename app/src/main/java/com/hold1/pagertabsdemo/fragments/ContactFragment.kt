package com.hold1.pagertabsdemo.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hold1.pagertabsdemo.R
import com.hold1.pagertabsdemo.databinding.ContactFragmentBinding

/**
 * Created by Cristian Holdunu on 08/11/2017.
 */
class ContactFragment : Fragment(), FragmentPresenter {

    private lateinit var binding: ContactFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ContactFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.openGit.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/Crysis21/PagerTabIndicator"))
            startActivity(intent)
        }
    }

    override val tabName: String = "Contact"
    override val tabImage: Int = R.drawable.ic_contact
    override val tabImageUrl: String? = null
}
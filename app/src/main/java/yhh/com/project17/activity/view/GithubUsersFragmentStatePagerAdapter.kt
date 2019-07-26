package yhh.com.project17.activity.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import yhh.com.project17.fragment.GithubUsersFragment

class GithubUsersFragmentStatePagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {
    var itemSize = 0
    var keyword = ""
    var itemPerPage = 0

    override fun getItem(position: Int): Fragment {
        return GithubUsersFragment().also {
            it.arguments = Bundle().apply {
                putString(GithubUsersFragment.ARGUMENT_KEY_WORD, keyword)
                putInt(GithubUsersFragment.ARGUMENT_PAGE_NUMBER, position + 1)
                putInt(GithubUsersFragment.ARGUMENT_PAGE_SIZE, itemPerPage)
            }
        }
    }

    override fun getCount(): Int = itemSize
}
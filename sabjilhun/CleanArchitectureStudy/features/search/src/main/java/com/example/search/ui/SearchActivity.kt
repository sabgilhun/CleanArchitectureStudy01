package com.example.search.ui

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.base.BaseActivity
import com.example.common.commons.*
import com.example.domain.entities.RepositorySummaryInfo
import com.example.search.R
import com.example.search.databinding.ActivitySearchBinding
import com.example.search.databinding.ItemRepositorySummaryInfoBinding

class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(SearchViewModel::class)
        binding.vm = viewModel

        setUpView()
        setUpRecyclerView()
        observeViewModel()
    }

    private fun setUpView() {
        binding.etSearchKeyword.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchRepositories()
                true
            } else {
                false
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvSearchedImage setUp {

            withLayoutManager {
                LinearLayoutManager(context)
            }

            adapter<RepositorySummaryInfo>(R.layout.item_repository_summary_info) {

                onClickItem { repositorySummaryInfo, _ ->
                    viewModel.openFileTreeOfRepository(repositorySummaryInfo)
                }

                onBind { viewDataBinding, index ->
                    (viewDataBinding as ItemRepositorySummaryInfoBinding).item = item[index]
                }

                observeLiveData(this@SearchActivity) {
                    viewModel.repositories
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.alertEmptyQuery.observe(this, Observer {
            showSnackBar(getString(R.string.alert_empty_query))
        })
    }
}
package com.example.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.common.base.BaseViewModel
import com.example.common.commons.SingleLiveEvent
import com.example.common.utils.TextUtils
import com.example.domain.entities.RepositorySummaryInfo
import com.example.domain.usecases.SearchRepositoriesUseCase
import com.example.navigator.FileTreeNavigator
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
    private val fileTreeNavigator: FileTreeNavigator
) : BaseViewModel() {

    val query = MutableLiveData<String>("")

    private val _repositories = MutableLiveData<List<RepositorySummaryInfo>>()
    val repositories: LiveData<List<RepositorySummaryInfo>> = _repositories

    private val _alertEmptyQuery = SingleLiveEvent<Unit>()
    val alertEmptyQuery: LiveData<Unit> = _alertEmptyQuery

    fun searchRepositories() {
        if (TextUtils.isEmpty(query.value)) {
            _alertEmptyQuery.setValue(Unit)
            return
        }

        searchRepositoriesUseCase(query.value!!)
            .compose(apiLoadingTransformer())
            .subscribeBy(
                onSuccess = _repositories::setValue,
                onError = ::handleApiErrorMessage
            ).add()
    }

    fun openFileTreeOfRepository(repositorySummaryInfo: RepositorySummaryInfo) {
        fileTreeNavigator.startFileTree(
            repositorySummaryInfo.ownerName,
            repositorySummaryInfo.repositoryName
        )
    }
}
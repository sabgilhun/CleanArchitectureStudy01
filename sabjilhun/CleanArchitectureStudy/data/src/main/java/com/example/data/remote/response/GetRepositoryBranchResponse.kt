package com.example.data.remote.response


import com.example.domain.entities.RepositoryBranch
import com.google.gson.annotations.SerializedName

data class GetRepositoryBranchResponse(
    @SerializedName("name")
    val name: String?,
    @SerializedName("commit")
    val commit: Commit?,
    @SerializedName("protected")
    val `protected`: Boolean?
) {
    data class Commit(
        @SerializedName("sha")
        val sha: String?,
        @SerializedName("url")
        val url: String?
    )

    companion object {
        fun toRepositoryBranchList(
            getRepositoryBranchResponse: List<GetRepositoryBranchResponse>
        ): List<RepositoryBranch> {
            return getRepositoryBranchResponse.mapNotNull {
                if (it.name == null) {
                    return@mapNotNull null
                } else {
                    return@mapNotNull RepositoryBranch(it.name)
                }
            }
        }
    }
}
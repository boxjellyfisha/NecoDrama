package com.kellyhong.necodrama.model

import com.kellyhong.necodrama.http.DramaService
import com.kellyhong.necodrama.http.EndPointGenerator
import com.kellyhong.necodrama.http.response.DramaListResponse
import io.reactivex.Single

class MainRemote : MainDataSource.Remote {

    private fun createService(): DramaService {
        return EndPointGenerator.createWithHeaderRx(null, DramaService::class.java)
    }

    override fun getDramaList(): Single<DramaListResponse> {
        return createService().getDramaList()
    }
}
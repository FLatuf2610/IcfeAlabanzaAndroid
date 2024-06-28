package com.example.icfealabanza.domain.use_cases

import com.example.icfealabanza.data.network.dto.by_id.album_by_id.toDomain
import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.AlbumDetail
import javax.inject.Inject

class GetAlbumByIdUseCase @Inject constructor(private val repositoryImpl: IcfeRepositoryImpl) {

    suspend operator fun invoke(albumId: String): AlbumDetail? {
        val result = repositoryImpl.getAlbumById(albumId)
        if (result.isSuccess) {
            return result.getOrNull()!!.toDomain()
        }
        else return null
    }
}
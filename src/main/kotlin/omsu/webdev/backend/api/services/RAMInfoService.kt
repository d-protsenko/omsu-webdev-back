package omsu.webdev.backend.api.services

import omsu.webdev.backend.api.common.db.Paged
import omsu.webdev.backend.api.common.db.Parameters
import omsu.webdev.backend.api.models.domain.RAMInfo
import omsu.webdev.backend.api.models.views.RAMInfoView
import omsu.webdev.backend.api.repositories.RAMInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RAMInfoService(
        @Autowired var ramInfoRepository: RAMInfoRepository,
        @Autowired var hardwareUsageService: HardwareUsageService
) {
    fun getLatestInfoPaged(parameters: Parameters): Paged<RAMInfoView?>? {
        val pagedInfo: Paged<RAMInfo> = ramInfoRepository.findLatestPaged(parameters)!!
        return Paged(
                pagedInfo.content?.map { it ->
                    RAMInfoView.from(it)
                },
                pagedInfo.totalPages,
                pagedInfo.totalEntities
        )
    }
    //TODO: split ram and cpu info to different services / endpoints
    fun generateAndInsertLatest(parameters: Parameters): RAMInfoView? {
        val ramInfo = hardwareUsageService.getRAMInfo()
        val info = RAMInfo(
                total = ramInfo.total,
                available = ramInfo.available,
                free = ramInfo.free,
                used = ramInfo.used,
                //TODO: check how it works with timezones
                updatedAt = LocalDateTime.now()
        )
        ramInfoRepository.insertInfo(parameters, info)
        return RAMInfoView.from(info)
    }
}
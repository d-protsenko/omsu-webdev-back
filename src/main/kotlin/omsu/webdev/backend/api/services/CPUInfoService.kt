package omsu.webdev.backend.api.services

import omsu.webdev.backend.api.common.db.Paged
import omsu.webdev.backend.api.common.db.Parameters
import omsu.webdev.backend.api.configurations.TimeZoneSingleton
import omsu.webdev.backend.api.models.domain.CPUInfo
import omsu.webdev.backend.api.models.forms.CPUInfoForm
import omsu.webdev.backend.api.models.views.CPUInfoView
import omsu.webdev.backend.api.repositories.CPUInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZonedDateTime

@Service
class CPUInfoService(
        @Autowired var cpuInfoRepository: CPUInfoRepository,
        @Autowired var hardwareUsageService: HardwareUsageService
) {
    fun getLatestInfoPaged(parameters: Parameters): Paged<CPUInfoView?>? {
        val pagedInfo: Paged<CPUInfo> = cpuInfoRepository.findLatestPaged(parameters)!!
        return Paged(
                pagedInfo.content?.map { it ->
                    CPUInfoView.from(it)
                },
                pagedInfo.totalPages,
                pagedInfo.totalEntities
        )
    }

    fun generateAndInsertLatest(parameters: Parameters): CPUInfoView? {
        val cpuInfo = hardwareUsageService.getCPUInfo()
        val info = CPUInfo(
                threads = cpuInfo.threads,
                cores = cpuInfo.cores,
                clock = cpuInfo.clock,
                cpuUsage = cpuInfo.cpuUsage,
                updatedAt = ZonedDateTime.ofInstant(Instant.now(), TimeZoneSingleton.getInstance().zone)
        )
        cpuInfoRepository.insertInfo(parameters, info)
        return CPUInfoView.from(info)
    }

    fun insert(parameters: Parameters, form: CPUInfoForm?): CPUInfoView? {
        form?.let { data ->
            CPUInfo.from(data)?.let {
                cpuInfoRepository.insertInfo(parameters, it)
                return CPUInfoView.from(it)
            }
        }
        return null
    }
}
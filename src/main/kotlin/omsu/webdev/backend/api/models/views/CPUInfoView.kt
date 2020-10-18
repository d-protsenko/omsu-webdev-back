package omsu.webdev.backend.api.models.views

import lombok.Data
import omsu.webdev.backend.api.models.domain.CPUInfo
import java.time.LocalDateTime

@Data
class CPUInfoView(
        var id: String? = null,
        var threads: Int? = null,
        var cores: Int? = null,
        var clock: Double? = null,
        var cpuUsage: Double? = null,
        var updatedAt: LocalDateTime? = null
) {
    companion object {
        fun from(model: CPUInfo?): CPUInfoView? {
            return model?.let {
                CPUInfoView(
                        id = it.id,
                        threads = it.threads,
                        cores = it.cores,
                        clock = it.clock,
                        cpuUsage = it.cpuUsage,
                        updatedAt = it.updatedAt
                )
            }
        }
    }
}

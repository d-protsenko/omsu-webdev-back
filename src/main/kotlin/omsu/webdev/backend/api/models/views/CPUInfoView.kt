package omsu.webdev.backend.api.models.views

import lombok.Data
import omsu.webdev.backend.api.models.domain.CPUInfo
import java.time.LocalDateTime

@Data
class CPUInfoView(
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
                        it.threads,
                        it.cores,
                        it.clock,
                        it.cpuUsage,
                        it.updatedAt
                )
            }
        }
    }
}

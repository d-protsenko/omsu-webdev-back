package omsu.webdev.backend.api.models.views

import lombok.Data
import omsu.webdev.backend.api.models.domain.RAMInfo
import java.time.LocalDateTime

@Data
class RAMInfoView(
        var id: String? = null,
        var total: Double? = null,
        var available: Double? = null,
        var free: Double? = null,
        var used: Double? = null,
        var updatedAt: LocalDateTime? = null
) {
    companion object {
        fun from(model: RAMInfo?): RAMInfoView? {
            return model?.let {
                RAMInfoView(
                        id = it.id,
                        total = it.total,
                        available = it.available,
                        free = it.free,
                        used = it.used,
                        updatedAt = it.updatedAt
                )
            }
        }
    }
}

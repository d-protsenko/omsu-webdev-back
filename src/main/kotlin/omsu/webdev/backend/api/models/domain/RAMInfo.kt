package omsu.webdev.backend.api.models.domain

import lombok.Data
import omsu.webdev.backend.api.common.db.operations.IIndexedModel
import omsu.webdev.backend.api.models.forms.RAMInfoForm
import java.time.LocalDateTime

@Data
class RAMInfo(
        override var id: String? = null,
        var total: Double? = null,
        var available: Double? = null,
        var free: Double? = null,
        var used: Double? = null,
        var updatedAt: LocalDateTime? = null
) : IIndexedModel {
    companion object {
        fun from(model: RAMInfoForm?): RAMInfo? {
            return model?.let {
                RAMInfo(
                        total = it.total,
                        available = it.available,
                        free = it.free,
                        used = it.used,
                        updatedAt = LocalDateTime.now()
                )
            }
        }
    }
}

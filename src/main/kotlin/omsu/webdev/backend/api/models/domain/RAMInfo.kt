package omsu.webdev.backend.api.models.domain

import lombok.Data
import omsu.webdev.backend.api.common.db.operations.IIndexedModel
import omsu.webdev.backend.api.configurations.TimeZoneSingleton
import omsu.webdev.backend.api.models.forms.RAMInfoForm
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

@Data
class RAMInfo(
        override var id: String? = null,
        var total: Double? = null,
        var available: Double? = null,
        var free: Double? = null,
        var used: Double? = null,
        var updatedAt: ZonedDateTime? = null
) : IIndexedModel {
    companion object {
        fun from(model: RAMInfoForm?): RAMInfo? {
            return model?.let {
                RAMInfo(
                        total = it.total,
                        available = it.available,
                        free = it.free,
                        used = it.used,
                        updatedAt = Optional.ofNullable(it.updatedAt)
                                .orElse(ZonedDateTime.ofInstant(Instant.now(), TimeZoneSingleton.getInstance().zone))
                )
            }
        }

        fun Builder(): RAMInfoBuilder {
            return Builder()
        }

        class RAMInfoBuilder {
            private var total: Double? = null
            private var available: Double? = null
            private var free: Double? = null
            private var used: Double? = null
            private var updatedAt: ZonedDateTime? = null
            fun total(total: Double?): RAMInfoBuilder {
                total.let { this.total = it }
                return this
            }

            fun available(available: Double?): RAMInfoBuilder {
                available.let { this.available = it }
                return this
            }

            fun free(free: Double?): RAMInfoBuilder {
                free.let { this.free = it }
                return this
            }

            fun used(used: Double?): RAMInfoBuilder {
                used.let { this.used = it }
                return this
            }

            fun updatedAt(updatedAt: ZonedDateTime?): RAMInfoBuilder {
                updatedAt.let { this.updatedAt = it }
                return this
            }

            fun build(): RAMInfo {
                return RAMInfo(
                        total = this.total,
                        available = this.available,
                        free = this.free,
                        used = this.used,
                        updatedAt = this.updatedAt
                )
            }
        }
    }
}

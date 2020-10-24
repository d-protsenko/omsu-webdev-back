package omsu.webdev.backend.api.models.domain

import lombok.Data
import omsu.webdev.backend.api.common.db.operations.IIndexedModel
import omsu.webdev.backend.api.configurations.TimeZoneSingleton
import omsu.webdev.backend.api.models.forms.CPUInfoForm
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

@Data
class CPUInfo(
    override var id: String? = null,
    var threads: Int? = null,
    var cores: Int? = null,
    var clock: Double? = null,
    var cpuUsage: Double? = null,
    var updatedAt: ZonedDateTime? = null
) : IIndexedModel {
    companion object {
        fun from(model: CPUInfoForm?): CPUInfo? {
            return model?.let {
                CPUInfo(
                    threads = it.threads,
                    cores = it.cores,
                    clock = it.clock,
                    cpuUsage = it.cpuUsage,
                    updatedAt = Optional.ofNullable(it.updatedAt)
                        .orElse(ZonedDateTime.ofInstant(Instant.now(), TimeZoneSingleton.getInstance().zone))
                )
            }
        }

        fun Builder(): CPUInfoBuilder {
            return Builder()
        }

        class CPUInfoBuilder {
            private var threads: Int? = null
            private var cores: Int? = null
            private var clock: Double? = null
            private var cpuUsage: Double? = null
            private var updatedAt: ZonedDateTime? = null
            fun threads(threads: Int?): CPUInfoBuilder {
                threads.let { this.threads = it }
                return this
            }

            fun cores(cores: Int?): CPUInfoBuilder {
                cores.let { this.cores = it }
                return this
            }

            fun clock(clock: Double?): CPUInfoBuilder {
                clock.let { this.clock = it }
                return this
            }

            fun cpuUsage(cpuUsage: Double?): CPUInfoBuilder {
                cpuUsage.let { this.cpuUsage = it }
                return this
            }

            fun updatedAt(updatedAt: ZonedDateTime?): CPUInfoBuilder {
                updatedAt.let { this.updatedAt = it }
                return this
            }

            fun build(): CPUInfo {
                return CPUInfo(
                    threads = this.threads,
                    cores = this.cores,
                    clock = this.clock,
                    cpuUsage = this.cpuUsage,
                    updatedAt = this.updatedAt
                )
            }
        }
    }
}

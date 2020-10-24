package omsu.webdev.backend.api.models.views

import lombok.Data
import omsu.webdev.backend.api.configurations.TimeZoneSingleton
import omsu.webdev.backend.api.models.domain.LoggingInfo
import java.time.Instant
import java.time.ZonedDateTime

@Data
class LoggingInfoView(
    var id: String? = null,
    var message: String? = null,
    var updatedAt: ZonedDateTime? = null
) {
    companion object {
        fun from(model: LoggingInfo?): LoggingInfoView? {
            return model?.let {
                LoggingInfoView(
                    id = it.id,
                    message = it.message,
                    updatedAt = ZonedDateTime.ofInstant(it.updatedAt, TimeZoneSingleton.getInstance().zone)
                )
            }
        }
    }
}

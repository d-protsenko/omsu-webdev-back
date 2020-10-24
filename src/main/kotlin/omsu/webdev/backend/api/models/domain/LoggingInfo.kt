package omsu.webdev.backend.api.models.domain

import lombok.Data
import omsu.webdev.backend.api.common.db.operations.IIndexedModel
import omsu.webdev.backend.api.models.forms.LoggingInfoForm
import java.time.Instant

@Data
class LoggingInfo(
    override var id: String? = null,
    var message: String? = null,
    var updatedAt: Instant? = null
) : IIndexedModel {
    companion object {
        fun from(form: LoggingInfoForm?): LoggingInfo? {
            return form?.let {
                LoggingInfo(
                    message = it.message,
                    updatedAt = it.updatedAt
                )
            }
        }
    }
}
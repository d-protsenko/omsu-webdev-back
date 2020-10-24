package omsu.webdev.backend.api.common.db.operations

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
class ReadDataFromDatabaseException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(formMessage(message))
    constructor(message: String?, cause: Throwable?) : super(formMessage(message), cause)
    constructor(cause: Throwable?) : super(cause)

    companion object {
        private const val MESSAGE = "Exception on reading data from database"
        private fun formMessage(message: String?): String {
            return if (message != null) "$MESSAGE - $message" else MESSAGE
        }
    }
}

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class DuplicateKeyOrCombinationOfKeysException : java.lang.RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(concatenateMessage(message))
    constructor(message: String?, cause: Throwable?) : super(concatenateMessage(message), cause)
    constructor(cause: Throwable?) : super(cause)
    constructor(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(concatenateMessage(message), cause, enableSuppression, writableStackTrace)

    companion object {
        private const val MESSAGE = "Duplicated key or combination of keys"
        private fun concatenateMessage(message: String?): String {
            return if (message != null) "$MESSAGE - $message" else MESSAGE
        }
    }
}

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
class DatabaseException : java.lang.RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(concatenateMessage(message))
    constructor(message: String?, cause: Throwable?) : super(concatenateMessage(message), cause)
    constructor(cause: Throwable?) : super(cause)
    protected constructor(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(concatenateMessage(message), cause, enableSuppression, writableStackTrace)

    companion object {
        private const val MESSAGE = "Database exception."
        private fun concatenateMessage(message: String?): String {
            return if (message != null) "$MESSAGE - $message" else MESSAGE
        }
    }
}

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
class InsertEntityToDatabaseException : java.lang.RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(concatenateMessage(message))
    constructor(message: String?, cause: Throwable?) : super(concatenateMessage(message), cause)
    constructor(cause: Throwable?) : super(cause)
    protected constructor(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(concatenateMessage(message), cause, enableSuppression, writableStackTrace)

    companion object {
        private const val MESSAGE = "Exception on inserting data to database"
        private fun concatenateMessage(message: String?): String {
            return if (message != null) "$MESSAGE - $message" else MESSAGE
        }
    }
}

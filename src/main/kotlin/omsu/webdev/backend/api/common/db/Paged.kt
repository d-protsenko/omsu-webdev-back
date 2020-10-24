package omsu.webdev.backend.api.common.db

class Paged<T>(
    val content: List<T>? = null,
    val totalPages: Int? = null,
    val totalEntities: Int? = null
)
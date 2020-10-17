package omsu.webdev.backend.api.common.db.operations

import omsu.webdev.backend.api.common.db.Parameters

class QueryHelper {
    companion object {
        fun addFiltersToQuery(
                parameters: Parameters
        ): String? {
            val query: String = parameters["query"]!!
            val filters: IQueryFilter? = parameters["filters"]
            parameters.get<String>("replaceable_tag")?.let {
                val replaceableTag: String = it
                return if (null != filters) {
                    query.replace(replaceableTag, " where " + filters.build())
                } else {
                    query.replace(replaceableTag, "")
                }
            }
            return query;
        }
    }


}
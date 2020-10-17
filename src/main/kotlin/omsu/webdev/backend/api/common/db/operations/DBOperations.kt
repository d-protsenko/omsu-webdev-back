package omsu.webdev.backend.api.common.db.operations

import omsu.webdev.backend.api.common.db.Paged
import omsu.webdev.backend.api.common.db.Parameters

interface ICountOperation {
    fun count(parameters: Parameters?): Int?
}

interface IGetOperation<M> {
    fun getEntity(parameters: Parameters?): M?
}

interface IGetPageOperation<M> {
    fun getPage(parameters: Parameters?): Paged<M>?
}

interface IInsertOperation<M> {
    fun create(parameters: Parameters?, model: M)
}

interface IUpdateOperation<M> {
    fun update(parameters: Parameters?, model: M)
}

interface IIndexedModel {
    var id: String?
}

interface ITwoArgsAction<A1, A2> {
    fun execute(arg1: A1, arg2: A2)
}
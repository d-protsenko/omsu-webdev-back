package omsu.webdev.backend.api.common.db

class Parameters {
    private val storage: MutableMap<String, Any> = HashMap()

    inner class Builder {
        fun add(fieldName: String, value: Any?): Builder {
            value?.let {
                this@Parameters[fieldName] = value
            }
            return this
        }

        fun build(): Parameters {
            return this@Parameters
        }
    }

    operator fun <T> get(name: String?): T? {
        return storage[name] as T?
    }

    operator fun set(name: String, value: Any) {
        storage[name] = value
    }

    companion object {
        fun builder(): Builder {
            return Parameters().Builder()
        }
    }
}
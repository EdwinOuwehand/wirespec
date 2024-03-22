package community.flock.wirespec.examples.app.todo

import community.flock.wirespec.examples.app.common.Value

data class Todo(
    val id: Id,
    val name: Name,
    val done: Boolean,
) {
    @JvmInline
    value class Id(override val value: String) : Value<String>
}

@JvmInline
value class Name(override val value: String) : Value<String>

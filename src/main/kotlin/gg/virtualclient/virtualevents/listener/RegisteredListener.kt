package gg.virtualclient.virtualevents.listener

import gg.virtualclient.virtualevents.event.Event

data class RegisteredListener<E : Event>(val listener: Any?, val priority: EventPriority, val executor: (event: E) -> Unit)
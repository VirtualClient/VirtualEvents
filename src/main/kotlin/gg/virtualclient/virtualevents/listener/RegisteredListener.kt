package gg.virtualclient.virtualevents.listener

import gg.virtualclient.virtualevents.event.Event
import java.util.function.Consumer

data class RegisteredListener<E : Event>(val listener: Any?, val priority: EventPriority, val executor: Consumer<E>)
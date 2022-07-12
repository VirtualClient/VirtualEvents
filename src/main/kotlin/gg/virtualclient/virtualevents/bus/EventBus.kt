package gg.virtualclient.virtualevents.bus

import gg.virtualclient.virtualevents.event.Event
import gg.virtualclient.virtualevents.listener.EventPriority
import gg.virtualclient.virtualevents.listener.RegisteredListener

interface EventBus {

    fun register(listener: Any)

    fun <T : Event> register(eventClass: Class<T>, block: (event: T) -> Unit) : RegisteredListener<T> {
        return register(eventClass, EventPriority.NORMAL, block)
    }

    fun <T : Event> register(eventClass: Class<T>, priority: EventPriority, block: (event: T) -> Unit): RegisteredListener<T>

    fun callEvent(event: Event): Event

    fun unregister(registeredListener: RegisteredListener<*>)

    fun unregister(listener: Any)

}
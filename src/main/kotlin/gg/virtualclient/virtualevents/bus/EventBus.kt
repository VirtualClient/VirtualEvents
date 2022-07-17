package gg.virtualclient.virtualevents.bus

import gg.virtualclient.virtualevents.event.Event
import gg.virtualclient.virtualevents.listener.EventPriority
import gg.virtualclient.virtualevents.listener.RegisteredListener
import java.util.function.Consumer

interface EventBus {

    fun register(listener: Any)

    fun <T : Event> register(eventClass: Class<T>, block: Consumer<T>) : RegisteredListener<T> {
        return register(eventClass, EventPriority.NORMAL, block)
    }

    fun <T : Event> register(eventClass: Class<T>, priority: EventPriority, block: Consumer<T>): RegisteredListener<T>

    fun callEvent(event: Event): Event

    fun unregister(registeredListener: RegisteredListener<*>)

    fun unregister(listener: Any)

}
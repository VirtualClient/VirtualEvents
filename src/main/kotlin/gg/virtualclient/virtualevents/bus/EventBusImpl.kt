package gg.virtualclient.virtualevents.bus

import gg.virtualclient.virtualevents.event.Event
import gg.virtualclient.virtualevents.listener.EventListener
import gg.virtualclient.virtualevents.listener.EventPriority
import gg.virtualclient.virtualevents.listener.RegisteredListener
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer

class EventBusImpl : EventBus {

    private val listeners: MutableMap<Class<out Event>, MutableList<RegisteredListener<out Event>>> = ConcurrentHashMap()

    override fun register(listener: Any) {
        listener.javaClass.declaredMethods.forEach {
            if(it.parameterTypes.size != 1)
                return@forEach
            if(!it.isAnnotationPresent(EventListener::class.java))
                return@forEach
            val annotation = it.getAnnotation(EventListener::class.java)

            @Suppress("UNCHECKED_CAST")
            val methodClass = it.parameterTypes[0] as Class<out Event>

            it.isAccessible = true
            register(listener, methodClass, annotation.priority) { event -> it.invoke(listener, event) }
        }
    }

    override fun <T : Event> register(
        eventClass: Class<T>,
        priority: EventPriority,
        block: Consumer<T>
    ): RegisteredListener<T> {
        return register(null, eventClass, priority, block)
    }

    override fun callEvent(event: Event): Event {
        val listeners: MutableList<RegisteredListener<out Event>>? = listeners[event.javaClass]
        listeners?.sortedBy { it.priority.ordinal }?.forEach {
            @Suppress("UNCHECKED_CAST")
            (it.executor as Consumer<Event>).accept(event)
        }
        return event
    }

    private fun <T : Event> register(
        listener: Any?,
        eventClass: Class<T>,
        priority: EventPriority,
        block: Consumer<T>
    ): RegisteredListener<T> {
        val registeredListener = RegisteredListener(listener, priority, block)
        val listeners = getOrCreateListenerList(eventClass)

        if(listeners.contains(registeredListener))
            throw IllegalStateException("Listener already registered")

        listeners.add(registeredListener)
        return registeredListener
    }

    override fun unregister(listener: Any) {
        listeners.values.forEach { data ->
            data.removeIf { it.listener == listener }
        }
        cleanUp()
    }

    override fun unregister(registeredListener: RegisteredListener<*>) {
        listeners.values.forEach { data ->
            data.removeIf { it == registeredListener }
        }
        cleanUp()
    }

    private fun cleanUp() {
        listeners.entries.removeIf { it.value.isEmpty() }
    }

    private fun getOrCreateListenerList(eventCls: Class<out Event>): MutableList<RegisteredListener<out Event>> {
        var list = listeners[eventCls]
        if(list != null)
            return list
        list = CopyOnWriteArrayList()
        listeners[eventCls] = list
        return list
    }

}
package gg.virtualclient.virtualevents.listener

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class EventListener(val priority: EventPriority = EventPriority.NORMAL)
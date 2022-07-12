import gg.virtualclient.virtualevents.bus.EventBusImpl
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class EventBusTest {

    private val eventBus = EventBusImpl()

    var testString: String? = null

    @Test
    @Order(0)
    fun registerListener() {
        eventBus.register(TestEvent::class.java) { event ->
            if(testString != null)
                throw IllegalStateException("Test String not null")
            testString = event.str
        }
    }

    @Test
    @Order(1)
    fun callEvent() {
        eventBus.callEvent(TestEvent("Hello World"))
    }

    @Test
    @Order(1)
    fun validateExecution() {
        assert(testString == "Hello World")
    }

}
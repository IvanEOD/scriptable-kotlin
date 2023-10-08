import kotlin.contracts.contract
import kotlin.js.Promise

/**
 *  Alert Builder
 *
 * @author IvanEOD ( 10/7/2023 at 3:36 PM EST )
 */


@DslMarker
annotation class AlertMarker

@AlertMarker
sealed interface AlertBuilderScope {
    val component: Any

}

sealed class AlertResult {

    data class Fulfilled(
        val index: Int,
        val inputs: Map<String, String>
    ): AlertResult() {
        operator fun get(key: String) = inputs[key]

        val wasCancelled = index == -1
        val wasNotCancelled = !wasCancelled
    }

    data class Rejected(
        val throwable: Throwable
    ): AlertResult() {
        val message by throwable::message
    }

    fun <T> fold(
        onFulfilled: (Fulfilled) -> T,
        onRejected: (Rejected) -> T,
    ): T = when (this) {
        is Fulfilled -> onFulfilled(this)
        is Rejected -> onRejected(this)
    }

    fun isFulfilled(): Boolean {
        contract {
            returns(true) implies (this@AlertResult is Fulfilled)
            returns(false) implies (this@AlertResult is Rejected)
        }
        return this is Fulfilled
    }

    fun isRejected(): Boolean {
        contract {
            returns(true) implies (this@AlertResult is Rejected)
            returns(false) implies (this@AlertResult is Fulfilled)
        }
        return this is Rejected
    }

}

class AlertKt(
    private val component: Alert,
    private val inputs: Map<String, Int>,
) {

    private fun present(
        sheet: Boolean,
        callback: (AlertResult) -> Unit,
    ) {
        (if (sheet) component.presentSheet() else component.present()).then(
            onFulfilled = { numberIndex: Number ->
                val index = numberIndex.toInt()
                val inputResults = mutableMapOf<String, String>()
                for ((key, inputIndex) in inputs) {
                    val input = component.textFieldValue(inputIndex)
                    inputResults[key] = input
                }
                callback(AlertResult.Fulfilled(index, inputResults))
            },
            onRejected = { throwable: Throwable ->
                callback(AlertResult.Rejected(throwable))
            }
        )
    }

    fun present(callback: AlertResult.() -> Unit) = present(false, callback)
    fun presentSheet(callback: AlertResult.() -> Unit) = present(true, callback)

}

class AlertBuilder(
    override val component: Alert = Alert()
): AlertBuilderScope {
    private val inputs = mutableMapOf<String, Int>()
    private var actionCount = -1

    fun title(
        text: String,
        message: String,
    ) {
        component.title = text
        component.message = message
    }

    fun action(
        text: String,
    ): Int {
        component.addAction(text)
        return actionCount++
    }

    fun destructiveAction(
        text: String,
    ): Int {
        component.addDestructiveAction(text)
        return actionCount++
    }

    fun input(
        key: String,
        placeholder: String,
        text: String? = null,
    ): Int {
        val index = inputs.size
        inputs[key] = index
        if (text != null) component.addTextField(placeholder, text)
        else component.addTextField(placeholder)
        return index
    }

    fun secureInput(
        key: String,
        placeholder: String,
        text: String? = null,
    ): String {
        val index = inputs.size
        inputs[key] = index
        if (text != null) component.addSecureTextField(placeholder, text)
        else component.addSecureTextField(placeholder)
        return key
    }

    fun cancel(
        text: String = "Cancel",
    ): Int {
        component.addCancelAction(text)
        return -1
    }

    internal fun build(): AlertKt = AlertKt(component, inputs)

}

fun buildAlert(
    component: Alert = Alert(),
    block: AlertBuilder.() -> Unit,
): AlertKt = AlertBuilder(component).apply(block).build()

fun buildAlert(
    title: String,
    message: String,
    component: Alert = Alert(),
    block: AlertBuilder.() -> Unit,
) = buildAlert(component) {
    title(title, message)
    block()
}
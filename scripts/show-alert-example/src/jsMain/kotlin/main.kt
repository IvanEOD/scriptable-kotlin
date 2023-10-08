/**
 * show-alert-example
 */
            
fun main() {

    val alert = buildAlert(
        "Kotlin Alert",
        "This is an alert from Kotlin!"
    ) {
        input("username", "Username")
        secureInput("password", "Password")
        action("Login")
        cancel()
    }

    alert.present {
        when (this) {
            is AlertResult.Fulfilled -> {
                if (wasCancelled) {
                    QuickLook.present("Alert was cancelled")
                    return@present
                }
                val username = inputs["username"]
                val password = inputs["password"]
                QuickLook.present("Username: $username\nPassword: $password")
            }
            is AlertResult.Rejected -> QuickLook.present("Alert was rejected with message: $message")
        }
    }

}

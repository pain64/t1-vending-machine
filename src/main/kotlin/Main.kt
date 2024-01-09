import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    val bootData = ConsoleVendingMachine::class.java.classLoader
        .getResource("boot.json")!!.readText()

    val bootState = Json.decodeFromString<BootState>(bootData)

    ConsoleVendingMachine(bootState).runInfinite()
}
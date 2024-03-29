import kotlinx.serialization.json.Json
import vm.fp.NamedProduct
import vm.fp.vmRunInfinite

val format = Json { allowStructuredMapKeys = true }

fun main(args: Array<String>) {
    val bootData = NamedProduct::class.java.classLoader
        .getResource("boot.json")!!.readText()

    val bootState = format.decodeFromString<Map<NamedProduct, Int>>(bootData)
    vmRunInfinite(bootState)
}
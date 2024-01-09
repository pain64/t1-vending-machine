import kotlinx.serialization.Serializable
import vm.*
import vm.Product as VmProduct

@Serializable
class Rubles(val amount: Int)

enum class RubleCoins(override val value: Rubles) : Coin<Rubles> {
    FIVE(Rubles(5)), TEN(Rubles(10))
}

@Serializable
class Product(
    override var availableCount: Int,
    override val price: Rubles,
    val name: String,
) : VmProduct<Rubles>

@Serializable
data class BootState(val products: List<Product>)

class ConsoleVendingMachine(private val bootState: BootState) {

    private val fsm = VendingMachineFsm<Rubles, Product, RubleCoins>(
        object : MoneyCalculator<Rubles> {
            override fun zero() = Rubles(0)

            override fun isLessThanOrEqual(m1: Rubles, m2: Rubles) =
                m1.amount <= m2.amount

            override fun add(m1: Rubles, m2: Rubles) =
                Rubles(m1.amount + m2.amount)
        }
    )

    companion object {
        val insertCommandRegex = "^i \\d+$".toRegex()
        val selectCommandRegex = "^s \\d+$".toRegex()
    }

    private fun processOutput(
        messages: List<VmOutput<Rubles, Product, RubleCoins>>
    ) {
        for (message in messages)
            when (message) {
                is DisplayProductIsOut ->
                    println("дисплей: продукт закончился")

                is DisplayProductNotSelected ->
                    println("дисплей: продукт не выбран")

                is EjectCoin ->
                    println("монеты: монета возвращена: ${message.coin.value.amount}")

                is EjectProduct ->
                    println("продукты: ваш продукт: ${message.product.name}")
            }
    }

    fun runInfinite(): Nothing {
        println("welcome to vending machine!")

        while (true) {
            val cmd = readln()

            if (cmd.matches(insertCommandRegex)) {
                val (_, sAmount) = cmd.split(" ")
                val coin = when (sAmount.toInt()) {
                    5 -> RubleCoins.FIVE
                    10 -> RubleCoins.TEN
                    else -> {
                        println("io error: incorrect coin value")
                        continue
                    }
                }

                processOutput(fsm.act(InsertCoin(coin)))

            } else if (cmd.matches(selectCommandRegex)) {
                val (_, sProductIndex) = cmd.split(" ")
                val product = bootState.products.getOrNull(sProductIndex.toInt())

                if (product != null)
                    processOutput(fsm.act(SelectProduct(product)))
                else
                    println("io error: product index out of range")
            } else
                println("io error: incorrect command")
        }
    }
}
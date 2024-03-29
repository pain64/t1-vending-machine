package vm.oop

import kotlinx.serialization.Serializable

class IntCalculator : MoneyCalculator<Int> {
    override fun zero(): Int = 0
    override fun isEnough(expected: Int, actual: Int) = expected <= actual
    override fun add(m1: Int, m2: Int): Int = m1 + m2
}

@Serializable
data class NamedProduct(
    override val price: Int,
    val name: String
) : Product<Int>

enum class RubleCoin(override val value: Int) : Coin<Int> {
    FIVE(5), TEN(10)
}

// stateful, IO
class ConsoleVendingMachine(
    private val products: MutableMap<NamedProduct, Int>
) : AbstractVendingMachine<Int, NamedProduct, RubleCoin>(IntCalculator(), products) {

    override fun onDisplayProductIsOut(product: NamedProduct) {
        println("Продукт закончился $product")
    }

    override fun onDisplayProductNotSelected() {
        println("продукт не выбран")
    }

    override fun onEjectProduct(product: NamedProduct) {
        println("продукт выдан $product")
    }

    override fun onEjectCoin(coin: RubleCoin) {
        println("монетка выдана $coin")
    }

    fun mainCycle() {
        while (true) {
            val (comm, value) = readln().split(" ", limit = 2)
            when (comm) {
                "i" -> insertCoin(
                    when (value.toInt()) {
                        5 -> RubleCoin.FIVE
                        10 -> RubleCoin.TEN
                        else -> throw Exception("unknown coin value")
                    }
                )

                "s" -> products.keys.find { it.name == value }?.let { selectProduct(it) }
            }
        }
    }
}
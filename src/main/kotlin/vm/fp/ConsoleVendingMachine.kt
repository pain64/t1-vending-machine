package vm.fp

import kotlinx.serialization.Serializable

enum class RubleCoin(override val value: Int) : Coin<Int> {
    FIVE(5), TEN(10)
}

@Serializable
data class NamedProduct(
    override val price: Int,
    val name: String,
) : Product<Int>

// pure function
fun formatMessage(message: VmOutput<Int, NamedProduct, RubleCoin>): String =
    TODO()

class BadInputException(override val message: String) : Exception(message)

// pure function
fun decodeInput(products: Map<NamedProduct, Int>, input: String)
        : VmInput<Int, NamedProduct, RubleCoin> {
    TODO()
}

// IO function
fun vmRunInfinite(products: Map<NamedProduct, Int>) {
    val calc = object : MoneyCalculator<Int> {
        override fun zero() = 0
        override fun isEnough(m1: Int, m2: Int) = m1 <= m2
        override fun add(m1: Int, m2: Int) = m1 + m2
    }

    fun run(ctx: Context<Int, NamedProduct, RubleCoin>) {
        TODO()
    }

    run(Context(products, Idle()))
}
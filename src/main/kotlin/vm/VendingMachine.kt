package vm

interface Product<M> {
    var availableCount: Int
    val price: M
}

interface Coin<M> {
    val value: M
}

sealed interface VmInput<M, P : Product<M>, C : Coin<M>>

class SelectProduct<M, P : Product<M>, C : Coin<M>>(
    val product: P
) : VmInput<M, P, C>

class InsertCoin<M, P : Product<M>, C : Coin<M>>(
    val coin: C
) : VmInput<M, P, C>

sealed interface VmOutput<M, P : Product<M>, C : Coin<M>>

class DisplayProductIsOut<M, P : Product<M>, C : Coin<M>>(
    val product: Product<M>
) : VmOutput<M, P, C>

class DisplayProductNotSelected<M, P : Product<M>, C : Coin<M>>
    : VmOutput<M, P, C>

class EjectCoin<M, P : Product<M>, C : Coin<M>>(
    val coin: C
) : VmOutput<M, P, C>

class EjectProduct<M, P : Product<M>, C : Coin<M>>(
    val product: P
) : VmOutput<M, P, C>


interface MoneyCalculator<M> {
    fun zero(): M
    fun add(m1: M, m2: M): M
    fun isLessThanOrEqual(m1: M, m2: M): Boolean
}

private sealed interface VmState<M, P : Product<M>, C : Coin<M>>

private class Idle<M, P : Product<M>, C : Coin<M>>
    : VmState<M, P, C>

private class Paying<M, P : Product<M>, C : Coin<M>>(
    val product: P,
    var sum: M,
) : VmState<M, P, C>

class VendingMachineFsm<M, P : Product<M>, C : Coin<M>>(
    private val calc: MoneyCalculator<M>,
) {
    private var state: VmState<M, P, C> = Idle()

    fun act(input: VmInput<M, P, C>): List<VmOutput<M, P, C>> =
        when (input) {
            is InsertCoin ->
                when (val st = state) {
                    is Idle -> listOf(
                        DisplayProductNotSelected(),
                        EjectCoin(input.coin),
                    )

                    is Paying -> {
                        st.sum = calc.add(st.sum, input.coin.value)
                        if (calc.isLessThanOrEqual(st.product.price, st.sum)) {
                            st.product.availableCount -= 1
                            state = Idle()
                            listOf(EjectProduct(st.product))
                        } else
                            emptyList()
                    }
                }

            is SelectProduct -> {
                if (input.product.availableCount == 0)
                    listOf(DisplayProductIsOut(input.product))
                else {
                    state = Paying(input.product, calc.zero())
                    emptyList()
                }
            }
        }
}
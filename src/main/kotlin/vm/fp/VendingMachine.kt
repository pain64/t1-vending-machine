package vm.fp

interface Product<M> {
    val price: M
}

interface Coin<M> {
    val value: M
}

interface MoneyCalculator<M> {
    fun zero(): M
    fun add(m1: M, m2: M): M
    fun isEnough(m1: M, m2: M): Boolean
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
    val product: P
) : VmOutput<M, P, C>

class DisplayProductNotSelected<M, P : Product<M>, C : Coin<M>>
    : VmOutput<M, P, C>

class EjectCoin<M, P : Product<M>, C : Coin<M>>(
    val coin: C
) : VmOutput<M, P, C>

class EjectProduct<M, P : Product<M>, C : Coin<M>>(
    val product: P
) : VmOutput<M, P, C>


sealed interface VmState<M, P : Product<M>, C : Coin<M>>

class Idle<M, P : Product<M>, C : Coin<M>>
    : VmState<M, P, C>

data class Paying<M, P : Product<M>, C : Coin<M>>(
    val selected: P,
    val sum: M,
) : VmState<M, P, C>


data class Context<M, P : Product<M>, C : Coin<M>>(
    val products: Map<P, Int>,
    val state: VmState<M, P, C>
)

// pure function
fun <M, P : Product<M>, C : Coin<M>> vendingMachineStep(
    calc: MoneyCalculator<M>,
    ctx: Context<M, P, C>,
    input: VmInput<M, P, C>
): Pair<Context<M, P, C>, List<VmOutput<M, P, C>>> =
    when (input) {
        is InsertCoin ->
            TODO()
        is SelectProduct ->
            TODO()
    }
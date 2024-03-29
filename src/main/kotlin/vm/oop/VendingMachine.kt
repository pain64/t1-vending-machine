package vm.oop

import vm.Coin
import vm.Idle
import vm.MoneyCalculator
import vm.Paying
import vm.Product

interface MoneyCalculator<M> {
    fun zero(): M
    fun add(m1: M, m2: M): M
    fun enough(expected: M, actual: M): Boolean
}

interface Product<M> {
    val price: M
}

interface Coin<M> {
    val value: M
}

sealed interface State<M, P : Product<M>>
class Idle<M, P : Product<M>> : State<M, P>
class Paying<M, P : Product<M>>(var selectedProduct: P, var sum: M) : State<M, P>

abstract class AbstractVendingMachine<M, P : Product<M>, C : Coin<M>>(
    private val calculator: MoneyCalculator<M>,
    private val products: MutableMap<P, Int>
) {
    private var state: State<M, P> = vm.Idle()

    abstract fun onDisplayProductIsOut(product: P)
    abstract fun onDisplayProductNotSelected()
    abstract fun onEjectCoin(coin: C)
    abstract fun onEjectProduct(product: P)

    fun insertCoin(c: C) {
        when (val s = state) {
            is Paying -> {

                s.sum = calculator.add(s.sum, c.value)

                if (calculator.enough(s.selectedProduct.price, s.sum)) {
                    onEjectProduct(s.selectedProduct)
                    products[s.selectedProduct] = products[s.selectedProduct]!! - 1
                }
            }

            is Idle -> {
                onDisplayProductNotSelected()
                onEjectCoin(c)
            }
        }
    }

    fun selectProduct(p: P) {
        if (state is Idle && (products[p] ?: 0) > 0) {
            state = vm.Paying(p, calculator.zero())
        } else {
            onDisplayProductIsOut(p)
        }
    }
}
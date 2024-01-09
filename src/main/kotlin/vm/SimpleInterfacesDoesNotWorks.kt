package vm

interface M {
    fun add(m1: M): M
}

class Ruble(amount: Int) : M {
    override fun add(m1: M): M {
        TODO()
    }
}

class Euro(amount: Int) : M {
    override fun add(m1: M): M {
        TODO()
    }
}

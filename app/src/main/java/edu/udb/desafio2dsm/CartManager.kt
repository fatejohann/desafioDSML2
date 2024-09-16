package edu.udb.desafio2dsm

object CartManager {
    private val itemsInCart = mutableListOf<MenuItem>()

    fun addItem(item: MenuItem) {
        itemsInCart.add(item)
    }

    fun getCartItems(): List<MenuItem> {
        return itemsInCart
    }

    fun clearCart() {
        itemsInCart.clear()
    }
}



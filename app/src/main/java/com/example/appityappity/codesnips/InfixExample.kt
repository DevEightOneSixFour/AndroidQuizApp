package com.example.appityappity.codesnips

fun main() {
    attackTest()
}
class Warrior(var hp: Int, var ap: Int) {
    infix fun attack(target: Warrior) {
        target.hp -= ap
    }
}

fun attackTest() {
    val warrior1 = Warrior(100, 20)
    val warrior2 = Warrior(100, 15)

    warrior1 attack warrior2
    warrior2 attack warrior1

    println("Warrior1 Remaining HP: ${warrior1.hp}")
    println("Warrior2 Remaining HP: ${warrior2.hp}")
}
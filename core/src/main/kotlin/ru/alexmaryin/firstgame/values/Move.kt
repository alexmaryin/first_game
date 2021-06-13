package ru.alexmaryin.firstgame.values

import com.badlogic.gdx.math.Vector3

object Move {
    val Up = Vector3(0f, 1f, 0f)
    val Down = Vector3(0f, -1f, 0f)
    val Left = Vector3(-1f, 0f, 0f)
    val Right = Vector3(1f, 0f, 0f)
    val Back = Vector3(0f, 0f, 1f)
    val Front = Vector3(0f, 0f, -1f)
}
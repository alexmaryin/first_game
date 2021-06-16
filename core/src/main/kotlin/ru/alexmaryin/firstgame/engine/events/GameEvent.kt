package ru.alexmaryin.firstgame.engine.events

import com.badlogic.ashley.core.Entity

sealed class GameEvent

class GameOver(val score: Int) : GameEvent()
class CopCatchEnemy(val cop: Entity, val enemy: Entity) : GameEvent()
object EnemyMissed : GameEvent()
object EnemyCaught : GameEvent()
object CopMissed : GameEvent()
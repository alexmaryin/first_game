package ru.alexmaryin.deter_rev.engine.events

import com.badlogic.ashley.core.Entity
import ru.alexmaryin.deter_rev.ui.DialogResult

sealed class GameEvent

class GameOver(val score: Int) : GameEvent()
class CopCatchEnemy(val cop: Entity, val enemy: Entity) : GameEvent()
class PlayerRestoresCop(val cop: Entity) : GameEvent()
class LevelUp(val level: Int) : GameEvent()
class DialogExit(val result: DialogResult) : GameEvent()
object PlayerSendCop : GameEvent()
object EnemyMissed : GameEvent()
object EnemyCaught : GameEvent()
object CopMissed : GameEvent()
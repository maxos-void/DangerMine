package me.maxos.votive.dangerMine.placeholderapi

import me.maxos.votive.dangerMine.extensions.DurationExtension.formatted
import me.maxos.votive.dangerMine.mine.manager.MineManager

class MineDataProvider(
	private val mineManager: MineManager
) {
	companion object {
		const val UNKNOWN_MINE = "---"
		const val UNKNOWN_OPEN = "Не сегодня..."
		const val UNKNOWN_CLOSE = "Уже закрыта"
	}

	fun status(mineId: String): String {
		val mine = mineManager.getMineById(mineId) ?: return UNKNOWN_MINE
		return when (mine.isOpen) {
			true -> "Открыта"
			false -> "Закрыта"
		}
	}

	fun beforeOpening(mineId: String): String {
		val mine = mineManager.getMineById(mineId) ?: return UNKNOWN_MINE
		return mine.beforeOpen?.formatted() ?: UNKNOWN_OPEN
	}

	fun beforeClosing(mineId: String): String {
		val mine = mineManager.getMineById(mineId) ?: return UNKNOWN_MINE
		return mine.beforeClosing?.formatted() ?: UNKNOWN_CLOSE
	}

	fun getCount(): String = mineManager.mines.size.toString()
	fun getCountOpen(): String = mineManager.mines
		.filter {
			it.isOpen
		}.size.toString()
}
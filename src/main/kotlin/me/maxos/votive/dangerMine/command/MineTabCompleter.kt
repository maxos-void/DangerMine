package me.maxos.votive.dangerMine.command

import me.maxos.votive.dangerMine.mine.manager.MineManager.Region
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class MineTabCompleter: TabCompleter {
	private val subCommands = listOf(
		"reset-radius",
		"reset-mine",
		"reset-global",
		"set-entrance",
		"reload"
	)

	private var minesNames: List<String>? = null
	fun initNames() {
		minesNames = Region.regionNames.toList()
	}

	override fun onTabComplete(
		sender: CommandSender,
		command: Command,
		label: String,
		args: Array<out String>?
	): List<String?>? {
		if (args?.size == 1) return subCommands

		val subCommand = args?.getOrNull(1)
		if (subCommand == "reset-mine" || subCommand == "set-entrance")
			return minesNames?.toList()

		return null
	}
}
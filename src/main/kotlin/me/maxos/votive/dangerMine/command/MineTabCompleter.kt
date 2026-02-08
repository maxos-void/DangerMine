package me.maxos.votive.dangerMine.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class MineTabCompleter: TabCompleter {
	companion object {
		val subCommands = listOf("reload")
	}
	override fun onTabComplete(
		sender: CommandSender,
		command: Command,
		label: String,
		args: Array<out String>?
	): List<String?>? {

		return if (args?.size == 1) subCommands else null
	}
}
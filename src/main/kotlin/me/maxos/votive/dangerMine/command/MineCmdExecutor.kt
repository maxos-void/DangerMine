package me.maxos.votive.dangerMine.command

import me.maxos.votive.dangerMine.DangerMine
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class MineCmdExecutor(
	private val plugin: DangerMine
): CommandExecutor {
	override fun onCommand(
		sender: CommandSender,
		command: Command,
		label: String,
		args: Array<out String>?
	): Boolean {

		when(args?.get(0) ?: return false) {
			"reload" -> {
				plugin.onReload()
				return true
			}
			else -> return false
		}
	}
}
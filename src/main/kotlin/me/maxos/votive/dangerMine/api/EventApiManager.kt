package me.maxos.votive.dangerMine.api

import me.maxos.votive.dangerMine.mine.action.MineAction
import me.maxos.votive.dangerMine.utils.Scheduler.runSyncTask
import org.bukkit.Bukkit
import org.bukkit.event.Event

object EventApiManager {
	private val manager = Bukkit.getPluginManager()
	fun callEvent(event: Event) {
		runSyncTask {
			manager.callEvent(event)
		}
	}

}
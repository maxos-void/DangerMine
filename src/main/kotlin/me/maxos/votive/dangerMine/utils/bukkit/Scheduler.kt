package me.maxos.votive.dangerMine.utils.bukkit

import me.maxos.votive.dangerMine.DangerMine
import org.bukkit.Bukkit

object Scheduler {

	private var plugin: DangerMine? = null

	fun initialization(plugin: DangerMine) {
		this.plugin = plugin
	}

	fun runSyncTask(operation: () -> Unit): Int {
		val taskId = Bukkit.getScheduler().runTask(plugin ?: return -1,
			Runnable(operation)
		).taskId
		return taskId
	}

	fun runAsyncTask(operation: () -> Unit): Int {
		val taskId = Bukkit.getScheduler().runTaskAsynchronously(plugin ?: return -1,
			Runnable(operation)
		).taskId
		return taskId
	}

	fun runAsyncTaskTimer(time: Int, operation: () -> Unit): Int {
		val timeToSecond = time * 20L
		val taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin ?: return -1,
			Runnable(operation), timeToSecond, timeToSecond
		).taskId
		return taskId
	}

	fun runSyncTaskTimer(time: Int, operation: () -> Unit): Int {
		val timeToSecond = time * 20L
		val taskId = Bukkit.getScheduler().runTaskTimer(plugin ?: return -1,
			Runnable(operation), timeToSecond, timeToSecond
		).taskId
		return taskId
	}

	fun stopTask(id: Int) {
		Bukkit.getScheduler().cancelTask(id)
	}

}
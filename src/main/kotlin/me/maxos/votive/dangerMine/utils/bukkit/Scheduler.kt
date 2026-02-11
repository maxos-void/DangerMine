package me.maxos.votive.dangerMine.utils.bukkit

import me.maxos.votive.dangerMine.DangerMine
import org.bukkit.Bukkit

object Scheduler {

	private var plugin: DangerMine? = null
	private val scheduler = Bukkit.getScheduler()

	fun initialization(plugin: DangerMine) {
		this.plugin = plugin
	}

	fun runSyncTask(operation: () -> Unit): Int {
		val taskId = scheduler.runTask(plugin ?: return -1,
			Runnable(operation)
		).taskId
		return taskId
	}

	fun runSyncTaskLater(time: Int, isTick: Boolean = false, operation: () -> Unit): Int {
		val timeToSecond = if (isTick) time.toLong() else time * 20L
		val taskId = scheduler.runTaskLater(plugin ?: return -1,
			Runnable(operation),
			timeToSecond
		).taskId
		return taskId
	}

	fun runAsyncTask(operation: () -> Unit): Int {
		val taskId = scheduler.runTaskAsynchronously(plugin ?: return -1,
			Runnable(operation)
		).taskId
		return taskId
	}

	fun runAsyncTaskTimer(time: Int, isTick: Boolean = false, operation: () -> Unit): Int {
		val timeToSecond = if (isTick) time.toLong() else time * 20L
		val taskId = scheduler.runTaskTimerAsynchronously(plugin ?: return -1,
			Runnable(operation), timeToSecond, timeToSecond
		).taskId
		return taskId
	}

	fun runSyncTaskTimer(time: Int, isTick: Boolean = false, operation: () -> Unit): Int {
		val timeToSecond = if (isTick) time.toLong() else time * 20L
		val taskId = scheduler.runTaskTimer(plugin ?: return -1,
			Runnable(operation), timeToSecond, timeToSecond
		).taskId
		return taskId
	}

	fun stopTask(id: Int?) {
		scheduler.cancelTask(id ?: return)
	}

	fun stopAllTask() {
		scheduler.cancelTasks(plugin ?: return)
	}

}
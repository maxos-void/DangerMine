package me.maxos.votive.dangerMine.file.config.msg

import me.maxos.votive.dangerMine.utils.ColorUtils

class Messages(
	private val messageLoader: MessageLoader,
) {

	private val defaultMap = mapOf(
		"closed" to "§cШахта закрыта!",
		"closed-teleport" to "§cШахта закрыта! Достаём вас из неё!",
		"creative-block" to "§eВы ломаете блоки в шахте, находясь в креативе!",
		"entrance-set" to "§aВы успешно изменили точку входа шахты!",
		"not-in-region" to "§cВы не находитесь в регионе существующей шахты!",
		"not-found" to "§cШахта с таким названием не обнаружена!",
	)

	private lateinit var prefix: String
	private lateinit var messages: Map<String, String>

	init {
		load()
	}

	fun msg(key: String): String = messages[key] ?: defaultMap[key] ?: ""

	fun reload() {
		messageLoader.reloadConfig()
		load()
	}

	private fun load() {
		prefix = messageLoader.initPrefix() ?: ""
		messages = messageLoader.initMessages()?.apply {
			colorize()
		} ?: defaultMap
	}

	private fun HashMap<String, String>.colorize() {
		this.replaceAll { _, str ->
			ColorUtils.colorize(str.parsePrefix())
		}
	}

	private fun String.parsePrefix(): String {
		return this.replace("{prefix}", prefix)
	}

}
package me.maxos.votive.dangerMine.utils

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

object ColorUtils {

	// поддерживаемые форматы &F | &#FFFFFF
	private val legacySerializer = LegacyComponentSerializer.builder()
		.hexColors()
		.useUnusualXRepeatedCharacterHexFormat()
		.build()
	// покраска строки
	fun colorize(message: String): String {
		return legacySerializer.serialize(
			LegacyComponentSerializer.legacyAmpersand().deserialize(message)
		)
	}
	// покраска списка строк
	fun colorizeList(messages: List<String>): List<String> {
		return messages.map { colorize(it) }
	}

}
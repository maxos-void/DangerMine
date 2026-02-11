package me.maxos.votive.dangerMine.file.config.msg

import me.maxos.votive.dangerMine.file.Config
import me.maxos.votive.dangerMine.file.FileManager

class MessageLoader(
	fileManager: FileManager
): Config(fileManager) {

	companion object {
		const val MESSAGES_SECTION_NAME = "messages"
		const val PREFIX_SECTION_NAME = "prefix"
	}

	fun initPrefix(): String? {
		return getRequiredSection(PREFIX_SECTION_NAME)?.getString("prefix")
	}

	fun initMessages(): HashMap<String, String>? {
		return getRequiredSection(MESSAGES_SECTION_NAME)?.let {
			hashMapOf<String, String>().apply {
				it.getKeys(false).forEach { key ->
					put(key, it.getString(key) ?: return@forEach)
				}
			}
		}
	}

}
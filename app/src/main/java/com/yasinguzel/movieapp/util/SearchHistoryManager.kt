package com.yasinguzel.movieapp.util

import android.content.Context
import android.content.SharedPreferences

class SearchHistoryManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("search_history_prefs", Context.MODE_PRIVATE)
    private val key = "history_items"

    /**
     * Saves a new search query.
     * It ensures the list is unique and limits the size to 10 items.
     */
    fun addSearchQuery(query: String) {
        if (query.isBlank()) return

        // Get current list
        val currentList = getHistory().toMutableList()

        // Remove if exists (to move it to the top)
        currentList.remove(query)

        // Add to the beginning
        currentList.add(0, query)

        // Keep only top 10
        if (currentList.size > 10) {
            currentList.removeAt(currentList.lastIndex)
        }

        // Save as a comma-separated string (Simple serialization)
        prefs.edit().putString(key, currentList.joinToString(",")).apply()
    }

    /**
     * Retrieves the list of past search queries.
     */
    fun getHistory(): List<String> {
        val historyString = prefs.getString(key, "") ?: ""
        return if (historyString.isEmpty()) {
            emptyList()
        } else {
            historyString.split(",")
        }
    }

    /**
     * Clears specific item from history.
     */
    fun removeItem(query: String) {
        val currentList = getHistory().toMutableList()
        currentList.remove(query)
        prefs.edit().putString(key, currentList.joinToString(",")).apply()
    }

    fun clearAll() {
        prefs.edit().remove(key).apply()
    }
}
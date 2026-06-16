package com.example.pos_utfpr_usingdb.utils

import android.content.ContentValues
import android.content.Context
import android.widget.TextView
import android.widget.Toast

object Utils {
    private const val DEFAULT_REQUIRED_MESSAGE = "Preencha todos os campos!"

    /**
     * Validates if the provided [fields] are not empty.
     *
     * If any field is empty or contains only whitespace, a [Toast] message is displayed
     * and the function returns false.
     *
     * @param context The context used to display the [Toast].
     * @param fields Variable number of [TextView] components to validate.
     * @param message The message to show in the [Toast] if validation fails.
     * @return `true` if all fields are non-empty, `false` otherwise.
     */
    fun validateFields(
        context: Context,
        vararg fields: TextView,
        message: String = DEFAULT_REQUIRED_MESSAGE
    ): Boolean {
        val hasEmptyField = fields.any { it.text.toString().trim().isEmpty() }

        if (hasEmptyField) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        return !hasEmptyField
    }

    fun getContentValues(vararg values: Pair<String, Any?>): ContentValues {
        return ContentValues().apply {
            values.forEach { (key, value) ->
                when (value) {
                    null -> putNull(key)
                    is String -> put(key, value)
                    is Int -> put(key, value)
                    is Long -> put(key, value)
                    is Float -> put(key, value)
                    is Double -> put(key, value)
                    is Boolean -> put(key, value)
                    is Byte -> put(key, value)
                    is Short -> put(key, value)
                    is ByteArray -> put(key, value)
                    else -> put(key, value.toString())
                }
            }
        }
    }

    fun clearFields(vararg fields: TextView) {
        fields.forEach { field ->
            field.text = ""
            field.error = null
        }
    }
}

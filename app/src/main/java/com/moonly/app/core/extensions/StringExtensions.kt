package com.moonly.app.core.extensions

import com.moonly.app.core.utils.ValidationUtils

/**
 * Verifica si el string es un email válido
 */
fun String.isValidEmail(): Boolean {
    return ValidationUtils.isValidEmail(this)
}

/**
 * Verifica si el string es una contraseña válida
 */
fun String.isValidPassword(): Boolean {
    return ValidationUtils.isValidPassword(this)
}

/**
 * Capitaliza la primera letra del string
 */
fun String.capitalizeFirst(): String {
    return this.replaceFirstChar { it.uppercase() }
}

/**
 * Trunca el string a una longitud máxima con "..."
 */
fun String.truncate(maxLength: Int): String {
    return if (this.length > maxLength) {
        "${this.substring(0, maxLength)}..."
    } else {
        this
    }
}

/**
 * Remueve espacios en blanco al inicio y final
 */
fun String.trimAll(): String {
    return this.trim()
}

/**
 * Verifica si el string está vacío o es solo espacios en blanco
 */
fun String.isBlankOrEmpty(): Boolean {
    return this.isBlank() || this.isEmpty()
}
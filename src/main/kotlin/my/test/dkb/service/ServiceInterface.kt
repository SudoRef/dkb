package my.test.dkb.service

import my.test.dkb.model.UrlModel


interface ServiceInterface {

    companion object {
        const val HASH_LENGTH: Int = 6
    }
    fun generateHash(url: String): String?
    fun findAlias(url: String): String?
    fun findUrl(alias: String): String?
    fun saveAlias(model: UrlModel)
}
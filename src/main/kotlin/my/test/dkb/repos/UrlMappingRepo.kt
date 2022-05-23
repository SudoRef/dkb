package my.test.dkb.repos

import my.test.dkb.model.UrlModel
import org.springframework.data.mongodb.repository.MongoRepository



interface UrlMappingRepo : MongoRepository<UrlModel, String> {
    fun findByAlias(alias: String): UrlModel?
    fun findByOriginalURL(url: String): UrlModel?
    fun existsByOriginalURL(url: String): Boolean

}
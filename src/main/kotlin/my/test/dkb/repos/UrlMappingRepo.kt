package my.test.dkb.repos

import my.test.dkb.model.UrlModel
import org.springframework.data.mongodb.repository.MongoRepository



interface UrlMappingRepo : MongoRepository<UrlModel, String> {
    fun findByAlias(alias: String): UrlModel
    fun existsByAlias(alias: String): Boolean
}
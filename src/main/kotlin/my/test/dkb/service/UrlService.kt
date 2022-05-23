package my.test.dkb.service

import my.test.dkb.model.UrlModel
import my.test.dkb.repos.UrlMappingRepo
import org.apache.commons.text.CharacterPredicate
import org.apache.commons.text.CharacterPredicates
import org.apache.commons.text.RandomStringGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UrlService(
    @Autowired
    val urlsRepo: UrlMappingRepo
) {

    companion object {
        const val HASH_LENGTH: Int = 6
    }

    private val stringGenerator = RandomStringGenerator.Builder()
        .withinRange('0'.code, 'z'.code)
        .filteredBy(CharacterPredicate { codePoint ->
            CharacterPredicates.LETTERS.test(codePoint) || CharacterPredicates.DIGITS.test(
                codePoint
            )
        })
        .build()

    fun generateHash(): String {
        var hash = stringGenerator.generate(HASH_LENGTH)
        while (urlsRepo.existsByOriginalURL(hash)) {
            hash = stringGenerator.generate(HASH_LENGTH)
        }
        return hash
    }

    fun findAlias(url: String): String? {
        return urlsRepo.findByOriginalURL(url)?.alias
    }

    fun findUrl(alias: String): String? {
        val model: UrlModel? = urlsRepo.findByAlias(alias)
        return model?.originalURL
    }

    fun saveAlias(model: UrlModel){
        urlsRepo.save(model)
    }
}
package my.test.dkb.service

import my.test.dkb.model.UrlModel
import my.test.dkb.repos.UrlMappingRepo
import org.apache.commons.text.CharacterPredicate
import org.apache.commons.text.CharacterPredicates
import org.apache.commons.text.RandomStringGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UrlService : ServiceInterface {

    @Autowired
    private lateinit var urlsRepo: UrlMappingRepo

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

    override fun generateHash(url: String): String? {
        val hash = stringGenerator.generate(HASH_LENGTH)
        if (urlsRepo.existsByOriginalURL(url)) {
            return urlsRepo.findByOriginalURL(url)?.alias
        }
        return hash
    }

    override fun findAlias(url: String): String? {
        return urlsRepo.findByOriginalURL(url)?.alias
    }

    override fun findUrl(alias: String): String? {
        val model: UrlModel? = urlsRepo.findByAlias(alias)
        return model?.originalURL
    }

    override fun saveAlias(model: UrlModel){
        urlsRepo.save(model)
    }
}
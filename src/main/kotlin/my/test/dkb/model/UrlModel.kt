package my.test.dkb.model
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "dkb")
data class UrlModel (
                    val alias: String?,
                     val originalURL: String?)
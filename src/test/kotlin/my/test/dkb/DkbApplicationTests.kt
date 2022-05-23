package my.test.dkb

import my.test.dkb.models.UrlModel
import my.test.dkb.repos.UrlMappingRepo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DkbApplicationTests {


    @Autowired lateinit var mockMvc: MockMvc

    @MockBean
    val urlMappingRepo: UrlMappingRepo? = null

    @Autowired
    val restTemplate: TestRestTemplate? = null

    @Test
    fun canCreateByOriginalURL() {
        // given
        given(urlMappingRepo?.findByOriginalURL("https://www.youtube.com/"))
            .willReturn(UrlModel("nkJrJL", "https://www.youtube.com/"))

        // when
        val urlResponse: ResponseEntity<String>? =
            restTemplate?.postForEntity("/urls?original-url=https://www.youtube.com/", String::class.java)

        // then
        assertThat(urlResponse?.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(urlResponse?.body).isEqualTo("Original URL https://www.youtube.com/ saved as nkJrJL")
    }

    @Test
    fun canGetByAlias() {
        // given
       mockMvc.get("/nkJrJL")
           .andExpect { status { is3xxRedirection() }}
           .andExpect { redirectedUrl("http://localhost:8080") }
    }

    @Test
    fun get404Error() {
        mockMvc.get("/asdf/asdf")
            .andExpect { status { isNotFound() }}

    }

}

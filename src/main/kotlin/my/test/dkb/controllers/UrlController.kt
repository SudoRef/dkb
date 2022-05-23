package my.test.dkb.controllers

import my.test.dkb.model.UrlModel
import my.test.dkb.service.ServiceInterface
import my.test.dkb.service.UrlService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView
import javax.servlet.http.HttpServletRequest

@Component
@RestController
class UrlController(
    @Autowired
    val service: ServiceInterface
) {

    @PostMapping("/urls")
    fun addURL(@RequestParam("originalURL") originalURL: String): ResponseEntity<String> {
        var alias = service.generateHash(originalURL)
        if (service.findAlias(originalURL).isNullOrEmpty()) {
            service.saveAlias(UrlModel(alias, originalURL))
        } else {
            alias = service.findAlias(originalURL)
        }
        return ResponseEntity.ok("Original URL $originalURL saved as $alias")
    }

    @GetMapping("/{alias:^\\w+}")
    fun getURL(@PathVariable("alias") alias: String, request: HttpServletRequest): RedirectView {
        var originalURL = service.findUrl(alias)
        if (originalURL.isNullOrEmpty()) {
            originalURL = request.baseUrl
        }
        return RedirectView(originalURL)
    }

    val HttpServletRequest.baseUrl: String
        get() = "$scheme://$serverName${if (serverName == "localhost") ":8080" else ""}"

}
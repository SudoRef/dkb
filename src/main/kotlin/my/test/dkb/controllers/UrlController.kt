package my.test.dkb.controllers

import my.test.dkb.model.UrlModel
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
    val service: UrlService
) {

    @PostMapping("/urls")
    fun addURL(@RequestParam("originalURL") originalURL: String): ResponseEntity<String> {
        val alias = service.generateHash()
        if (service.findAlias(alias).isNullOrEmpty()) {
            service.saveAlias(UrlModel(alias, originalURL))
        }
        return ResponseEntity.ok("saved $originalURL as $alias")
    }

    @GetMapping("/{alias}")
    fun getURL(@PathVariable("alias") alias: String, request: HttpServletRequest): RedirectView {
        var originalURL = service.findAlias(alias)
        if(originalURL.isNullOrEmpty()){
            originalURL = request.baseUrl
        }
        return RedirectView(originalURL)
    }

    val HttpServletRequest.baseUrl: String
        get() = "$scheme://$serverName${if (serverName == "localhost") ":8080" else ""}"

}
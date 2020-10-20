package omsu.webdev.backend.api.resources

import omsu.webdev.backend.api.common.db.Paged
import omsu.webdev.backend.api.common.db.Parameters
import omsu.webdev.backend.api.models.forms.CPUInfoForm
import omsu.webdev.backend.api.models.views.CPUInfoView
import omsu.webdev.backend.api.services.CPUInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(value = ["/sensors/cpu"])
class CPUInfoController(@Autowired var cpuInfoService: CPUInfoService) {

    @RequestMapping(value = [""], produces = [MediaType.APPLICATION_JSON_VALUE], method = [RequestMethod.GET])
    fun getLatestInfo(
            @RequestParam("page") page: Int?,
            @RequestParam("size") size: Int?
    ): ResponseEntity<Paged<CPUInfoView?>?>? {
        return cpuInfoService.getLatestInfoPaged(
                Parameters
                        .builder()
                        .add("pagination",
                                Optional.ofNullable(
                                        page?.let {
                                            size?.let {
                                                PageRequest.of(page, size)
                                            }
                                        }
                                ).orElseGet {
                                    PageRequest.of(0, 50)
                                }
                        )
                        .add("total", 100)
                        .build()
        )?.let { ResponseEntity.ok(it) }
    }

    @RequestMapping(value = ["/new"], produces = [MediaType.APPLICATION_JSON_VALUE], method = [RequestMethod.GET])
    fun collectAndGetInfo(): ResponseEntity<CPUInfoView?>? {
        return cpuInfoService.generateAndInsertLatest(
                Parameters().Builder().build()
        )?.let { ResponseEntity.ok(it) }
    }

    @RequestMapping(value = ["/create"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE], method = [RequestMethod.GET])
    fun create(@RequestBody body: CPUInfoForm): ResponseEntity<CPUInfoView?>? {
        return cpuInfoService.insert(
                Parameters().Builder().build(),
                body
        )?.let { ResponseEntity.ok(it) }
    }

}
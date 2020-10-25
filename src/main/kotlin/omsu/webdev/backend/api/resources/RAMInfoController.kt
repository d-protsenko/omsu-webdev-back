package omsu.webdev.backend.api.resources

import omsu.webdev.backend.api.common.db.Paged
import omsu.webdev.backend.api.common.db.Parameters
import omsu.webdev.backend.api.models.forms.LoggingInfoForm
import omsu.webdev.backend.api.models.forms.RAMInfoForm
import omsu.webdev.backend.api.models.views.RAMInfoView
import omsu.webdev.backend.api.services.LoggingInfoService
import omsu.webdev.backend.api.services.RAMInfoService
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
@RequestMapping(value = ["/sensors/ram"])
class RAMInfoController(
    @Autowired var ramInfoService: RAMInfoService,
    @Autowired var loggingInfoService: LoggingInfoService
) {

    @RequestMapping(value = [""], produces = [MediaType.APPLICATION_JSON_VALUE], method = [RequestMethod.GET])
    fun getLatestInfo(
        @RequestParam("page") page: Int?,
        @RequestParam("size") size: Int?
    ): ResponseEntity<Paged<RAMInfoView?>?>? {
        return ramInfoService.getLatestInfoPaged(
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
                .build()
        )?.let { ResponseEntity.ok(it) }
    }

    @RequestMapping(value = ["/new"], produces = [MediaType.APPLICATION_JSON_VALUE], method = [RequestMethod.GET])
    fun collectAndGetInfo(): ResponseEntity<RAMInfoView?>? {
        val view: RAMInfoView? = ramInfoService.generateAndInsertLatest(
            Parameters().Builder().build()
        )
        loggingInfoService.insert(
            Parameters().Builder().build(),
            LoggingInfoForm(
                message = "Collecting a RAM info from local machine. RAM usage:" + view?.used,
                updatedAt = view?.updatedAt?.toInstant()
            )
        )
        return view?.let { ResponseEntity.ok(it) }
    }

    @RequestMapping(value = ["/create"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE], method = [RequestMethod.POST])
    fun create(@RequestBody body: RAMInfoForm): ResponseEntity<RAMInfoView?>? {
        val view: RAMInfoView? = ramInfoService.insert(
            Parameters().Builder().build(),
            body
        )
        loggingInfoService.insert(
            Parameters().Builder().build(),
            LoggingInfoForm(
                message = "Inserting a RAM info gathered from remote machine. RAM usage:" + view?.used,
                updatedAt = view?.updatedAt?.toInstant()
            )
        )
        return view?.let { ResponseEntity.ok(it) }
    }
}
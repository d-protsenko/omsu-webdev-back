package omsu.webdev.backend.api.services

import lombok.Data
import omsu.webdev.backend.api.models.domain.CPUInfo
import omsu.webdev.backend.api.models.domain.RAMInfo
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import oshi.SystemInfo
import oshi.hardware.CentralProcessor
import oshi.hardware.GlobalMemory
import oshi.hardware.HardwareAbstractionLayer

@Service
class HardwareUsageService {
    companion object {
        private const val biGiga: Double = 1024 * 1024 * 1024.0
        private const val decGiga: Double = 1000000000.0
    }
    private val si = SystemInfo()
    private val hal: HardwareAbstractionLayer = si.hardware
    private val cpu: CentralProcessor = hal.processor
    private val memory: GlobalMemory = si.hardware.memory
    private var prevTicks = LongArray(CentralProcessor.TickType.values().size)

    @Scheduled(
            fixedRate = 300000
    )
    fun updateTicks() {
        prevTicks = cpu.systemCpuLoadTicks
    }

    fun getCPUInfo(): CPUInfo {
        val cpuLoad: Double = cpu.getSystemCpuLoadBetweenTicks(prevTicks) * 100
        prevTicks = cpu.systemCpuLoadTicks
        return CPUInfo(
                threads = cpu.logicalProcessors.size,
                cores =  cpu.physicalProcessorCount,
                clock =  cpu.maxFreq / decGiga,
                cpuUsage =  cpuLoad
        )
    }

    fun getRAMInfo(): RAMInfo {
        val info = RAMInfo(
                total =  memory.total / biGiga,
                available =  memory.available / biGiga
        )
        info.available?.let { available ->
            info.total?.let { total ->
                info.free = available / total * 100
                info.used = 100 - info.free!!
            }
        }
        return info
    }
}

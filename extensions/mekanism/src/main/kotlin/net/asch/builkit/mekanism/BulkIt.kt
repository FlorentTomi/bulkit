package net.asch.builkit.mekanism

import net.asch.builkit.mekanism.setup.Resources
import net.asch.bulkit.api.BulkItApi
import net.neoforged.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(BulkIt.ID)
object BulkIt {
    const val ID = "${BulkItApi.ID}_mekanism"
    val LOGGER: Logger = LogManager.getLogger()

    val RESOURCES = Resources()
}
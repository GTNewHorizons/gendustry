/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/gendustry
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.gendustry.machines.mproducer

import net.bdew.gendustry.gui.BlockGuiWrenchable
import net.bdew.gendustry.machines.BaseMachineBlock
import net.bdew.gendustry.misc.BlockTooltipHelper
import net.bdew.lib.block.{BlockKeepData, BlockTooltip, HasTE}
import net.bdew.lib.covers.BlockCoverable
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

object BlockMutagenProducer
    extends BaseMachineBlock("mutagen_producer")
    with HasTE[TileMutagenProducer]
    with BlockCoverable[TileMutagenProducer]
    with BlockGuiWrenchable
    with BlockTooltip
    with BlockKeepData {
  val TEClass = classOf[TileMutagenProducer]
  lazy val guiId = MachineMutagenProducer.guiId

  override def getTooltip(
      stack: ItemStack,
      player: EntityPlayer,
      advanced: Boolean
  ): List[String] = {
    if (stack.hasTagCompound && stack.getTagCompound.hasKey("data")) {
      val data = stack.getTagCompound.getCompoundTag("data")
      List.empty ++
        BlockTooltipHelper.getPowerTooltip(data, "power") ++
        BlockTooltipHelper.getTankTooltip(data, "tank") ++
        BlockTooltipHelper.getItemsTooltip(data)
    } else List.empty
  }
}

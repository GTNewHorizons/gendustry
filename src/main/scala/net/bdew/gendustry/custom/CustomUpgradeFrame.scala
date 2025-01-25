package net.bdew.gendustry.custom

import cpw.mods.fml.common.Loader;
import net.bdew.lib.items.SimpleItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting

import java.util

object CustomUpgradeFrame extends SimpleItem("UpgradeFrame") {
  override def addInformation(
      p_77624_1_ : ItemStack,
      p_77624_2_ : EntityPlayer,
      p_77624_3_ : util.List[_],
      p_77624_4_ : Boolean
  ): Unit = {
    if (Loader.isModLoaded("dreamcraft")) {
      p_77624_3_
        .asInstanceOf[util.List[String]]
        .add(
          EnumChatFormatting.RED + "DEPRECATED: Put in crafting table to get back !"
        )
    }
  }
}

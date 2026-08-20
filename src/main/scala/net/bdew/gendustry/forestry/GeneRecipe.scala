/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/gendustry
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.gendustry.forestry

import net.bdew.gendustry.items.{GeneSample, GeneTemplate}
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World

class GeneRecipe extends IRecipe {
  def matches(inv: InventoryCrafting, world: World): Boolean =
    getCraftingResult(inv) != null
  def getCraftingResult(inv: InventoryCrafting): ItemStack = {
    var template: ItemStack = null
    var samples: Array[GeneSampleInfo] = null
    var sampleCount = 0
    var i = 0
    while (i < 3) {
      var j = 0
      while (j < 3) {
        val itm = inv.getStackInRowAndColumn(i, j)
        if (itm != null && itm.getItem == GeneSample && itm.hasTagCompound) {
          val sample = GeneSample.getInfo(itm)
          if (sample.root == null || sample.allele == null) return null
          if (samples == null) samples = new Array[GeneSampleInfo](9)
          samples(sampleCount) = sample
          sampleCount += 1
        } else if (
          itm != null && itm.getItem == GeneTemplate && template == null
        ) template = itm
        else if (itm != null) return null
        j += 1
      }
      i += 1
    }
    if (sampleCount == 0 || template == null) return null
    val out = template.copy()
    i = 0
    while (i < sampleCount) {
      if (!GeneTemplate.addSample(out, samples(i))) return null
      i += 1
    }
    return out
  }
  def getRecipeSize: Int = 9
  def getRecipeOutput: ItemStack = new ItemStack(GeneTemplate)
}

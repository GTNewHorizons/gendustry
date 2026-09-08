/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/gendustry
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.gendustry.nei.helpers

import codechicken.nei.PositionedStack
import net.bdew.gendustry.gui.Textures
import net.bdew.gendustry.nei.NEIDrawTarget
import net.bdew.lib.gui.Rect
import net.minecraftforge.fluids.FluidStack

class PositionedFluidStack(
    f: FluidStack,
    x: Int,
    y: Int,
    w: Int,
    h: Int,
    capacity: Int
) extends PositionedStack.Fluid(f, x, y, w, h, capacity) {
  override def draw(x: Int, y: Int) {
    super.draw(x, y)
    NEIDrawTarget.drawTexture(
      new Rect(relx, rely, width, height),
      Textures.tankOverlay
    )
  }
}

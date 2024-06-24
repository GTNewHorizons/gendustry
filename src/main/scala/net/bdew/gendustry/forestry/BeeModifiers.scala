/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/gendustry
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.gendustry.forestry

import forestry.api.apiculture.{IBeeGenome, IBeeHousing, IBeeModifier}

import scala.collection.JavaConversions._

/** Combines multiple IBeeModifier together Conceptually based on
  * BeeHousingModifier from Forestry
  * @param modifiers
  *   collection of modifiers
  */
case class BeeModifiers(modifiers: Traversable[IBeeModifier])
    extends IBeeModifier {
  override def getTerritoryModifier(
      genome: IBeeGenome,
      currentModifier: Float
  ): Float =
    modifiers.foldLeft(currentModifier)((v, m) =>
      v * m.getTerritoryModifier(genome, v)
    )

  override def getMutationModifier(
      genome: IBeeGenome,
      mate: IBeeGenome,
      currentModifier: Float
  ): Float =
    modifiers.foldLeft(currentModifier)((v, m) =>
      v * m.getMutationModifier(genome, mate, v)
    )

  override def getLifespanModifier(
      genome: IBeeGenome,
      mate: IBeeGenome,
      currentModifier: Float
  ): Float =
    modifiers.foldLeft(currentModifier)((v, m) =>
      v * m.getLifespanModifier(genome, mate, v)
    )

  override def getProductionModifier(
      genome: IBeeGenome,
      currentModifier: Float
  ): Float =
    modifiers.foldLeft(currentModifier)((v, m) =>
      v + m.getProductionModifier(genome, v)
    )

  override def getFloweringModifier(
      genome: IBeeGenome,
      currentModifier: Float
  ): Float =
    modifiers.foldLeft(currentModifier)((v, m) =>
      v * m.getFloweringModifier(genome, v)
    )

  override def getGeneticDecay(
      genome: IBeeGenome,
      currentModifier: Float
  ): Float =
    modifiers.foldLeft(currentModifier)((v, m) =>
      v * m.getGeneticDecay(genome, v)
    )

  override def isSealed: Boolean = modifiers.exists(_.isSealed)
  override def isSelfLighted: Boolean = modifiers.exists(_.isSelfLighted)
  override def isSunlightSimulated: Boolean =
    modifiers.exists(_.isSunlightSimulated)
  override def isHellish: Boolean = modifiers.exists(_.isHellish)
}

object BeeModifiers {
  def from(beeHousing: IBeeHousing) = BeeModifiers(beeHousing.getBeeModifiers)
}

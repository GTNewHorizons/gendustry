/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/gendustry
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.gendustry.nei

import java.util

import codechicken.nei.recipe.GuiRecipe
import forestry.api.apiculture.{EnumBeeType, IBeeRoot}
import forestry.api.arboriculture.{EnumGermlingType, ITreeRoot}
import forestry.api.genetics._
import forestry.api.lepidopterology.IButterflyRoot
import net.bdew.gendustry.Gendustry
import net.bdew.gendustry.config.Items
import net.bdew.gendustry.forestry.GeneSampleInfo
import net.bdew.gendustry.items.GeneTemplate
import net.bdew.gendustry.machines.imprinter.MachineImprinter
import net.bdew.gendustry.nei.helpers.PowerComponent
import net.bdew.lib.Misc
import net.bdew.lib.gui.Rect
import net.bdew.lib.items.IStack
import net.minecraft.item.ItemStack

class ImprinterHandler extends BaseRecipeHandler(5, 13) {
  val mutagenRect = new Rect(32, 19, 16, 58)
  val mjRect = new Rect(8, 19, 16, 58)

  import scala.collection.JavaConversions._

  class ImprinterRecipe(tpl: ItemStack) extends CachedRecipeWithComponents {
    val getResult = position(getExampleStack(tpl, true), 137, 49)
    val input = position(getExampleStack(tpl, false), 41, 49)
    val template = position(tpl, 74, 28)
    val labware = position(new ItemStack(Items.labware), 98, 28)

    components :+= new PowerComponent(
      mjRect,
      MachineImprinter.mjPerItem,
      MachineImprinter.maxStoredEnergy
    )

    override def getIngredients = List(input, template, labware)
  }

  def getExampleStack(template: ItemStack, modded: Boolean): ItemStack = {
    val root = GeneTemplate.getSpecies(template)

    val tpl = root match {
      case bees: IBeeRoot => bees.getTemplate("forestry.speciesForest").clone()
      case trees: ITreeRoot => trees.getTemplate("forestry.treeOak").clone()
      case flies: IButterflyRoot =>
        flies.getTemplate("forestry.lepiCabbageWhite").clone()
    }

    if (modded) {
      for (sample <- GeneTemplate.getSamples(template))
        tpl(sample.chromosome) = sample.allele
    }

    val individual = root.templateAsIndividual(tpl)
    individual.analyze()

    root match {
      case bees: IBeeRoot =>
        bees.getMemberStack(individual, EnumBeeType.PRINCESS.ordinal())
      case trees: ITreeRoot =>
        trees.getMemberStack(individual, EnumGermlingType.SAPLING.ordinal())
      case flies: IButterflyRoot => flies.getMemberStack(individual, 0)
    }
  }

  def getRecipe(i: Int) = arecipes.get(i).asInstanceOf[ImprinterRecipe]

  override def loadTransferRects() {
    addTransferRect(Rect(63, 49, 66, 15), "Imprinter")
  }

  def addExample() {
    val bees = AlleleManager.alleleRegistry.getSpeciesRoot("rootBees")
    val cult =
      AlleleManager.alleleRegistry.getAllele("forestry.speciesCultivated")
    val template = new ItemStack(GeneTemplate)
    GeneTemplate.addSample(template, GeneSampleInfo(bees, 0, cult))
    arecipes.add(new ImprinterRecipe(template))
  }

  override def loadUsageRecipes(outputId: String, results: AnyRef*): Unit = {
    Some(outputId, results) collect {
      case ("item", Seq(IStack(x))) if x == Items.labware => addExample()
      case ("item", Seq(stack: ItemStack)) if stack.getItem == GeneTemplate =>
        if (GeneTemplate.getSpecies(stack) == null)
          addExample()
        else
          arecipes.add(new ImprinterRecipe(stack))
      case ("Imprinter", _) => addExample()
    }
  }

  override def loadCraftingRecipes(outputId: String, results: AnyRef*): Unit = {
    Some(outputId, results) collect { case ("Imprinter", _) =>
      addExample()
    }
  }

  override def handleItemTooltip(
      gui: GuiRecipe[_],
      stack: ItemStack,
      tip: util.List[String],
      recipe: Int
  ): util.List[String] = {
    if (stack == getRecipe(recipe).labware.item)
      tip += Misc.toLocalF(
        "gendustry.label.consume",
        MachineImprinter.labwareConsumeChance.toInt
      )
    super.handleItemTooltip(gui, stack, tip, recipe)
  }

  def getGuiTexture = Gendustry.modId + ":textures/gui/imprinter.png"
  def getRecipeName = Misc.toLocal("tile.gendustry.imprinter.name")
}

package net.bdew.gendustry.compat

import com.ruling_0.materiallib.api.StackResolver
import net.bdew.gendustry.Gendustry
import net.minecraft.item.ItemStack

/** Resolves the `I:ml:"{material}:{shape}"` item references MaterialLib-aware
  * config files carry. Naming an item by material and shape keeps a reference
  * valid across sessions that renumber MaterialLib's metadata.
  *
  * MaterialLib types appear only here, so the rest of Gendustry loads without
  * MaterialLib installed. Reference this object only when
  * `cpw.mods.fml.common.Loader.isModLoaded("materiallib")` holds.
  */
object MaterialLibStacks {
  private var resolved = 0
  private var invalid = 0

  /** The stack `ref` names, with `cnt` as its stack size. Null when the
    * reference is malformed or names nothing MaterialLib registers.
    */
  def resolve(ref: String, cnt: Int): ItemStack = {
    val parts = ref.split(":")
    if (parts.length != 2 || parts(0).isEmpty || parts(1).isEmpty) {
      Gendustry.logWarn(
        "Malformed MaterialLib item reference [%s], expected {material}:{shape}",
        ref
      )
      invalid += 1
      return null
    }

    val stack = StackResolver.getStack(parts(0), parts(1), cnt)
    if (stack == null) {
      invalid += 1
      return null
    }

    resolved += 1
    stack
  }

  /** Logs the tally of the config load pass that just finished, then starts a
    * fresh tally.
    */
  def logPass(): Unit = {
    if (resolved + invalid > 0)
      Gendustry.logInfo(
        "Resolved %d MaterialLib entries (%d invalid)",
        resolved,
        invalid
      )
    resolved = 0
    invalid = 0
  }
}

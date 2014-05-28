/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/pressure/master/MMPL-1.0.txt
 */

package net.bdew.pressure.blocks

import net.minecraft.client.renderer.texture.IconRegister
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.bdew.lib.rotate.{BaseRotateableBlock, IconType}
import net.minecraft.util.Icon
import net.minecraft.world.{World, IBlockAccess}
import net.minecraftforge.common.ForgeDirection
import net.bdew.lib.block.HasTE
import net.bdew.pressure.render.RotatedBlockRenderer

class BaseIOBlock[T <: TileFilterable](id: Int, name: String, teClass: Class[T])
  extends Block(id, Material.iron) with BaseRotateableBlock with HasTE[T] with BlockFilterable[T] {
  override val TEClass = teClass

  setUnlocalizedName("pressure." + name)
  setHardness(2)

  override def getFacing(world: IBlockAccess, x: Int, y: Int, z: Int) =
    ForgeDirection.values()(world.getBlockMetadata(x, y, z))

  override def setFacing(world: World, x: Int, y: Int, z: Int, facing: ForgeDirection) =
    world.setBlockMetadataWithNotify(x, y, z, facing.ordinal(), 2)

  override def rotateBlock(world: World, x: Int, y: Int, z: Int, axis: ForgeDirection) = {
    world.setBlockMetadataWithNotify(x, y, z, (world.getBlockMetadata(x, y, z) + 1) % 6, 2)
    true
  }

  @SideOnly(Side.CLIENT)
  override def getRenderType = RotatedBlockRenderer.id

  @SideOnly(Side.CLIENT)
  override def getIcon(meta: Int, kind: IconType.Value) = kind match {
    case IconType.BACK => backIcon
    case IconType.FRONT => frontIcon
    case _ => sideIcon
  }

  var frontIcon, sideIcon, backIcon: Icon = null
  @SideOnly(Side.CLIENT)
  override def registerIcons(ir: IconRegister) = {
    frontIcon = ir.registerIcon("pressure:%s/front".format(name))
    backIcon = ir.registerIcon("pressure:%s/back".format(name))
    sideIcon = ir.registerIcon("pressure:%s/side".format(name))
  }
}
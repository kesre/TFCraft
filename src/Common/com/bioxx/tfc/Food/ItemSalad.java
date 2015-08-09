package com.bioxx.tfc.Food;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.bioxx.tfc.Core.TFC_Sounds;
import com.bioxx.tfc.Core.TFC_Time;
import com.bioxx.tfc.api.TFCItems;

public class ItemSalad extends ItemMeal
{

	public ItemSalad()
	{
		super();
		this.hasSubtypes = true;
		this.MetaNames = new String[]{"Salad0","Salad1","Salad2","Salad3"};
		this.MetaIcons = new IIcon[4];
		this.setFolder("food/");
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List list)
	{
		list.add(createTag(new ItemStack(this, 1)));
	}

	//Creates empty food to prevent NBT errors when food is loaded in NEI
	public static ItemStack createTag(ItemStack is)
	{
		NBTTagCompound nbt = is.getTagCompound();
		if (nbt == null)
			nbt = new NBTTagCompound();

		int[] FG = new int[] { -1, -1, -1, -1 };
		nbt.setIntArray("FG", FG);
		nbt.setFloat("foodWeight", 0);
		nbt.setFloat("foodDecay", 0);
		nbt.setInteger("decayTimer", (int) TFC_Time.getTotalHours() + 1);

		is.setTagCompound(nbt);
		return is;
	}

	@Override
	public ItemStack onEaten(ItemStack is, World world, EntityPlayer player)
	{
		// Handle eating of the salad
		is = super.onEaten(is, world, player);

		// If the last of the salad has been eaten
		if (is.stackSize == 0)
		{
			// 50% chance the bowl is broken, and the sound is played
			if (world.rand.nextInt(2) == 0)
			{
				world.playSoundAtEntity(player, TFC_Sounds.CERAMICBREAK, 0.7f, player.worldObj.rand.nextFloat() * 0.2F + 0.8F);
			}
			// If the bowl didn't break, try to add it to an existing stack of bowls in the inventory
			else if (!player.inventory.addItemStackToInventory(new ItemStack(TFCItems.PotteryBowl, 1, 1)))
			{
				// If the bowl can't be fit in the inventory, put it in the newly emptied held slot
				return new ItemStack(TFCItems.PotteryBowl, 1, 1);
			}
		}

		return is;
	}
}

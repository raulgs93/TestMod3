package choonster.testmod3.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * An item that clears all whitelisted blocks from the player's current chunk when used.
 *
 * @author Choonster
 */
public class ItemClearer extends Item {
	private static final ImmutableList<Block> whitelist = ImmutableList.of(Blocks.STONE, Blocks.DIRT, Blocks.GRASS, Blocks.GRAVEL, Blocks.SAND, Blocks.WATER, Blocks.LAVA, Blocks.ICE);

	private static final int MODE_WHITELIST = 0;
	private static final int MODE_ALL = 1;

	public ItemClearer(final Item.Properties properties) {
		super(properties);
	}

	private int getMode(final ItemStack stack) {
		return stack.getOrCreateTag().getInt("Mode");
	}

	private void setMode(final ItemStack stack, final int mode) {
		stack.getOrCreateTag().putInt("Mode", mode);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (!world.isRemote) {
			final int currentMode = getMode(heldItem);

			if (player.isSneaking()) {
				final int newMode = currentMode == MODE_ALL ? MODE_WHITELIST : MODE_ALL;
				setMode(heldItem, newMode);
				player.sendMessage(new TextComponentTranslation("message.testmod3.clearer.mode.%s", newMode));
			} else {
				final int minX = MathHelper.floor(player.posX / 16) * 16;
				final int minZ = MathHelper.floor(player.posZ / 16) * 16;

				player.sendMessage(new TextComponentTranslation("message.testmod3.clearer.clearing", minX, minZ));

				for (int x = minX; x < minX + 16; x++) {
					for (int z = minZ; z < minZ + 16; z++) {
						for (int y = 0; y < 256; y++) {
							final BlockPos pos = new BlockPos(x, y, z);
							final Block block = world.getBlockState(pos).getBlock();
							if ((currentMode == MODE_ALL && block != Blocks.BEDROCK) || whitelist.contains(block)) {
								world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
							}
						}
					}
				}

				final BlockPos pos = player.getPosition();
				final IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 3);

				player.sendMessage(new TextComponentTranslation("message.testmod3.clearer.cleared"));
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean hasEffect(final ItemStack stack) {
		return getMode(stack) == MODE_ALL;
	}
}

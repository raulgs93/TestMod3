package choonster.testmod3.util;

import choonster.testmod3.TestMod3;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldWriter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constants {
	public static final String RESOURCE_PREFIX = TestMod3.MODID + ":";

	/**
	 * The armour equipment slots.
	 */
	public static final Set<EquipmentSlotType> ARMOUR_SLOTS = ImmutableSet.copyOf(
			Stream.of(EquipmentSlotType.values())
					.filter(equipmentSlot -> equipmentSlot.getSlotType() == EquipmentSlotType.Group.ARMOR)
					.collect(Collectors.toList())
	);

	public static class BlockFlags {
		/**
		 * Default flags for {@link IWorldWriter#setBlockState(BlockPos, BlockState, int)}
		 */
		public static final int DEFAULT_FLAGS = 3;
	}
}

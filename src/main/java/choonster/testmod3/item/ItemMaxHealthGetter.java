package choonster.testmod3.item;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * An item that tells the player the current max health and the bonus max health provided by the entity's {@link IMaxHealth} when right clicked on an entity.
 *
 * @author Choonster
 */
public class ItemMaxHealthGetter extends Item {
	public ItemMaxHealthGetter(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer player, final EntityLivingBase target, final EnumHand hand) {
		if (!player.world.isRemote) {
			CapabilityMaxHealth.getMaxHealth(target).ifPresent(maxHealth -> {
				player.sendMessage(new TextComponentTranslation("message.testmod3.max_health.get", target.getDisplayName(), target.getMaxHealth(), maxHealth.getBonusMaxHealth()));
			});
		}

		return true;
	}
}

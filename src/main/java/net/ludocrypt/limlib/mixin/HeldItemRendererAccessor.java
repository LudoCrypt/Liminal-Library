package net.ludocrypt.limlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;

@Mixin(HeldItemRenderer.class)
public interface HeldItemRendererAccessor {

	@Invoker
	static HeldItemRenderer.HandRenderType callGetHandRenderType(ClientPlayerEntity player) {
		throw new UnsupportedOperationException();
	}

	@Accessor
	ItemStack getMainHand();

	@Accessor
	ItemStack getOffHand();

	@Accessor
	float getEquipProgressMainHand();

	@Accessor
	float getPrevEquipProgressMainHand();

	@Accessor
	float getEquipProgressOffHand();

	@Accessor
	float getPrevEquipProgressOffHand();

}

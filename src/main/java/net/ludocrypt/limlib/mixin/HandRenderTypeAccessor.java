package net.ludocrypt.limlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.item.HeldItemRenderer;

@Mixin(HeldItemRenderer.HandRenderType.class)
public interface HandRenderTypeAccessor {

	@Accessor
	boolean isRenderMainHand();

	@Accessor
	boolean isRenderOffHand();

}

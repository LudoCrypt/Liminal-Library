package net.ludocrypt.limlib.mixin.client.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ludocrypt.limlib.access.ItemRendererAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.LivingEntity;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

	@Unique
	private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

	@Inject(method = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(IIIFFLnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"))
	private static void limlib$drawEntity$head(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, CallbackInfo ci) {
		((ItemRendererAccess) CLIENT.getItemRenderer()).setInGui(true);
	}

	@Inject(method = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(IIIFFLnet/minecraft/entity/LivingEntity;)V", at = @At("TAIL"))
	private static void limlib$drawEntity$tail(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, CallbackInfo ci) {
		((ItemRendererAccess) CLIENT.getItemRenderer()).setInGui(false);
	}

}

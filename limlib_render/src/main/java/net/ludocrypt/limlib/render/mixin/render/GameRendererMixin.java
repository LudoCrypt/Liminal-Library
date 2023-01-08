package net.ludocrypt.limlib.render.mixin.render;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.shader.ShaderStage;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.render.LimlibRender;
import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.ResourceFactory;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Inject(method = "loadShaders", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 54, shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$loadShaders(ResourceFactory manager, CallbackInfo ci, List<ShaderStage> list, List<Pair<ShaderProgram, Consumer<ShaderProgram>>> list2) {
		LimlibRender.LOADED_SHADERS.clear();
		SpecialModelRenderer.SPECIAL_MODEL_RENDERER.getEntries().stream().map(Entry::getKey).map(RegistryKey::getValue).forEach((id) -> {
			try {
				list2.add(Pair.of(new ShaderProgram(manager, "rendertype_" + id.getNamespace() + "_" + id.getPath(), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL), (shader) -> LimlibRender.LOADED_SHADERS.put(SpecialModelRenderer.SPECIAL_MODEL_RENDERER.get(id), shader)));
			} catch (IOException e) {
				list2.forEach((pair) -> pair.getFirst().close());
				LimlibRender.LOGGER.error("Could not reload shader: {}", id);
				e.printStackTrace();
				throw new RuntimeException();
			}
		});
	}

}

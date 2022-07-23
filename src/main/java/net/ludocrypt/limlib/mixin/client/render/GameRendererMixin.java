package net.ludocrypt.limlib.mixin.client.render;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.impl.LimlibRendering;
import net.minecraft.client.gl.Program;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Inject(method = "loadShaders", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 53, shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$loadShaders(ResourceManager manager, CallbackInfo ci, List<Program> list, List<Pair<Shader, Consumer<Shader>>> list2) {
		LimlibRendering.LIMINAL_CORE_SHADER.forEach((core) -> {
			try {
				Identifier id = LimlibRendering.LIMINAL_CORE_SHADER.getId(core);
				list2.add(Pair.of(new Shader(manager, "rendertype_" + id.getNamespace() + "_" + id.getPath(), core.getVertexFormat()), core::setShader));
			} catch (IOException e) {
				list2.forEach((pair) -> {
					pair.getFirst().close();
				});
				throw new RuntimeException("could not reload shaders", e);
			}
		});
	}

}

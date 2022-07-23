package net.ludocrypt.limlib.mixin.client.render.model;

import java.lang.reflect.Type;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.ludocrypt.limlib.access.ModelAccess;
import net.ludocrypt.limlib.impl.LimlibRendering;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.util.Identifier;

@Mixin(JsonUnbakedModel.Deserializer.class)
public abstract class JsonUnbakedModelDeserializerMixin {

	@Inject(method = "Lnet/minecraft/client/render/model/json/JsonUnbakedModel$Deserializer;deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;", at = @At("RETURN"), cancellable = true)
	private void limlib$deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext, CallbackInfoReturnable<JsonUnbakedModel> ci) {
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		if (jsonObject.has("limlib_renderer")) {
			if (LimlibRendering.LIMINAL_QUAD_RENDERER.containsId(new Identifier(jsonObject.get("limlib_renderer").getAsString()))) {
				((ModelAccess) ci.getReturnValue()).setLiminalQuadRenderer(new Identifier(jsonObject.get("limlib_renderer").getAsString()));
			}
		}
	}

	@Shadow
	protected abstract List<ModelElement> elementsFromJson(JsonDeserializationContext context, JsonObject json);

}

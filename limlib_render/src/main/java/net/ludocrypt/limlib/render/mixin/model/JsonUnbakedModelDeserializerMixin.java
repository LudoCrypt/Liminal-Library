package net.ludocrypt.limlib.render.mixin.model;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import net.ludocrypt.limlib.render.LimlibRender;
import net.ludocrypt.limlib.render.access.UnbakedModelAccess;
import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;

@Mixin(JsonUnbakedModel.Deserializer.class)
public abstract class JsonUnbakedModelDeserializerMixin {

	@Inject(method = "Lnet/minecraft/client/render/model/json/JsonUnbakedModel$Deserializer;deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;", at = @At("RETURN"), cancellable = true)
	private void limlib$deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext, CallbackInfoReturnable<JsonUnbakedModel> ci) {
		Map<Identifier, SpecialModelRenderer> map = Maps.newHashMap();
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		if (jsonObject.has("limlib_extra")) {
			JsonObject limlibExtra = jsonObject.get("limlib_extra").getAsJsonObject();
			for (Entry<String, JsonElement> entry : limlibExtra.entrySet()) {

				SpecialModelRenderer decodedModelRenderer = null;

				try {
					decodedModelRenderer = SpecialModelRenderer.CODEC.decode(JsonOps.INSTANCE, entry.getValue()).result().get().getFirst();
					LimlibRender.SPECIAL_MODEL_RENDERERS.put(decodedModelRenderer, new Identifier(entry.getValue().getAsJsonObject().get("type").getAsString()));
				} catch (Exception e) {
					LimlibRender.LOGGER.error("Could not decode '{}'", entry);
				}

				if (decodedModelRenderer != null) {
					if (!map.containsKey(new Identifier(entry.getKey()))) {
						map.put(new Identifier(entry.getKey()), decodedModelRenderer);
					} else {
						LimlibRender.LOGGER.warn("Duplicate entry '{}'", entry.getKey());
					}
				}
			}

		}
		((UnbakedModelAccess) ci.getReturnValue()).getSubModels().putAll(map);
	}

}

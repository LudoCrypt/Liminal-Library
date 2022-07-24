package net.ludocrypt.limlib.mixin.client.render.model;

import java.lang.reflect.Type;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.ludocrypt.limlib.access.UnbakedModelAccess;
import net.ludocrypt.limlib.impl.LimlibRendering;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

@Mixin(JsonUnbakedModel.Deserializer.class)
public abstract class JsonUnbakedModelDeserializerMixin {

	@Inject(method = "Lnet/minecraft/client/render/model/json/JsonUnbakedModel$Deserializer;deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;", at = @At("RETURN"), cancellable = true)
	private void limlib$deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext, CallbackInfoReturnable<JsonUnbakedModel> ci) {
		Map<Identifier, ModelIdentifier> map = Maps.newHashMap();
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		if (jsonObject.has("limlib_extra")) {
			LimlibRendering.LIMINAL_QUAD_RENDERER.getEntrySet().forEach((entry) -> {
				JsonObject limlibExtra = jsonObject.get("limlib_extra").getAsJsonObject();
				if (limlibExtra.has(entry.getKey().getValue().toString())) {
					map.put(entry.getKey().getValue(), new ModelIdentifier(limlibExtra.get(entry.getKey().getValue().toString()).getAsString()));
				}
			});
		}
		((UnbakedModelAccess) ci.getReturnValue()).getSubModels().putAll(map);
	}

}

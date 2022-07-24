package net.ludocrypt.limlib.mixin.client.render.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.access.BakedModelAccess;
import net.ludocrypt.limlib.access.UnbakedModelAccess;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

@Mixin(JsonUnbakedModel.class)
public abstract class JsonUnbakedModelMixin implements UnbakedModelAccess {

	@Shadow
	@Final
	private static Logger LOGGER;

	@Unique
	private Map<Identifier, ModelIdentifier> subModels = Maps.newHashMap();

	@Shadow
	public String id;

	@Shadow
	protected JsonUnbakedModel parent;

	@Inject(method = "Lnet/minecraft/client/render/model/json/JsonUnbakedModel;getModelDependencies()Ljava/util/Collection;", at = @At("RETURN"), cancellable = true)
	private void limlib$getModelDependencies(CallbackInfoReturnable<Collection<Identifier>> ci) {
		HashSet<Identifier> set = Sets.newHashSet();
		set.addAll(ci.getReturnValue());
		set.addAll(subModels.values());
		ci.setReturnValue(set);
	}

	@Inject(method = "Lnet/minecraft/client/render/model/json/JsonUnbakedModel;getTextureDependencies(Ljava/util/function/Function;Ljava/util/Set;)Ljava/util/Collection;", at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V", ordinal = 0, shift = Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences, CallbackInfoReturnable<Collection<SpriteIdentifier>> ci, Set<JsonUnbakedModel> set, JsonUnbakedModel jsonUnbakedModel, Set<SpriteIdentifier> set2) {
		this.getSubModels().values().forEach(subModel -> {
			UnbakedModel unbakedModel = (UnbakedModel) unbakedModelGetter.apply(subModel);
			if (!Objects.equals(unbakedModel, (JsonUnbakedModel) (Object) this)) {
				set2.addAll(unbakedModel.getTextureDependencies(unbakedModelGetter, unresolvedTextureReferences));
			}
		});
	}

	@Inject(method = "Lnet/minecraft/client/render/model/json/JsonUnbakedModel;bake(Lnet/minecraft/client/render/model/ModelLoader;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;Ljava/util/function/Function;Lnet/minecraft/client/render/model/ModelBakeSettings;Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/model/BakedModel;", at = @At("RETURN"), cancellable = true)
	private void limlib$bake(ModelLoader loader, JsonUnbakedModel parent, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings settings, Identifier id, boolean hasDepth, CallbackInfoReturnable<BakedModel> ci) {
		Map<Identifier, BakedModel> subModels = Maps.newHashMap();
		this.getSubModels().forEach((rendererId, modelId) -> subModels.put(rendererId, loader.getOrLoadModel(modelId).bake(loader, textureGetter, settings, modelId)));
		((BakedModelAccess) ci.getReturnValue()).getSubModels().putAll(subModels);
	}

	@Override
	public Map<Identifier, ModelIdentifier> getSubModels() {
		return subModels;
	}

}

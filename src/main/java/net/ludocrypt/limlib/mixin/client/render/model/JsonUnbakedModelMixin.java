package net.ludocrypt.limlib.mixin.client.render.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.access.BakedModelAccess;
import net.ludocrypt.limlib.access.UnbakedModelAccess;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

@Mixin(JsonUnbakedModel.class)
public abstract class JsonUnbakedModelMixin implements UnbakedModelAccess {

	@Unique
	private Map<Identifier, List<ModelElement>> subElements = Maps.newHashMap();

	@Shadow
	public String id;

	@Shadow
	protected JsonUnbakedModel parent;

	@Inject(method = "Lnet/minecraft/client/render/model/json/JsonUnbakedModel;getTextureDependencies(Ljava/util/function/Function;Ljava/util/Set;)Ljava/util/Collection;", at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V", ordinal = 0, shift = Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences, CallbackInfoReturnable<Collection<SpriteIdentifier>> ci, Set<JsonUnbakedModel> set, JsonUnbakedModel jsonUnbakedModel, Set<SpriteIdentifier> set2) {
		this.getSubElements().forEach((subElementId, subElement) -> {
			subElement.forEach((modelElement) -> {
				modelElement.faces.values().forEach((modelElementFace) -> {
					SpriteIdentifier spriteIdentifier = this.resolveSprite(modelElementFace.textureId);
					if (Objects.equals(spriteIdentifier.getTextureId(), MissingSprite.getMissingSpriteId())) {
						unresolvedTextureReferences.add(Pair.of(modelElementFace.textureId, this.id));
					}
					set2.add(spriteIdentifier);
				});
			});
		});
	}

	@Inject(method = "Lnet/minecraft/client/render/model/json/JsonUnbakedModel;bake(Lnet/minecraft/client/render/model/ModelLoader;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;Ljava/util/function/Function;Lnet/minecraft/client/render/model/ModelBakeSettings;Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/model/BakedModel;", at = @At("RETURN"), cancellable = true)
	private void limlib$bake(ModelLoader loader, JsonUnbakedModel parent, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings settings, Identifier id, boolean hasDepth, CallbackInfoReturnable<BakedModel> ci) {
		Map<Identifier, List<Pair<BakedQuad, Optional<Direction>>>> subQuads = Maps.newHashMap();

		this.getSubElements().forEach((subElementId, subElement) -> {
			List<Pair<BakedQuad, Optional<Direction>>> quads = Lists.newArrayList();
			subElement.forEach((modelElement) -> {
				for (Direction direction : modelElement.faces.keySet()) {
					ModelElementFace modelElementFace = modelElement.faces.get(direction);
					Sprite sprite2 = textureGetter.apply(parent.resolveSprite(modelElementFace.textureId));
					if (modelElementFace.cullFace == null) {
						quads.add(Pair.of(createQuad(modelElement, modelElementFace, sprite2, direction, settings, id), Optional.empty()));
						continue;
					}
					quads.add(Pair.of(createQuad(modelElement, modelElementFace, sprite2, direction, settings, id), Optional.of(Direction.transform(settings.getRotation().getMatrix(), modelElementFace.cullFace))));
				}
			});

			subQuads.put(subElementId, quads);
		});

		((BakedModelAccess) ci.getReturnValue()).getSubQuads().putAll(subQuads);
	}

	@Shadow
	private native static BakedQuad createQuad(ModelElement element, ModelElementFace elementFace, Sprite sprite, Direction side, ModelBakeSettings settings, Identifier id);

	@Shadow
	public abstract SpriteIdentifier resolveSprite(String spriteName);

	@Override
	public Map<Identifier, List<ModelElement>> getSubElements() {
		Map<Identifier, List<ModelElement>> conjoinedElements = Maps.newHashMap();

		conjoinedElements.putAll(this.subElements);

		if (this.parent != null) {
			conjoinedElements.putAll(((UnbakedModelAccess) this.parent).getSubElements());
		}

		return conjoinedElements;
	}

	@Override
	public JsonUnbakedModel getParent() {
		return this.parent;
	}

}

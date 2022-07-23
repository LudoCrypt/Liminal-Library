package net.ludocrypt.limlib.mixin.client.render.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.access.BakedModelAccess;
import net.ludocrypt.limlib.access.UnbakedModelAccess;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

@Mixin(JsonUnbakedModel.class)
public abstract class JsonUnbakedModelMixin implements UnbakedModelAccess {

	@Unique
	private Map<Identifier, List<ModelElement>> subElements = Maps.newHashMap();

	@Shadow
	protected JsonUnbakedModel parent;

	@Inject(method = "Lnet/minecraft/client/render/model/json/JsonUnbakedModel;bake(Lnet/minecraft/client/render/model/ModelLoader;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;Ljava/util/function/Function;Lnet/minecraft/client/render/model/ModelBakeSettings;Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/model/BakedModel;", at = @At("RETURN"), cancellable = true)
	private void limlib$bake(ModelLoader loader, JsonUnbakedModel parent, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings settings, Identifier id, boolean hasDepth, CallbackInfoReturnable<BakedModel> ci) {
		((BakedModelAccess) ci.getReturnValue()).getSubQuads().putAll(this.bakeQuads(loader, parent, textureGetter, settings, id, hasDepth));
	}

	@Override
	public Map<Identifier, List<Pair<BakedQuad, Optional<Direction>>>> bakeQuads(ModelLoader loader, JsonUnbakedModel parent, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings settings, Identifier id, boolean hasDepth) {
		Map<Identifier, List<Pair<BakedQuad, Optional<Direction>>>> subQuads = Maps.newHashMap();

		this.getSubElements().forEach((subElementId, subElement) -> {
			List<Pair<BakedQuad, Optional<Direction>>> quads = Lists.newArrayList();
			subElement.forEach((modelElement) -> {
				for (Direction direction : modelElement.faces.keySet()) {
					ModelElementFace modelElementFace = modelElement.faces.get(direction);
					Sprite sprite2 = textureGetter.apply(((JsonUnbakedModel) (Object) this).resolveSprite(modelElementFace.textureId));
					if (modelElementFace.cullFace == null) {
						quads.add(Pair.of(createQuad(modelElement, modelElementFace, sprite2, direction, settings, id), Optional.empty()));
						continue;
					}
					quads.add(Pair.of(createQuad(modelElement, modelElementFace, sprite2, direction, settings, id), Optional.of(Direction.transform(settings.getRotation().getMatrix(), modelElementFace.cullFace))));
				}
			});

			subQuads.put(subElementId, quads);
		});

		if (((UnbakedModelAccess) parent).getParent() != null) {
			subQuads.putAll(((UnbakedModelAccess) ((UnbakedModelAccess) parent).getParent()).bakeQuads(loader, ((UnbakedModelAccess) parent).getParent(), textureGetter, settings, id, hasDepth));
		}

		return subQuads;
	}

	@Shadow
	private native static BakedQuad createQuad(ModelElement element, ModelElementFace elementFace, Sprite sprite, Direction side, ModelBakeSettings settings, Identifier id);

	@Override
	public Map<Identifier, List<ModelElement>> getSubElements() {
		return subElements;
	}

	@Override
	public JsonUnbakedModel getParent() {
		return this.parent;
	}

}

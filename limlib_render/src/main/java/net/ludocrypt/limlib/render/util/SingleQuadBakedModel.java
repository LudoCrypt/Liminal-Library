package net.ludocrypt.limlib.render.util;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;

public class SingleQuadBakedModel implements BakedModel {

	private BakedModel parent;
	private BakedQuad quad;

	public SingleQuadBakedModel(BakedModel parent, BakedQuad quad) {
		this.parent = parent;
		this.quad = quad;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction face, RandomGenerator random) {
		return List.of(quad);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return parent.useAmbientOcclusion();
	}

	@Override
	public boolean hasDepth() {
		return parent.hasDepth();
	}

	@Override
	public boolean isSideLit() {
		return parent.isSideLit();
	}

	@Override
	public boolean isBuiltin() {
		return parent.isBuiltin();
	}

	@Override
	public Sprite getParticleSprite() {
		return parent.getParticleSprite();
	}

	@Override
	public ModelTransformation getTransformation() {
		return parent.getTransformation();
	}

	@Override
	public ModelOverrideList getOverrides() {
		return parent.getOverrides();
	}

}

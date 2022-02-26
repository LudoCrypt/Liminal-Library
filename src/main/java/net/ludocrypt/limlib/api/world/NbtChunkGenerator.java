package net.ludocrypt.limlib.api.world;

import java.util.HashMap;
import java.util.List;

import net.ludocrypt.limlib.mixin.BlockEntityAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;

public abstract class NbtChunkGenerator extends LiminalChunkGenerator {

	public final HashMap<String, NbtPlacerUtil> loadedStructures = new HashMap<String, NbtPlacerUtil>(30);
	public final Identifier nbtId;
	public final List<String> structures;

	public NbtChunkGenerator(BiomeSource biomeSource, long worldSeed, Identifier nbtId) {
		super(biomeSource, worldSeed);
		this.nbtId = nbtId;
		this.structures = List.of();
	}

	public NbtChunkGenerator(BiomeSource biomeSource, long worldSeed, Identifier nbtId, List<String> structures) {
		super(biomeSource, worldSeed);
		this.nbtId = nbtId;
		this.structures = structures;
	}

	public NbtChunkGenerator(BiomeSource biomeSource, MultiNoiseSampler multiNoiseSampler, long worldSeed, Identifier nbtId, List<String> structures) {
		super(biomeSource, multiNoiseSampler, worldSeed);
		this.nbtId = nbtId;
		this.structures = structures;
	}

	@Override
	public int getSeaLevel() {
		return 0;
	}

	@Override
	public int getMinimumY() {
		return 0;
	}

	public void storeStructures(ServerWorld world) {
		this.structures.forEach((string) -> this.store(string, world));
	}

	protected void store(String id, ServerWorld world) {
		loadedStructures.put(id, NbtPlacerUtil.load(world.getServer().getResourceManager(), new Identifier(this.nbtId.getNamespace(), "nbt/" + this.nbtId.getPath() + "/" + id + ".nbt")).get());
	}

	protected void store(String id, ServerWorld world, int from, int to) {
		for (int i = from; i <= to; i++) {
			store(id + "_" + i, world);
		}
	}

	protected void generateNbt(ChunkRegion region, BlockPos at, String id) {
		generateNbt(region, at, id, BlockRotation.NONE);
	}

	protected void generateNbt(ChunkRegion region, BlockPos at, String id, BlockRotation rotation) {
		loadedStructures.get(id).rotate(rotation).generateNbt(region, at, (pos, state, nbt) -> this.modifyStructure(region, pos, state, nbt)).spawnEntities(region, at, rotation);
	}

	protected void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state, NbtCompound nbt) {
		if (!state.isAir()) {
			if (state.isOf(Blocks.BARREL)) {
				region.setBlockState(pos, state, Block.NOTIFY_ALL, 1);
				if (region.getBlockEntity(pos)instanceof BarrelBlockEntity barrel) {
					barrel.setLootTable(this.getBarrelLootTable(), region.getSeed() + MathHelper.hashCode(pos));
				}
			} else if (state.isOf(Blocks.BARRIER)) {
				region.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL, 1);
			} else {
				region.setBlockState(pos, state, Block.NOTIFY_ALL, 1);
			}
			BlockEntity blockEntity = region.getBlockEntity(pos);
			if (blockEntity != null) {
				if (state.isOf(blockEntity.getCachedState().getBlock())) {
					((BlockEntityAccessor) blockEntity).callWriteNbt(nbt);
				}
			}
		}
	}

	protected Identifier getBarrelLootTable() {
		return LootTables.SIMPLE_DUNGEON_CHEST;
	}

}

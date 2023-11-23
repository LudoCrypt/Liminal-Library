package net.ludocrypt.limlib.api.world.chunk;

import java.util.Optional;

import net.ludocrypt.limlib.api.world.FunctionMap;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.NbtPlacerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;

public abstract class AbstractNbtChunkGenerator extends LiminalChunkGenerator {

	public final NbtGroup nbtGroup;
	public final FunctionMap<Identifier, NbtPlacerUtil, ResourceManager> structures;

	public AbstractNbtChunkGenerator(BiomeSource biomeSource, NbtGroup nbtGroup) {
		this(biomeSource, nbtGroup, new FunctionMap<Identifier, NbtPlacerUtil, ResourceManager>(NbtPlacerUtil::load));
	}

	public AbstractNbtChunkGenerator(BiomeSource biomeSource, NbtGroup nbtGroup,
			FunctionMap<Identifier, NbtPlacerUtil, ResourceManager> structures) {
		super(biomeSource);
		this.nbtGroup = nbtGroup;
		this.structures = structures;
		this.nbtGroup.fill(structures);
	}

	public void generateNbt(ChunkRegion region, BlockPos at, Identifier id) {
		generateNbt(region, at, id, BlockRotation.NONE, BlockMirror.NONE);
	}

	public void generateNbt(ChunkRegion region, BlockPos at, Identifier id, BlockRotation rotation) {
		generateNbt(region, at, id, rotation, BlockMirror.NONE);
	}

	public void generateNbt(ChunkRegion region, BlockPos at, Identifier id, BlockMirror mirror) {
		generateNbt(region, at, id, BlockRotation.NONE, mirror);
	}

	public void generateNbt(ChunkRegion region, BlockPos at, Identifier id, BlockRotation rotation, BlockMirror mirror) {

		try {
			structures
				.eval(id, region.getServer().getResourceManager())
				.manipulate(rotation, mirror)
				.generateNbt(region, at, (pos, state, nbt) -> this.modifyStructure(region, pos, state, nbt))
				.spawnEntities(region, at, rotation, mirror);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException("Attempted to load undefined structure \'" + id + "\'");
		}

	}

	public void generateNbt(ChunkRegion region, BlockPos offset, BlockPos from, BlockPos to, Identifier id,
			BlockRotation rotation, BlockMirror mirror) {

		try {
			structures
				.eval(id, region.getServer().getResourceManager())
				.manipulate(rotation, mirror)
				.generateNbt(region, offset, from, to, (pos, state, nbt) -> this.modifyStructure(region, pos, state, nbt))
				.spawnEntities(region, offset, from, to, rotation, mirror);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException("Attempted to load undefined structure \'" + id + "\'");
		}

	}

	protected void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state,
			Optional<NbtCompound> blockEntityNbt) {
		this.modifyStructure(region, pos, state, blockEntityNbt, Block.NOTIFY_ALL);
	}

	protected void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state, Optional<NbtCompound> blockEntityNbt,
			int update) {

		if (!state.isAir()) {

			if (state.isOf(Blocks.BARRIER)) {
				region.setBlockState(pos, Blocks.AIR.getDefaultState(), update, 1);
			} else {
				region.setBlockState(pos, state, update, 1);
			}

			if (blockEntityNbt.isPresent()) {
				BlockEntity blockEntity = region.getBlockEntity(pos);

				if (blockEntity != null) {

					if (state.isOf(blockEntity.getCachedState().getBlock())) {
						blockEntity.readNbt(blockEntityNbt.get());
					}

				}

				if (blockEntity instanceof LootableContainerBlockEntity lootTable) {
					lootTable
						.setLootTable(this.getContainerLootTable(lootTable), region.getSeed() + LimlibHelper.blockSeed(pos));
				}

			}

		}

	}

	protected Identifier getContainerLootTable(LootableContainerBlockEntity container) {
		return LootTables.SIMPLE_DUNGEON_CHEST;
	}

}

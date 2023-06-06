package net.ludocrypt.limlib.api.world.maze;

import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.api.world.maze.MazeComponent.CellState;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.random.RandomGenerator;

public enum RectangularMazePiece {
	T_PIECE,
	F_PIECE,
	I_PIECE,
	L_PIECE,
	NUB,
	BLANK;

	public static Pair<RectangularMazePiece, BlockRotation> getFromCell(CellState state, RandomGenerator random) {

		RectangularMazePiece piece = RectangularMazePiece.BLANK;
		BlockRotation rotation = BlockRotation.NONE;

		if (state.isNorth() && state.isEast() && state.isSouth() && state.isWest()) {
			piece = RectangularMazePiece.T_PIECE;
			rotation = random.nextBoolean() ? BlockRotation.NONE
					: random.nextBoolean() ? random.nextBoolean() ? BlockRotation.CLOCKWISE_90 : BlockRotation.CLOCKWISE_180 : BlockRotation.COUNTERCLOCKWISE_90;
		} else if (state.isNorth() && state.isEast() && state.isSouth() && !state.isWest()) {
			piece = RectangularMazePiece.F_PIECE;
			rotation = BlockRotation.CLOCKWISE_180;
		} else if (state.isNorth() && state.isEast() && !state.isSouth() && state.isWest()) {
			piece = RectangularMazePiece.F_PIECE;
			rotation = BlockRotation.CLOCKWISE_90;
		} else if (state.isNorth() && state.isEast() && !state.isSouth() && !state.isWest()) {
			piece = RectangularMazePiece.L_PIECE;
			rotation = BlockRotation.CLOCKWISE_90;
		} else if (state.isNorth() && !state.isEast() && state.isSouth() && state.isWest()) {
			piece = RectangularMazePiece.F_PIECE;
		} else if (state.isNorth() && !state.isEast() && state.isSouth() && !state.isWest()) {
			piece = RectangularMazePiece.I_PIECE;
			rotation = random.nextBoolean() ? BlockRotation.NONE : BlockRotation.CLOCKWISE_180;
		} else if (state.isNorth() && !state.isEast() && !state.isSouth() && state.isWest()) {
			piece = RectangularMazePiece.L_PIECE;
		} else if (state.isNorth() && !state.isEast() && !state.isSouth() && !state.isWest()) {
			piece = RectangularMazePiece.NUB;
		} else if (!state.isNorth() && state.isEast() && state.isSouth() && state.isWest()) {
			piece = RectangularMazePiece.F_PIECE;
			rotation = BlockRotation.COUNTERCLOCKWISE_90;
		} else if (!state.isNorth() && state.isEast() && state.isSouth() && !state.isWest()) {
			piece = RectangularMazePiece.L_PIECE;
			rotation = BlockRotation.CLOCKWISE_180;
		} else if (!state.isNorth() && state.isEast() && !state.isSouth() && state.isWest()) {
			piece = RectangularMazePiece.I_PIECE;
			rotation = random.nextBoolean() ? BlockRotation.CLOCKWISE_90 : BlockRotation.COUNTERCLOCKWISE_90;
		} else if (!state.isNorth() && state.isEast() && !state.isSouth() && !state.isWest()) {
			piece = RectangularMazePiece.NUB;
			rotation = BlockRotation.CLOCKWISE_90;
		} else if (!state.isNorth() && !state.isEast() && state.isSouth() && state.isWest()) {
			piece = RectangularMazePiece.L_PIECE;
			rotation = BlockRotation.COUNTERCLOCKWISE_90;
		} else if (!state.isNorth() && !state.isEast() && state.isSouth() && !state.isWest()) {
			piece = RectangularMazePiece.NUB;
			rotation = BlockRotation.CLOCKWISE_180;
		} else if (!state.isNorth() && !state.isEast() && !state.isSouth() && state.isWest()) {
			piece = RectangularMazePiece.NUB;
			rotation = BlockRotation.COUNTERCLOCKWISE_90;
		}

		return Pair.of(piece, rotation);

	}

}

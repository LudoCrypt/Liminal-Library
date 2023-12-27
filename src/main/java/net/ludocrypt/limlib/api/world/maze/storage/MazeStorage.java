package net.ludocrypt.limlib.api.world.maze.storage;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.ludocrypt.limlib.api.world.maze.MazeComponent;
import net.ludocrypt.limlib.api.world.maze.MazeComponent.Vec2i;
import net.ludocrypt.limlib.api.world.maze.MazeGenerator;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;

public class MazeStorage {

	private static final Logger LOGGER = LogUtils.getLogger();

	private final Map<String, MazeGenerator> generators;

	private Queue<Runnable> dirt = new LinkedList<Runnable>();

	private File dir;

	public MazeStorage(Map<String, MazeGenerator> generators, File dir) {
		this.generators = generators;
		this.dir = dir;
		dir.mkdirs();

		for (Entry<String, MazeGenerator> entry : generators.entrySet()) {
			entry.getValue().connect((pos, maze) -> dirt.add(() -> serialize(entry.getKey(), pos, maze)));
		}

	}

	public void read() {

		for (String id : generators.keySet()) {
			File mazeDir = new File(dir, id);

			for (File data : mazeDir.listFiles((file) -> FilenameUtils.getExtension(file.getAbsolutePath()).equals("nbt"))) {

				try {
					NbtCompound readMaze = NbtIo.read(data);

					String[] sizeRaw = FilenameUtils.getBaseName(data.getAbsolutePath()).substring(2).split("\\.");
					Vec2i size = new Vec2i(Integer.valueOf(sizeRaw[0]), Integer.valueOf(sizeRaw[1]));

					this.generators.get(id).getMazes().put(size, new MazeComponent(size.getX(), size.getY()).read(readMaze));
				} catch (IOException | NullPointerException e) {
					LOGGER.error("Could not read data {}", this, e);
				}

			}

		}

	}

	public void save() {
		Runnable serializer;

		while ((serializer = dirt.poll()) != null) {
			serializer.run();
		}

	}

	public void serialize(String mazeId, Vec2i pos, MazeComponent maze) {

		try {
			NbtIo
				.writeCompressed(maze.write(new NbtCompound()),
					new File(new File(dir, mazeId), "m." + pos.getX() + "." + pos.getY() + ".nbt"));
		} catch (IOException e) {
			LOGGER.error("Could not save data {}", this, e);
		}

	}

}

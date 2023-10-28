package net.ludocrypt.limlib.api.world;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.util.random.RandomGenerator;

public class NbtGroup {

	public static final Codec<NbtGroup> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Identifier.CODEC.fieldOf("id").stable().forGetter((sky) -> {
			return sky.id;
		}), Codec.unboundedMap(Codec.STRING, Codec.list(Codec.STRING)).fieldOf("groups").stable().forGetter((sky) -> {
			return sky.groups;
		})).apply(instance, instance.stable(NbtGroup::new));
	});
	Identifier id;
	Map<String, List<String>> groups;

	public NbtGroup(Identifier id, Map<String, List<String>> groups) {
		this.id = id;
		this.groups = groups;
	}

	public Identifier nbtId(String group, String nbt) {
		return new Identifier(this.id.getNamespace(), "nbt/" + this.id.getPath() + "/" + group + "/" + nbt + ".nbt");
	}

	public Identifier pick(String key, RandomGenerator random) {

		if (!groups.containsKey(key)) {
			throw new NullPointerException();
		}

		List<String> group = groups.get(key);
		return nbtId(key, group.get(random.nextInt(group.size())));
	}

	public boolean contains(String key, String nbt) {

		if (!groups.containsKey(key)) {
			return false;
		}

		List<String> group = groups.get(key);
		return group.contains(nbt);
	}

	public boolean contains(String key) {
		return groups.containsKey(key);
	}

	public void forEach(Consumer<Identifier> runnable) {

		for (Entry<String, List<String>> entry : groups.entrySet()) {

			for (String nbt : entry.getValue()) {
				runnable.accept(nbtId(entry.getKey(), nbt));
			}

		}

	}

	public Identifier getId() {
		return id;
	}

	public Map<String, List<String>> getGroups() {
		return groups;
	}

}

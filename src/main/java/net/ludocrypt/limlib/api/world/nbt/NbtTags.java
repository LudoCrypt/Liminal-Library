package net.ludocrypt.limlib.api.world.nbt;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class NbtTags {

	final ArrayListMultimap<Identifier, ImmutableNbtCompound> tagsForStruct;
	final Map<String, Set<Identifier>> matchingCache;

	public NbtTags(ArrayListMultimap<Identifier, ImmutableNbtCompound> tagsForStruct,
			Map<String, Set<Identifier>> matchingCache) {
		this.tagsForStruct = tagsForStruct;
		this.matchingCache = matchingCache;
	}

	public NbtTags() {
		this.tagsForStruct = ArrayListMultimap.create();
		this.matchingCache = Maps.newHashMap();
	}

	public static NbtTags parse(NbtGroup group, ResourceManager manager) {
		NbtTags tags = new NbtTags();

		group.forEach((id) -> {
			NbtCompound readTags = NbtPlacerUtil.loadTags(id, manager);

			for (String tagKey : readTags.getKeys()) {
				ImmutableNbtCompound tag = new ImmutableNbtCompound(readTags.getCompound(tagKey));
				tags.tagsForStruct.put(id, tag);
			}

		});

		return tags;
	}

	public Set<Identifier> matching(Predicate<ImmutableNbtCompound> matcher) {

		Set<Identifier> matching = Sets.newHashSet();

		for (Entry<Identifier, ImmutableNbtCompound> entry : this.tagsForStruct.entries()) {

			if (matcher.test(entry.getValue())) {
				matching.add(entry.getKey());
			}

		}

		return matching;
	}

	public Set<Identifier> matching(String cache, Predicate<ImmutableNbtCompound> matcher) {
		return this.matchingCache.computeIfAbsent(cache, (c) -> matching(matcher));
	}

	public void closeCache() {
		this.matchingCache.clear();
	}

}

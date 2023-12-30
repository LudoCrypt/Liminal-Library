package net.ludocrypt.limlib.api.world.nbt;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class NbtTags {

	final ArrayListMultimap<Identifier, ImmutableNbtCompound> tagsForStruct;
	final ArrayListMultimap<ImmutableNbtCompound, Identifier> structsForTag;

	public NbtTags(ArrayListMultimap<Identifier, ImmutableNbtCompound> tagsForStruct,
			ArrayListMultimap<ImmutableNbtCompound, Identifier> structsForTag) {
		this.tagsForStruct = tagsForStruct;
		this.structsForTag = structsForTag;
	}

	public NbtTags() {
		this.tagsForStruct = ArrayListMultimap.create();
		this.structsForTag = ArrayListMultimap.create();
	}

	public static NbtTags parse(NbtGroup group, ResourceManager manager) {
		NbtTags tags = new NbtTags();

		Iterator<Identifier> iterator = group.iterator();

		while (iterator.hasNext()) {
			Identifier id = iterator.next();
			NbtCompound readTags = NbtPlacerUtil.loadTags(id, manager);

			for (String tagKey : readTags.getKeys()) {
				ImmutableNbtCompound tag = new ImmutableNbtCompound(readTags.getCompound(tagKey));
				tags.tagsForStruct.put(id, tag);
				tags.structsForTag.put(tag, id);
			}

		}

		return tags;
	}

	public List<Identifier> matching(ImmutableNbtCompound... tags) {

		if (tags.length == 0) {
			return Lists.newArrayList();
		}

		List<Identifier> commonItems = null;

		for (int i = 0; i < tags.length; i++) {

			if (commonItems == null) {

				if (this.structsForTag.containsKey(tags[i])) {
					commonItems = Lists.newArrayList(this.structsForTag.get(tags[i]));
				}

			} else if (commonItems != null) {
				commonItems.retainAll(this.structsForTag.get(tags[i]));
			}

		}

		if (commonItems == null) {
			return Lists.newArrayList();
		}

		return Lists.newArrayList(commonItems);
	}

}

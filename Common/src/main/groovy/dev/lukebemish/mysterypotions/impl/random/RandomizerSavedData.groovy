package dev.lukebemish.mysterypotions.impl.random

import dev.lukebemish.mysterypotions.impl.Constants
import groovy.transform.CompileStatic
import net.minecraft.core.Registry
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData

@CompileStatic
class RandomizerSavedData extends SavedData {
    static String makeDataKey(ResourceKey<? extends Registry<?>> registry) {
        return "${Constants.MOD_ID}:randomizer/${registry.location().namespace}/${registry.location().path}"
    }

    final Map<ResourceLocation, Map<String, ResourceLocation>> itemKnownKeys = new HashMap<>()

    static RandomizerSavedData getOrCreate(ServerLevel world, ResourceKey<? extends Registry<?>> registry) {
        return world.getDataStorage().<RandomizerSavedData>computeIfAbsent({ load(it) }, {-> new RandomizerSavedData()}, makeDataKey(registry))
    }

    @Override
    CompoundTag save(CompoundTag compoundTag) {
        itemKnownKeys.forEach {key, value ->
            CompoundTag child = new CompoundTag()
            value.each { k, v ->
                child.putString(k, v.toString())
            }
            compoundTag.put(key.toString(), child)
        }
        return compoundTag
    }

    static RandomizerSavedData load(CompoundTag compoundTag) {
        RandomizerSavedData data = new RandomizerSavedData()
        compoundTag.getAllKeys().each {
            ResourceLocation key = new ResourceLocation(it)
            CompoundTag child = compoundTag.getCompound(it)
            Map<String, ResourceLocation> value = new HashMap<>()
            child.allKeys.each {
                value.put(it, new ResourceLocation(child.getString(it)))
            }
            data.itemKnownKeys.put(key, value)
        }
        return data
    }

    void setup(PiecewiseRandomizable target, long seed) {
        Map<String, ResourceLocation> map = itemKnownKeys.computeIfAbsent(target.seedLocation) { new HashMap<>() }
        target.randomizers.each { key, holder ->
            holder.setup(map.get(key), target.seedLocation, seed)
        }

        target.randomizers.each { key, holder ->
            map.put(key, holder.enabledLocation)
        }
    }
}

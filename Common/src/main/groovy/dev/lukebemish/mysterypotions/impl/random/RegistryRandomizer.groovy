package dev.lukebemish.mysterypotions.impl.random


import groovy.transform.CompileStatic
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel

@CompileStatic
class RegistryRandomizer {
    final Map<ResourceLocation, PiecewiseRandomizable> randomizables = new HashMap<>()

    private final ResourceKey<? extends Registry<?>> registry

    RegistryRandomizer(ResourceKey<? extends Registry<?>> registry) {
        this.registry = registry
    }

    void setup(ServerLevel world) {
        RandomizerSavedData data = RandomizerSavedData.getOrCreate(world, registry)

        randomizables.each { key, value ->
            data.setup(value, world.seed)
            data.itemKnownKeys.put(key, value.randomizers.collectEntries { str, holder ->
                [str, holder.enabledLocation]
            })
        }
    }
}

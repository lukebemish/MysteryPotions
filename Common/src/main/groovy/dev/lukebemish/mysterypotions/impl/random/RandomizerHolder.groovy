package dev.lukebemish.mysterypotions.impl.random

import groovy.transform.CompileStatic
import net.minecraft.resources.ResourceLocation

@CompileStatic
class RandomizerHolder<T> {
    private final Map<ResourceLocation, T> possible

    private T enabled
    private ResourceLocation enabledLocation

    RandomizerHolder(Map<ResourceLocation, T> data) {
        possible = Map.copyOf(data)
    }

    synchronized void setup(ResourceLocation existing, ResourceLocation seedLocation, long seed) {
        if (existing !== null && possible.containsKey(existing)) {
            enabled = possible.get(existing)
            return
        }

        Random random = new Random(seed + seedLocation.hashCode())
        List<ResourceLocation> possibleLocations = possible.keySet().sort(false) { it.toString() }
        int index = random.nextInt() % possibleLocations.size()
        this.enabledLocation = possibleLocations.get(index)
        this.enabled = possible.get(this.enabledLocation)
    }

    ResourceLocation getEnabledLocation() {
        return enabledLocation
    }

    T getEnabled() {
        return enabled
    }
}

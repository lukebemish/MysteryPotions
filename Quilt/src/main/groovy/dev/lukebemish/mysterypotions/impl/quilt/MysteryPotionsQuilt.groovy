/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.mysterypotions.impl.quilt

import dev.lukebemish.mysterypotions.impl.MysteryPotionsCommon
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents

MysteryPotionsCommon.init()

ServerLifecycleEvents.READY << ({ MinecraftServer server ->
    ServerLevel level = server.getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation("overworld")))
    MysteryPotionsCommon.setupRandomized(level)
} as ServerLifecycleEvents.Ready)

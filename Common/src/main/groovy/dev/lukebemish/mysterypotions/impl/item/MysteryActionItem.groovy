package dev.lukebemish.mysterypotions.impl.item

import dev.lukebemish.mysterypotions.impl.random.PiecewiseRandomizable
import dev.lukebemish.mysterypotions.impl.random.RandomizerHolder
import groovy.transform.CompileStatic
import net.minecraft.resources.ResourceLocation
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent

@CompileStatic
class MysteryActionItem extends Item implements PiecewiseRandomizable {
    private final ResourceLocation location
    private final MysteryActionData data

    MysteryActionItem(ResourceLocation location, Properties properties, MysteryActionData data) {
        super(properties)
        this.location = location
        this.data = data
    }

    @Override
    ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player) {
            if (!level.isClientSide) {
                data.actions.enabled.accept(livingEntity)
            }
            livingEntity.awardStat(Stats.ITEM_USED.get(this))
            if (!livingEntity.abilities.instabuild) {
                stack.shrink(1)
            }
        }
        if (livingEntity !instanceof Player || !((Player) livingEntity).abilities.instabuild) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE)
            }

            if (livingEntity instanceof Player) {
                livingEntity.inventory.add(new ItemStack(Items.GLASS_BOTTLE))
            }
        }

        livingEntity.gameEvent(GameEvent.DRINK)
        return stack
    }

    @Override
    int getUseDuration(ItemStack stack) {
        return 32
    }

    @Override
    UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK
    }

    @Override
    InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return ItemUtils.startUsingInstantly(level, player, usedHand)
    }

    @Override
    ResourceLocation getSeedLocation() {
        return location
    }

    @Override
    Map<String, RandomizerHolder<?>> getRandomizers() {
        Map<String, RandomizerHolder<?>> map = new HashMap<>()
        map['action'] = data.actions
        return map
    }
}

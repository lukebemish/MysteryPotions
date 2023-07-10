package dev.lukebemish.mysterypotions.impl.item

import dev.lukebemish.mysterypotions.impl.random.PiecewiseRandomizable
import dev.lukebemish.mysterypotions.impl.random.RandomizerHolder
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.ChatFormatting
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import org.jetbrains.annotations.Nullable

@CompileStatic
class MysteryActionItem extends Item implements PiecewiseRandomizable {
    private final ResourceLocation location
    private final MysteryActionData data
    private final Closure<Boolean> allowUse
    private final boolean tooltip

    MysteryActionItem(ResourceLocation location, Properties properties, MysteryActionData data) {
        this(location, properties, data, { level, entity -> true }, false)
    }

    MysteryActionItem(ResourceLocation location, Properties properties, MysteryActionData data,
                      @ClosureParams(value = SimpleType.class, options = [
                          'net.minecraft.server.level.ServerLevel',
                          'net.minecraft.world.entity.LivingEntity'
                      ]) Closure<Boolean> allowUse, boolean tooltip) {
        super(properties)
        this.location = location
        this.data = data
        this.allowUse = allowUse
        this.tooltip = tooltip
    }

    @Override
    void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (level instanceof ServerLevel) {
            if (!allowUse.call(level, livingEntity)) {
                livingEntity.stopUsingItem()
            }
        }
    }

    @Override
    void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY))
    }

    @Override
    ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player) {
            if (livingEntity instanceof ServerPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(livingEntity, stack)
            }
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

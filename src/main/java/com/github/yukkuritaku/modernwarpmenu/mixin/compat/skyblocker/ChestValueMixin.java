package com.github.yukkuritaku.modernwarpmenu.mixin.compat.skyblocker;

import com.github.yukkuritaku.modernwarpmenu.client.gui.screens.CustomContainerScreen;
import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.hysky.skyblocker.skyblock.ChestValue;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Restriction(require = {
        @Condition("skyblocker")
})
@Mixin(ChestValue.class)
public class ChestValueMixin {

    //@Inject(method = "lambda$init$3", at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/api/client/screen/v1/Screens;getButtons(Lnet/minecraft/client/gui/screens/Screen;)Ljava/util/List;"), cancellable = true)
    @WrapOperation(method = "lambda$init$3", at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/api/client/screen/v1/Screens;getButtons(Lnet/minecraft/client/gui/screens/Screen;)Ljava/util/List;"))
    private static List<AbstractWidget> onGetScreen(Screen screen, Operation<List<AbstractWidget>> original){
        if (screen instanceof CustomContainerScreen){
            // if current screen is warp menu, don't show chest value button
            return Lists.newArrayList();
        }
        // otherwise show chest value button
        return original.call(screen);
    }
}

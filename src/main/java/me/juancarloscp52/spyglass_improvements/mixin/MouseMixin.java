package me.juancarloscp52.spyglass_improvements.mixin;

import me.juancarloscp52.spyglass_improvements.client.SpyglassImprovementsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseScroll",at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"),cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci){
        float d = (float) ((MinecraftClient.getInstance().options.discreteMouseScroll ? Math.signum(vertical) : vertical) * MinecraftClient.getInstance().options.mouseWheelSensitivity);
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player != null && player.isUsingSpyglass() && MinecraftClient.getInstance().options.getPerspective().isFirstPerson()){
            SpyglassImprovementsClient.MULTIPLIER = MathHelper.clamp(SpyglassImprovementsClient.MULTIPLIER-(d* SpyglassImprovementsClient.getInstance().settings.multiplierDelta), .1f,.8f);
            player.playSound(SoundEvents.ITEM_SPYGLASS_STOP_USING, 1.0f, 1.0f+(1*(1- SpyglassImprovementsClient.MULTIPLIER)*(1- SpyglassImprovementsClient.MULTIPLIER)));
            ci.cancel();
        }
    }

}
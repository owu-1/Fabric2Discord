package space.ryzhenkov.fabric2discord.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Inject(method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V", at = @At("HEAD"))
    private void onDeath(DamageSource source, CallbackInfo ci) {
        space.ryzhenkov.fabric2discord.ktmixins.ServerPlayerEntityMixin.INSTANCE.onDeath(Objects.requireNonNull(this.getCommandSource().getPlayer()), source, getDamageTracker());
    }

    @Inject(method = "worldChanged", at = @At("TAIL"))
    private void worldChanged(ServerWorld origin, CallbackInfo ci) {
        space.ryzhenkov.fabric2discord.ktmixins.ServerPlayerEntityMixin.INSTANCE.worldChanged(Objects.requireNonNull(this.getCommandSource().getPlayer()), origin);
    }
}
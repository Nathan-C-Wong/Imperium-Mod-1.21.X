package net.natedubs.imperiummod.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ModDamageTypes {

    public static final RegistryKey<DamageType> HUNTING_RIFLE_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("imperium-mod", "hunting_rifle_damage"));
    public static final RegistryKey<DamageType> RAY_GUN_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("imperium-mod", "ray_gun_damage"));

    // If there is no attacker
    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(
                world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }

    // To link the attacker to the damage
    public static DamageSource of(World world, RegistryKey<DamageType> key, LivingEntity attacker) {
        return world.getDamageSources().create(key, attacker);
    }



}

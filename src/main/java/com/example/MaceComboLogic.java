package com.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class MaceComboLogic {

    private static int delayTimer = 0;

    public static void executeCombo(MinecraftClient client) {
        if (delayTimer > 0) {
            delayTimer--;
            return;
        }

        Entity target = getLookTarget(client);
        if (target == null) return;

        boolean holdsMace = client.player.getMainHandStack().isOf(Items.MACE);
        
        if (holdsMace && client.player.fallDistance > 1.2f) {
            if (client.player.isInAttackRange(target)) {
                client.interactionManager.attackEntity(client.player, target);
                client.player.swingHand(Hand.MAIN_HAND);
                
                swapArmor(client, Items.NETHERITE_CHESTPLATE);
                
                useRocket(client);
                
                swapArmor(client, Items.ELYTRA);
                
                delayTimer = 10;
            }
        }
    }

    private static Entity getLookTarget(MinecraftClient client) {
        HitResult hitResult = client.crosshairTarget;
        if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
            return ((EntityHitResult) hitResult).getEntity();
        }
        return null;
    }

    private static void swapArmor(MinecraftClient client, net.minecraft.item.Item targetItem) {
        PlayerInventory inventory = client.player.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isOf(targetItem)) {
                client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, i, 0, SlotActionType.QUICK_MOVE, client.player);
                break;
            }
        }
    }

    private static void useRocket(MinecraftClient client) {
        PlayerInventory inventory = client.player.getInventory();
        int oldSlot = inventory.selectedSlot;

        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).isOf(Items.FIREWORK_ROCKET)) {
                inventory.selectedSlot = i;
                client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
                inventory.selectedSlot = oldSlot;
                break;
            }
        }
    }
}

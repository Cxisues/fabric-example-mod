package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class MaceComboClient implements ClientModInitializer {

    public static KeyBinding comboKey;
    private static boolean isComboActive = false;

    @Override
    public void onInitializeClient() {
        comboKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.macecombo.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                "category.macecombo"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            if (comboKey.wasPressed()) {
                isComboActive = !isComboActive;
                if (isComboActive) {
                    client.player.sendMessage(Text.literal("§aMace Makrosu Aktif!"), true);
                } else {
                    client.player.sendMessage(Text.literal("§cMace Makrosu Devre Dışı!"), true);
                }
            }

            if (isComboActive) {
                MaceComboLogic.executeCombo(client);
            }
        });
    }
}

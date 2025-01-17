package com.emperdog.boxlink;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.UUID;

public class BoxLinkServerEventHandler {
    public ArrayList<UUID> LOGGED_IN = new ArrayList<>();

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        UUID playerUUID = event.getEntity().getUUID();
        if(BoxLinkConfig.boxLinkBindRequiresItem || LOGGED_IN.contains(playerUUID))
            return;

        event.getEntity().sendSystemMessage(Component.translatable("cobblemonboxlink.message.box_link_not_required",
                        Component.keybind(BoxLinkMod.OPEN_PC_KEY_NAME)
                                .withStyle(ChatFormatting.AQUA)
                                .withStyle(ChatFormatting.BOLD))
                .withStyle(ChatFormatting.YELLOW));
        LOGGED_IN.add(playerUUID);
    }
}

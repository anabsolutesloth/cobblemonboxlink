package com.emperdog.boxlink;

import com.emperdog.boxlink.network.RequestOpenPCPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class BoxLinkClientEventHandler {

    public static KeyMapping openPCKey =
            new KeyMapping(BoxLinkMod.OPEN_PC_KEY_NAME, GLFW.GLFW_KEY_BACKSLASH, "key.cobblemonboxlink.category");

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        if(openPCKey.consumeClick() && Minecraft.getInstance().isWindowActive())
            PacketDistributor.sendToServer(new RequestOpenPCPacket(Minecraft.getInstance().getUser().getProfileId()));
    }
}

package com.emperdog.boxlink;

import com.emperdog.boxlink.network.RequestOpenPCPacket;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class BoxLinkClientEventHandler {

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        if(BoxLinkMod.openPCKey.consumeClick() && Minecraft.getInstance().isWindowActive())
            PacketDistributor.sendToServer(new RequestOpenPCPacket(Minecraft.getInstance().getUser().getProfileId()));
    }
}

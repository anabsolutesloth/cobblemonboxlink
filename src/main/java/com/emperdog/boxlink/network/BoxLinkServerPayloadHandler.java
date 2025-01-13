package com.emperdog.boxlink.network;

import com.emperdog.boxlink.BoxLinkConfig;
import com.emperdog.boxlink.BoxLinkMod;
import com.emperdog.boxlink.compat.CuriosCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.UUID;

import static java.util.Objects.isNull;

public class BoxLinkServerPayloadHandler {

    private static final HashMap<UUID, Integer> storedLinkIndexes = new HashMap<>();

    public static void handle(final RequestOpenPCPacket data, final IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        Inventory playerInventory = player.getInventory();

        BoxLinkMod.LOGGER.info("received RequestOpenPCPacket from {} with data UUID {}", player.getUUID(), data.uuid());

        BoxLinkMod.LOGGER.info("boxLinkBindRequiresItem: {}", BoxLinkConfig.boxLinkBindRequiresItem);
        if(player.getUUID().equals(data.uuid())) {
            // skip search logic and immediately open PC if item is not required.
            if(BoxLinkConfig.boxLinkBindRequiresItem) {
                // check stored inventory index
                if (!isNull(storedLinkIndexes.get(player.getUUID()))
                        && playerInventory.getItem(storedLinkIndexes.get(player.getUUID())).getItem().equals(BoxLinkMod.BOX_LINK_ITEM.get()))
                    BoxLinkMod.openPCStorage(player);
                // check Curio slots
                else if (BoxLinkMod.curiosLoaded
                        && CuriosCompat.hasBoxLinkAsCurio(player)) {
                    BoxLinkMod.openPCStorage(player);
                } else {
                    // otherwise search for Box Link
                    for (int index = 0; index < 36; index++) {
                        ItemStack item = playerInventory.getItem(index);
                        if (item.getItem().equals(BoxLinkMod.BOX_LINK_ITEM.get())) {
                            storedLinkIndexes.put(player.getUUID(), index);
                            BoxLinkMod.openPCStorage(player);
                            break;
                        }
                        if (index == 35)
                            player.displayClientMessage(
                                    Component.translatable("cobblemonboxlink.key_open_pc.no_box_link").withStyle(ChatFormatting.RED),
                                    true
                            );
                    }
                }

            } else BoxLinkMod.openPCStorage(player);
        } else {
            throw new IllegalArgumentException("Player UUID "+ player.getUUID() +" does not match provided UUID "+ data.uuid());
        }
    }
}

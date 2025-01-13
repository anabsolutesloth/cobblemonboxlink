package com.emperdog.boxlink.compat;

import com.emperdog.boxlink.BoxLinkMod;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosCompat {
    public static boolean hasBoxLinkAsCurio(Player player) {
        BoxLinkMod.LOGGER.info("Looking for Box Link in Curio");
        BoxLinkMod.LOGGER.info("Curios Inventory available: {}", CuriosApi.getCuriosInventory(player).isPresent());
        if(CuriosApi.getCuriosInventory(player).isEmpty())
            return false;
        BoxLinkMod.LOGGER.info("Box Link found in Curio: {}", !CuriosApi.getCuriosInventory(player).get().findCurios(BoxLinkMod.BOX_LINK_ITEM.get()).isEmpty());
        return !CuriosApi.getCuriosInventory(player).get().findCurios(BoxLinkMod.BOX_LINK_ITEM.get()).isEmpty();
    }
}

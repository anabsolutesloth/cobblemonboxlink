package com.emperdog.boxlink.compat;

import com.emperdog.boxlink.BoxLinkMod;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosCompat {
    public static boolean hasBoxLinkAsCurio(Player player) {
        if(CuriosApi.getCuriosInventory(player).isEmpty())
            return false;
        return !CuriosApi.getCuriosInventory(player).get().findCurios(BoxLinkMod.BOX_LINK_ITEM.get()).isEmpty();
    }
}

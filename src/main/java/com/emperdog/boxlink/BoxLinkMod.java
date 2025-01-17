package com.emperdog.boxlink;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.api.storage.pc.PCStore;
import com.cobblemon.mod.common.api.storage.pc.link.PCLink;
import com.cobblemon.mod.common.api.storage.pc.link.PCLinkManager;
import com.cobblemon.mod.common.item.group.CobblemonItemGroups;
import com.cobblemon.mod.common.net.messages.client.storage.pc.OpenPCPacket;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.emperdog.boxlink.item.BoxLinkItem;
import com.emperdog.boxlink.network.RequestOpenPCPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.emperdog.boxlink.BoxLinkClientEventHandler.openPCKey;

@Mod(BoxLinkMod.MODID)
public class BoxLinkMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "cobblemonboxlink";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean curiosLoaded = ModList.get().isLoaded("curios");

    public static final String OPEN_PC_KEY_NAME = "key.cobblemonboxlink.open_pc.desc";


    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> BOX_LINK_ITEM = ITEMS.register("box_link",
            () -> new BoxLinkItem(new Item.Properties()));

    public BoxLinkMod(IEventBus modEventBus, ModContainer modContainer)
    {
        ITEMS.register(modEventBus);

        modEventBus.addListener(RequestOpenPCPacket::register);

        modEventBus.addListener(this::commonSetup);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.SERVER, BoxLinkConfig.SPEC);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CobblemonItemGroups.getUTILITY_ITEMS_KEY())
            event.accept(BOX_LINK_ITEM);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new BoxLinkServerEventHandler());
    }

    // Function to open the Player's PC Storage and allow them to modify it.
    public static void openPCStorage(ServerPlayer player) {
        if(PlayerExtensionsKt.isInBattle(player)) {
            player.sendSystemMessage(Component.translatable("cobblemon.pc.inbattle").withColor(0xFF0000));
            return;
        }

        PCStore pc = Cobblemon.INSTANCE.getStorage().getPC(player);
        PCLinkManager.INSTANCE.addLink(new PCLink(pc, player.getUUID()));
        player.level().playSound(null, player.blockPosition(), CobblemonSounds.PC_ON, SoundSource.NEUTRAL, 0.5f, 1.0f);
        new OpenPCPacket(pc.getUuid()).sendToPlayer(player);
    }

    @EventBusSubscriber(modid = BoxLinkMod.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientProxy {
        @SubscribeEvent
        public static void registerKeybinds(final RegisterKeyMappingsEvent event) {
            event.register(openPCKey);
        }

        @SubscribeEvent
        public static void setupClient(FMLClientSetupEvent event) {
            NeoForge.EVENT_BUS.register(new BoxLinkClientEventHandler());
        }
    }
}

package net.programmer.igoodie.streamspawn.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.programmer.igoodie.streamspawn.StreamSpawn;
import net.programmer.igoodie.streamspawn.config.ModConfigs;
import net.programmer.igoodie.streamspawn.network.ModNetwork;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = StreamSpawn.MOD_ID)
public class LifecycleEvents {

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        ModConfigs.loadConfigs();
        ModNetwork.initialize();
    }

}

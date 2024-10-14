package net.programmer.igoodie.streamspawn.event;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.programmer.igoodie.streamspawn.StreamSpawn;
import net.programmer.igoodie.streamspawn.client.gui.screen.StreamSpawnScreen;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = StreamSpawn.MOD_ID)
public class ModInputEvents {

    public static KeyMapping keyStreamSpawnScreen;

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(keyStreamSpawnScreen = new KeyMapping("key.streamSpawnScreen", GLFW.GLFW_KEY_F4, "key.categories.streamspawn"));
    }

    @Mod.EventBusSubscriber(Dist.CLIENT)
    public static class ForgeInputEvents {

        @SubscribeEvent
        public static void handleKeyInput(InputEvent.Key event) {
            Minecraft minecraft = Minecraft.getInstance();

            if (keyStreamSpawnScreen.isDown()) {
                if (minecraft.screen == null) {
                    minecraft.setScreen(new StreamSpawnScreen());
                }
            }
        }

    }

}

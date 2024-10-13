package net.programmer.igoodie.streamspawn.event;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.programmer.igoodie.streamspawn.client.gui.screen.StreamSpawnScreen;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ModInputEvents {

    public static KeyMapping keyStreamSpawnScreen;

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(keyStreamSpawnScreen = new KeyMapping("key.streamSpawnScreen", GLFW.GLFW_KEY_F4, "key.categories.streamspawn"));
    }

    @SubscribeEvent
    public static void test(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.screen == null && keyStreamSpawnScreen.isDown()) {
            minecraft.setScreen(new StreamSpawnScreen());
        }
    }

}

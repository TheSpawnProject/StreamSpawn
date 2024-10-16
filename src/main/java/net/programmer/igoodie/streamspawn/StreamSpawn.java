package net.programmer.igoodie.streamspawn;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(StreamSpawn.MOD_ID)
public class StreamSpawn {

    public static final String MOD_ID = "streamspawn";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public StreamSpawn() {}

}

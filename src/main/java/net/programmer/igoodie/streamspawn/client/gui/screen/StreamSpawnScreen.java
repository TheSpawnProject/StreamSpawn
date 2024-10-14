package net.programmer.igoodie.streamspawn.client.gui.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.programmer.igoodie.streamspawn.StreamSpawn;
import org.jetbrains.annotations.NotNull;

public class StreamSpawnScreen extends Screen {

    public static final ResourceLocation GOODIE_POSE_TEXTURE = StreamSpawn.id("textures/goodie_pose.png");

    protected Button startButton;
    protected Button stopButton;
    protected Button refreshButton;
    protected Button openConfigFolderButton;

    protected float blockChangeTick = 0;
    protected ItemStack itemStack;

    public StreamSpawnScreen() {
        super(Component.literal("StreamSpawn"));
    }

    public void pickRandomItem() {
        Item[] items = {
                Items.BEACON,
                Items.MELON,
                Items.CACTUS,
                Items.DIAMOND,
                Items.SLIME_BALL,
                Items.CHORUS_FRUIT,
                Items.CHORUS_FLOWER,
                Items.AXOLOTL_BUCKET,
        };

        Item item = Math.random() >= 0.4
//                ? ModItems.TWITCH_LOGO
                ? Items.MELON
                : items[(int) Math.floor(Math.random() * items.length)];

        itemStack = new ItemStack(item);
    }

    @Override
    protected void init() {
        int buttonYOffset = 80;

        this.startButton = addRenderableWidget(
                new Button(
                        10, buttonYOffset,
                        100, 20,
                        Component.literal("Connect"),
                        (button) -> System.out.println("Start")
                )
        );

        pickRandomItem();
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);

        super.render(poseStack, mouseX, mouseY, partialTicks);
        
        renderPlayers(poseStack);

        blockChangeTick += partialTicks;
        if (blockChangeTick >= 20f) {
            pickRandomItem();
            blockChangeTick = 0f;
        }
    }

    protected void renderPlayers(PoseStack poseStack) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;

        {
            float scale = 50;

            int x = width - 140;
            int y = height - 20;

            int ux = 0;
            int uy = 0;
            int tw = 362;
            int th = 362;
            int vx = 223;
            int vh = 362;

            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderTexture(0, GOODIE_POSE_TEXTURE);
            RenderSystem.setShaderColor(1, 1, 1, 1);

            poseStack.pushPose();
            poseStack.translate(x - 21, y - 1.9 * scale, 500);
            float scl = 0.27f;
            poseStack.scale(scl, scl, scl);
            GuiComponent.blit(poseStack,
                    0, 0, ux, uy, vx, vh, tw, th);
            poseStack.popPose();

            String text = "iGoodie";

            font.draw(poseStack, text,
                    x - font.width(text) / 2f + 0.05f * scale,
                    y - 2.2f * scale,
                    0xFF_AAAAAA);
        }

        {
            float p_98854_ = 100;
            float p_98855_ = -20;
            float scale = 50;

            int posX = width - 50;
            int posY = height - 20;

            LocalPlayer entity = minecraft.player;

            float f = (float) Math.atan((double) (p_98854_ / 40.0F));
            float f1 = (float) Math.atan((double) (p_98855_ / 40.0F));
            PoseStack posestack = RenderSystem.getModelViewStack();
            posestack.pushPose();
            posestack.translate((double) posX, (double) posY, 1050.0D);
            posestack.scale(1.0F, 1.0F, -1.0F);
            RenderSystem.applyModelViewMatrix();
            PoseStack posestack1 = new PoseStack();
            posestack1.translate(0.0D, 0.0D, 1000.0D);
            posestack1.scale((float) scale, (float) scale, (float) scale);
            Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
            Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
            quaternion.mul(quaternion1);
            posestack1.mulPose(quaternion);
            float f2 = entity.yBodyRot;
            float f3 = entity.getYRot();
            float f4 = entity.getXRot();
            float f5 = entity.yHeadRotO;
            float f6 = entity.yHeadRot;
            entity.yBodyRot = 180.0F + f * 20.0F;
            entity.setYRot(180.0F + f * 40.0F);
            entity.setXRot(-f1 * 20.0F);
            entity.yHeadRot = entity.getYRot();
            entity.yHeadRotO = entity.getYRot();
            Lighting.setupForEntityInInventory();
            EntityRenderDispatcher entityrenderdispatcher = minecraft.getEntityRenderDispatcher();
            quaternion1.conj();
            entityrenderdispatcher.overrideCameraOrientation(quaternion1);
            entityrenderdispatcher.setRenderShadow(false);
            MultiBufferSource.BufferSource multibuffersource$buffersource = minecraft.renderBuffers().bufferSource();
//        RenderSystem.runAsFancy(() -> {
//            entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880);
//        });
            entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880);


            {
                ItemRenderer renderer = minecraft.getItemRenderer();

                posestack.pushPose();
                posestack1.pushPose();
                posestack1.translate(1.1, 0.088f * scale, -18);
                float scl = 0.5f;
                posestack1.scale(scl, scl, scl);
                double rotation = -100 * ((System.currentTimeMillis() / 1000D) + (1 / 25f)) % 360 * (Math.PI / 180d);
                posestack1.mulPose(Quaternion.fromXYZ((float) 0, ((float) rotation), (float) 0));
                double yOffset = Math.sin(rotation) / 15;
                posestack1.translate(0, yOffset, 0);
                renderer.renderStatic(
                        itemStack,
                        ItemTransforms.TransformType.FIXED,
                        15728880, OverlayTexture.NO_OVERLAY,
                        posestack1,
                        multibuffersource$buffersource,
                        0
                );
                posestack1.popPose();
                posestack.popPose();
            }

            multibuffersource$buffersource.endBatch();
            entityrenderdispatcher.setRenderShadow(true);
            entity.yBodyRot = f2;
            entity.setYRot(f3);
            entity.setXRot(f4);
            entity.yHeadRotO = f5;
            entity.yHeadRot = f6;

            posestack.popPose();
            RenderSystem.applyModelViewMatrix();
            Lighting.setupFor3DItems();

            String text = entity.getName().getString();

            font.draw(poseStack, text,
                    posX - font.width(text) / 2f - 0.05f * scale,
                    posY - 2.2f * scale,
                    0xFF_AAAAAA);
        }
    }

}

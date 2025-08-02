package io.redspace.bagels_spell.entity.flower_surround;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Random;

public class FlowerSurroundRenderer extends EntityRenderer<FlowerSurroundProjectile> {

    private static final ResourceLocation[] TEXTURES = {
            new ResourceLocation("textures/particle/sweep_0.png"),
            new ResourceLocation("textures/particle/sweep_1.png"),
            new ResourceLocation("textures/particle/sweep_2.png"),
            new ResourceLocation("textures/particle/sweep_3.png"),
            new ResourceLocation("textures/particle/sweep_4.png"),
            new ResourceLocation("textures/particle/sweep_5.png"),
            new ResourceLocation("textures/particle/sweep_6.png"),
            new ResourceLocation("textures/particle/sweep_7.png")
    };

    public FlowerSurroundRenderer(Context context) {
        super(context);
    }

    @Override
    public void render(FlowerSurroundProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();

        Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        // Unique seed for consistent randomness per entity
        Random random = new Random(entity.getId());

        // Number of slashes to render around this projectile
        int numSlashes = 8;
        float angleStep = 360.0f / numSlashes;
        float radius = 1.5f;

        for (int i = 0; i < numSlashes; i++) {
            float angle = i * angleStep;

            poseStack.pushPose();

            float radians = angle * Mth.DEG_TO_RAD;
            float xOffset = Mth.cos(radians) * radius;
            float zOffset = Mth.sin(radians) * radius;
            float yOffset = random.nextFloat() * 3 - 1.5f;

            poseStack.translate(xOffset, yOffset, zOffset);

            // ---- Align to velocity ----
            Vec3 velocity = entity.getDeltaMovement();
            if (!velocity.equals(Vec3.ZERO)) {
                double motionX = velocity.x;
                double motionY = velocity.y;
                double motionZ = velocity.z;

                float motionYaw = (float) (Mth.atan2(motionX, motionZ) * (180f / Math.PI));
                float motionPitch = (float) (Mth.atan2(motionY, Math.sqrt(motionX * motionX + motionZ * motionZ)) * (180f / Math.PI));

                poseStack.mulPose(Axis.YP.rotationDegrees(-motionYaw));
                poseStack.mulPose(Axis.XP.rotationDegrees(motionPitch));
            }

            // ---- Optional: Random Z-tilt ----
            float randomTiltZ = (random.nextFloat() * 2f - 1f) * 15f;
            poseStack.mulPose(Axis.ZP.rotationDegrees(randomTiltZ));

            // Render slash
            drawSlash(poseStack.last(), entity, bufferSource, light, entity.getBbWidth(), i);

            poseStack.popPose();
        }

        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    private void drawSlash(Pose pose, FlowerSurroundProjectile entity, MultiBufferSource bufferSource, int light, float width, int offset) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity, offset)));
        float halfWidth = width * 0.5f;

        consumer.vertex(poseMatrix, -halfWidth, -.1f, -halfWidth).color(255, 182, 193, 155).uv(0f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, halfWidth, -.1f, -halfWidth).color(255, 182, 193, 155).uv(1f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, halfWidth, -.1f, halfWidth).color(255, 182, 193, 155).uv(1f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, -halfWidth, -.1f, halfWidth).color(255, 182, 193, 155).uv(0f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0f, 1f, 0f).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(FlowerSurroundProjectile entity) {
        int frame = (entity.animationTime / 4) % TEXTURES.length;
        return TEXTURES[frame];
    }

    private ResourceLocation getTextureLocation(FlowerSurroundProjectile entity, int offset) {
        int frame = (entity.animationTime / 6 + offset) % TEXTURES.length;
        return TEXTURES[frame];
    }
}
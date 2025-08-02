package io.redspace.bagels_spell.entity.thousand_blossoms;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ThousandBlossomRenderer extends EntityRenderer<ThousandBlossomProjectile> {
    //
    private static ResourceLocation TEXTURES[] = {
            new ResourceLocation("textures/particle/sweep_0.png"),
            new ResourceLocation("textures/particle/sweep_1.png"),
            new ResourceLocation("textures/particle/sweep_2.png"),
            new ResourceLocation("textures/particle/sweep_3.png"),
            new ResourceLocation("textures/particle/sweep_4.png"),
            new ResourceLocation("textures/particle/sweep_5.png"),
            new ResourceLocation("textures/particle/sweep_6.png"),
            new ResourceLocation("textures/particle/sweep_7.png")
    };

    public ThousandBlossomRenderer(Context context) {
        super(context);
    }

    @Override
    public void render(ThousandBlossomProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();

        Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
        poseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        entity.animationTime++;
        poseStack.mulPose(Axis.ZP.rotationDegrees(((entity.animationSeed % 30) - 15) * (float) Math.sin(entity.animationTime * .015)));

        //VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));

        float radius = entity.getRadius();
        float width = radius * 2.0f;
        //float halfWidth = width * .5f;
        //old color: 125, 0, 10

        //drawSlash(pose, bufferSource, light, width, 2);

        poseStack.mulPose(Axis.YP.rotationDegrees(-15));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-10));
        drawSlash(pose,entity, bufferSource, light, width, 4);

        poseStack.mulPose(Axis.YP.rotationDegrees(30));
        poseStack.mulPose(Axis.ZP.rotationDegrees(20));
        drawSlash(pose,entity, bufferSource, light, width, 0);

        // First slash (rotated +45° around Z)
        poseStack.mulPose(Axis.ZP.rotationDegrees(45));
        drawSlash(pose, entity, bufferSource, light, width, 4);

        // Second slash (rotated -45° around Z)
        poseStack.mulPose(Axis.ZP.rotationDegrees(-90)); // Total rotation becomes -45° after first
        drawSlash(pose, entity, bufferSource, light, width, 0);

        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    private void drawSlash(Pose pose, ThousandBlossomProjectile entity, MultiBufferSource bufferSource, int light, float width, int offset) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        int transparency = 0;

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity,offset)));
        float halfWidth = width * .5f;
        //old color: 125, 0, 10
        consumer.vertex(poseMatrix, -halfWidth, -.1f, -halfWidth).color(255, 182, 193, transparency).uv(0f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, halfWidth, -.1f, -halfWidth).color(255, 182, 193, transparency).uv(1f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, halfWidth, -.1f, halfWidth).color(255, 182, 193, transparency).uv(1f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, -halfWidth, -.1f, halfWidth).color(255, 182, 193, transparency).uv(0f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0f, 1f, 0f).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(ThousandBlossomProjectile entity) {
        int frame = (entity.animationTime / 4) % TEXTURES.length;
        return TEXTURES[frame];
        //return TEXTURE;
    }

    private ResourceLocation getTextureLocation(ThousandBlossomProjectile entity, int offset) {
        int frame = (entity.animationTime / 6 + offset) % TEXTURES.length;
        return TEXTURES[frame];
    }
}
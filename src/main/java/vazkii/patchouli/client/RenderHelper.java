package vazkii.patchouli.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class RenderHelper {
	public static void renderItemStackInGui(MatrixStack ms, ItemStack stack, int x, int y) {
		transferMsToGl(ms, () -> Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(stack, x, y));
	}

	/**
	 * Temporary shim to allow methods such as
	 * {@link net.minecraft.client.render.item.ItemRenderer#renderInGuiWithOverrides}
	 * to support matrixstack transformations. Hopefully Mojang finishes this migration up...
	 * Transfers the current CPU matrixstack to the openGL matrix stack, then runs the provided function
	 * Assumption: the "root" state of the MatrixStack is same as the currently GL state,
	 * such that multiplying the MatrixStack to the current GL matrix state will get us where we want to be.
	 * If there have been intervening changes to the GL matrix state since the MatrixStack was constructed, then this
	 * won't work.
	 */
	public static void transferMsToGl(MatrixStack ms, Runnable toRun) {
		RenderSystem.pushMatrix();
		RenderSystem.multMatrix(ms.getLast().getMatrix());
		toRun.run();
		RenderSystem.popMatrix();
	}
}

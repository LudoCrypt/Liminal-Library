package net.ludocrypt.limlib.access;

import net.minecraft.client.util.math.MatrixStack;

public interface HeldItemRendererAccess {

	public MatrixStack getLeftHandStack();

	public MatrixStack getRightHandStack();

	public void setLeftHandStack(MatrixStack stack);

	public void setRightHandStack(MatrixStack stack);

}

/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.modelviewer;

import net.runelite.cache.models.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class Camera
{
	private static final float MAX_X = 89;

	public float moveSpeed = 0.60f;

	private float mouseSensitivity = 0.05f;

	private Vector3f pos = new Vector3f(0, 0, 0);
	private Vector3f rotation = new Vector3f(0, 0, 0);

	public void apply()
	{
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		glRotatef(rotation.x, 1, 0, 0);
		glRotatef(rotation.y, 0, 1, 0);
		glRotatef(rotation.z, 0, 0, 1);
		glTranslatef(-pos.x, -pos.y, -pos.z);
	}

	public void acceptInput(float delta)
	{
		acceptInputRotate(delta);
		acceptInputGrab();
		acceptInputMove(delta);
	}

	public void acceptInputRotate(float delta)
	{
		if (Mouse.isGrabbed())
		{
			float mouseDX = Mouse.getDX();
			float mouseDY = -Mouse.getDY();

			rotation.y += mouseDX * mouseSensitivity * delta;
			rotation.x += mouseDY * mouseSensitivity * delta;

			rotation.y %= 360.0f;
			rotation.x %= 360.0f;

			// bound y between (-180, 180]
			if (rotation.y > 180.0f)
			{
				rotation.y = -360.0f + rotation.y;
			}
			if (rotation.y <= -180.0f)
			{
				rotation.y = 360.0f + rotation.y;
			}

			// cap x to prevent from flipping upsidedown
			if (rotation.x < -MAX_X)
			{
				rotation.x = -MAX_X;
			}
			if (rotation.x > MAX_X)
			{
				rotation.x = MAX_X;
			}
		}
	}

	public void acceptInputGrab()
	{
		if (Mouse.isInsideWindow() && Mouse.isButtonDown(0))
		{
			Mouse.setGrabbed(true);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Keyboard.isKeyDown(Keyboard.KEY_LMENU))
		{
			Mouse.setGrabbed(false);
		}
	}

	public void acceptInputMove(float delta)
	{
		boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_W);
		boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_S);
		boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_D);
		boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_A);
		boolean keyFast = Keyboard.isKeyDown(Keyboard.KEY_Q);
		boolean keySlow = Keyboard.isKeyDown(Keyboard.KEY_E);
		boolean keyFlyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		boolean keyFlyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

		float speed;

		if (keyFast)
		{
			speed = moveSpeed * 5;
		}
		else if (keySlow)
		{
			speed = moveSpeed / 2;
		}
		else
		{
			speed = moveSpeed;
		}

		speed *= delta;

		// sin(0) = 0
		// cos(0) = 1
		if (keyFlyUp)
		{
			pos.y += Math.cos(Math.toRadians(rotation.x)) * speed;
			pos.z -= Math.cos(Math.toRadians(rotation.y)) * Math.sin(Math.toRadians(rotation.x)) * speed;
			pos.x += Math.sin(Math.toRadians(rotation.x)) * Math.sin(Math.toRadians(rotation.y)) * speed;
		}
		if (keyFlyDown)
		{
			pos.y -= Math.cos(Math.toRadians(rotation.x)) * speed;
			pos.z += Math.cos(Math.toRadians(rotation.y)) * Math.sin(Math.toRadians(rotation.x)) * speed;
			pos.x -= Math.sin(Math.toRadians(rotation.x)) * Math.sin(Math.toRadians(rotation.y)) * speed;
		}

		if (keyDown)
		{
			pos.x -= Math.sin(Math.toRadians(rotation.y)) * speed;
			pos.z += Math.cos(Math.toRadians(rotation.x)) * Math.cos(Math.toRadians(rotation.y)) * speed;
			pos.y += Math.sin(Math.toRadians(rotation.x)) * speed;
		}
		if (keyUp)
		{
			pos.x += Math.sin(Math.toRadians(rotation.y)) * speed;
			pos.z -= Math.cos(Math.toRadians(rotation.x)) * Math.cos(Math.toRadians(rotation.y)) * speed;
			pos.y -= Math.sin(Math.toRadians(rotation.x)) * speed;
		}

		if (keyLeft)
		{
			pos.x += Math.sin(Math.toRadians(rotation.y - 90)) * speed;
			pos.z -= Math.cos(Math.toRadians(rotation.y - 90)) * speed;
		}
		if (keyRight)
		{
			pos.x += Math.sin(Math.toRadians(rotation.y + 90)) * speed;
			pos.z -= Math.cos(Math.toRadians(rotation.y + 90)) * speed;
		}
	}

	public void setSpeed(float speed)
	{
		moveSpeed = speed;
	}

	public void setPos(Vector3f pos)
	{
		this.pos = pos;
	}

	public Vector3f getPos()
	{
		return pos;
	}

	public Vector3f getRotation()
	{
		return rotation;
	}

	public void setRotation(Vector3f rotation)
	{
		this.rotation = rotation;
	}

	public void setMouseSensitivity(float mouseSensitivity)
	{
		this.mouseSensitivity = mouseSensitivity;
	}

	public float getMouseSensitivity()
	{
		return mouseSensitivity;
	}
}

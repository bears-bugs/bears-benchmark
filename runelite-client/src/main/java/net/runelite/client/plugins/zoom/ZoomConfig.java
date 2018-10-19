/*
 * Copyright (c) 2018 Abex
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
package net.runelite.client.plugins.zoom;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("zoom")
public interface ZoomConfig extends Config
{
	@ConfigItem(
		keyName = "enabled",
		name = "Expand outer zoom limit",
		description = "Configures whether or not the outer zoom limit is reduced",
		position = 1
	)
	default boolean outerLimit()
	{
		return true;
	}

	@ConfigItem(
		keyName = "inner",
		name = "Expand inner zoom limit",
		description = "Configures whether or not the inner zoom limit is reduced",
		position = 2
	)
	default boolean innerLimit()
	{
		return false;
	}

	@ConfigItem(
		keyName = "relaxCameraPitch",
		name = "Vertical camera",
		description = "Relax the camera's upper pitch limit",
		position = 3
	)
	default boolean relaxCameraPitch()
	{
		return false;
	}

	@ConfigItem(
		keyName = "requireControlDown",
		name = "Require control down",
		description = "Configures if holding control is required for zooming",
		position = 4
	)
	default boolean requireControlDown()
	{
		return false;
	}
}

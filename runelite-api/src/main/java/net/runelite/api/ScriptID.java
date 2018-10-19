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
package net.runelite.api;

public final class ScriptID
{
	/**
	 * Updates the scrollbar handle and container to the new height of the content container
	 * <ul>
	 * <li> int (WidgetID) Scrollbar's widget ID </li>
	 * <li> int (WidgetID) Container widget ID </li>
	 * <li> int how far down to scroll </li>
	 * </ul>
	 */
	public static final int UPDATE_SCROLLBAR = 72;

	/**
	 * Sends a chat message
	 * <ul>
	 * <li> int (byte) Flags </li>
	 * <li> String Message to send </li>
	 * </ul>
	 */
	public static final int CHATBOX_INPUT = 96;

	/**
	 * Closes the chatbox input
	 * <ul>
	 * <li> int (boolean) Clear the current text </li>
	 * <li> int (boolean) Unknown; set to 1 </li>
	 * </ul>
	 */
	public static final int CLOSE_CHATBOX_INPUT = 299;

	/**
	 * Updates the Diary/Quest interface's scrollbar
	 * <ul>
	 * <li> int (boolean) Reset scroll position </li>
	 * <li> int Number of lines </li>
	 * </ul>
	 */
	public static final int DIARY_QUEST_UPDATE_LINECOUNT = 2523;

	/**
	 * Initializes the chatbox input to use RuneLite callbacks
	 * <ul>
	 * <li> String  Prompt text </li>
	 * <li> String  Default value </li>
	 * </ul>
	 */
	public static final int RUNELITE_CHATBOX_INPUT_INIT = 10001;

	/**
	 * Does nothing
	 *
	 * This is used to eat events when you want a menu action attached to it
	 * because you need an op listener attached to it for it to work
	 */
	public static final int NULL = 10003;

	/**
	 * Send a private message.
	 */
	public static final int PRIVMSG = 10004;
}

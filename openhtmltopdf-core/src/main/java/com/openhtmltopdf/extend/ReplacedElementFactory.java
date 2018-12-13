/*
 * {{{ header & license
 * Copyright (c) 2006 Wisconsin Court System
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * }}}
 */
package com.openhtmltopdf.extend;

import com.openhtmltopdf.layout.LayoutContext;
import com.openhtmltopdf.render.BlockBox;
import org.w3c.dom.Element;

public interface ReplacedElementFactory {
    
    /**
     * <b>NOTE:</b> Only block equivalent elements can be replaced.
     * 
     * @param cssWidth The CSS width of the element in dots (or <code>-1</code> if
     * width is <code>auto</code>)
     * @param cssHeight The CSS height of the element in dots (or <code>-1</code>
     * if the height should be treated as <code>auto</code>)
     * @return The <code>ReplacedElement</code> or <code>null</code> if no
     * <code>ReplacedElement</code> applies 
     */
    public ReplacedElement createReplacedElement(
            LayoutContext c, BlockBox box,
            UserAgentCallback uac, int cssWidth, int cssHeight);

    public boolean isReplacedElement(Element e);
    
}

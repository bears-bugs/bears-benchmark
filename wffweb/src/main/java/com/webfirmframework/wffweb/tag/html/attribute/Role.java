/*
 * Copyright 2014-2018 Web Firm Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * @author WFF
 */
package com.webfirmframework.wffweb.tag.html.attribute;

import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;

/**
 * The role value can be any {@code Role.*}, for eg {@code Role.BUTTON}. Refer
 * http://w3c.github.io/html/dom.html#global-aria--attributes for more info
 *
 * @author WFF
 * @since 2.0.1
 */
public class Role extends AbstractAttribute implements GlobalAttributable {

    private static final long serialVersionUID = 1_0_0L;

    /**
     * ARIA global states and properties can be used on any HTML element.
     */
    public static final String ANY = "any";

    /**
     * A message with important, and usually time-sensitive, information.
     */
    public static final String ALERT = "alert";

    /**
     * A type of dialog that contains an alert message, where initial focus goes
     * to an element within the dialog.
     */
    public static final String ALERTDIALOG = "alertdialog";

    /**
     * A region declared as a web application, as opposed to a web document.
     */
    public static final String APPLICATION = "application";

    /**
     * A section of a page that consists of a composition that forms an
     * independent part of a document, page, or site.
     */
    public static final String ARTICLE = "article";

    /**
     * A region that contains mostly site-oriented content, rather than
     * page-specific content.
     */
    public static final String BANNER = "banner";

    /**
     * An input that allows for user-triggered actions when clicked or pressed.
     */
    public static final String BUTTON = "button";

    /**
     * A checkable input that has three possible values: true, false, or mixed.
     */
    public static final String CHECKBOX = "checkbox";

    /**
     * A cell containing header information for a column.
     */
    public static final String COLUMNHEADER = "columnheader";

    /**
     * A presentation of a select; usually similar to a textbox where users can
     * type ahead to select an option, or type to enter arbitrary text as a new
     * item in the list. See related listbox.
     */
    public static final String COMBOBOX = "combobox";

    /**
     * A supporting section of the document, designed to be complementary to the
     * main content at a similar level in the DOM hierarchy, but remains
     * meaningful when separated from the main content.
     */
    public static final String COMPLEMENTARY = "complementary";

    /**
     * A large perceivable region that contains information about the parent
     * document.
     */
    public static final String CONTENTINFO = "contentinfo";

    /**
     * A definition of a term or concept.
     */
    public static final String DEFINITION = "definition";

    /**
     * A dialog is an application window that is designed to interrupt the
     * current processing of an application in order to prompt the user to enter
     * information or require a response.
     */
    public static final String DIALOG = "dialog";

    /**
     * A list of references to members of a group, such as a static table of
     * contents.
     */
    public static final String DIRECTORY = "directory";

    /**
     * A region containing related information that is declared as document
     * content, as opposed to a web application.
     */
    public static final String DOCUMENT = "document";

    /**
     * A landmark region that contains a collection of items and objects that,
     * as a whole, combine to create a form.
     */
    public static final String FORM = "form";

    /**
     * A grid is an interactive control which contains cells of tabular data
     * arranged in rows and columns, like a table.
     */
    public static final String GRID = "grid";

    /**
     * A cell in a grid or treegrid.
     */
    public static final String GRIDCELL = "gridcell";

    /**
     * A set of user interface objects which are not intended to be included in
     * a page summary or table of contents by assistive technologies.
     */
    public static final String GROUP = "group";

    /**
     * A heading for a section of the page.
     */
    public static final String HEADING = "heading";

    /**
     * A container for a collection of elements that form an image.
     */
    public static final String IMG = "img";

    /**
     * An interactive reference to an internal or external resource that, when
     * activated, causes the user agent to navigate to that resource.
     */
    public static final String LINK = "link";

    /**
     * A group of non-interactive list items.
     */
    public static final String LIST = "list";

    /**
     * A widget that allows the user to select one or more items from a list of
     * choices.
     */
    public static final String LISTBOX = "listbox";

    /**
     * A single item in a list or directory.
     */
    public static final String LISTITEM = "listitem";

    /**
     * A type of live region where new information is added in meaningful order
     * and old information may disappear.
     */
    public static final String LOG = "log";

    /**
     * The main content of a document.
     */
    public static final String MAIN = "main";

    /**
     * A type of live region where non-essential information changes frequently.
     */
    public static final String MARQUEE = "marquee";

    /**
     * Content that represents a mathematical expression.
     */
    public static final String MATH = "math";

    /**
     * A type of widget that offers a list of choices to the user.
     */
    public static final String MENU = "menu";

    /**
     * A presentation of menu that usually remains visible and is usually
     * presented horizontally.
     */
    public static final String MENUBAR = "menubar";

    /**
     * An option in a group of choices contained by a menu or menubar.
     */
    public static final String MENUITEM = "menuitem";

    /**
     * A checkable menuitem that has three possible values: true, false, or
     * mixed.
     */
    public static final String MENUITEMCHECKBOX = "menuitemcheckbox";

    /**
     * A checkable menuitem in a group of menuitemradio roles, only one of which
     * can be checked at a time.
     */
    public static final String MENUITEMRADIO = "menuitemradio";

    /**
     * A collection of navigational elements (usually links) for navigating the
     * document or related documents.
     */
    public static final String NAVIGATION = "navigation";

    /**
     * A section whose content is parenthetic or ancillary to the main content
     * of the resource.
     */
    public static final String NOTE = "note";

    /**
     * A selectable item in a select list.
     */
    public static final String OPTION = "option";

    /**
     * An element whose implicit native role semantics will not be mapped to the
     * accessibility API.
     */
    public static final String PRESENTATION = "presentation";

    /**
     * An element that displays the progress status for tasks that take a long
     * time.
     */
    public static final String PROGRESSBAR = "progressbar";

    /**
     * A checkable input in a group of radio roles, only one of which can be
     * checked at a time.
     */
    public static final String RADIO = "radio";

    /**
     * A group of radio buttons.
     */
    public static final String RADIOGROUP = "radiogroup";

    /**
     * A large perceivable section of a web page or document, that the author
     * feels is important enough to be included in a page summary or table of
     * contents, for example, an area of the page containing live sporting event
     * statistics.
     */
    public static final String REGION = "region";

    /**
     * A row of cells in a grid.
     */
    public static final String ROW = "row";

    /**
     * A group containing one or more row elements in a grid.
     */
    public static final String ROWGROUP = "rowgroup";

    /**
     * A cell containing header information for a row in a grid.
     */
    public static final String ROWHEADER = "rowheader";

    /**
     * A graphical object that controls the scrolling of content within a
     * viewing area, regardless of whether the content is fully displayed within
     * the viewing area.
     */
    public static final String SCROLLBAR = "scrollbar";

    /**
     * A landmark region that contains a collection of items and objects that,
     * as a whole, combine to create a search facility.
     */
    public static final String SEARCH = "search";

    /**
     * A divider that separates and distinguishes sections of content or groups
     * of menuitems.
     */
    public static final String SEPARATOR = "separator";

    /**
     * A user input where the user selects a value from within a given range.
     */
    public static final String SLIDER = "slider";

    /**
     * A form of range that expects the user to select from among discrete
     * choices.
     */
    public static final String SPINBUTTON = "spinbutton";

    /**
     * A container whose content is advisory information for the user but is not
     * important enough to justify an alert, often but not necessarily presented
     * as a status bar.
     */
    public static final String STATUS = "status";

    /**
     * A grouping label providing a mechanism for selecting the tab content that
     * is to be rendered to the user.
     */
    public static final String TAB = "tab";

    /**
     * A list of tab elements, which are references to tabpanel elements.
     */
    public static final String TABLIST = "tablist";

    /**
     * A container for the resources associated with a tab, where each tab is
     * contained in a tablist.
     */
    public static final String TABPANEL = "tabpanel";

    /**
     * Input that allows free-form text as its value.
     */
    public static final String TEXTBOX = "textbox";

    /**
     * A type of live region containing a numerical counter which indicates an
     * amount of elapsed time from a start point, or the time remaining until an
     * end point.
     */
    public static final String TIMER = "timer";

    /**
     * A collection of commonly used function buttons represented in compact
     * visual form.
     */
    public static final String TOOLBAR = "toolbar";

    /**
     * A contextual popup that displays a description for an element.
     */
    public static final String TOOLTIP = "tooltip";

    /**
     * A type of list that may contain sub-level nested groups that can be
     * collapsed and expanded.
     */
    public static final String TREE = "tree";

    /**
     * A grid whose rows can be expanded and collapsed in the same manner as for
     * a tree.
     */
    public static final String TREEGRID = "treegrid";

    /**
     * An option item of a tree. This is an element within a tree that may be
     * expanded or collapsed if it contains a sub-level group of treeitems.
     */
    public static final String TREEITEM = "treeitem";

    {
        super.setAttributeName(AttributeNameConstants.ROLE);
        init();
    }

    /**
     *
     * @param value
     *            the value for the attribute
     * @since 2.0.1
     * @author WFF
     */
    public Role(final String value) {
        setAttributeValue(value);
    }

    /**
     * sets the value for this attribute
     *
     * @param value
     *            the value for the attribute.
     * @since 2.0.1
     * @author WFF
     */
    public void setValue(final String value) {
        super.setAttributeValue(value);
    }

    /**
     * sets the value for this attribute
     *
     * @param updateClient
     *            true to update client browser page if it is available. The
     *            default value is true but it will be ignored if there is no
     *            client browser page.
     * @param value
     *            the value for the attribute.
     * @since 2.1.15
     * @author WFF
     */
    public void setValue(final boolean updateClient, final String value) {
        super.setAttributeValue(updateClient, value);
    }

    /**
     * gets the value of this attribute
     *
     * @return the value of the attribute
     * @since 2.0.1
     * @author WFF
     */
    public String getValue() {
        return super.getAttributeValue();
    }

    /**
     * invokes only once per object
     *
     * @author WFF
     * @since 2.0.1
     */
    protected void init() {
        // to override and use this method
    }

}

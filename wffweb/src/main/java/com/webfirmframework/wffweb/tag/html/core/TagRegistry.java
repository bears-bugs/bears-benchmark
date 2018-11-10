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
 */
package com.webfirmframework.wffweb.tag.html.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.BaseFont;
import com.webfirmframework.wffweb.tag.html.Body;
import com.webfirmframework.wffweb.tag.html.Br;
import com.webfirmframework.wffweb.tag.html.H1;
import com.webfirmframework.wffweb.tag.html.H2;
import com.webfirmframework.wffweb.tag.html.H3;
import com.webfirmframework.wffweb.tag.html.H4;
import com.webfirmframework.wffweb.tag.html.H5;
import com.webfirmframework.wffweb.tag.html.H6;
import com.webfirmframework.wffweb.tag.html.Hr;
import com.webfirmframework.wffweb.tag.html.Html;
import com.webfirmframework.wffweb.tag.html.P;
import com.webfirmframework.wffweb.tag.html.Qfn;
import com.webfirmframework.wffweb.tag.html.TagNameConstants;
import com.webfirmframework.wffweb.tag.html.TitleTag;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.formatting.Abbr;
import com.webfirmframework.wffweb.tag.html.formatting.Address;
import com.webfirmframework.wffweb.tag.html.formatting.B;
import com.webfirmframework.wffweb.tag.html.formatting.Bdo;
import com.webfirmframework.wffweb.tag.html.formatting.BlockQuote;
import com.webfirmframework.wffweb.tag.html.formatting.Cite;
import com.webfirmframework.wffweb.tag.html.formatting.Code;
import com.webfirmframework.wffweb.tag.html.formatting.Del;
import com.webfirmframework.wffweb.tag.html.formatting.Dfn;
import com.webfirmframework.wffweb.tag.html.formatting.Em;
import com.webfirmframework.wffweb.tag.html.formatting.I;
import com.webfirmframework.wffweb.tag.html.formatting.Ins;
import com.webfirmframework.wffweb.tag.html.formatting.Kbd;
import com.webfirmframework.wffweb.tag.html.formatting.Pre;
import com.webfirmframework.wffweb.tag.html.formatting.Q;
import com.webfirmframework.wffweb.tag.html.formatting.S;
import com.webfirmframework.wffweb.tag.html.formatting.Samp;
import com.webfirmframework.wffweb.tag.html.formatting.Small;
import com.webfirmframework.wffweb.tag.html.formatting.Strong;
import com.webfirmframework.wffweb.tag.html.formatting.Sub;
import com.webfirmframework.wffweb.tag.html.formatting.Sup;
import com.webfirmframework.wffweb.tag.html.formatting.U;
import com.webfirmframework.wffweb.tag.html.formatting.Var;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.formsandinputs.FieldSet;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Form;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Input;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Label;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Legend;
import com.webfirmframework.wffweb.tag.html.formsandinputs.OptGroup;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Option;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Select;
import com.webfirmframework.wffweb.tag.html.formsandinputs.TextArea;
import com.webfirmframework.wffweb.tag.html.frames.IFrame;
import com.webfirmframework.wffweb.tag.html.html5.Circle;
import com.webfirmframework.wffweb.tag.html.html5.Data;
import com.webfirmframework.wffweb.tag.html.html5.Ellipse;
import com.webfirmframework.wffweb.tag.html.html5.HGroup;
import com.webfirmframework.wffweb.tag.html.html5.Line;
import com.webfirmframework.wffweb.tag.html.html5.MathTag;
import com.webfirmframework.wffweb.tag.html.html5.Path;
import com.webfirmframework.wffweb.tag.html.html5.Polygon;
import com.webfirmframework.wffweb.tag.html.html5.Polyline;
import com.webfirmframework.wffweb.tag.html.html5.Rect;
import com.webfirmframework.wffweb.tag.html.html5.Source;
import com.webfirmframework.wffweb.tag.html.html5.Svg;
import com.webfirmframework.wffweb.tag.html.html5.Template;
import com.webfirmframework.wffweb.tag.html.html5.Text;
import com.webfirmframework.wffweb.tag.html.html5.Track;
import com.webfirmframework.wffweb.tag.html.html5.Video;
import com.webfirmframework.wffweb.tag.html.html5.audiovideo.Audio;
import com.webfirmframework.wffweb.tag.html.html5.formatting.Bdi;
import com.webfirmframework.wffweb.tag.html.html5.formatting.Mark;
import com.webfirmframework.wffweb.tag.html.html5.formatting.Meter;
import com.webfirmframework.wffweb.tag.html.html5.formatting.Progress;
import com.webfirmframework.wffweb.tag.html.html5.formatting.Rp;
import com.webfirmframework.wffweb.tag.html.html5.formatting.Rt;
import com.webfirmframework.wffweb.tag.html.html5.formatting.Ruby;
import com.webfirmframework.wffweb.tag.html.html5.formatting.Time;
import com.webfirmframework.wffweb.tag.html.html5.formatting.Wbr;
import com.webfirmframework.wffweb.tag.html.html5.formsandinputs.DataList;
import com.webfirmframework.wffweb.tag.html.html5.formsandinputs.KeyGen;
import com.webfirmframework.wffweb.tag.html.html5.formsandinputs.Output;
import com.webfirmframework.wffweb.tag.html.html5.images.Canvas;
import com.webfirmframework.wffweb.tag.html.html5.images.FigCaption;
import com.webfirmframework.wffweb.tag.html.html5.images.Figure;
import com.webfirmframework.wffweb.tag.html.html5.images.Picture;
import com.webfirmframework.wffweb.tag.html.html5.links.Nav;
import com.webfirmframework.wffweb.tag.html.html5.lists.Menu;
import com.webfirmframework.wffweb.tag.html.html5.lists.MenuItem;
import com.webfirmframework.wffweb.tag.html.html5.programming.Embed;
import com.webfirmframework.wffweb.tag.html.html5.stylesandsemantics.Article;
import com.webfirmframework.wffweb.tag.html.html5.stylesandsemantics.Aside;
import com.webfirmframework.wffweb.tag.html.html5.stylesandsemantics.Details;
import com.webfirmframework.wffweb.tag.html.html5.stylesandsemantics.Dialog;
import com.webfirmframework.wffweb.tag.html.html5.stylesandsemantics.Footer;
import com.webfirmframework.wffweb.tag.html.html5.stylesandsemantics.Header;
import com.webfirmframework.wffweb.tag.html.html5.stylesandsemantics.Main;
import com.webfirmframework.wffweb.tag.html.html5.stylesandsemantics.Section;
import com.webfirmframework.wffweb.tag.html.html5.stylesandsemantics.Summary;
import com.webfirmframework.wffweb.tag.html.images.Area;
import com.webfirmframework.wffweb.tag.html.images.Img;
import com.webfirmframework.wffweb.tag.html.images.MapTag;
import com.webfirmframework.wffweb.tag.html.links.A;
import com.webfirmframework.wffweb.tag.html.links.Link;
import com.webfirmframework.wffweb.tag.html.lists.Dd;
import com.webfirmframework.wffweb.tag.html.lists.Dl;
import com.webfirmframework.wffweb.tag.html.lists.Dt;
import com.webfirmframework.wffweb.tag.html.lists.Li;
import com.webfirmframework.wffweb.tag.html.lists.Ol;
import com.webfirmframework.wffweb.tag.html.lists.Ul;
import com.webfirmframework.wffweb.tag.html.metainfo.Base;
import com.webfirmframework.wffweb.tag.html.metainfo.Head;
import com.webfirmframework.wffweb.tag.html.metainfo.Meta;
import com.webfirmframework.wffweb.tag.html.programming.NoScript;
import com.webfirmframework.wffweb.tag.html.programming.ObjectTag;
import com.webfirmframework.wffweb.tag.html.programming.Param;
import com.webfirmframework.wffweb.tag.html.programming.Script;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Span;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.StyleTag;
import com.webfirmframework.wffweb.tag.html.tables.Caption;
import com.webfirmframework.wffweb.tag.html.tables.Col;
import com.webfirmframework.wffweb.tag.html.tables.ColGroup;
import com.webfirmframework.wffweb.tag.html.tables.TBody;
import com.webfirmframework.wffweb.tag.html.tables.TFoot;
import com.webfirmframework.wffweb.tag.html.tables.THead;
import com.webfirmframework.wffweb.tag.html.tables.Table;
import com.webfirmframework.wffweb.tag.html.tables.Td;
import com.webfirmframework.wffweb.tag.html.tables.Th;
import com.webfirmframework.wffweb.tag.html.tables.Tr;

public class TagRegistry {

    public static final Logger LOGGER = Logger
            .getLogger(TagRegistry.class.getName());

    private static List<String> tagNames;

    private static final Set<String> tagNamesSet;

    private static final Map<String, String> TAG_CLASS_NAME_BY_TAG_NAME;

    private static Map<String, Class<?>> tagClassByTagName;

    static {

        final Field[] fields = TagNameConstants.class.getFields();
        final int initialCapacity = fields.length;

        tagClassByTagName = new HashMap<>(initialCapacity);
        TAG_CLASS_NAME_BY_TAG_NAME = new HashMap<>(initialCapacity);

        tagClassByTagName.put(TagNameConstants.A, A.class);
        tagClassByTagName.put(TagNameConstants.ABBR, Abbr.class);
        tagClassByTagName.put(TagNameConstants.ADDRESS, Address.class);
        tagClassByTagName.put(TagNameConstants.AREA, Area.class);
        tagClassByTagName.put(TagNameConstants.ARTICLE, Article.class);
        tagClassByTagName.put(TagNameConstants.ASIDE, Aside.class);
        tagClassByTagName.put(TagNameConstants.AUDIO, Audio.class);
        tagClassByTagName.put(TagNameConstants.B, B.class);
        tagClassByTagName.put(TagNameConstants.BASE, Base.class);
        tagClassByTagName.put(TagNameConstants.BASEFONT, BaseFont.class);
        tagClassByTagName.put(TagNameConstants.BDI, Bdi.class);
        tagClassByTagName.put(TagNameConstants.BDO, Bdo.class);
        tagClassByTagName.put(TagNameConstants.BLOCKQUOTE, BlockQuote.class);
        tagClassByTagName.put(TagNameConstants.BODY, Body.class);
        tagClassByTagName.put(TagNameConstants.BR, Br.class);
        tagClassByTagName.put(TagNameConstants.BUTTON, Button.class);
        tagClassByTagName.put(TagNameConstants.CANVAS, Canvas.class);
        tagClassByTagName.put(TagNameConstants.CAPTION, Caption.class);
        tagClassByTagName.put(TagNameConstants.CITE, Cite.class);
        tagClassByTagName.put(TagNameConstants.CODE, Code.class);
        tagClassByTagName.put(TagNameConstants.COL, Col.class);
        tagClassByTagName.put(TagNameConstants.COLGROUP, ColGroup.class);
        tagClassByTagName.put(TagNameConstants.DATA, Data.class);
        tagClassByTagName.put(TagNameConstants.DATALIST, DataList.class);
        tagClassByTagName.put(TagNameConstants.DD, Dd.class);
        tagClassByTagName.put(TagNameConstants.DEL, Del.class);
        tagClassByTagName.put(TagNameConstants.DETAILS, Details.class);
        tagClassByTagName.put(TagNameConstants.DFN, Dfn.class);
        tagClassByTagName.put(TagNameConstants.DIALOG, Dialog.class);
        tagClassByTagName.put(TagNameConstants.DIV, Div.class);
        tagClassByTagName.put(TagNameConstants.DL, Dl.class);
        tagClassByTagName.put(TagNameConstants.DT, Dt.class);
        tagClassByTagName.put(TagNameConstants.EM, Em.class);
        tagClassByTagName.put(TagNameConstants.EMBED, Embed.class);
        tagClassByTagName.put(TagNameConstants.FIELDSET, FieldSet.class);
        tagClassByTagName.put(TagNameConstants.FIGCAPTION, FigCaption.class);
        tagClassByTagName.put(TagNameConstants.FIGURE, Figure.class);
        tagClassByTagName.put(TagNameConstants.FOOTER, Footer.class);
        tagClassByTagName.put(TagNameConstants.FORM, Form.class);
        tagClassByTagName.put(TagNameConstants.H1, H1.class);
        tagClassByTagName.put(TagNameConstants.H2, H2.class);
        tagClassByTagName.put(TagNameConstants.H3, H3.class);
        tagClassByTagName.put(TagNameConstants.H4, H4.class);
        tagClassByTagName.put(TagNameConstants.H5, H5.class);

        tagClassByTagName.put(TagNameConstants.HEAD, Head.class);
        tagClassByTagName.put(TagNameConstants.HEADER, Header.class);
        tagClassByTagName.put(TagNameConstants.HGROUP, HGroup.class);
        tagClassByTagName.put(TagNameConstants.HR, Hr.class);
        tagClassByTagName.put(TagNameConstants.HTML, Html.class);
        tagClassByTagName.put(TagNameConstants.I, I.class);
        tagClassByTagName.put(TagNameConstants.IFRAME, IFrame.class);
        tagClassByTagName.put(TagNameConstants.IMG, Img.class);
        tagClassByTagName.put(TagNameConstants.INPUT, Input.class);
        tagClassByTagName.put(TagNameConstants.INS, Ins.class);
        tagClassByTagName.put(TagNameConstants.KBD, Kbd.class);
        tagClassByTagName.put(TagNameConstants.KEYGEN, KeyGen.class);
        tagClassByTagName.put(TagNameConstants.LABEL, Label.class);
        tagClassByTagName.put(TagNameConstants.LEGEND, Legend.class);
        tagClassByTagName.put(TagNameConstants.LI, Li.class);
        tagClassByTagName.put(TagNameConstants.LINK, Link.class);
        tagClassByTagName.put(TagNameConstants.MAIN, Main.class);
        tagClassByTagName.put(TagNameConstants.MAP, MapTag.class);
        tagClassByTagName.put(TagNameConstants.MARK, Mark.class);
        tagClassByTagName.put(TagNameConstants.MATH, MathTag.class);
        tagClassByTagName.put(TagNameConstants.MENU, Menu.class);
        tagClassByTagName.put(TagNameConstants.MENUITEM, MenuItem.class);
        tagClassByTagName.put(TagNameConstants.META, Meta.class);
        tagClassByTagName.put(TagNameConstants.METER, Meter.class);
        tagClassByTagName.put(TagNameConstants.NAV, Nav.class);
        tagClassByTagName.put(TagNameConstants.NOSCRIPT, NoScript.class);
        tagClassByTagName.put(TagNameConstants.OBJECT, ObjectTag.class);
        tagClassByTagName.put(TagNameConstants.OL, Ol.class);
        tagClassByTagName.put(TagNameConstants.OPTGROUP, OptGroup.class);
        tagClassByTagName.put(TagNameConstants.OPTION, Option.class);
        tagClassByTagName.put(TagNameConstants.OUTPUT, Output.class);
        tagClassByTagName.put(TagNameConstants.P, P.class);
        tagClassByTagName.put(TagNameConstants.PARAM, Param.class);
        tagClassByTagName.put(TagNameConstants.PRE, Pre.class);
        tagClassByTagName.put(TagNameConstants.PROGRESS, Progress.class);
        tagClassByTagName.put(TagNameConstants.Q, Q.class);
        tagClassByTagName.put(TagNameConstants.QFN, Qfn.class);
        tagClassByTagName.put(TagNameConstants.RP, Rp.class);
        tagClassByTagName.put(TagNameConstants.RT, Rt.class);
        tagClassByTagName.put(TagNameConstants.RUBY, Ruby.class);
        tagClassByTagName.put(TagNameConstants.S, S.class);
        tagClassByTagName.put(TagNameConstants.SAMP, Samp.class);
        tagClassByTagName.put(TagNameConstants.SCRIPT, Script.class);
        tagClassByTagName.put(TagNameConstants.SECTION, Section.class);
        tagClassByTagName.put(TagNameConstants.SELECT, Select.class);
        tagClassByTagName.put(TagNameConstants.SMALL, Small.class);
        tagClassByTagName.put(TagNameConstants.SOURCE, Source.class);
        tagClassByTagName.put(TagNameConstants.SPAN, Span.class);
        tagClassByTagName.put(TagNameConstants.STRONG, Strong.class);
        tagClassByTagName.put(TagNameConstants.STYLE, StyleTag.class);
        tagClassByTagName.put(TagNameConstants.SUB, Sub.class);
        tagClassByTagName.put(TagNameConstants.SUMMARY, Summary.class);
        tagClassByTagName.put(TagNameConstants.SUP, Sup.class);
        tagClassByTagName.put(TagNameConstants.SVG, Svg.class);
        tagClassByTagName.put(TagNameConstants.TABLE, Table.class);
        tagClassByTagName.put(TagNameConstants.TBODY, TBody.class);
        tagClassByTagName.put(TagNameConstants.TD, Td.class);
        tagClassByTagName.put(TagNameConstants.TEMPLATE, Template.class);
        tagClassByTagName.put(TagNameConstants.TEXTAREA, TextArea.class);
        tagClassByTagName.put(TagNameConstants.TFOOT, TFoot.class);
        tagClassByTagName.put(TagNameConstants.TH, Th.class);
        tagClassByTagName.put(TagNameConstants.THEAD, THead.class);
        tagClassByTagName.put(TagNameConstants.TIME, Time.class);
        tagClassByTagName.put(TagNameConstants.TITLE_TAG, TitleTag.class);
        tagClassByTagName.put(TagNameConstants.TR, Tr.class);
        tagClassByTagName.put(TagNameConstants.TRACK, Track.class);
        tagClassByTagName.put(TagNameConstants.U, U.class);
        tagClassByTagName.put(TagNameConstants.UL, Ul.class);
        tagClassByTagName.put(TagNameConstants.VAR, Var.class);
        tagClassByTagName.put(TagNameConstants.VIDEO, Video.class);
        tagClassByTagName.put(TagNameConstants.WBR, Wbr.class);
        tagClassByTagName.put(TagNameConstants.H6, H6.class);
        tagClassByTagName.put(TagNameConstants.PICTURE, Picture.class);
        tagClassByTagName.put(TagNameConstants.RECT, Rect.class);
        tagClassByTagName.put(TagNameConstants.CIRCLE, Circle.class);
        tagClassByTagName.put(TagNameConstants.ELLIPSE, Ellipse.class);
        tagClassByTagName.put(TagNameConstants.LINE, Line.class);
        tagClassByTagName.put(TagNameConstants.POLYGON, Polygon.class);
        tagClassByTagName.put(TagNameConstants.POLYLINE, Polyline.class);
        tagClassByTagName.put(TagNameConstants.PATH, Path.class);
        tagClassByTagName.put(TagNameConstants.TEXT, Text.class);

        for (final Entry<String, Class<?>> entry : tagClassByTagName
                .entrySet()) {
            TAG_CLASS_NAME_BY_TAG_NAME.put(entry.getKey(),
                    entry.getValue().getSimpleName());
        }

        tagNames = new ArrayList<>(initialCapacity);
        tagNamesSet = new HashSet<>(initialCapacity);

        tagNamesSet.addAll(TAG_CLASS_NAME_BY_TAG_NAME.keySet());
        tagNames.addAll(tagNamesSet);

        for (final Field field : fields) {
            try {
                final String tagName = field.get(null).toString();
                tagNamesSet.add(tagName);
            } catch (final Exception e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }

        tagNames.addAll(tagNamesSet);

        Collections.sort(tagNames, (o1, o2) -> {

            final Integer length1 = o1.length();
            final Integer length2 = o2.length();

            return length1.compareTo(length2);
        });
    }

    /**
     * @return the list of tag names sorted in the ascending order of its length
     * @since 1.1.3
     * @author WFF
     */
    public static List<String> getTagNames() {
        return tagNames;
    }

    /**
     *
     * @param tagNamesToRegister
     *            the tag names to register , eg:- register("new-tag1",
     *            "new-tag2")
     * @since 1.1.3
     * @author WFF
     */
    public static void register(final String... tagNamesToRegister) {

        final Set<String> tagNamesWithoutDuplicates = new HashSet<>(
                tagNamesToRegister.length);
        Collections.addAll(tagNamesWithoutDuplicates, tagNamesToRegister);

        tagNamesSet.addAll(tagNamesWithoutDuplicates);

        tagNames.clear();
        tagNames.addAll(tagNamesSet);

        Collections.sort(tagNames, (o1, o2) -> {

            final Integer length1 = o1.length();
            final Integer length2 = o2.length();

            return length1.compareTo(length2);
        });

    }

    /**
     * @return a map containing tag name as key and value as tag class name
     *         without package name
     * @since 1.1.3
     * @author WFF
     */
    public static Map<String, String> getTagClassNameByTagName() {
        return TAG_CLASS_NAME_BY_TAG_NAME;
    }

    /**
     * Loads all tag classes.
     *
     * @since 2.1.13
     * @author WFF
     */
    public static void loadAllTagClasses() {

        final Map<String, Class<?>> unloadedClasses = new HashMap<>();

        for (final Entry<String, Class<?>> entry : tagClassByTagName
                .entrySet()) {
            try {

                Class.forName(entry.getValue().getName());

            } catch (final ClassNotFoundException e) {
                unloadedClasses.put(entry.getKey(), entry.getValue());
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning("Could not load tag class "
                            + entry.getValue().getName());
                }

            }
        }
        tagClassByTagName.clear();
        if (unloadedClasses.size() > 0) {
            tagClassByTagName.putAll(unloadedClasses);
        } else {
            tagClassByTagName = null;
        }
    }

    // only for testing purpose
    static void test() throws InstantiationException, IllegalAccessException,
            NoSuchFieldException, SecurityException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException,
            InvalidValueException {
        for (final Entry<String, Class<?>> each : tagClassByTagName
                .entrySet()) {
            final String expectedTagName = each.getKey();
            final Class<?> tagClass = each.getValue();
            final AbstractHtml newInstance = (AbstractHtml) tagClass
                    .getConstructor(AbstractHtml.class,
                            AbstractAttribute[].class)
                    .newInstance(null, new AbstractAttribute[0]);
            final String actualTagName = newInstance.getTagName();

            if (!expectedTagName.equals(actualTagName)) {
                throw new InvalidValueException("expectedTagName: "
                        + expectedTagName + " actualTagName: " + actualTagName);
            }
        }
    }

}

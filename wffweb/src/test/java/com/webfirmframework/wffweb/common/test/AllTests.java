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
package com.webfirmframework.wffweb.common.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.webfirmframework.wffweb.common.performance.test.CodePerformanceTest;
import com.webfirmframework.wffweb.css.BackgroundColorTest;
import com.webfirmframework.wffweb.css.BackgroundImageTest;
import com.webfirmframework.wffweb.css.BorderBottomColorTest;
import com.webfirmframework.wffweb.css.BorderBottomTest;
import com.webfirmframework.wffweb.css.BorderBottomWidthTest;
import com.webfirmframework.wffweb.css.BorderColorCssValuesTest;
import com.webfirmframework.wffweb.css.BorderColorTest;
import com.webfirmframework.wffweb.css.BorderLeftColorTest;
import com.webfirmframework.wffweb.css.BorderLeftTest;
import com.webfirmframework.wffweb.css.BorderLeftWidthTest;
import com.webfirmframework.wffweb.css.BorderRightColorTest;
import com.webfirmframework.wffweb.css.BorderRightTest;
import com.webfirmframework.wffweb.css.BorderRightWidthTest;
import com.webfirmframework.wffweb.css.BorderSpacingTest;
import com.webfirmframework.wffweb.css.BorderTest;
import com.webfirmframework.wffweb.css.BorderTopColorTest;
import com.webfirmframework.wffweb.css.BorderTopTest;
import com.webfirmframework.wffweb.css.BorderTopWidthTest;
import com.webfirmframework.wffweb.css.BorderWidthTest;
import com.webfirmframework.wffweb.css.BottomTest;
import com.webfirmframework.wffweb.css.ColorTest;
import com.webfirmframework.wffweb.css.CssPropertyEnumTests;
import com.webfirmframework.wffweb.css.CursorTest;
import com.webfirmframework.wffweb.css.FontFamilyTest;
import com.webfirmframework.wffweb.css.FontSizeTest;
import com.webfirmframework.wffweb.css.FontTest;
import com.webfirmframework.wffweb.css.HeightCssTest;
import com.webfirmframework.wffweb.css.HslCssValueTest;
import com.webfirmframework.wffweb.css.HslaCssValueTest;
import com.webfirmframework.wffweb.css.LeftTest;
import com.webfirmframework.wffweb.css.LetterSpacingTest;
import com.webfirmframework.wffweb.css.LineHeightTest;
import com.webfirmframework.wffweb.css.ListStyleImageTest;
import com.webfirmframework.wffweb.css.MarginBottomTest;
import com.webfirmframework.wffweb.css.MarginLeftTest;
import com.webfirmframework.wffweb.css.MarginRightTest;
import com.webfirmframework.wffweb.css.MarginTest;
import com.webfirmframework.wffweb.css.MarginTopTest;
import com.webfirmframework.wffweb.css.OutlineColorTest;
import com.webfirmframework.wffweb.css.OutlineOffsetTest;
import com.webfirmframework.wffweb.css.OutlineTest;
import com.webfirmframework.wffweb.css.OutlineWidthTest;
import com.webfirmframework.wffweb.css.PaddingBottomTest;
import com.webfirmframework.wffweb.css.PaddingLeftTest;
import com.webfirmframework.wffweb.css.PaddingRightTest;
import com.webfirmframework.wffweb.css.PaddingTest;
import com.webfirmframework.wffweb.css.PaddingTopTest;
import com.webfirmframework.wffweb.css.RgbCssValueTest;
import com.webfirmframework.wffweb.css.RgbaCssValueTest;
import com.webfirmframework.wffweb.css.RightTest;
import com.webfirmframework.wffweb.css.SrcCssPropertyTest;
import com.webfirmframework.wffweb.css.TopTest;
import com.webfirmframework.wffweb.css.UrlCss3ValueTest;
import com.webfirmframework.wffweb.css.WidthCssTest;
import com.webfirmframework.wffweb.css.WordSpacingTest;
import com.webfirmframework.wffweb.css.css3.AlignContentTest;
import com.webfirmframework.wffweb.css.css3.AnimationIterationCountTest;
import com.webfirmframework.wffweb.css.css3.BackgroundSizeTest;
import com.webfirmframework.wffweb.css.css3.BorderImageOutsetTest;
import com.webfirmframework.wffweb.css.css3.BorderImageRepeatTest;
import com.webfirmframework.wffweb.css.css3.BorderImageSliceTest;
import com.webfirmframework.wffweb.css.css3.BorderImageSourceTest;
import com.webfirmframework.wffweb.css.css3.BorderImageWidthTest;
import com.webfirmframework.wffweb.css.css3.ColumnCountTest;
import com.webfirmframework.wffweb.css.css3.ColumnGapTest;
import com.webfirmframework.wffweb.css.css3.ColumnRuleColorTest;
import com.webfirmframework.wffweb.css.css3.ColumnRuleTest;
import com.webfirmframework.wffweb.css.css3.ColumnRuleWidthTest;
import com.webfirmframework.wffweb.css.css3.ColumnWidthTest;
import com.webfirmframework.wffweb.css.css3.ColumnsTest;
import com.webfirmframework.wffweb.css.css3.FlexBasisTest;
import com.webfirmframework.wffweb.css.css3.FlexGrowTest;
import com.webfirmframework.wffweb.css.css3.FlexShrinkTest;
import com.webfirmframework.wffweb.css.css3.FlexTest;
import com.webfirmframework.wffweb.css.css3.FontSizeAdjustTest;
import com.webfirmframework.wffweb.css.css3.IconTest;
import com.webfirmframework.wffweb.css.css3.MozBackgroundSizeTest;
import com.webfirmframework.wffweb.css.css3.MozColumnCountTest;
import com.webfirmframework.wffweb.css.css3.MozColumnGapTest;
import com.webfirmframework.wffweb.css.css3.MozColumnRuleColorTest;
import com.webfirmframework.wffweb.css.css3.MozColumnRuleTest;
import com.webfirmframework.wffweb.css.css3.MozColumnRuleWidthTest;
import com.webfirmframework.wffweb.css.css3.MozColumnWidthTest;
import com.webfirmframework.wffweb.css.css3.MozFlexBasisTest;
import com.webfirmframework.wffweb.css.css3.MozFlexGrowTest;
import com.webfirmframework.wffweb.css.css3.MozFlexShrinkTest;
import com.webfirmframework.wffweb.css.css3.MozFlexTest;
import com.webfirmframework.wffweb.css.css3.MsFlexTest;
import com.webfirmframework.wffweb.css.css3.OBackgroundSizeTest;
import com.webfirmframework.wffweb.css.css3.OpacityTest;
import com.webfirmframework.wffweb.css.css3.PerspectiveOriginTest;
import com.webfirmframework.wffweb.css.css3.PerspectiveTest;
import com.webfirmframework.wffweb.css.css3.WebkitBackgroundSizeTest;
import com.webfirmframework.wffweb.css.css3.WebkitColumnCountTest;
import com.webfirmframework.wffweb.css.css3.WebkitColumnGapTest;
import com.webfirmframework.wffweb.css.css3.WebkitColumnRuleColorTest;
import com.webfirmframework.wffweb.css.css3.WebkitColumnRuleTest;
import com.webfirmframework.wffweb.css.css3.WebkitColumnRuleWidthTest;
import com.webfirmframework.wffweb.css.css3.WebkitColumnSpanTest;
import com.webfirmframework.wffweb.css.css3.WebkitColumnWidthTest;
import com.webfirmframework.wffweb.css.css3.WebkitFlexBasisTest;
import com.webfirmframework.wffweb.css.css3.WebkitFlexGrowTest;
import com.webfirmframework.wffweb.css.css3.WebkitFlexShrinkTest;
import com.webfirmframework.wffweb.css.css3.WebkitFlexTest;
import com.webfirmframework.wffweb.css.file.CssFileTest;
import com.webfirmframework.wffweb.js.JsUtilTest;
import com.webfirmframework.wffweb.streamer.WffBinaryMessageOutputStreamerTest;
import com.webfirmframework.wffweb.tag.html.AbstractHtmlRepositoryTest;
import com.webfirmframework.wffweb.tag.html.AbstractHtmlTest;
import com.webfirmframework.wffweb.tag.html.HrTest;
import com.webfirmframework.wffweb.tag.html.HtmlTest;
import com.webfirmframework.wffweb.tag.html.attribute.CheckedTest;
import com.webfirmframework.wffweb.tag.html.attribute.RelTest;
import com.webfirmframework.wffweb.tag.html.attribute.SelectedTest;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttributeTest;
import com.webfirmframework.wffweb.tag.html.attribute.core.AttributeRegistryTest;
import com.webfirmframework.wffweb.tag.html.attribute.core.AttributeUtilTest;
import com.webfirmframework.wffweb.tag.html.attribute.global.ClassAttributeTest;
import com.webfirmframework.wffweb.tag.html.attribute.global.StyleTest;
import com.webfirmframework.wffweb.tag.html.core.TagRegistryTest;
import com.webfirmframework.wffweb.tag.html.formsandinputs.FormTest;
import com.webfirmframework.wffweb.tag.html.formsandinputs.InputTest;
import com.webfirmframework.wffweb.tag.html.formsandinputs.TextAreaTest;
import com.webfirmframework.wffweb.tag.html.html5.attribute.AutoCompleteTest;
import com.webfirmframework.wffweb.tag.html.images.ImgTest;
import com.webfirmframework.wffweb.tag.html.model.AbstractHtml5SharedObjectTest;
import com.webfirmframework.wffweb.tag.htmlwff.BlankTest;
import com.webfirmframework.wffweb.tag.htmlwff.NoTagTest;
import com.webfirmframework.wffweb.tag.repository.TagRepositoryTest;
import com.webfirmframework.wffweb.util.CssLengthUtilTest;
import com.webfirmframework.wffweb.util.CssValueUtilTest;
import com.webfirmframework.wffweb.util.HashUtilTest;
import com.webfirmframework.wffweb.util.ObjectUtilTest;
import com.webfirmframework.wffweb.util.StringUtilTest;
import com.webfirmframework.wffweb.util.WffBinaryMessageUtilTest;
import com.webfirmframework.wffweb.wffbm.data.WffBMObjectArrayTest;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
@RunWith(Suite.class)
@SuiteClasses({ HtmlTest.class, TagPrintTest.class, CssPropertyEnumTests.class,
        AlignContentTest.class, StyleTest.class, CursorTest.class,
        ListStyleImageTest.class, WordSpacingTest.class,
        BorderBottomWidthTest.class, BorderLeftWidthTest.class,
        BorderRightWidthTest.class, BorderTopWidthTest.class,
        ColumnRuleWidthTest.class, WebkitColumnRuleWidthTest.class,
        MozColumnRuleWidthTest.class, ColumnWidthTest.class,
        WebkitColumnWidthTest.class, MozColumnWidthTest.class,
        ColumnRuleColorTest.class, WebkitColumnRuleColorTest.class,
        MozColumnRuleColorTest.class, ColumnRuleTest.class,
        MozColumnRuleTest.class, WebkitColumnRuleTest.class,
        BackgroundColorTest.class, BorderBottomColorTest.class,
        BorderTopColorTest.class, BorderLeftColorTest.class,
        BorderRightColorTest.class, BorderColorTest.class, ColorTest.class,
        OutlineColorTest.class, OutlineWidthTest.class, OutlineOffsetTest.class,
        BorderWidthTest.class, WidthCssTest.class,
        BorderColorCssValuesTest.class, HslCssValueTest.class,
        RgbCssValueTest.class, HslaCssValueTest.class, RgbaCssValueTest.class,
        BorderTest.class, PaddingTopTest.class, PaddingRightTest.class,
        PaddingBottomTest.class, PaddingLeftTest.class, PaddingTest.class,
        StringUtilTest.class, CssValueUtilTest.class, BorderTopTest.class,
        BorderRightTest.class, BorderBottomTest.class, BorderLeftTest.class,
        MarginTopTest.class, MarginRightTest.class, MarginBottomTest.class,
        MarginLeftTest.class, MarginTest.class, OutlineTest.class,
        TopTest.class, RightTest.class, BottomTest.class, LeftTest.class,
        ColumnGapTest.class, MozColumnGapTest.class, WebkitColumnGapTest.class,
        LetterSpacingTest.class, LineHeightTest.class, BorderSpacingTest.class,
        BackgroundSizeTest.class, WebkitBackgroundSizeTest.class,
        MozBackgroundSizeTest.class, OBackgroundSizeTest.class,
        OpacityTest.class, PerspectiveTest.class, PerspectiveOriginTest.class,
        BackgroundImageTest.class, IconTest.class, FlexBasisTest.class,
        WebkitFlexBasisTest.class, MozFlexBasisTest.class,
        AnimationIterationCountTest.class, FlexGrowTest.class,
        WebkitFlexGrowTest.class, MozFlexGrowTest.class, FlexShrinkTest.class,
        MozFlexShrinkTest.class, WebkitFlexShrinkTest.class,
        FontSizeAdjustTest.class, ColumnCountTest.class,
        MozColumnCountTest.class, WebkitColumnCountTest.class, FlexTest.class,
        WebkitFlexTest.class, MozFlexTest.class, MsFlexTest.class,
        FontFamilyTest.class, FontSizeTest.class, FontTest.class,
        ColumnsTest.class, CssLengthUtilTest.class, ObjectUtilTest.class,
        BorderImageRepeatTest.class, BorderImageWidthTest.class,
        BorderImageOutsetTest.class, BorderImageSliceTest.class,
        BorderImageSourceTest.class, WffBinaryMessageUtilTest.class,
        ColumnCountTest.class, WebkitColumnSpanTest.class,
        ClassAttributeTest.class, ImgTest.class, HrTest.class, InputTest.class,
        CssFileTest.class, WffBinaryMessageOutputStreamerTest.class,
        NoTagTest.class, BlankTest.class, TagRegistryTest.class,
        AttributeRegistryTest.class, AttributeUtilTest.class,
        AbstractHtmlTest.class, AbstractAttributeTest.class, JsUtilTest.class,
        TextAreaTest.class, SelectedTest.class, CheckedTest.class,
        FormTest.class, TagRepositoryTest.class, SrcCssPropertyTest.class,
        UrlCss3ValueTest.class, WffBMObjectArrayTest.class, RelTest.class,
        AbstractHtml5SharedObjectTest.class, HeightCssTest.class,
        AutoCompleteTest.class, AbstractHtmlRepositoryTest.class,
        CodePerformanceTest.class, HashUtilTest.class,
        AbstractHtml5SharedObjectTest.class})
public class AllTests {

}

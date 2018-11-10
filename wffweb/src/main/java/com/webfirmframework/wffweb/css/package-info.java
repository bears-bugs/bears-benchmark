/**
 * @author WFF
 */
// @formatter:off
/*
 *
 * {@link http://www.w3schools.com/cssref/css3_pr_align-content.asp}
 * <br>
 *CSS Properties :-<br>
 *+ align-content: stretch|center|flex-start|flex-end|space-between|space-around|initial|inherit;<br>
 *+ align-items: stretch|center|flex-start|flex-end|baseline|initial|inherit;<br>
 *+ align-self: auto|stretch|center|flex-start|flex-end|baseline|initial|inherit; <br>
 *- animation: name duration timing-function delay iteration-count direction fill-mode play-state; <br>
 *- animation-delay: time|initial|inherit;<br>
 *+ animation-direction: normal|reverse|alternate|alternate-reverse|initial|inherit; <br>
 *+ -webkit-animation-direction: normal|reverse|alternate|alternate-reverse|initial|inherit; <br>
 *- animation-duration: time|initial|inherit; <br>
 *+ animation-fill-mode: none|forwards|backwards|both|initial|inherit; <br>
 *+ -webkit-animation-fill-mode: none|forwards|backwards|both|initial|inherit; <br>
 *+ animation-iteration-count: number|infinite|initial|inherit;<br>
 *- animation-name: keyframename|none|initial|inherit; <br>
 *+ animation-play-state: paused|running|initial|inherit;<br>
 *+ -webkit-animation-play-state: paused|running|initial|inherit;<br>
 *- animation-timing-function: linear|ease|ease-in|ease-out|cubic-bezier(n,n,n,n)|initial|inherit;<br>
 *+ backface-visibility: visible|hidden|initial|inherit; <br>
 *- background: color position/size repeat origin clip attachment image|initial|inherit; <br>
 *+ background-attachment: scroll|fixed|local|initial|inherit;<br>
 *+ background-clip: border-box|padding-box|content-box|initial|inherit; <br>
 *+ background-color: color|transparent|initial|inherit; <br>
 *+ background-image: url|none|initial|inherit; <br>
 *+ background-origin: padding-box|border-box|content-box|initial|inherit; <br>
 *- background-position: left top|left center|left bottom|right top|right center|right bottom|center top|center center|center bottom|x% y%|xpos ypos|initial|inherit; <br>
 *+ background-repeat: repeat|repeat-x|repeat-y|no-repeat|initial|inherit; <br>
 *+ background-size: auto|length|cover|contain|initial|inherit;<br>
 *+ border: border-width border-style border-color|initial|inherit; <br>
 *+ border-bottom: border-width border-style border-color|initial|inherit;<br>
 *+ border-bottom-color: color|transparent|initial|inherit;<br>
 *+ border-bottom-left-radius: length|% [length|%]|initial|inherit;<br>
 *+ border-bottom-right-radius: length|% [length|%]|initial|inherit;<br>
 *+ border-bottom-style:none|hidden|dotted|dashed|solid|double|groove|ridge|inset|outset|initial|inherit; <br>
 *+ border-bottom-width: medium|thin|thick|length|initial|inherit; <br>
 *+ border-collapse: separate|collapse|initial|inherit; <br>
 *+ border-color: color|transparent|initial|inherit; <br>
 *- border-image: source slice width outset repeat|initial|inherit;<br>
 *- border-image-outset: length|number|initial|inherit;<br>
 *+ border-image-repeat: stretch|repeat|round|initial|inherit; <br>
 *+ border-image-slice: number|%|fill|initial|inherit; <br>
 *+ border-image-source: none|image|initial|inherit; <br>
 *+ border-image-width: number|%|auto|initial|inherit;<br>
 *- border-left: border-width border-style border-color|initial|inherit; <br>
 *+ border-left-color: color|transparent|initial|inherit;<br>
 *+ border-left-style:none|hidden|dotted|dashed|solid|double|groove|ridge|inset|outset|initial|inherit; <br>
 *+ border-left-width: medium|thin|thick|length|initial|inherit; <br>
 *- border-radius: 1-4 length|% / 1-4 length|%|initial|inherit;  <br>
 *- border-right: border-width border-style border-color|initial|inherit; <br>
 *+ border-right-color: color|transparent|initial|inherit;<br>
 *+ border-right-style <br>
 *+ border-right-width: medium|thin|thick|length|initial|inherit;<br>
 *+ border-spacing: length|initial|inherit; <br>
 *+ border-style:none|hidden|dotted|dashed|solid|double|groove|ridge|inset|outset|initial|inherit; <br>
 *- border-top: border-width border-style border-color|initial|inherit; <br>
 *+ border-top-color: color|transparent|initial|inherit;<br>
 *- border-top-left-radius: length|% [length|%]|initial|inherit;<br>
 *- border-top-right-radius: length|% [length|%]|initial|inherit;<br>
 *+ border-top-style <br>
 *+ border-top-width: medium|thin|thick|length|initial|inherit; <br>
 *+ border-width: medium|thin|thick|length|initial|inherit; <br>
 *+ bottom: auto|length|initial|inherit; <br>
 *- box-shadow: none|h-shadow v-shadow blur spread color |inset|initial|inherit;<br>
 *+ box-sizing: content-box|border-box|initial|inherit;     <br>
 *+ caption-side: top|bottom|initial|inherit;<br>
 *+ clear: none|left|right|both|initial|inherit;<br>
 *- clip: auto|shape|initial|inherit; <br>
 *+ color: color|initial|inherit;<br>
 *- column-count: number|auto|initial|inherit;<br>
 *+ column-fill: balance|auto|initial|inherit;<br>
 *+ column-gap: length|normal|initial|inherit; <br>
 *+ column-rule: column-rule-width column-rule-style column-rule-color|initial|inherit; <br>
 *+ column-rule-color: color|initial|inherit;<br>
 *+ -webkit-column-rule-color: color|initial|inherit;<br>
 *+ -moz-column-rule-color: color|initial|inherit;<br>
 *+ column-rule-style: none|hidden|dotted|dashed|solid|double|groove|ridge|inset|outset|initial|inherit; <br>
 *+ column-rule-width: medium|thin|thick|length|initial|inherit;<br>
 *+ -webkit-column-rule-width: medium|thin|thick|length|initial|inherit;<br>
 *+ -moz-column-rule-width: medium|thin|thick|length|initial|inherit;<br>
 *- column-span: 1|all|initial|inherit; <br>
 *+ column-width: auto|length|initial|inherit; <br>
 *+ -webkit-column-width: auto|length|initial|inherit; <br>
 *+ -moz-column-width: auto|length|initial|inherit; <br>
 *+ columns: auto|column-width column-count|initial|inherit; <br>
 *- content: normal|none|counter|attr|string|open-quote|close-quote|no-open-quote|no-close-quote|url|initial|inherit; <br>
 *- counter-increment: none|id|initial|inherit;<br>
 *- counter-reset: none|name number|initial|inherit;<br>
 *+ cursor : alias | all-scroll | auto | cell | context-menu | col-resize | copy | crosshair | default | e-resize | ew-resize | grab | grabbing | help | move | n-resize | ne-resize | nesw-resize | ns-resize | nw-resize | nwse-resize | no-drop | none | not-allowed | pointer | progress | row-resize | s-resize | se-resize | sw-resize | text | URL | vertical-text | w-resize | wait | zoom-in | zoom-out | initial | inherit; <br>
 *+ direction: ltr|rtl|initial|inherit; <br>
 *+ display: inline | block | flex | inline-block | inline-flex | inline-table | list-item | run-in | table | table-caption | table-column-group | table-header-group | table-footer-group | table-row-group | table-cell | table-column | table-row | none | initial | inherit <br>
 *+ empty-cells: show|hide|initial|inherit;<br>
 *- flex: flex-grow flex-shrink flex-basis|auto|initial|inherit;<br>
 *+ flex-basis: number|auto|initial|inherit;<br>
 *+ flex-direction: row|row-reverse|column|column-reverse|initial|inherit; <br>
 *- flex-flow: flex-direction flex-wrap|initial|inherit; <br>
 *- flex-grow: number|initial|inherit; <br>
 *- flex-shrink: number|initial|inherit; <br>
 *- flex-wrap: nowrap|wrap|wrap-reverse|initial|inherit; <br>
 *+ float: none|left|right|initial|inherit; <br>
 *+ font: font-style font-variant font-weight font-size/line-height font-family|caption|icon|menu|message-box|small-caption|status-bar|initial|inherit; <br>
 *@font-face : cannot be used directly<br>
 *+ font-family: font|initial|inherit;<br>
 *+ font-size:medium|xx-small|x-small|small|large|x-large|xx-large|smaller|larger|length|initial|inherit;<br>
 *+ font-size-adjust: number|none|initial|inherit;<br>
 *+ font-stretch: ultra-condensed|extra-condensed|condensed|semi-condensed|normal|semi-expanded|expanded|extra-expanded|ultra-expanded|initial|inherit;<br>
 *+ font-style: normal|italic|oblique|initial|inherit;<br>
 *+ font-variant: normal|small-caps|initial|inherit;<br>
 *+ font-weight: normal|bold|bolder|lighter|number|initial|inherit;<br>
 *+ hanging-punctuation: none|first|last|allow-end|force-end|initial|inherit;<br>
 *+ height: auto|length|initial|inherit;<br>
 *+ icon: auto|URL|initial|inherit;<br>
 *+ justify-content: flex-start|flex-end|center|space-between|space-around|initial|inherit;<br>
 *@keyframes : cannot be used directly<br>
 *+ left: auto|length|initial|inherit;<br>
 *+ letter-spacing: normal|length|initial|inherit;<br>
 *+ line-height: normal|number|length|initial|inherit;<br>
 *+ list-style: list-style-type list-style-position list-style-image|initial|inherit;<br>
 *+ list-style-image: none|url|initial|inherit;<br>
 *+ list-style-position: inside|outside|initial|inherit;<br>
 *+ list-style-type: | disc | armenian | circle | cjk-ideographic | decimal | decimal-leading-zero | georgian | hebrew | hiragana | hiragana-iroha | katakana | katakana-iroha | lower-alpha | lower-greek | lower-latin | lower-roman | none | square | upper-alpha | upper-latin | upper-roman | initial | inherit<br>
 *+ margin: length|auto|initial|inherit;<br>
 *+ margin-bottom: length|auto|initial|inherit;<br>
 *+ margin-left: length|auto|initial|inherit;<br>
 *+ margin-right: length|auto|initial|inherit;<br>
 *+ margin-top: length|auto|initial|inherit;<br>
 *+ max-height<br>
 *+ max-width<br>
 *@media : cannot be used directly<br>
 *+ min-height<br>
 *+ min-width<br>
 *- nav-down: auto|id|target-name|initial|inherit;<br>
 *- nav-index: auto|number|initial|inherit;<br>
 *- nav-left: auto|id|target-name|initial|inherit;<br>
 *- nav-right: auto|id|target-name|initial|inherit;<br>
 *- nav-up: auto|id|target-name|initial|inherit;<br>
 *+ opacity: number|initial|inherit;<br>
 *- order: number|initial|inherit;<br>
 *- outline: outline-color outline-style outline-width|initial|inherit;<br>
 *+ outline-color: invert|color|initial|inherit;<br>
 *+ outline-offset: length|initial|inherit;<br>
 *+ outline-style: none|hidden|dotted|dashed|solid|double|groove|ridge|inset|outset|initial|inherit;<br>
 *+ outline-width: medium|thin|thick|length|initial|inherit;<br>
 *+ overflow: visible|hidden|scroll|auto|initial|inherit;<br>
 *+ overflow-x: visible|hidden|scroll|auto|initial|inherit;<br>
 *+ overflow-y: visible|hidden|scroll|auto|initial|inherit;<br>
 *- padding: length|initial|inherit;<br>
 *- padding-bottom: length|initial|inherit;<br>
 *- padding-left: length|initial|inherit;<br>
 *- padding-right: length|initial|inherit;<br>
 *- padding-top: length|initial|inherit;<br>
 *+ page-break-after: auto|always|avoid|left|right|initial|inherit; (also used in @media type)<br>
 *+ page-break-before: auto|always|avoid|left|right|initial|inherit; (also used in @media type)<br>
 *+ page-break-inside: auto|avoid|initial|inherit; (also used in @media type)<br>
 *+ perspective<br>
 *perspective-origin<br>
 *+ position: static|absolute|fixed|relative|initial|inherit;<br>
 *quotes<br>
 *+ resize: none|both|horizontal|vertical|initial|inherit;<br>
 *+ right<br>
 *tab-size<br>
 *+ table-layout: auto|fixed|initial|inherit;<br>
 *+ text-align: left|right|center|justify|initial|inherit;<br>
 *+ text-align-last: auto|left|right|center|justify|start|end|initial|inherit;<br>
 *+ text-decoration: none|underline|overline|line-through|initial|inherit;<br>
 *text-decoration-color<br>
 *+ text-decoration-line: none|underline|overline|line-through|initial|inherit;<br>
 *+ text-decoration-style: solid|double|dotted|dashed|wavy|initial|inherit;<br>
 *+ -moz-text-decoration-line: none|underline|overline|line-through|initial|inherit;
 *text-indent<br>
 *+ text-justify: auto|inter-word|inter-ideograph|inter-cluster|distribute|kashida|trim|initial|inherit;<br>
 *text-overflow<br>
 *text-shadow<br>
 *+ text-transform: none|capitalize|uppercase|lowercase|initial|inherit;<br>
 *+ top<br>
 *transform<br>
 *transform-origin<br>
 *+ transform-style: flat|preserve-3d|initial|inherit;<br>
 *+ -webkit-transform-style: flat|preserve-3d|initial|inherit;<br>
 *transition<br>
 *transition-delay<br>
 *transition-duration<br>
 *transition-property<br>
 *transition-timing-function<br>
 *+ unicode-bidi: normal|embed|bidi-override|intitial|inherit;<br>
 *vertical-align<br>
 *+ visibility: visible|hidden|collapse|initial|inherit;<br>
 *+ white-space: normal|nowrap|pre|pre-line|pre-wrap|initial|inherit;<br>
 *+ width<br>
 *+ word-break: normal|break-all|keep-all|initial|inherit;<br>
 *+ word-spacing: normal|length|initial|inherit;<br>
 *+ word-wrap: normal|break-word|initial|inherit;<br>
 *z-index<br>
 *
 */
// @formatter:on
package com.webfirmframework.wffweb.css;
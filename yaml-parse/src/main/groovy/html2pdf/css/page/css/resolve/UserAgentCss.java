/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    This program is offered under a commercial and under the AGPL license.
    For commercial licensing, contact us at https://itextpdf.com/sales.  For AGPL licensing, see below.

    AGPL licensing:
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package html2pdf.css.page.css.resolve;

import main.groovy.io.util.ResourceUtil;
import main.groovy.styledxmlparser.css.CssDeclaration;
import main.groovy.styledxmlparser.css.CssStyleSheet;
import main.groovy.styledxmlparser.css.media.MediaDeviceDescription;
import main.groovy.styledxmlparser.css.parse.CssStyleSheetParser;
import main.groovy.styledxmlparser.node.INode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Utilities class to get the styles of a node.
 */
class UserAgentCss {

    /** The path to the default CSS file. */
    private static final String DEFAULT_CSS_PATH = "com/itextpdf/html2pdf/default.css";
    
    /** The default {@link CssStyleSheet} instance. */
    private static final CssStyleSheet defaultCss;

    static {
        CssStyleSheet parsedStylesheet = new CssStyleSheet();
        try {
            parsedStylesheet = CssStyleSheetParser.parse(ResourceUtil.getResourceStream(DEFAULT_CSS_PATH));
        } catch (Exception exc) {
            Logger logger = LoggerFactory.getLogger(UserAgentCss.class);
            logger.error("Error parsing default.css", exc);
        } finally {
            defaultCss = parsedStylesheet;
        }
    }

    /**
     * Gets the styles of a node.
     *
     * @param node the node
     * @return a list of {@link CssDeclaration} values
     */
    public static List<CssDeclaration> getStyles(INode node) {
        return defaultCss.getCssDeclarations(node, MediaDeviceDescription.createDefault());
    }
}

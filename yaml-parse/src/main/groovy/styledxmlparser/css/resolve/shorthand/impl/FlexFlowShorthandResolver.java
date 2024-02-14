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
package styledxmlparser.css.resolve.shorthand.impl;

import com.itextpdf.commons.utils.MessageFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import styledxmlparser.css.CommonCssConstants;
import styledxmlparser.css.CssDeclaration;
import styledxmlparser.css.resolve.CssDefaults;
import styledxmlparser.css.resolve.shorthand.IShorthandResolver;
import styledxmlparser.css.util.CssTypesValidationUtils;
import styledxmlparser.css.validate.CssDeclarationValidationMaster;
import styledxmlparser.logs.StyledXmlParserLogMessageConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FlexFlowShorthandResolver implements IShorthandResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlexFlowShorthandResolver.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CssDeclaration> resolveShorthand(String shorthandExpression) {
        shorthandExpression = shorthandExpression.trim();
        if (CssTypesValidationUtils.isInitialOrInheritOrUnset(shorthandExpression)) {
            return Arrays.asList(
                    new CssDeclaration(CommonCssConstants.FLEX_DIRECTION, shorthandExpression),
                    new CssDeclaration(CommonCssConstants.FLEX_WRAP, shorthandExpression)
            );
        }
        if (CssTypesValidationUtils.containsInitialOrInheritOrUnset(shorthandExpression)) {
            return handleExpressionError(StyledXmlParserLogMessageConstant.INVALID_CSS_PROPERTY_DECLARATION,
                    CommonCssConstants.FLEX_FLOW,
                    shorthandExpression);
        }
        if (shorthandExpression.isEmpty()) {
            return handleExpressionError(StyledXmlParserLogMessageConstant.SHORTHAND_PROPERTY_CANNOT_BE_EMPTY,
                    CommonCssConstants.FLEX_FLOW, shorthandExpression);
        }

        final String[] flexFlowProps = shorthandExpression.split(" ");
        final List<CssDeclaration> resolvedProperties = new ArrayList<>();

        if (1 == flexFlowProps.length) {
            final CssDeclaration flexDirectionDeclaration = new CssDeclaration(CommonCssConstants.FLEX_DIRECTION,
                    flexFlowProps[0]);
            if (CssDeclarationValidationMaster.checkDeclaration(flexDirectionDeclaration)) {
                resolvedProperties.add(flexDirectionDeclaration);
            } else {
                final CssDeclaration flexWrapDeclaration = new CssDeclaration(CommonCssConstants.FLEX_WRAP,
                        flexFlowProps[0]);
                if (CssDeclarationValidationMaster.checkDeclaration(flexWrapDeclaration)) {
                    resolvedProperties.add(flexWrapDeclaration);
                } else {
                    return handleExpressionError(StyledXmlParserLogMessageConstant.INVALID_CSS_PROPERTY_DECLARATION,
                            CommonCssConstants.FLEX_FLOW,
                            shorthandExpression);
                }
            }
        } else if (2 == flexFlowProps.length) {
            CssDeclaration flexDirectionDeclaration = new CssDeclaration(CommonCssConstants.FLEX_DIRECTION,
                    flexFlowProps[0]);
            CssDeclaration flexWrapDeclaration = new CssDeclaration(CommonCssConstants.FLEX_WRAP,
                    flexFlowProps[1]);

            if (CssDeclarationValidationMaster.checkDeclaration(flexDirectionDeclaration)) {
                resolvedProperties.add(flexDirectionDeclaration);
            } else {
                // for some reasons browsers do support flex-wrap flex-direction order
                flexDirectionDeclaration = new CssDeclaration(CommonCssConstants.FLEX_DIRECTION,
                        flexFlowProps[1]);
                flexWrapDeclaration = new CssDeclaration(CommonCssConstants.FLEX_WRAP,
                        flexFlowProps[0]);

                if (CssDeclarationValidationMaster.checkDeclaration(flexDirectionDeclaration)) {
                    resolvedProperties.add(flexDirectionDeclaration);
                } else {
                    return handleExpressionError(StyledXmlParserLogMessageConstant.INVALID_CSS_PROPERTY_DECLARATION,
                            CommonCssConstants.FLEX_DIRECTION,
                            shorthandExpression);
                }
            }
            if (CssDeclarationValidationMaster.checkDeclaration(flexWrapDeclaration)) {
                resolvedProperties.add(flexWrapDeclaration);
            } else {
                return handleExpressionError(StyledXmlParserLogMessageConstant.INVALID_CSS_PROPERTY_DECLARATION,
                        CommonCssConstants.FLEX_WRAP,
                        shorthandExpression);
            }
        } else {
            return handleExpressionError(StyledXmlParserLogMessageConstant.INVALID_CSS_PROPERTY_DECLARATION,
                    CommonCssConstants.FLEX_FLOW,
                    shorthandExpression);
        }

        fillUnresolvedPropertiesWithDefaultValues(resolvedProperties);
        return resolvedProperties;
    }

    private static List<CssDeclaration> handleExpressionError(String logMessage, String attribute,
                                                              String shorthandExpression) {
        LOGGER.warn(MessageFormatUtil.format(logMessage, attribute, shorthandExpression));
        return Collections.<CssDeclaration>emptyList();
    }

    private void fillUnresolvedPropertiesWithDefaultValues(List<CssDeclaration> resolvedProperties) {
        if (!resolvedProperties.stream()
                .anyMatch(property -> property.getProperty().equals(CommonCssConstants.FLEX_DIRECTION))) {
            resolvedProperties.add(new CssDeclaration(CommonCssConstants.FLEX_DIRECTION,
                    CssDefaults.getDefaultValue(CommonCssConstants.FLEX_DIRECTION)));
        }
        if (!resolvedProperties.stream()
                .anyMatch(property -> property.getProperty().equals(CommonCssConstants.FLEX_WRAP))) {
            resolvedProperties.add(new CssDeclaration(CommonCssConstants.FLEX_WRAP,
                    CssDefaults.getDefaultValue(CommonCssConstants.FLEX_WRAP)));
        }
    }
}

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
import com.itextpdf.styledxmlparser.css.util.CssBackgroundUtils.BackgroundPropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import styledxmlparser.css.CommonCssConstants;
import styledxmlparser.css.CssDeclaration;
import styledxmlparser.css.resolve.CssDefaults;
import styledxmlparser.css.resolve.shorthand.IShorthandResolver;
import styledxmlparser.css.resolve.shorthand.ShorthandResolverFactory;
import styledxmlparser.css.util.CssBackgroundUtils;
import styledxmlparser.css.util.CssTypesValidationUtils;
import styledxmlparser.css.util.CssUtils;
import styledxmlparser.css.validate.CssDeclarationValidationMaster;
import styledxmlparser.logs.StyledXmlParserLogMessageConstant;

import java.util.*;

/**
 * {@link IShorthandResolver} implementation for backgrounds.
 */
public class BackgroundShorthandResolver implements IShorthandResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundShorthandResolver.class);

    // With CSS3, you can apply multiple backgrounds to elements. These are layered atop one another
    // with the first background you provide on top and the last background listed in the back. Only
    // the last background can include a background color.

    /* (non-Javadoc)
     * @see com.itextpdf.styledxmlparser.css.resolve.shorthand.IShorthandResolver#resolveShorthand(java.lang.String)
     */
    @Override
    public List<CssDeclaration> resolveShorthand(final String shorthandExpression) {
        if (CssTypesValidationUtils.isInitialOrInheritOrUnset(shorthandExpression)) {
            return Arrays.asList(
                    new CssDeclaration(CommonCssConstants.BACKGROUND_COLOR, shorthandExpression),
                    new CssDeclaration(CommonCssConstants.BACKGROUND_IMAGE, shorthandExpression),
                    new CssDeclaration(CommonCssConstants.BACKGROUND_POSITION, shorthandExpression),
                    new CssDeclaration(CommonCssConstants.BACKGROUND_SIZE, shorthandExpression),
                    new CssDeclaration(CommonCssConstants.BACKGROUND_REPEAT, shorthandExpression),
                    new CssDeclaration(CommonCssConstants.BACKGROUND_ORIGIN, shorthandExpression),
                    new CssDeclaration(CommonCssConstants.BACKGROUND_CLIP, shorthandExpression),
                    new CssDeclaration(CommonCssConstants.BACKGROUND_ATTACHMENT, shorthandExpression)
            );
        }
        if (shorthandExpression.trim().isEmpty()) {
            LOGGER.warn(MessageFormatUtil.format(
                    StyledXmlParserLogMessageConstant.SHORTHAND_PROPERTY_CANNOT_BE_EMPTY, CommonCssConstants.BACKGROUND));
            return new ArrayList<>();
        }

        final List<List<String>> propsList = CssUtils.extractShorthandProperties(shorthandExpression);

        final Map<BackgroundPropertyType, String> resolvedProps = new HashMap<>();
        fillMapWithPropertiesTypes(resolvedProps);
        for (final List<String> props : propsList) {
            if (!processProperties(props, resolvedProps)) {
                return new ArrayList<>();
            }
        }
        if (resolvedProps.get(BackgroundPropertyType.BACKGROUND_COLOR) == null) {
            resolvedProps.put(BackgroundPropertyType.BACKGROUND_COLOR,
                    CommonCssConstants.TRANSPARENT);
        }
        if (!checkProperties(resolvedProps)) {
            return new ArrayList<>();
        }

        return Arrays.asList(
                new CssDeclaration(CssBackgroundUtils.getBackgroundPropertyNameFromType(
                        BackgroundPropertyType.BACKGROUND_COLOR),
                        resolvedProps.get(BackgroundPropertyType.BACKGROUND_COLOR)),
                new CssDeclaration(CssBackgroundUtils.getBackgroundPropertyNameFromType(
                        BackgroundPropertyType.BACKGROUND_IMAGE),
                        resolvedProps.get(BackgroundPropertyType.BACKGROUND_IMAGE)),
                new CssDeclaration(CssBackgroundUtils.getBackgroundPropertyNameFromType(
                        BackgroundPropertyType.BACKGROUND_POSITION),
                        resolvedProps.get(BackgroundPropertyType.BACKGROUND_POSITION)),
                new CssDeclaration(CssBackgroundUtils.getBackgroundPropertyNameFromType(
                        BackgroundPropertyType.BACKGROUND_SIZE),
                        resolvedProps.get(BackgroundPropertyType.BACKGROUND_SIZE)),
                new CssDeclaration(CssBackgroundUtils.getBackgroundPropertyNameFromType(
                        BackgroundPropertyType.BACKGROUND_REPEAT),
                        resolvedProps.get(BackgroundPropertyType.BACKGROUND_REPEAT)),
                new CssDeclaration(CssBackgroundUtils.getBackgroundPropertyNameFromType(
                        BackgroundPropertyType.BACKGROUND_ORIGIN),
                        resolvedProps.get(BackgroundPropertyType.BACKGROUND_ORIGIN)),
                new CssDeclaration(CssBackgroundUtils.getBackgroundPropertyNameFromType(
                        BackgroundPropertyType.BACKGROUND_CLIP),
                        resolvedProps.get(BackgroundPropertyType.BACKGROUND_CLIP)),
                new CssDeclaration(CssBackgroundUtils.getBackgroundPropertyNameFromType(
                        BackgroundPropertyType.BACKGROUND_ATTACHMENT),
                        resolvedProps.get(BackgroundPropertyType.BACKGROUND_ATTACHMENT))
        );
    }

    private static boolean checkProperties(Map<BackgroundPropertyType, String> resolvedProps) {
        for (final Map.Entry<BackgroundPropertyType, String> property : resolvedProps.entrySet()) {
            if (!CssDeclarationValidationMaster.checkDeclaration(new CssDeclaration(
                    CssBackgroundUtils.getBackgroundPropertyNameFromType(property.getKey()), property.getValue()))) {
                LOGGER.warn(MessageFormatUtil.format(
                        StyledXmlParserLogMessageConstant.INVALID_CSS_PROPERTY_DECLARATION, property.getValue()));
                return false;
            }
            final IShorthandResolver resolver = ShorthandResolverFactory
                    .getShorthandResolver(CssBackgroundUtils.getBackgroundPropertyNameFromType(property.getKey()));
            if (resolver != null && resolver.resolveShorthand(property.getValue()).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static void removeSpacesAroundSlash(final List<String> props) {
        for (int i = 0; i < props.size(); ++i) {
            if ("/".equals(props.get(i))) {
                if (i != 0 && i != props.size() - 1) {
                    final String property = props.get(i - 1) + props.get(i) + props.get(i + 1);
                    props.set(i + 1, property);
                    props.remove(i);
                    props.remove(i - 1);
                }
                return;
            }
            if (props.get(i).startsWith("/")) {
                if (i != 0) {
                    final String property = props.get(i - 1) + props.get(i);
                    props.set(i, property);
                    props.remove(i - 1);
                }
                return;
            }
            if (props.get(i).endsWith("/")) {
                if (i != props.size() - 1) {
                    final String property = props.get(i) + props.get(i + 1);
                    props.set(i + 1, property);
                    props.remove(i);
                }
                return;
            }
        }
    }

    private static void fillMapWithPropertiesTypes(
            Map<BackgroundPropertyType, String> resolvedProps) {
        resolvedProps.put(BackgroundPropertyType.BACKGROUND_COLOR, null);
        resolvedProps.put(BackgroundPropertyType.BACKGROUND_IMAGE, null);
        resolvedProps.put(BackgroundPropertyType.BACKGROUND_POSITION, null);
        resolvedProps.put(BackgroundPropertyType.BACKGROUND_SIZE, null);
        resolvedProps.put(BackgroundPropertyType.BACKGROUND_REPEAT, null);
        resolvedProps.put(BackgroundPropertyType.BACKGROUND_ORIGIN, null);
        resolvedProps.put(BackgroundPropertyType.BACKGROUND_CLIP, null);
        resolvedProps.put(BackgroundPropertyType.BACKGROUND_ATTACHMENT, null);
    }

    private static boolean processProperties(List<String> props,
                                             Map<BackgroundPropertyType, String> resolvedProps) {
        if (props.isEmpty()) {
            LOGGER.warn(MessageFormatUtil.format(
                    StyledXmlParserLogMessageConstant.SHORTHAND_PROPERTY_CANNOT_BE_EMPTY, CommonCssConstants.BACKGROUND));
            return false;
        }
        if (resolvedProps.get(BackgroundPropertyType.BACKGROUND_COLOR) != null) {
            LOGGER.warn(StyledXmlParserLogMessageConstant.ONLY_THE_LAST_BACKGROUND_CAN_INCLUDE_BACKGROUND_COLOR);
            return false;
        }
        removeSpacesAroundSlash(props);
        final Set<BackgroundPropertyType> usedTypes = new HashSet<>();
        if (processAllSpecifiedProperties(props, resolvedProps, usedTypes)) {
            fillNotProcessedProperties(resolvedProps, usedTypes);
            return true;
        } else {
            return false;
        }
    }

    private static boolean processAllSpecifiedProperties(List<String> props,
            Map<BackgroundPropertyType, String> resolvedProps,
            Set<BackgroundPropertyType> usedTypes) {
        final List<String> boxValues = new ArrayList<>();
        boolean slashEncountered = false;
        boolean propertyProcessedCorrectly = true;
        for (final String value : props) {
            final int slashCharInd = value.indexOf('/');
            if (slashCharInd > 0 && slashCharInd < value.length() - 1 && !slashEncountered && !value.contains("url(")
                    && !value.contains("device-cmyk(")) {
                slashEncountered = true;
                propertyProcessedCorrectly = processValueWithSlash(value, slashCharInd, resolvedProps, usedTypes);
            } else {
                final BackgroundPropertyType type = CssBackgroundUtils.resolveBackgroundPropertyType(value);
                if (BackgroundPropertyType.BACKGROUND_ORIGIN_OR_CLIP == type) {
                    boxValues.add(value);
                } else {
                    propertyProcessedCorrectly = putPropertyBasedOnType(changePropertyType(type, slashEncountered),
                            value, resolvedProps, usedTypes);
                }
            }
            if (!propertyProcessedCorrectly) {
                return false;
            }
        }
        return addBackgroundClipAndBackgroundOriginBoxValues(boxValues, resolvedProps, usedTypes);
    }

    private static boolean addBackgroundClipAndBackgroundOriginBoxValues(List<String> boxValues,
            Map<BackgroundPropertyType, String> resolvedProps,
            Set<BackgroundPropertyType> usedTypes) {
        if (boxValues.size() == 1) {
            return putPropertyBasedOnType(BackgroundPropertyType.BACKGROUND_CLIP,
                    boxValues.get(0), resolvedProps, usedTypes);
        } else if (boxValues.size() >= 2) {
            for (int i = 0; i < 2; i++) {
                final BackgroundPropertyType type =
                        i == 0 ? BackgroundPropertyType.BACKGROUND_ORIGIN : BackgroundPropertyType.BACKGROUND_CLIP;
                if (!putPropertyBasedOnType(type, boxValues.get(i), resolvedProps, usedTypes)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean processValueWithSlash(String value, int slashCharInd,
                                                 Map<BackgroundPropertyType, String> resolvedProps,
                                                 Set<BackgroundPropertyType> usedTypes) {
        final String value1 = value.substring(0, slashCharInd);
        final BackgroundPropertyType typeBeforeSlash =
                changePropertyType(CssBackgroundUtils.resolveBackgroundPropertyType(value1), false);
        if (typeBeforeSlash != BackgroundPropertyType.BACKGROUND_POSITION &&
                typeBeforeSlash != BackgroundPropertyType.BACKGROUND_POSITION_OR_SIZE) {
            LOGGER.warn(MessageFormatUtil.format(StyledXmlParserLogMessageConstant.UNKNOWN_PROPERTY,
                    CommonCssConstants.BACKGROUND_POSITION, value1));
            return false;
        }

        final String value2 = value.substring(slashCharInd + 1);
        final BackgroundPropertyType typeAfterSlash =
                changePropertyType(CssBackgroundUtils.resolveBackgroundPropertyType(value2), true);
        if (typeAfterSlash != BackgroundPropertyType.BACKGROUND_SIZE &&
                typeAfterSlash != BackgroundPropertyType.BACKGROUND_POSITION_OR_SIZE) {
            LOGGER.warn(MessageFormatUtil.format(StyledXmlParserLogMessageConstant.UNKNOWN_PROPERTY,
                    CommonCssConstants.BACKGROUND_SIZE, value2));
            return false;
        }

        return putPropertyBasedOnType(typeBeforeSlash, value1, resolvedProps, usedTypes) &&
                putPropertyBasedOnType(typeAfterSlash, value2, resolvedProps, usedTypes);
    }

    private static void fillNotProcessedProperties(Map<BackgroundPropertyType, String> resolvedProps,
                                                   final Set<BackgroundPropertyType> usedTypes) {
        for (final BackgroundPropertyType type : new ArrayList<>(resolvedProps.keySet())) {
            if (!usedTypes.contains(type) && type != BackgroundPropertyType.BACKGROUND_COLOR) {
                if (resolvedProps.get(type) == null) {
                    resolvedProps.put(type,
                            CssDefaults.getDefaultValue(CssBackgroundUtils.getBackgroundPropertyNameFromType(type)));
                } else {
                    resolvedProps.put(type, resolvedProps.get(type) + "," +
                            CssDefaults.getDefaultValue(CssBackgroundUtils.getBackgroundPropertyNameFromType(type)));
                }
            }
        }
    }

    private static BackgroundPropertyType changePropertyType(
            BackgroundPropertyType propertyType,
            boolean slashEncountered) {
        if (propertyType == BackgroundPropertyType.BACKGROUND_POSITION_X
                || propertyType == BackgroundPropertyType.BACKGROUND_POSITION_Y) {
            propertyType = BackgroundPropertyType.BACKGROUND_POSITION;
        }
        if (propertyType == BackgroundPropertyType.BACKGROUND_POSITION_OR_SIZE) {
            return slashEncountered ? BackgroundPropertyType.BACKGROUND_SIZE :
                    BackgroundPropertyType.BACKGROUND_POSITION;
        }
        if (propertyType == BackgroundPropertyType.BACKGROUND_SIZE && !slashEncountered) {
            return BackgroundPropertyType.UNDEFINED;
        }
        if (propertyType == BackgroundPropertyType.BACKGROUND_POSITION && slashEncountered) {
            return BackgroundPropertyType.UNDEFINED;
        }
        return propertyType;
    }

    /**
     * Registers a property based on its type.
     *
     * @param type          the property type
     * @param value         the property value
     * @param resolvedProps the resolved properties
     * @param usedTypes     already used types
     * @return false if the property is invalid. True in all other cases
     */
    private static boolean putPropertyBasedOnType(BackgroundPropertyType type, String value,
                                                  Map<BackgroundPropertyType, String> resolvedProps,
                                                  Set<BackgroundPropertyType> usedTypes) {
        if (type == BackgroundPropertyType.UNDEFINED) {
            LOGGER.warn(MessageFormatUtil.format(
                    StyledXmlParserLogMessageConstant.WAS_NOT_ABLE_TO_DEFINE_BACKGROUND_CSS_SHORTHAND_PROPERTIES, value));
            return false;
        }

        if (resolvedProps.get(type) == null) {
            resolvedProps.put(type, value);
        } else if (usedTypes.contains(type)) {
            resolvedProps.put(type, resolvedProps.get(type) + " " + value);
        } else {
            resolvedProps.put(type, resolvedProps.get(type) + "," + value);
        }
        usedTypes.add(type);
        return true;
    }
}

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
package styledxmlparser.css.validate.impl.datatype;

import styledxmlparser.css.util.CssUtils;
import styledxmlparser.css.validate.ICssDataTypeValidator;
import styledxmlparser.css.validate.ICssDeclarationValidator;

import java.util.List;

/**
 * {@link ICssDeclarationValidator} implementation to validate an array of specified element types.
 */
public class ArrayDataTypeValidator implements ICssDataTypeValidator {

    /** The data type validator. */
    private final ICssDataTypeValidator dataTypeValidator;

    /**
     * Creates a new {@link ArrayDataTypeValidator} instance.
     *
     * @param dataTypeValidator the data type validator for each array element
     */
    public ArrayDataTypeValidator(ICssDataTypeValidator dataTypeValidator) {
        this.dataTypeValidator = dataTypeValidator;
    }

    @Override
    public boolean isValid(String objectString) {
        if (objectString == null) {
            return false;
        }
        List<String> values = CssUtils.splitStringWithComma(objectString);
        for (String value : values) {
            if (!dataTypeValidator.isValid(value.trim())) {
                return false;
            }
        }
        return true;
    }
}

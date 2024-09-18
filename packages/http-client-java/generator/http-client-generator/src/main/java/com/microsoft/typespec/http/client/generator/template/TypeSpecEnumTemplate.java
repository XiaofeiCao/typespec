// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.typespec.http.client.generator.template;

import com.microsoft.typespec.http.client.generator.core.template.EnumTemplate;

/**
 * TypeSpec implementation for EnumTemplate.
 */
public class TypeSpecEnumTemplate extends EnumTemplate {
    private static final TypeSpecEnumTemplate INSTANCE = new TypeSpecEnumTemplate();

    public static TypeSpecEnumTemplate getInstance() {
        return INSTANCE;
    }
}

package com.meenah.meenahsignature.bucket;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Bucket {
    PRODUCT_IMAGE("meenahsignature-image-upload");

    private final String name;
}

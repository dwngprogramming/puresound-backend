package com.puresound.backend.util;

import java.text.Normalizer;

public class NormalizeLetterUtil {
    public static String normalizeVietnamese(String input) {
        if (input == null) return "";

        String preNormalize = input.replaceAll("[đĐ]", "d");

        return Normalizer.normalize(preNormalize, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "")
                .trim();
    }
}

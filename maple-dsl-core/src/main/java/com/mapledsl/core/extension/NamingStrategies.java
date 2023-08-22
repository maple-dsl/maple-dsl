package com.mapledsl.core.extension;

import java.util.Locale;

/**
 * @author bofa1ex
 * @since 2023/08/22
 */
public enum NamingStrategies implements NamingStrategy {
    LOWER_CAMEL_CASE {
        @Override
        public String translate(String input, Locale locale) {
            return input;
        }
    },
    UPPER_CAMEL_CASE {
        @Override
        public String translate(String input, Locale locale) {
            if (input == null) return input;
            if (input.isEmpty()) return input;
            char c = input.charAt(0);
            char uc = Character.toUpperCase(c);
            if (c == uc) return input;

            StringBuilder sb = new StringBuilder(input);
            sb.setCharAt(0, uc);
            return sb.toString();
        }
    },
    SNAKE_CASE {
        @Override
        public String translate(String input, Locale locale) {
            if (input == null) return input;
            int length = input.length();
            StringBuilder result = new StringBuilder(length * 2);
            int resultLength = 0;
            boolean wasPrevTranslated = false;

            for (int i = 0; i < length; ++i) {
                char c = input.charAt(i);
                if (i > 0 || c != '_') {
                    if (Character.isUpperCase(c)) {
                        if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_') {
                            result.append('_');
                            ++resultLength;
                        }

                        c = Character.toLowerCase(c);
                        wasPrevTranslated = true;
                    } else {
                        wasPrevTranslated = false;
                    }

                    result.append(c);
                    ++resultLength;
                }
            }

            return resultLength > 0 ? result.toString() : input;
        }
    },
    UPPER_SNAKE_CASE {
        @Override
        public String translate(String input, Locale locale) {
            String translate = SNAKE_CASE.translate(input, locale);
            return translate == null ? null : translate.toUpperCase(locale);
        }
    },
    LOWER_CASE {
        @Override
        public String translate(String input, Locale locale) {
            return input.toLowerCase(locale);
        }
    },
    KEBAB_CASE {
        @Override
        public String translate(String input, Locale locale) {
            return translateLowerCaseWithSeparator(input, '-');
        }
    },
    LOWER_DOT_CASE {
        @Override
        public String translate(String input, Locale locale) {
            return translateLowerCaseWithSeparator(input, '.');
        }
    };

    final String translateLowerCaseWithSeparator(String input, char separator) {
        if (input == null) return input;
        int length = input.length();
        if (length == 0) return input;
        StringBuilder result = new StringBuilder(length + (length >> 1));
        int upperCount = 0;

        for (int i = 0; i < length; ++i) {
            char ch = input.charAt(i);
            char lc = Character.toLowerCase(ch);
            if (lc == ch) {
                if (upperCount > 1) {
                    result.insert(result.length() - 1, separator);
                }

                upperCount = 0;
            } else {
                if (upperCount == 0 && i > 0) {
                    result.append(separator);
                }

                ++upperCount;
            }

            result.append(lc);
        }

        return result.toString();
    }
}
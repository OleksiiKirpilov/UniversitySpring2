package com.example.s1.util;

/**
 * Language code enum
 */
public enum Lang {
        EN, RU;

        public String getName() {
            return name().toLowerCase();
        }

        public static final String LANG_EN_NAME = "en";
        public static final String LANG_RU_NAME = "ru";

}
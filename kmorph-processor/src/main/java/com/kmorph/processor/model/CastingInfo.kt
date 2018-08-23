package com.kmorph.processor.model

import java.util.*

object CastingInfo {
    val upCastingHashMap = hashMapOf<String, ArrayList<String>>()
    val downCastingHashMap = hashMapOf<String, ArrayList<String>>()
    val primitiveWrapperClassHashMap = hashMapOf<String, String>()

    init {
        upCastingHashMap["byte"] = arrayListOf("short", "int", "long", "float", "double");
        upCastingHashMap["short"] = arrayListOf("int", "long", "float", "double");
        upCastingHashMap["char"] = arrayListOf("int", "long", "float", "double");
        upCastingHashMap["int"] = arrayListOf("long", "float", "double");
        upCastingHashMap["long"] = arrayListOf("float", "double");
        upCastingHashMap["float"] = arrayListOf("double");

        downCastingHashMap["double"] = arrayListOf("byte", "short", "char", "int", "long", "float");
        downCastingHashMap["float"] = arrayListOf("byte", "short", "char", "int", "long");
        downCastingHashMap["long"] = arrayListOf("byte", "short", "char", "int");
        downCastingHashMap["int"] = arrayListOf("byte", "short", "char");
        downCastingHashMap["char"] = arrayListOf("byte", "short");
        downCastingHashMap["short"] = arrayListOf("byte", "char");

        primitiveWrapperClassHashMap["double"] = Double::class.javaObjectType.canonicalName
        primitiveWrapperClassHashMap["byte"] = Byte::class.javaObjectType.canonicalName
        primitiveWrapperClassHashMap["short"] = Short::class.javaObjectType.canonicalName
        primitiveWrapperClassHashMap["char"] = Character::class.javaObjectType.canonicalName
        primitiveWrapperClassHashMap["int"] = Integer::class.javaObjectType.canonicalName
        primitiveWrapperClassHashMap["long"] = Long::class.javaObjectType.canonicalName
        primitiveWrapperClassHashMap["float"] = Float::class.javaObjectType.canonicalName
        primitiveWrapperClassHashMap["boolean"] = Boolean::class.javaObjectType.canonicalName
    }
}
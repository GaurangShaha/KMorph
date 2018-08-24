package com.kmorph.processor.model

import java.util.*

object CastingInfo {
    val upCastingHashMap = hashMapOf<String, ArrayList<String>>()
    val downCastingHashMap = hashMapOf<String, ArrayList<String>>()
    val downCastingHashMapForWrapperClass = hashMapOf<String, ArrayList<String>>()
    val upCastingHashMapForWrapperClass = hashMapOf<String, ArrayList<String>>()
    val primitiveWrapperClassHashMap = hashMapOf<String, String>()

    init {
        upCastingHashMap["byte"] = arrayListOf("short", "int", "long", "float", "double")
        upCastingHashMap["short"] = arrayListOf("int", "long", "float", "double")
        upCastingHashMap["char"] = arrayListOf("int", "long", "float", "double")
        upCastingHashMap["int"] = arrayListOf("long", "float", "double")
        upCastingHashMap["long"] = arrayListOf("float", "double")
        upCastingHashMap["float"] = arrayListOf("double")

        upCastingHashMapForWrapperClass[Byte::class.javaObjectType.canonicalName] = arrayListOf(Short::class.javaObjectType.canonicalName, Int::class.javaObjectType.canonicalName, Long::class.javaObjectType.canonicalName, Float::class.javaObjectType.canonicalName, Double::class.javaObjectType.canonicalName)
        upCastingHashMapForWrapperClass[Short::class.javaObjectType.canonicalName] = arrayListOf(Int::class.javaObjectType.canonicalName, Long::class.javaObjectType.canonicalName, Float::class.javaObjectType.canonicalName, Double::class.javaObjectType.canonicalName)
        upCastingHashMapForWrapperClass[Char::class.javaObjectType.canonicalName] = arrayListOf(Int::class.javaObjectType.canonicalName, Long::class.javaObjectType.canonicalName, Float::class.javaObjectType.canonicalName, Double::class.javaObjectType.canonicalName)
        upCastingHashMapForWrapperClass[Int::class.javaObjectType.canonicalName] = arrayListOf(Long::class.javaObjectType.canonicalName, Float::class.javaObjectType.canonicalName, Double::class.javaObjectType.canonicalName)
        upCastingHashMapForWrapperClass[Long::class.javaObjectType.canonicalName] = arrayListOf(Float::class.javaObjectType.canonicalName, Double::class.javaObjectType.canonicalName)
        upCastingHashMapForWrapperClass[Float::class.javaObjectType.canonicalName] = arrayListOf(Double::class.javaObjectType.canonicalName)

        downCastingHashMap["double"] = arrayListOf("byte", "short", "char", "int", "long", "float")
        downCastingHashMap["float"] = arrayListOf("byte", "short", "char", "int", "long")
        downCastingHashMap["long"] = arrayListOf("byte", "short", "char", "int")
        downCastingHashMap["int"] = arrayListOf("byte", "short", "char")
        downCastingHashMap["char"] = arrayListOf("byte", "short")
        downCastingHashMap["short"] = arrayListOf("byte", "char")

        downCastingHashMapForWrapperClass[Double::class.javaObjectType.canonicalName] = arrayListOf(Byte::class.javaObjectType.canonicalName, Short::class.javaObjectType.canonicalName, Char::class.javaObjectType.canonicalName, Int::class.javaObjectType.canonicalName, Long::class.javaObjectType.canonicalName, Float::class.javaObjectType.canonicalName)
        downCastingHashMapForWrapperClass[Float::class.javaObjectType.canonicalName] = arrayListOf(Byte::class.javaObjectType.canonicalName, Short::class.javaObjectType.canonicalName, Char::class.javaObjectType.canonicalName, Int::class.javaObjectType.canonicalName, Long::class.javaObjectType.canonicalName)
        downCastingHashMapForWrapperClass[Long::class.javaObjectType.canonicalName] = arrayListOf(Byte::class.javaObjectType.canonicalName, Short::class.javaObjectType.canonicalName, Char::class.javaObjectType.canonicalName, Int::class.javaObjectType.canonicalName)
        downCastingHashMapForWrapperClass[Int::class.javaObjectType.canonicalName] = arrayListOf(Byte::class.javaObjectType.canonicalName, Short::class.javaObjectType.canonicalName, Char::class.javaObjectType.canonicalName)
        downCastingHashMapForWrapperClass[Char::class.javaObjectType.canonicalName] = arrayListOf(Byte::class.javaObjectType.canonicalName, Short::class.javaObjectType.canonicalName)
        downCastingHashMapForWrapperClass[Short::class.javaObjectType.canonicalName] = arrayListOf(Byte::class.javaObjectType.canonicalName, Char::class.javaObjectType.canonicalName)

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
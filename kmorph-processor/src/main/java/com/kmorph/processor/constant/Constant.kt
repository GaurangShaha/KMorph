package com.kmorph.processor.constant

object Constant {
    const val ERROR_INTERFACE_FOUND = "%s is an interface, only public class can be used with @MorphTo"
    const val ERROR_PRIVATE_CLASS_FOUND = "%s is private class, only public class can be used with @MorphTo"
    const val ERROR_ABSTRACT_CLASS_FOUND = "%s is an abstract class, only non abstract public class can be used with @MorphTo"
    const val ERROR_ENUM_FOUND = "%s is an enum, only public class can be used with @MorphTo"
    const val ERROR_ANNOTATION_TYPE_FOUND = "%s is an annotation, only public class can be used with @MorphTo"
    const val ERROR_PROTECTED_CLASS_FOUND = "%s is protected class, only public class can be used with @MorphTo"
    const val ERROR_PUBLIC_CLASS_NOT_FOUND ="%s is not public class, only public class can be used with @MorphTo"
    const val ERROR_TRANSFORMER_CLASS_NOT_IMPLEMENTED_FIELD_TRANSFORMER_CONTRACT = "%s class should implement interface FieldTransformerContract"
    const val ERROR_INTERFACE_USED_AS_FIELD_TRANSFORMER = "%s in an interface, only public class can be used with @FieldTransformer"
    const val ERROR_ENUM_USED_AS_FIELD_TRANSFORMER = "%s in an enum, only public class can be used with @FieldTransformer"
    const val ERROR_ANNOTATION_TYPE_AS_FIELD_TRANSFORMER = "%s in an annotation, only public class can be used with @FieldTransformer"
    const val ERROR_PRIVATE_CLASS_AS_FIELD_TRANSFORMER = "%s in a private class, only public class can be used with @FieldTransformer"
    const val ERROR_PROTECTED_CLASS_AS_FIELD_TRANSFORMER = "%s in a protected class, only public class can be used with @FieldTransformer"
    const val ERROR_ABSTRACT_CLASS_AS_FIELD_TRANSFORMER = "%s in an abstract class, only public class can be used with @FieldTransformer"
    const val GET = "get"
    const val SET = "set"
    const val IS = "is"
    const val TRANSFORM = "transform"
    const val REVERSE_TRANSFORM = "reverseTransform"
    const val GENERATED_FILE_PATH_KEY = "kapt.kotlin.generated"
    const val AUTO_GENERATE_CLASS_FROM_KMORPH_LIBRARY = "Auto Generate file from KMorph library. Changes made to this file will get overwritten during source regeneration."
}

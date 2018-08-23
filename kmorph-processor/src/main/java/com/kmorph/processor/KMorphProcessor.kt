package com.kmorph.processor

import com.kmorph.annotation.MorphTo
import com.kmorph.processor.constant.Constant
import com.kmorph.processor.exception.ValidationException
import com.kmorph.processor.generator.KMorphGenerator
import com.kmorph.processor.util.extractClassMetaData
import com.kmorph.processor.util.getTypeElementFromAnnotationValue
import com.kmorph.processor.util.isValidElementForMorphToAnnotation
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Types
import javax.tools.Diagnostic

class KMorphProcessor : AbstractProcessor() {
    private lateinit var messager: Messager
    private lateinit var filer: Filer
    private lateinit var typeUtils: Types

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        messager = processingEnvironment.messager
        filer = processingEnvironment.filer
        typeUtils = processingEnvironment.typeUtils
    }

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        roundEnvironment.getElementsAnnotatedWith(MorphTo::class.java).forEach {
            if (it !is TypeElement)
                return@forEach

            try {
                if (it.isValidElementForMorphToAnnotation()) {
                    val targetClassElement = it.getTypeElementFromAnnotationValue(kClass = MorphTo::class, typeUtils = typeUtils)

                    if (targetClassElement != null && targetClassElement.isValidElementForMorphToAnnotation()) {
                        KMorphGenerator(processingEnv.options[Constant.GENERATED_FILE_PATH_KEY]!!).generate(it.extractClassMetaData(typeUtils), targetClassElement.extractClassMetaData(typeUtils))
                    }
                }
            } catch (validationException: ValidationException) {
                messager.printMessage(Diagnostic.Kind.ERROR, validationException.error)
            }
        }
        return true
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(MorphTo::class.java.name)
    }
}


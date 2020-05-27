package com.hydbest.annotationcompiler.process

import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.logger.Logger
import com.bennyhuo.aptutils.types.isSubTypeOf
import com.csz.annotation.Builder
import com.csz.annotation.Optional
import com.csz.annotation.Required
import com.hydbest.annotationcompiler.base.ActivityClass
import com.hydbest.annotationcompiler.entity.Field
import com.hydbest.annotationcompiler.entity.OptionalField
import com.sun.tools.javac.code.Symbol
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

open class BuilderProcessor : AbstractProcessor() {

    private val supportAnnotations = setOf(Builder::class.java, Required::class.java, Optional::class.java)

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> = supportAnnotations.mapTo(HashSet<String>(), Class<*>::getCanonicalName)

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        AptContext.init(processingEnv)
    }


    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val activityClass = HashMap<Element, ActivityClass>()
        roundEnv.getElementsAnnotatedWith(Builder::class.java)
                .filter { it.kind.isClass }
                .forEach { element: Element ->
                    try {
                        if (element.asType().isSubTypeOf("android.app.Activity")) {
                            activityClass[element] = ActivityClass(element as TypeElement)
                        } else {
                            Logger.error(element, "Unsupported typeElement : ${element.simpleName}")
                        }
                    } catch (e: Exception) {
                        Logger.logParsingError(element, Builder::class.java, e)
                    }
                }

        roundEnv.getElementsAnnotatedWith(Required::class.java)
                .filter { it.kind.isField }
                .forEach { element: Element ->
                    //TODO
                    activityClass[element.enclosingElement]?.fields?.add(Field(element as Symbol.VarSymbol))
                            ?: Logger.error(element,"filed $element required while ${element.enclosingElement} not annotate")
                }

        roundEnv.getElementsAnnotatedWith(Optional::class.java)
                .filter { it.kind.isField }
                .forEach { element: Element ->
                    activityClass[element.enclosingElement]?.fields?.add(OptionalField(element as Symbol.VarSymbol))
                            ?: Logger.error(element,"filed $element required while ${element.enclosingElement} not annotate")
                }

        activityClass.values.forEach{
            it.builder.build(AptContext.filer)
        }
        return true
    }
}
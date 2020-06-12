package com.hydbest.annotationcompiler.base

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import com.squareup.kotlinpoet.FileSpec
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier
import javax.tools.StandardLocation

class ActivityClassBuilder(private val activityClass: ActivityClass) {
    companion object{
        const val SUFFIX = "Builder"
        const val METHOD_NAME = "start"
        const val METHOD_NAME_NO_OPTIONAL = METHOD_NAME + "WithoutOptional"
        const val METHOD_NAME_FOR_OPTIONAL = METHOD_NAME + "WithOptional"
        const val METHOD_NAME_FOR_OPTIONALS= METHOD_NAME + "WithOptionals"

    }

    fun build(filer: Filer){
        if (activityClass.isAbstract) return
        val typeSpecBuilder = TypeSpec.classBuilder(activityClass.simpleName + SUFFIX)
                .addModifiers(Modifier.FINAL,Modifier.PUBLIC)
        ConstantBuilder(activityClass).build(typeSpecBuilder)
        StartMethodBuilder(activityClass).build(typeSpecBuilder)
        SaveStateMethodBuilder(activityClass).build(typeSpecBuilder)
        InjectMethodBuilder(activityClass).build(typeSpecBuilder)

        if (activityClass.isKotlin){
            val fileBuilder = FileSpec.builder(activityClass.packageName, activityClass.simpleName + SUFFIX)
            StartKotlinFunBuilder(activityClass).build(fileBuilder)
            writeKotlinToFile(filer,fileBuilder.build())
        }
        writeJavaToFile(filer,typeSpecBuilder.build())
    }

    private fun writeKotlinToFile(filer: Filer, fileSpec: FileSpec) {
        try {
            filer.createResource(StandardLocation.SOURCE_OUTPUT, activityClass.packageName, fileSpec.name + ".kt").apply {
                openWriter().also {
                    fileSpec.writeTo(it)
                }.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun writeJavaToFile(filer: Filer,typeSpec: TypeSpec){
        try {
            JavaFile.builder(activityClass.packageName, typeSpec).build().writeTo(filer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
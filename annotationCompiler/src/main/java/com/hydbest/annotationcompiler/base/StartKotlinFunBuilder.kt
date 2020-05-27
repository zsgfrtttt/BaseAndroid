package com.hydbest.annotationcompiler.base

import com.hydbest.annotationcompiler.entity.OptionalField
import com.squareup.kotlinpoet.*
import javax.naming.Context

class StartKotlinFunBuilder(private val activityClass: ActivityClass) {
    fun build(fileBuilder:FileSpec.Builder){
        val name = ActivityClassBuilder.METHOD_NAME + activityClass.simpleName
        val functionBuilder = FunSpec.builder(name)
                .receiver(CONTEXT.kotlin)
                .addModifiers(KModifier.PUBLIC)
                .returns(UNIT)
                .addStatement("val intent = %T(this,%T::class.java)", INTENT.kotlin,activityClass.typeElement)

        activityClass.fields.forEach { field ->
            val name = field.name
            val className = field.asKotlinTypeName()
            if (field is OptionalField){
                //TODO
                functionBuilder.addParameter(ParameterSpec.builder(name,className).defaultValue("null").build())
            }else{
                functionBuilder.addParameter(name,className)
            }
            functionBuilder.addStatement("intent.putExtra(%S,%L)",name,name)
        }
        functionBuilder.addStatement("%T.INSTANCE.startActivity(this,intent)", ACTIVITY_BUILDER.kotlin)
        fileBuilder.addFunction(functionBuilder.build())
    }
}
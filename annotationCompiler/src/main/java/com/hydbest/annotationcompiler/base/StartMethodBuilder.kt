package com.hydbest.annotationcompiler.base

import com.bennyhuo.aptutils.logger.Logger
import com.csz.annotation.Optional
import com.hydbest.annotationcompiler.entity.OptionalField
import com.squareup.javapoet.*
import java.util.*
import javax.lang.model.element.Modifier

class StartMethodBuilder(private val activityClass: ActivityClass) {

    fun build(typeSpec: TypeSpec.Builder){
        val startMethod = StartMethod(activityClass,ActivityClassBuilder.METHOD_NAME)

        val groupFields = activityClass.fields.groupBy { it is OptionalField }
        val requiredFields  = groupFields[false].orEmpty()
        val optionalFields = groupFields[true].orEmpty()

        startMethod.addAllField(requiredFields)
        val startMethodNoOptional = startMethod.copy(ActivityClassBuilder.METHOD_NAME_NO_OPTIONAL)

        startMethod.addAllField(optionalFields)
        startMethod.build(typeSpec)

        if (optionalFields.isNotEmpty()){
            startMethodNoOptional.build(typeSpec)
        }

        if(optionalFields.size < 3){
            optionalFields.forEach { field ->
                startMethodNoOptional.copy(ActivityClassBuilder.METHOD_NAME_FOR_OPTIONAL + field.name.capitalize())
                        .also { it.addField(field) }
                        .build(typeSpec)
            }
        }else{
            val builderName = activityClass.simpleName + ActivityClassBuilder.SUFFIX
            val fillIntentMethodBuilder = MethodSpec.methodBuilder("fillIntent")
                    .addModifiers(Modifier.PRIVATE)
                    .returns(TypeName.VOID)
                    .addParameter(INTENT.java,"intent")

            val buildClassName = ClassName.get(activityClass.packageName,builderName)
            optionalFields.forEach { field ->
                typeSpec.addField(FieldSpec.builder(field.asJavaTypeName(),field.name,Modifier.PRIVATE).build())
                typeSpec.addMethod(MethodSpec.methodBuilder(field.name)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(field.asJavaTypeName(),field.name)
                        .addStatement("this.${field.name} = ${field.name}")
                        .addStatement("return this")
                        .returns(buildClassName)
                        .build())

                if (field.isPrimitive){//基本类型
                    fillIntentMethodBuilder.addStatement("intent.putExtra(\$S,\$L)",field.name,field.name)
                } else {
                    fillIntentMethodBuilder.beginControlFlow("if(\$L != null)",field.name)
                            .addStatement("intent.putExtra(\$S,\$L)",field.name,field.name)
                            .endControlFlow()
                }

            }
            typeSpec.addMethod(fillIntentMethodBuilder.build())
            startMethodNoOptional.copy(ActivityClassBuilder.METHOD_NAME_FOR_OPTIONALS)
                    .static(false)
                    .build(typeSpec)
        }
    }
}
package com.hydbest.annotationcompiler.base

import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

class SaveStateMethodBuilder(private val activityClass: ActivityClass) {
    fun build(typeSpec: TypeSpec.Builder) {
        val methodBuilder = MethodSpec.methodBuilder("saveState")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(ACTIVITY.java, "instance")
                .addParameter(BUNDLE.java, "outState")
                .beginControlFlow("if(instance instanceof \$T)", activityClass.typeElement)
                .addStatement("\$T typeInstance=(\$T)instance", activityClass.typeElement, activityClass.typeElement)
                .addStatement("\$T intent = new \$T()", INTENT.java, INTENT.java)
        activityClass.fields.forEach { field ->
            val name = field.name
            if (field.isPrivate){
                methodBuilder.addStatement("intent.putExtra(\$S,typeInstance.get\$L())",name,name.capitalize())
            }else{
                methodBuilder.addStatement("intent.putExtra(\$S,typeInstance.\$L)",name,name)
            }
        }
        methodBuilder.addStatement("outState.putAll(intent.getExtras())").endControlFlow()
        typeSpec.addMethod(methodBuilder.build())
    }
}
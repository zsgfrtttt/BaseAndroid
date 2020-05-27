package com.hydbest.annotationcompiler.base

import com.hydbest.annotationcompiler.entity.OptionalField
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

class InjectMethodBuilder(private val activityClass: ActivityClass) {
    fun build(typeSpec: TypeSpec.Builder) {
        val injectMethodBuilder = MethodSpec.methodBuilder("inject")
                .addParameter(ACTIVITY.java, "instance")
                .addParameter(BUNDLE.java, "bundle")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeName.VOID)
                .beginControlFlow("if(instance instanceof \$T)", activityClass.typeElement)
                .addStatement("\$T typeInstance = (\$T)instance", activityClass.typeElement, activityClass.typeElement)
                .addStatement("\$T extras = bundle == null ? typeInstance.getIntent().getExtras() : bundle", BUNDLE.java)
                .beginControlFlow("if(extras != null)")
        activityClass.fields.forEach { field ->
            val name = field.name
            val typeName = field.asJavaTypeName().box()
            if (field is OptionalField) {
                injectMethodBuilder.addStatement("\$T \$L=\$T.<\$T>get(extras,\$S,\$L)", typeName, name, BUNDLE_UTIL.java, typeName, name, field.defaultValue)
            } else {
                injectMethodBuilder.addStatement("\$T \$L=\$T.<\$T>get(extras,\$S)", typeName, name, BUNDLE_UTIL.java, typeName, name)
            }

            if (field.isPrivate) {
                injectMethodBuilder.addStatement("typeInstance.set\$L(\$L)", name.capitalize(), name)
            } else {
                injectMethodBuilder.addStatement("typeInstance.\$L = \$L", name, name)
            }
        }
        injectMethodBuilder.endControlFlow().endControlFlow()
        typeSpec.addMethod(injectMethodBuilder.build())
    }
}
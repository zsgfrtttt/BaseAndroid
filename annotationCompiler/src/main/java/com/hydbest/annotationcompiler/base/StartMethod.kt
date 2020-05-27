package com.hydbest.annotationcompiler.base

import com.hydbest.annotationcompiler.entity.Field
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import java.util.logging.Logger
import javax.lang.model.element.Modifier

class StartMethod(private val activityClass: ActivityClass, private val name: String) {
    private val fields = ArrayList<Field>()

    private var isStaticMethod = true

    fun static(static: Boolean): StartMethod {
        isStaticMethod = static
        return this
    }

    fun addAllField(fileds: List<Field>) {
        this.fields += fileds
    }

    fun addField(field: Field) { this.fields += field }

    fun copy(name:String) = StartMethod(activityClass,name).also {
        it.fields.addAll(StartMethod@this.fields)
    }

    fun build(typeSpec: TypeSpec.Builder){
        val methodBuilder = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(CONTEXT.java, "context")
                //Intent i = new Intent(xontext,xx.class)
                .addStatement("\$T intent=new \$T(context,\$T.class)", INTENT.java, INTENT.java, activityClass.typeElement)

        fields.forEach {
            filed ->
            val name = filed.name
            methodBuilder.addParameter(filed.asJavaTypeName(),name)
                    .addStatement("intent.putExtra(\$S,\$L)",name,name)
        }

        if (isStaticMethod){
            methodBuilder.addModifiers(Modifier.STATIC)
        }else{
            methodBuilder.addStatement("fillIntent(intent)")
        }

        methodBuilder.addStatement("\$T.INSTANCE.startActivity(context,intent)",ACTIVITY_BUILDER.java)
        typeSpec.addMethod(methodBuilder.build())
    }

}
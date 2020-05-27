package com.hydbest.annotationcompiler.base

import com.hydbest.annotationcompiler.base.ActivityClass
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeSpec
import java.util.*
import javax.lang.model.element.Modifier

class ConstantBuilder(private val activityClass: ActivityClass) {

    //public static final String REQUIED_AGE = "age"
    fun build(typeBuilder: TypeSpec.Builder) {
        activityClass.fields.forEach {
            field ->
            val f = FieldSpec.builder(String::class.java,
                    field.prefix + field.name.toUpperCase(Locale.ENGLISH),
                    Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL).initializer("\$S", field.name).build()
            typeBuilder.addField(f)
        }
    }

}
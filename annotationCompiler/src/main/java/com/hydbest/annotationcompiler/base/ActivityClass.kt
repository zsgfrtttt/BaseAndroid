package com.hydbest.annotationcompiler.base

import com.bennyhuo.aptutils.types.packageName
import com.bennyhuo.aptutils.types.simpleName
import com.hydbest.annotationcompiler.entity.Field
import java.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class ActivityClass(val typeElement: TypeElement) {
    companion object{
        val META_DATA = Class.forName("kotlin.Metadata") as Class<Annotation>
    }
    val simpleName = typeElement.simpleName()
    val packageName  = typeElement.packageName()
    val builder = ActivityClassBuilder(this)
    val fields = TreeSet<Field>()
    val isAbstract = typeElement.modifiers.contains(Modifier.ABSTRACT)
    val isKotlin = typeElement.getAnnotation(META_DATA) != null

    override fun toString(): String {
        return "$packageName.${simpleName}.[${fields.joinToString()}]"
    }
}
package com.hydbest.annotationcompiler.entity

import com.bennyhuo.aptutils.types.asJavaTypeName
import com.bennyhuo.aptutils.types.asKotlinTypeName
import com.squareup.kotlinpoet.TypeName
import com.sun.tools.javac.code.Symbol

open class Field(private val symbol:Symbol.VarSymbol):Comparable<Field> {

    val name  = symbol.qualifiedName.toString()

    open val prefix = "REQUIRED_"
    val isPrivate = symbol.isPrivate
    val isPrimitive = symbol.type.isPrimitive

    fun asJavaTypeName() = symbol.type.asJavaTypeName()
    open fun asKotlinTypeName() = symbol.type.asKotlinTypeName()

    override fun compareTo(other: Field): Int {
        return name.compareTo(other.name)
    }

    override fun toString(): String {
        return "$name:${symbol.type}"
    }
}
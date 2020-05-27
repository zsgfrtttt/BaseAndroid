package com.hydbest.annotationcompiler.entity

import com.bennyhuo.aptutils.types.asKotlinTypeName
import com.bennyhuo.aptutils.types.asTypeMirror
import com.csz.annotation.Optional
import com.squareup.kotlinpoet.TypeName
import com.sun.tools.javac.code.Symbol
import javax.lang.model.type.TypeKind

class OptionalField(symbol: Symbol.VarSymbol) : Field(symbol) {
    var defaultValue: Any? = null
        private set

    override val prefix = "OPTIONAL_"

    init {
        val optional = symbol.getAnnotation(Optional::class.java)
        defaultValue = when (symbol.type.kind) {
            TypeKind.BYTE -> {
                "(byte)${optional.byteValue}"
            }
            TypeKind.CHAR -> {
                "'${optional.charValue}'"
            }
            TypeKind.SHORT -> {
                "(short)${optional.shortValue}"
            }
            TypeKind.INT -> {
               optional.intValue
            }
            TypeKind.LONG -> {
                "${optional.longValue}L"
            }
            TypeKind.FLOAT -> {
                "${optional.floatValue}f"
            }
            TypeKind.DOUBLE -> {
                "${optional.doubleValue}d"
            }
            TypeKind.BOOLEAN -> {
                 optional.booleanValue
            }
            else ->
                if (symbol.type == String::class.java.asTypeMirror()) {
                     """"${optional.stringValue}""""
                }else{
                    null
                }
        }
    }

    override fun compareTo(other: Field): Int {
        return if (other is OptionalField) {
            super.compareTo(other)
        } else {
            1
        }
    }

    override open fun asKotlinTypeName(): TypeName {
        return super.asKotlinTypeName().asNullable()
    }

}
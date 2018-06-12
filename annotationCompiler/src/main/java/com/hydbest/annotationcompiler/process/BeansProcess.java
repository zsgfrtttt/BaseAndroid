package com.hydbest.annotationcompiler.process;

import com.hydbest.annotationcompiler.annotation.Seriable;
import com.sun.javafx.util.Logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

/**
 * Created by csz on 2018/6/12.
 * <p>
 * 继承AbstractProcessor必须是java lib
 * </p>
 */
@SupportedAnnotationTypes("com/hydbest/annotationcompiler/annotation/Seriable")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BeansProcess extends AbstractProcessor {
    //元素操作的辅助类
    Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //获得注解所声明的元素
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Seriable.class);
        TypeElement typeElement = null;
        List<VariableElement> fields = null;

        Map<String, List<VariableElement>> maps = new HashMap<>();
        for (Element element : elements) {
            //判断元素的类型
            if (element.getKind() == ElementKind.CLASS) {
                typeElement = (TypeElement) element;
                maps.put(typeElement.getQualifiedName().toString(), fields = new ArrayList<>());
            } else if (element.getKind() == ElementKind.FIELD) {
                VariableElement variableElement = (VariableElement) element;
                TypeElement enclosingElement = (TypeElement) variableElement.getEnclosingElement();
                String key = enclosingElement.getQualifiedName().toString();
                fields = maps.get(key);
                if (fields == null) {
                    maps.put(key, fields = new ArrayList<>());
                }
                fields.add(variableElement);
            }
        }

        for (String key : maps.keySet()) {
            if (maps.get(key).size() == 0) {
                TypeElement typeElement1 = elementUtils.getTypeElement(key);
                List<? extends Element> allMembers = elementUtils.getAllMembers(typeElement1);
                if (allMembers.size() > 0) {
                    maps.get(key).addAll(ElementFilter.fieldsIn(allMembers));
                }
            }
        }

        generateCodes(maps);
        return true;
    }

    private void generateCodes(Map<String, List<VariableElement>> maps) {
        File dir = new File("/process/");
        //File dir = new File("f://apt_test");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (String key : maps.keySet()) {
            File file = new File(dir, key.replaceAll("\\.", "_") + ".txt");
            try {
                FileWriter fw = new FileWriter(file);
                fw.append("{").append("class:").append("\"" + key + "\"")
                        .append(",\n ");
                fw.append("fields:\n {\n");
                List<VariableElement> fields = maps.get(key);

                for (int i = 0; i < fields.size(); i++) {
                    VariableElement field = fields.get(i);
                    fw.append("  ").append(field.getSimpleName()).append(":")
                            .append("\"" + field.asType().toString() + "\"");
                    if (i < fields.size() - 1) {
                        fw.append(",");
                        fw.append("\n");
                    }
                }
                fw.append("\n }\n");
                fw.append("}");
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

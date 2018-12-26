package com.csz.pluginlibrary;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.println;

/**
 * Created by csz on 2018/12/25.
 */

public class ExternalPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().add("exter",new ExternalObj());
        project.task("exterTask") .doLast(new Action<Task>() {
            @Override
            public void execute(Task task) {
                ExternalObj fristExtension = (ExternalObj) project.getExtensions().getByName("customPlugin");
                println("name:"+fristExtension.getName());
                println("age:"+fristExtension.getAge());
            }
        });
    }
}

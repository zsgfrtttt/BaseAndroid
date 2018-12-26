package com.csz.plugin;

import com.csz.plugin.extension.FristExtension;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.println;


/**
 * Created by csz on 2018/12/25.
 */

public class FristPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getExtensions().add("customPlugin",new FristExtension());
        project.task("showInfo") .doLast(new Action<Task>() {
            @Override
            public void execute(Task task) {
                FristExtension fristExtension = (FristExtension) project.getExtensions().getByName("customPlugin");
                println("name:"+fristExtension.getName());
                println("age:"+fristExtension.getAge());
            }
        });
    }
}

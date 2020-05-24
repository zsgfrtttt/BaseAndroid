package com.hydbest.baseandroid.activity.plugin;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hydbest.baseandroid.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by csz on 2019/1/21.
 */

public class ClassLoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classloader);
    }

    public void loader(View view) {
       // new Test();
        try {
            Class<?> aClass = getClassLoader().loadClass("com.hydbest.baseandroid.activity.plugin.ClassLoaderActivity$Test");
            aClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载啊字节码文件
     */
    class FileSystemClassLoader extends ClassLoader {
        private String rootDir;

        public FileSystemClassLoader(String rootDir) {
            this.rootDir = rootDir;
        }

        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] classData = getClassData(name);
            if (classData == null) {
                throw new ClassNotFoundException();
            } else {
                return defineClass(name, classData, 0, classData.length);
            }
        }

        private byte[] getClassData(String className) {
            String path = classNameToPath(className);
            try {
                InputStream ins = new FileInputStream(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int bufferSize = 4096;
                byte[] buffer = new byte[bufferSize];
                int bytesNumRead = 0;
                while ((bytesNumRead = ins.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesNumRead);
                }
                return baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String classNameToPath(String className) {
            return rootDir + File.separatorChar
                    + className.replace('.', File.separatorChar) + ".class";
        }
    }

    private static class Test {
        static {
            Log.i("csz", "静态方法块！");
        }

        {
            Log.i("csz", "普通方法块！");
        }

        public Test() {
            Log.i("csz", "构造器！");
        }
    }
}

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

public class ClassCreationDemo {

    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // Для теста: public class SomeClass { public void doWord() {System.out.println("Hello!");}}
        System.out.println("Enter source code:");
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        String line;
        while (true) {
            line = scanner.nextLine();
            if (!"".equals(line)) {
                sb.append(line).append("\n");
            } else {
                break;
            }
        }
        String source = sb.toString();

        // создание .java файла
        String path = "D:\\dev_edu\\STC13_HT\\HT07_RuntimeCreateClass\\src\\";
        File file = new File(path);
        File src = new File(new File(path), "SomeClass.java");
        Files.write(src.toPath(), source.getBytes());
        String javaFile = src.getPath();
        System.out.println(".java file created  " + javaFile);

        // создание .class файла
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, javaFile);
        System.out.println(".class file compiled");

        // загрузка класса и запуск метода
        ClassLoader originalLoader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});
        SomeClassLoader someClassLoader = new SomeClassLoader(originalLoader,"SomeClass", path);
        Class mainClass = someClassLoader.loadClass("SomeClass");
        System.out.println("Method run result:");
        mainClass.getMethod("doWord").invoke(mainClass.newInstance(), null);
    }
}


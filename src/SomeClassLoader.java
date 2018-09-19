import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class SomeClassLoader extends ClassLoader {

    private String classname;
    private String classpath;

    public SomeClassLoader(ClassLoader parent, String classname, String classpath) {
        super(parent);
        this.classname = classname;
        this.classpath = classpath;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // указываем составное имя заменяемого класса
        if (name.equals(classname)) {
            // путь до файла скомпилированного класса
            String dest = "file:" + classpath + classname + ".class";
            // массив для хранения байт кода
            byte[] classData = null;
            //получаем стрим с адреса
            try (InputStream inputStream  =  new URL(dest).openConnection().getInputStream();
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
                int data = inputStream.read();
                while (data != -1) {
                    // пишем в байтстрим скомпилированный код
                    byteArrayOutputStream.write(data);
                    // читаем байткод
                    data = inputStream.read();
                }
                inputStream.close();
                // итоговый массив байтов скомпилированного класса
                classData = byteArrayOutputStream.toByteArray();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return defineClass(name, classData, 0, classData.length);
        } else {
            return super.loadClass(name);
        }
    }
}

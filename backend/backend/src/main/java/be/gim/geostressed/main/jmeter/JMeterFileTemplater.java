package be.gim.geostressed.main.jmeter;


import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class JMeterFileTemplater {
    private Mustache m;

    public JMeterFileTemplater() {
        MustacheFactory mf = new DefaultMustacheFactory();
        m = mf.compile("templates/jmx.mustache");

    }

    public File template(TemplateConfiguration configuration) throws IOException {
        File tempFile = File.createTempFile("test", ".jmx");

        FileWriter fileWriter = new FileWriter(tempFile);
        m.execute(fileWriter, configuration).flush();
        fileWriter.close();
        return tempFile;
    }
}

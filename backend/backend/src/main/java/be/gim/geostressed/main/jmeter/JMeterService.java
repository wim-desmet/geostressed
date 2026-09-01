package be.gim.geostressed.main.jmeter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class JMeterService {

    private final JMeterFileTemplater jMeterFileTemplater;

    public JMeterService(JMeterFileTemplater jMeterFileTemplater) {
        this.jMeterFileTemplater = jMeterFileTemplater;
    }

    public File generateTestFile(TestConfigurationDAO configuration) throws IOException {
        TemplateConfiguration templateConfiguration = new TemplateConfiguration();

        templateConfiguration.setLoops(
                configuration.getLoops()
        );
        templateConfiguration.setUsers(configuration.getUsers());
        templateConfiguration.setWfs(
                configuration.getLayers().stream()
                        .filter(TestConfigurationDAO.Layer::getWfs)
                        .map(layer -> {
                            TemplateConfiguration.Layer templateLayer = new TemplateConfiguration.Layer();
                            templateLayer.setName(layer.getName());
                            return templateLayer;
                        }).toList());
        templateConfiguration.setWms(
                configuration.getLayers().stream()
                        .filter(TestConfigurationDAO.Layer::getWms)
                        .map(layer -> {
                            TemplateConfiguration.Layer templateLayer = new TemplateConfiguration.Layer();
                            templateLayer.setName(layer.getName());
                            return templateLayer;
                        }).toList());
        templateConfiguration.setWmts(
                configuration.getLayers().stream()
                        .filter(TestConfigurationDAO.Layer::getWmts)
                        .map(layer -> {
                            TemplateConfiguration.Layer templateLayer = new TemplateConfiguration.Layer();
                            templateLayer.setName(layer.getName());
                            return templateLayer;
                        }).toList());
        TemplateConfiguration.Url templateUrl = new TemplateConfiguration.Url();
        templateUrl.setHost(configuration.getUrl().getHost());
        templateUrl.setProtocol(configuration.getUrl().getProtocol());
        templateUrl.setPath(configuration.getUrl().getPath());

        templateConfiguration.setUrl(templateUrl);

        return jMeterFileTemplater.template(templateConfiguration);
    }

    public JmeterOutput run(File jmxFile) throws IOException, InterruptedException {
        String jmxFileLocation = jmxFile.getAbsolutePath();

        Path jmeterResultFile = File.createTempFile("jmeter", ".result").toPath();
        Path jmeterResultFolder = Files.createTempDirectory("jmeter_result");

        JmeterOutput jmeterOutput = new JmeterOutput(
                jmeterResultFile.getFileName().toString(),
                jmeterResultFolder.getFileName().toString());

        ProcessBuilder pb = new ProcessBuilder(
                "C:\\Users\\wimd\\AppData\\Local\\Programs\\JMeter\\bin\\jmeter.bat",
                "-n", "-t", jmxFileLocation,
                "-l", jmeterResultFile.toAbsolutePath().toString(),
                "-e", "-o", jmeterResultFolder.toAbsolutePath().toString());

        System.out.println(jmxFileLocation);
        System.out.println(jmeterResultFile.toAbsolutePath().toString());
        System.out.println(jmeterResultFolder.toAbsolutePath().toString());

        File log = new File("log");
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        Process p = pb.start();

//        File resultFile = new File(jmeterResultFile);

        p.onExit().thenAccept(process -> {
            System.out.println("Process exited with code " + process.exitValue());
        });
        return jmeterOutput;
    }
}

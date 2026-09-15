package be.gim.geostressed.main.jmeter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import us.abstracta.jmeter.javadsl.core.DslTestPlan;
import us.abstracta.jmeter.javadsl.http.DslHttpSampler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

@Service
public class JMeterService {

    @Value("${app.jmeter.path}")
    private String jmeterPath;

    @Value("classpath:templates/bboxRandomizer.groovy")
    private Resource jsrBboxScript;


    private final JMeterFileTemplater jMeterFileTemplater;

    public JMeterService(JMeterFileTemplater jMeterFileTemplater) {
        this.jMeterFileTemplater = jMeterFileTemplater;
    }

    private TemplateConfiguration.Protocol buildProtocol(TestConfigurationDAO configuration, String protocol, String crs, Predicate<? super TestConfigurationDAO.Layer> hasProtocol) {
        return new TemplateConfiguration.Protocol(
                protocol,
                crs,
                configuration.layers().stream().filter(
                        hasProtocol
                ).map(layer -> new TemplateConfiguration.Layer(
                        layer.name(),
                        layer.defaultStyle()
                )).toList()
        );
    }

    private TemplateConfiguration.ThreadGroup buildThreadGroup(TestConfigurationDAO configuration, String crs, String protocol, Predicate<? super TestConfigurationDAO.Layer> hasProtocol) {
        return new TemplateConfiguration.ThreadGroup(
                crs,
                protocol.toUpperCase(),
                "wfs".equalsIgnoreCase(protocol) ? buildProtocol(configuration, "wfs", crs, TestConfigurationDAO.Layer::wfs) : null,
                "wms".equalsIgnoreCase(protocol) ? buildProtocol(configuration, "wms", crs, TestConfigurationDAO.Layer::wms) : null,
                "wmts".equalsIgnoreCase(protocol) ? buildProtocol(configuration, "wmts", crs, TestConfigurationDAO.Layer::wmts) : null,
                "features".equalsIgnoreCase(protocol) ? buildProtocol(configuration, "features", crs, TestConfigurationDAO.Layer::features) : null,
                "maps".equalsIgnoreCase(protocol) ? buildProtocol(configuration, "maps", crs, TestConfigurationDAO.Layer::maps) : null
        );
    }

    public TemplateConfiguration generateTemplateConfiguration(TestConfigurationDAO configuration) {

        List<TemplateConfiguration.ThreadGroup> threadGroups = new ArrayList<>();

        for (String crs : configuration.crs()) {
            if (configuration.layers().stream().anyMatch(TestConfigurationDAO.Layer::wfs)) {
                threadGroups.add(buildThreadGroup(configuration, crs, "wfs", TestConfigurationDAO.Layer::wfs));
            }
            if (configuration.layers().stream().anyMatch(TestConfigurationDAO.Layer::wms)) {
                threadGroups.add(buildThreadGroup(configuration, crs, "wms", TestConfigurationDAO.Layer::wms));
            }
            if (configuration.layers().stream().anyMatch(TestConfigurationDAO.Layer::wmts)) {
                threadGroups.add(buildThreadGroup(configuration, crs, "wmts", TestConfigurationDAO.Layer::wmts));
            }
            if (configuration.layers().stream().anyMatch(TestConfigurationDAO.Layer::features)) {
                threadGroups.add(buildThreadGroup(configuration, crs, "features", TestConfigurationDAO.Layer::features));
            }
            if (configuration.layers().stream().anyMatch(TestConfigurationDAO.Layer::maps)) {
                threadGroups.add(buildThreadGroup(configuration, crs, "maps", TestConfigurationDAO.Layer::maps));
            }
        }

        TemplateConfiguration templateConfiguration = new TemplateConfiguration(
                threadGroups,
                configuration.users(),
                configuration.loops(),
                new TemplateConfiguration.Url(
                        configuration.url().protocol(),
                        configuration.url().host(),
                        configuration.url().path()
                )
        );
        return templateConfiguration;

//        System.out.println(templateConfiguration);

//        return jMeterFileTemplater.template(templateConfiguration);
    }

    public JmeterOutput run(File jmxFile) throws IOException, InterruptedException {
        String jmxFileLocation = jmxFile.getAbsolutePath();

        Path jmeterResultFile = File.createTempFile("jmeter", ".result").toPath();
        Path jmeterResultFolder = Files.createTempDirectory("jmeter_result");

        JmeterOutput jmeterOutput = new JmeterOutput(
                jmxFileLocation,
                jmeterResultFile.getFileName().toString(),
                jmeterResultFolder.getFileName().toString());

        ProcessBuilder pb = new ProcessBuilder(
                jmeterPath,
                "-n",
                "-t", jmxFileLocation,
                "-l", jmeterResultFile.toAbsolutePath().toString(),
                "-e",
                "-o", jmeterResultFolder.toAbsolutePath().toString());

        pb.environment().put("HEAP", "-Xms4g -Xmx5g -XX:MaxMetaspaceSize=512m");

        System.out.println(jmxFileLocation);
        System.out.println(jmeterResultFile.toAbsolutePath().toString());
        System.out.println(jmeterResultFolder.toAbsolutePath().toString());

        File log = new File("log");
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        Process p = pb.start();

        p.waitFor();

        System.out.println("Process exited with code " + p.exitValue());
        return jmeterOutput;
    }

    public void generateTestConfiguration(TestConfigurationDAO configurationDAO) throws IOException {

        TemplateConfiguration configuration = generateTemplateConfiguration(configurationDAO);

        DslTestPlan geoStressedTestPlan = testPlan(
                httpDefaults()
                        .protocol(configuration.url().protocol())
                        .host(configuration.url().host())
                        .path(configuration.url().path()),
                jsr223PreProcessor(jsrBboxScript.getContentAsString(Charset.defaultCharset()))
                        .language("groovy")
        ).children(
                configuration.threadGroups().stream().map(threadGroupConfig ->
                        threadGroup(
                                "Thread Group " + threadGroupConfig.protocol() + " " + threadGroupConfig.crs(),
                                configuration.users(),
                                configuration.loops()
                        ).children(
                                samplers(configuration, threadGroupConfig)
                        ).children(
                                resultsTreeVisualizer()
                        )


                ).toArray(DslTestPlan.TestPlanChild[]::new)
        ).children(
                htmlReporter("html_results")
        );

        geoStressedTestPlan.saveAsJmx("dsl-test-plan.jmx");
        geoStressedTestPlan.run();
    }

    private DslHttpSampler[] samplers(TemplateConfiguration configuration, TemplateConfiguration.ThreadGroup threadGroupConfig) {
        switch (threadGroupConfig.protocol().toLowerCase()) {
            case "wfs":
                return wfsSamplers(configuration, threadGroupConfig);
            case "wms":
                return wmsSamplers(configuration, threadGroupConfig);
            case "wmts":
                return wmtsSamplers(configuration, threadGroupConfig);
            case "features":
                return featuresSamplers(configuration, threadGroupConfig);
            case "maps":
                return mapsSamplers(configuration, threadGroupConfig);
            default:
                throw new RuntimeException("protocol not known");
        }
    }

    private DslHttpSampler[] wfsSamplers(TemplateConfiguration configuration, TemplateConfiguration.ThreadGroup threadGroupConfig) {
        return threadGroupConfig.wfs().layers().stream().map(layer ->
                httpSampler(
                        "WFS Request: " + layer.name() + " (" + threadGroupConfig.crs() + ")",
                        "geoserver/ows")
                        .param("request", "GetFeature")
                        .param("service", "WFS")
                        .param("count", "1000")
                        .param("outputformat", "application/json")
                        .param("crs", threadGroupConfig.crs())
                        .param("srsname", threadGroupConfig.crs())
                        .param("typename", layer.name())
                        .param("bbox", "${epsgResult}")
        ).toArray(DslHttpSampler[]::new);
    }

    private DslHttpSampler[] wmsSamplers(TemplateConfiguration configuration, TemplateConfiguration.ThreadGroup threadGroupConfig) {
        return threadGroupConfig.wms().layers().stream().map(layer ->
                httpSampler(
                        "WMS Request: " + layer.name() + " (" + threadGroupConfig.crs() + ")",
                        "geoserver/ows")
                        .param("request", "GetMap")
                        .param("service", "WMS")
                        .param("version", "1.3.0")
                        .param("format", "image/png")
                        .param("transparent", "true")
                        .param("width", "256")
                        .param("height", "256")
                        .param("crs", threadGroupConfig.crs())
                        .param("styles", layer.defaultStyle())
                        .param("layers", layer.name())
                        .param("bbox", "${epsgResult}")
        ).toArray(DslHttpSampler[]::new);
    }

    private DslHttpSampler[] wmtsSamplers(TemplateConfiguration configuration, TemplateConfiguration.ThreadGroup threadGroupConfig) {
        return threadGroupConfig.wmts().layers().stream().map(layer -> httpSampler("geoserver/ows")).toArray(DslHttpSampler[]::new);
    }

    private DslHttpSampler[] featuresSamplers(TemplateConfiguration configuration, TemplateConfiguration.ThreadGroup threadGroupConfig) {
        return threadGroupConfig.features().layers().stream().map(layer ->
                httpSampler(
                        "Features Request: " + layer.name() + " (" + threadGroupConfig.crs() + ")",
                        "geoserver/ogc/features/v1/collections/" + layer.name() + "/items")
                        .param("limit", "1000")
                        .param("bbox", "${epsgResult}")
                        .param("crs", threadGroupConfig.crs())
                        .param("bbox-crs", threadGroupConfig.crs())
        ).toArray(DslHttpSampler[]::new);
    }

    private DslHttpSampler[] mapsSamplers(TemplateConfiguration configuration, TemplateConfiguration.ThreadGroup threadGroupConfig) {
        return threadGroupConfig.maps().layers().stream().map(layer -> httpSampler("geoserver/ows")).toArray(DslHttpSampler[]::new);
    }


}

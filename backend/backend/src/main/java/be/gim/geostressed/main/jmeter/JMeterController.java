package be.gim.geostressed.main.jmeter;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("api/jmeter")
public class JMeterController {

    private final JMeterService jMeterService;

    public JMeterController(JMeterService jMeterService) {
        this.jMeterService = jMeterService;
    }


    @PostMapping("/test")
    public JmeterOutput test(@RequestBody TestConfigurationDAO configuration) throws IOException, InterruptedException {
        File testFile = jMeterService.generateTestFile(configuration);
        return jMeterService.run(testFile);
    }
}

package be.gim.geostressed.main.jmeter;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/jmeter")
public class JMeterController {

    private final JMeterService jMeterService;

    public JMeterController(JMeterService jMeterService) {
        this.jMeterService = jMeterService;
    }


    @PostMapping("/test")
    public String test(@RequestBody TestConfigurationDAO configuration) throws IOException, InterruptedException {
        System.out.println(configuration);
//        File testFile = jMeterService.generateTestFile(configuration);
        jMeterService.generateTestConfiguration(configuration);
//        return jMeterService.run/**/();
        return "done";
    }
}

package be.gim.geostressed.main.jmeter;

import java.io.File;
import java.nio.file.Files;

public record JmeterOutput(
        String jmeterResultFile,
        String jmeterResultFolder
) {
}

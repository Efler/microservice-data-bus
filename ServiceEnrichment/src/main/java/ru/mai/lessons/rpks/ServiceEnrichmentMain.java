package ru.mai.lessons.rpks;

import ru.mai.lessons.rpks.impl.ConfigurationReader;
import ru.mai.lessons.rpks.impl.ServiceEnrichment;

public class ServiceEnrichmentMain {

    public static void main(String[] args) {

        ConfigReader configReader = new ConfigurationReader();
        Service service = new ServiceEnrichment();
        service.start(configReader.loadConfig());

    }

}

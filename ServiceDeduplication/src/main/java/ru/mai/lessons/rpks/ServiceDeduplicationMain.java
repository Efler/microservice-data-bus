package ru.mai.lessons.rpks;

import ru.mai.lessons.rpks.impl.ConfigurationReader;
import ru.mai.lessons.rpks.impl.ServiceDeduplication;

public class ServiceDeduplicationMain {

    public static void main(String[] args) {

        ConfigReader configReader = new ConfigurationReader();
        Service service = new ServiceDeduplication();
        service.start(configReader.loadConfig());

    }

}

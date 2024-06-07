package ru.mai.lessons.rpks;

import ru.mai.lessons.rpks.impl.ConfigurationReader;
import ru.mai.lessons.rpks.impl.ServiceFiltering;

public class ServiceFilteringMain {

    public static void main(String[] args) {
        ConfigReader configReader = new ConfigurationReader();
        Service service = new ServiceFiltering();
        service.start(configReader.loadConfig());
    }

}

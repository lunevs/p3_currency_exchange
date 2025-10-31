package ru.skillbox.currency.exchange.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.model.xml.CbrCurrencyList;

import java.io.InputStream;

@Service
public class XmlParseService {

    public CbrCurrencyList parseXml(InputStream inputStream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CbrCurrencyList.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (CbrCurrencyList) unmarshaller.unmarshal(inputStream);
    }


}

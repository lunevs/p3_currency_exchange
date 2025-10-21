package ru.skillbox.currency.exchange.model.xml;

import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CbrCurrencyList {

    @XmlAttribute(name = "Date")
    private String date;

    @XmlAttribute(name = "name")
    private String name;

    @XmlElement(name = "Valute")
    private List<CbrCurrencyItem> currencies = new ArrayList<>();

}

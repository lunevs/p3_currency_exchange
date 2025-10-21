package ru.skillbox.currency.exchange.model.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.*;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CbrCurrencyItem {

    @XmlAttribute(name = "ID")
    private String id;

    @XmlElement(name = "NumCode")
    private Long isoNumCode;

    @XmlElement(name = "CharCode")
    private String isoCode;

    @XmlElement(name = "Nominal")
    private Long nominal;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Value")
    private Double nominalValue;

    @XmlElement(name = "VunitRate")
    private Double value;

}

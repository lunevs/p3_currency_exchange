package ru.skillbox.currency.exchange;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skillbox.currency.exchange.model.dto.CurrencyDto;
import ru.skillbox.currency.exchange.service.CurrencyService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AbstractTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected CurrencyService currencyService;

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:12.3")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test")
                    .withReuse(false);

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    protected static CurrencyDto cUSD = new CurrencyDto(1L, "Доллар США", 1L, 78.98480, 840L, "USD");
    protected static CurrencyDto cEUR = new CurrencyDto(2L, "Евро", 1L, 92.02330, 978L, "EUR");
    protected static CurrencyDto cZZZ = new CurrencyDto(999L, "Incorrect Currency", 1L, 100., 1L, "ZZZ");


    @BeforeEach
    public void setUp() {
        currencyService.create(cUSD);
        currencyService.create(cEUR);
    }

    @AfterEach
    public void tearDown() {
        currencyService.deleteAllCurrencies();
    }

}

package io.helidon.socksshop;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SockShopResourceMockTest {

    private List<Socks> socksList = List.of(new Socks(1L, "Model1", 10.00),
            new Socks(2L, "Model2", 20.00));

    @InjectMocks
    private SockShopResource sockShopResource;

    @Mock
    private ShoppingService shoppingService;

    @BeforeEach
    private void init() {
        Mockito.lenient().doCallRealMethod().when(shoppingService).allSocks();
    }

    @Test
    void allSocksTest() {
        Mockito.doReturn(socksList).when(shoppingService).allSocks();

        String response = sockShopResource.allSocks();
        assertEquals(response, "[{\"model\":\"Model1\",\"price\":10.0},{\"model\":\"Model2\",\"price\":20.0}]");
        Mockito.verifyNoMoreInteractions(shoppingService);

    }
}

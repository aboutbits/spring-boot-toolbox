package it.aboutbits.springboot.toolbox.boot.swagger;

import it.aboutbits.springboot.toolbox.support.HttpTest;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@HttpTest
public class SwaggerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldBeAccessible() throws Exception {
        var swaggerUiString = performGetAndReturnResult("/docs/swagger-ui/index.html");
        var swaggerSchemaString = performGetAndReturnResult("/docs/api");

        assertThat(swaggerUiString).isNotBlank();
        assertThat(swaggerSchemaString).isNotBlank();
    }

    private @NonNull String performGetAndReturnResult(@NonNull String url) throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);

        return mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}

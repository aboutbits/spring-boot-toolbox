package it.aboutbits.springboot.toolbox.boot.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.aboutbits.springboot.toolbox.boot.mvc.body.BodyWithEmailAddress;
import it.aboutbits.springboot.toolbox.boot.mvc.body.BodyWithIban;
import it.aboutbits.springboot.toolbox.boot.mvc.body.BodyWithScaledBigDecimal;
import it.aboutbits.springboot.toolbox.support.HttpTest;
import it.aboutbits.springboot.toolbox.type.EmailAddress;
import it.aboutbits.springboot.toolbox.type.Iban;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import lombok.NonNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@HttpTest
public class CustomTypeBindingsForControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Nested
    class EmailAddressType {
        @Test
        void emailAddressAsPathVariable() throws Exception {
            var value = new EmailAddress("herbert@aboutbits.it");

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/type/EmailAddress/as-path-variable/%s", value)
            );

            var actual = objectMapper.readValue(resultAsString, EmailAddress.class);

            assertThat(actual).isEqualTo(value);
        }

        @Test
        void emailAddressAsRequestParameter() throws Exception {
            var value = new EmailAddress("herbert@aboutbits.it");

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/type/EmailAddress/as-request-parameter?value=%s", value)
            );

            var actual = objectMapper.readValue(resultAsString, EmailAddress.class);

            assertThat(actual).isEqualTo(value);
        }

        @Test
        void emailAddressAsBody() throws Exception {
            var value = new BodyWithEmailAddress(
                    new EmailAddress("herbert@aboutbits.it")
            );

            var resultAsString = performPostAndReturnResult(
                    "/test/type/EmailAddress/as-body",
                    value
            );

            var actual = objectMapper.readValue(resultAsString, BodyWithEmailAddress.class);

            assertThat(actual).isEqualTo(value);
        }
    }

    @Nested
    class IbanType {
        @Test
        void IbanAsPathVariable() throws Exception {
            var value = new Iban("NL63ABNA7864733042");

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/type/Iban/as-path-variable/%s", value)
            );

            var actual = objectMapper.readValue(resultAsString, Iban.class);

            assertThat(actual).isEqualTo(value);
        }

        @Test
        void IbanAsRequestParameter() throws Exception {
            var value = new Iban("NL63ABNA7864733042");

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/type/Iban/as-request-parameter?value=%s", value)
            );

            var actual = objectMapper.readValue(resultAsString, Iban.class);

            assertThat(actual).isEqualTo(value);
        }

        @Test
        void IbanAsBody() throws Exception {
            var value = new BodyWithIban(
                    new Iban("NL63ABNA7864733042")
            );

            var resultAsString = performPostAndReturnResult(
                    "/test/type/Iban/as-body",
                    value
            );

            var actual = objectMapper.readValue(resultAsString, BodyWithIban.class);

            assertThat(actual).isEqualTo(value);
        }
    }

    @Nested
    class ScaledBigDecimalType {
        @ParameterizedTest
        @ValueSource(doubles = {-1, 0, 1, -0.001, 0.001, -100_000_000, 100_000_000})
        void ScaledBigDecimalAsPathVariable(double doubleValue) throws Exception {
            var value = new ScaledBigDecimal(doubleValue);

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/type/ScaledBigDecimal/as-path-variable/%s", value)
            );

            var actual = objectMapper.readValue(resultAsString, ScaledBigDecimal.class);

            assertThat(actual).isEqualTo(value);
        }

        @ParameterizedTest
        @ValueSource(doubles = {-1, 0, 1, -0.001, 0.001, -100_000_000, 100_000_000})
        void ScaledBigDecimalAsRequestParameter(double doubleValue) throws Exception {
            var value = new ScaledBigDecimal(doubleValue);

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/type/ScaledBigDecimal/as-request-parameter?value=%s", value)
            );

            var actual = objectMapper.readValue(resultAsString, ScaledBigDecimal.class);

            assertThat(actual).isEqualTo(value);
        }

        @ParameterizedTest
        @ValueSource(doubles = {-1, 0, 1, -0.001, 0.001, -100_000_000, 100_000_000})
        void ScaledBigDecimalAsBody(double doubleValue) throws Exception {
            var value = new BodyWithScaledBigDecimal(
                    new ScaledBigDecimal(doubleValue)
            );

            var resultAsString = performPostAndReturnResult(
                    "/test/type/ScaledBigDecimal/as-body",
                    value
            );

            var actual = objectMapper.readValue(resultAsString, BodyWithScaledBigDecimal.class);

            assertThat(actual).isEqualTo(value);
        }
    }

    private @NonNull String performGetAndReturnResult(@NonNull String url) throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);

        return mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private @NonNull String performPostAndReturnResult(@NonNull String url, @NonNull Object body) throws Exception {
        var requestBuilder = MockMvcRequestBuilders.post(url)
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON);

        return mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

}

package it.aboutbits.springboot.toolbox.autoconfiguration.mvc;

import it.aboutbits.springboot.toolbox._support.HttpTest;
import it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body.BodyWithEmailAddress;
import it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body.BodyWithIban;
import it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body.BodyWithScaledBigDecimal;
import it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body.BodyWithUUID;
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
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@HttpTest
public class CustomTypeBindingsForControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JsonMapper jsonMapper;

    @Nested
    class EmailAddressType {
        @Test
        void emailAddressAsPathVariable() throws Exception {
            var value = new EmailAddress("herbert@aboutbits.it");

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/type/EmailAddress/as-path-variable/%s", value)
            );

            var actual = jsonMapper.readValue(resultAsString, EmailAddress.class);

            assertThat(actual).isEqualTo(value);
        }

        @Test
        void emailAddressAsRequestParameter() throws Exception {
            var value = new EmailAddress("herbert@aboutbits.it");

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/type/EmailAddress/as-request-parameter?value=%s", value)
            );

            var actual = jsonMapper.readValue(resultAsString, EmailAddress.class);

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

            var actual = jsonMapper.readValue(resultAsString, BodyWithEmailAddress.class);

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

            var actual = jsonMapper.readValue(resultAsString, Iban.class);

            assertThat(actual).isEqualTo(value);
        }

        @Test
        void IbanAsRequestParameter() throws Exception {
            var value = new Iban("NL63ABNA7864733042");

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/type/Iban/as-request-parameter?value=%s", value)
            );

            var actual = jsonMapper.readValue(resultAsString, Iban.class);

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

            var actual = jsonMapper.readValue(resultAsString, BodyWithIban.class);

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

            var actual = jsonMapper.readValue(resultAsString, ScaledBigDecimal.class);

            assertThat(actual).isEqualTo(value);
        }

        @ParameterizedTest
        @ValueSource(doubles = {-1, 0, 1, -0.001, 0.001, -100_000_000, 100_000_000})
        void ScaledBigDecimalAsRequestParameter(double doubleValue) throws Exception {
            var value = new ScaledBigDecimal(doubleValue);

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/type/ScaledBigDecimal/as-request-parameter?value=%s", value)
            );

            var actual = jsonMapper.readValue(resultAsString, ScaledBigDecimal.class);

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

            var actual = jsonMapper.readValue(resultAsString, BodyWithScaledBigDecimal.class);

            assertThat(actual).isEqualTo(value);
        }
    }

    @Nested
    class UUIDType {
        @ParameterizedTest
        @ValueSource(strings = {"d414ed05-c370-445a-8430-1fd1021c9856", "0682a03d-3618-470f-a8f9-78e4a52f1a2c"})
        void UUIDAsPathVariable(String uuidStringValue) throws Exception {
            var value = UUID.fromString(uuidStringValue);

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/type/UUID/as-path-variable/%s", value)
            );

            var actual = jsonMapper.readValue(resultAsString, UUID.class);

            assertThat(actual).isEqualTo(value);
        }

        @ParameterizedTest
        @ValueSource(strings = {"d414ed05-c370-445a-8430-1fd1021c9856", "0682a03d-3618-470f-a8f9-78e4a52f1a2c"})
        void UUIDAsRequestParameter(String uuidStringValue) throws Exception {
            var value = UUID.fromString(uuidStringValue);

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/type/UUID/as-request-parameter?value=%s", value)
            );

            var actual = jsonMapper.readValue(resultAsString, UUID.class);

            assertThat(actual).isEqualTo(value);
        }

        @ParameterizedTest
        @ValueSource(strings = {"d414ed05-c370-445a-8430-1fd1021c9856", "0682a03d-3618-470f-a8f9-78e4a52f1a2c"})
        void UUIDAsBody(String uuidStringValue) throws Exception {
            var value = new BodyWithUUID(
                    UUID.fromString(uuidStringValue)
            );

            var resultAsString = performPostAndReturnResult(
                    "/test/type/UUID/as-body",
                    value
            );

            var actual = jsonMapper.readValue(resultAsString, BodyWithUUID.class);

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
                .content(jsonMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON);

        return mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

}

package it.aboutbits.springboot.toolbox.autoconfiguration.mvc;

import com.tngtech.archunit.junit.ArchIgnore;
import it.aboutbits.springboot.toolbox._support.HttpTest;
import it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body.BodyWithEntityId;
import it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body.BodyWithEnumEntityId;
import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.CustomTypeEnumTestModel;
import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.CustomTypeTestModel;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;

@ArchIgnore(reason = "This test class has no matching counterpart in the production code.")
@HttpTest
@NullMarked
class EntityIdBindingsForControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JsonMapper jsonMapper;

    @Nested
    class EntityId {
        @Test
        void emailAddressAsPathVariable() throws Exception {
            var value = new CustomTypeTestModel.ID(512L);

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/entity-id/CustomTypeTestModel.ID/as-path-variable/%s", value)
            );

            var actual = jsonMapper.readValue(resultAsString, CustomTypeTestModel.ID.class);

            assertThat(actual).isEqualTo(value);
        }

        @Test
        void emailAddressAsRequestParameter() throws Exception {
            var value = new CustomTypeTestModel.ID(512L);

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/entity-id/CustomTypeTestModel.ID/as-request-parameter?value=%s", value)
            );

            var actual = jsonMapper.readValue(resultAsString, CustomTypeTestModel.ID.class);

            assertThat(actual).isEqualTo(value);
        }

        @Test
        void emailAddressAsBody() throws Exception {
            var value = new BodyWithEntityId(
                    new CustomTypeTestModel.ID(512L)
            );

            var resultAsString = performPostAndReturnResult(
                    "/test/entity-id/CustomTypeTestModel.ID/as-body",
                    value
            );

            var actual = jsonMapper.readValue(resultAsString, BodyWithEntityId.class);

            assertThat(actual).isEqualTo(value);
        }
    }

    @Nested
    class EnumEntityId {
        @Test
        void emailAddressAsPathVariable() throws Exception {
            var value = new CustomTypeEnumTestModel.ID(CustomTypeEnumTestModel.CustomTypeEnum.ENUM_FIRST);

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/entity-id/CustomTypeEnumTestModel.ID/as-path-variable/%s", value)
            );

            var actual = jsonMapper.readValue(resultAsString, CustomTypeEnumTestModel.ID.class);

            assertThat(actual).isEqualTo(value);
        }

        @Test
        void emailAddressAsRequestParameter() throws Exception {
            var value = new CustomTypeEnumTestModel.ID(CustomTypeEnumTestModel.CustomTypeEnum.ENUM_OTHER);

            var resultAsString = performGetAndReturnResult(
                    String.format("/test/entity-id/CustomTypeEnumTestModel.ID/as-request-parameter?value=%s", value)
            );

            var actual = jsonMapper.readValue(resultAsString, CustomTypeEnumTestModel.ID.class);

            assertThat(actual).isEqualTo(value);
        }

        @Test
        void emailAddressAsBody() throws Exception {
            var value = new BodyWithEnumEntityId(
                    new CustomTypeEnumTestModel.ID(CustomTypeEnumTestModel.CustomTypeEnum.ENUM_LAST)
            );

            var resultAsString = performPostAndReturnResult(
                    "/test/entity-id/CustomTypeEnumTestModel.ID/as-body",
                    value
            );

            var actual = jsonMapper.readValue(resultAsString, BodyWithEnumEntityId.class);

            assertThat(actual).isEqualTo(value);
        }
    }

    private String performGetAndReturnResult(String url) throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);

        return mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private String performPostAndReturnResult(String url, Object body) throws Exception {
        var requestBuilder = MockMvcRequestBuilders.post(url)
                .content(jsonMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON);

        return mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}

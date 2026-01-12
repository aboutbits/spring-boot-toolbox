package it.aboutbits.springboot.toolbox.web;

import it.aboutbits.springboot.toolbox.parameter.SortParameter;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@NullMarked
class SortParameterResolverTest {
    @Mock
    private MethodParameter methodParameter;
    @Mock
    private ModelAndViewContainer mavContainer;
    @Mock
    private NativeWebRequest webRequest;
    @Mock
    private WebDataBinderFactory binderFactory;

    @InjectMocks
    private SortParameterResolver sut;

    @BeforeEach
    void setUp() {
        when(methodParameter.getParameterName()).thenReturn("sort");
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    @SuppressWarnings({"unchecked", "rawtypes"})
    void supportsOrderListParameter_shouldReturnTrue() {
        // given
        var clazz = (Class) SortParameter.class;
        when(methodParameter.getParameterType()).thenReturn(clazz);

        // then
        assertThat(sut.supportsParameter(methodParameter)).isTrue();
    }

    @Test
    void orderListWithNoOrderParameter_shouldReturnUnsorted() {
        // given
        var parameterValues = webRequest.getParameterValues("sort");
        when(parameterValues).thenReturn(null);

        // when
        var result = sut.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        // then
        assertThat(result).isEqualTo(SortParameter.unsorted());
    }

    @Test
    void orderListWithEmptyOrderParameterArray_shouldReturnUnsorted() {
        // given
        var parameterValues = webRequest.getParameterValues("sort");
        when(parameterValues).thenReturn(new String[]{});

        // when
        var result = sut.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        // then
        assertThat(result).isEqualTo(SortParameter.unsorted());
    }

    @SuppressWarnings("java:S115")
    enum ESort implements SortParameter.Definition {
        name,
        date
    }

    @Test
    void orderByNameAscAndDateDesc_shouldReturnCorrectlySortedOrderList() {
        // given
        String[] orders = {
                "name:asc",
                "date:desc"
        };

        when(webRequest.getParameterValues("sort")).thenReturn(orders);

        // when
        @SuppressWarnings("unchecked")
        var result = (SortParameter<ESort>) sut.resolveArgument(
                methodParameter,
                mavContainer,
                webRequest,
                binderFactory
        );

        // then
        assertThat(result).isNotNull();
        assertThat(result.sortFields()).hasSize(2);
        assertThat(result.sortFields().getFirst().property()).isEqualTo("name");
        assertThat(result.sortFields().getFirst().direction()).isEqualTo(Sort.Direction.ASC);
        assertThat(result.sortFields().getFirst().nullHandling()).isEqualTo(Sort.NullHandling.NATIVE);
        assertThat(result.sortFields().getLast().property()).isEqualTo("date");
        assertThat(result.sortFields().getLast().direction()).isEqualTo(Sort.Direction.DESC);
        assertThat(result.sortFields().getLast().nullHandling()).isEqualTo(Sort.NullHandling.NATIVE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"name:invalidAsc", "name:invalidDesc"})
    void notExistingOrderBySortParameter_shouldThrowIllegalArgumentException(String value) {
        // given
        String[] orders = {value};

        when(webRequest.getParameterValues("sort")).thenReturn(orders);

        // then
        assertThatIllegalArgumentException().isThrownBy(
                        () -> /* when */ sut.resolveArgument(
                                methodParameter,
                                mavContainer,
                                webRequest,
                                binderFactory
                        )
                )
                .withMessage(
                        "Invalid value '%s' for orders given; Has to be either 'desc' or 'asc' (case insensitive)".formatted(
                                value.split(":")[1] // We only want the second part of the order query param after the :
                        )
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"name:asc:invalidNullHandling1", "name:desc:invalidNullHandling2"})
    void notExistingOrderByNullHandlingParameter_shouldThrowIllegalArgumentException(String value) {
        // given
        String[] orders = {value};

        when(webRequest.getParameterValues("sort")).thenReturn(orders);

        // then
        assertThatIllegalArgumentException().isThrownBy(
                        () -> /* when */ sut.resolveArgument(
                                methodParameter,
                                mavContainer,
                                webRequest,
                                binderFactory
                        )
                )
                .withMessage(
                        "Only native, first or last are allowed as null handling strategy -> Your input %s".formatted(
                                value.split(":")[2]
                        )
                );
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "name:asc:first:invalidAsNoFourthPartExists",
                    "cannot:parse:this:as:there:are:too:many:parts"
            }
    )
    void invalidOrderByParameter_shouldThrowIllegalArgumentException(String value) {
        // given
        String[] orders = {value};

        when(webRequest.getParameterValues("sort")).thenReturn(orders);

        // then
        assertThatIllegalArgumentException().isThrownBy(
                () -> /* when */ sut.resolveArgument(
                        methodParameter,
                        mavContainer,
                        webRequest,
                        binderFactory
                )
        ).withMessage(
                """
                Invalid sort order, only a maximum of two semicolons are allowed\
                 in format <property>[:asc|desc][:native|first|last] -> Your input %s\
                """.formatted(value)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOrderListTestCases")
    void givenValidTestCases_shouldReturnCorrectlySortedOrderList(
            String[] orderQueryParams,
            SortParameter<?> sortParameter
    ) {
        // given
        when(webRequest.getParameterValues("sort")).thenReturn(orderQueryParams);

        // when
        var result = (SortParameter<?>) sut.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        assertThat(result).isNotNull();
        assertThat(result.sortFields()).hasSize(sortParameter.sortFields().size());
        assertThat(result.sortFields())
                .isEqualTo(sortParameter.sortFields());
    }

    @SuppressWarnings("checkstyle:methodlength")
    private static Stream<Arguments> provideOrderListTestCases() {
        return Stream.of(
                // null order query params list should return unsorted order list
                Arguments.of(
                        null,
                        SortParameter.unsorted()
                ),
                // Empty order query params list should return unsorted order list
                Arguments.of(
                        new String[]{},
                        SortParameter.unsorted()
                ),
                Arguments.of(
                        new String[]{"name"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.ASC,
                                Sort.NullHandling.NATIVE
                        )))
                ),
                Arguments.of(
                        new String[]{"name:asc"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.ASC,
                                Sort.NullHandling.NATIVE
                        )))
                ),
                Arguments.of(
                        new String[]{"name:ASC"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.ASC,
                                Sort.NullHandling.NATIVE
                        )))
                ),
                Arguments.of(
                        new String[]{"name:desc"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.DESC,
                                Sort.NullHandling.NATIVE
                        )))
                ),
                Arguments.of(
                        new String[]{"name:DESC"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.DESC,
                                Sort.NullHandling.NATIVE
                        )))
                ),
                Arguments.of(
                        new String[]{"name:asc:first"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.ASC,
                                Sort.NullHandling.NULLS_FIRST
                        )))
                ),
                Arguments.of(
                        new String[]{"name:asc:FIRST"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.ASC,
                                Sort.NullHandling.NULLS_FIRST
                        )))
                ),
                Arguments.of(
                        new String[]{"name:asc:last"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.ASC,
                                Sort.NullHandling.NULLS_LAST
                        )))
                ),
                Arguments.of(
                        new String[]{"name:asc:LAST"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.ASC,
                                Sort.NullHandling.NULLS_LAST
                        )))
                ),
                Arguments.of(
                        new String[]{"name:asc:native"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.ASC,
                                Sort.NullHandling.NATIVE
                        )))
                ),
                Arguments.of(
                        new String[]{"name:asc:NATIVE"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.ASC,
                                Sort.NullHandling.NATIVE
                        )))
                ),
                Arguments.of(
                        new String[]{"name:desc:first"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.DESC,
                                Sort.NullHandling.NULLS_FIRST
                        )))
                ),
                Arguments.of(
                        new String[]{"name:desc:last"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.DESC,
                                Sort.NullHandling.NULLS_LAST
                        )))
                ),
                Arguments.of(
                        new String[]{"name:desc:native"},
                        new SortParameter<>(List.of(new SortParameter.SortField(
                                "name",
                                Sort.Direction.DESC,
                                Sort.NullHandling.NATIVE
                        )))
                ),
                Arguments.of(
                        new String[]{
                                "lastName",
                                "firstName:desc",
                                "date:DESC:LAST"
                        },
                        new SortParameter<>(List.of(
                                new SortParameter.SortField("lastName", Sort.Direction.ASC, Sort.NullHandling.NATIVE),
                                new SortParameter.SortField(
                                        "firstName",
                                        Sort.Direction.DESC,
                                        Sort.NullHandling.NATIVE
                                ),
                                new SortParameter.SortField("date", Sort.Direction.DESC, Sort.NullHandling.NULLS_LAST)
                        ))
                ),
                Arguments.of(
                        new String[]{
                                "lastName:asc:last",
                                "firstName:asc:last",
                                "date:desc:native"
                        },
                        new SortParameter<>(List.of(
                                new SortParameter.SortField(
                                        "lastName",
                                        Sort.Direction.ASC,
                                        Sort.NullHandling.NULLS_LAST
                                ),
                                new SortParameter.SortField(
                                        "firstName",
                                        Sort.Direction.ASC,
                                        Sort.NullHandling.NULLS_LAST
                                ),
                                new SortParameter.SortField("date", Sort.Direction.DESC, Sort.NullHandling.NATIVE)
                        ))
                ),
                Arguments.of(
                        new String[]{
                                "col7:asc:first",
                                "col1:desc:native",
                                "col3:desc:last",
                                "col5:asc:last",
                                "col4:desc:first",
                                "col2:asc:native"
                        },
                        new SortParameter<>(List.of(
                                new SortParameter.SortField("col7", Sort.Direction.ASC, Sort.NullHandling.NULLS_FIRST),
                                new SortParameter.SortField("col1", Sort.Direction.DESC, Sort.NullHandling.NATIVE),
                                new SortParameter.SortField("col3", Sort.Direction.DESC, Sort.NullHandling.NULLS_LAST),
                                new SortParameter.SortField("col5", Sort.Direction.ASC, Sort.NullHandling.NULLS_LAST),
                                new SortParameter.SortField(
                                        "col4",
                                        Sort.Direction.DESC,
                                        Sort.NullHandling.NULLS_FIRST
                                ),
                                new SortParameter.SortField("col2", Sort.Direction.ASC, Sort.NullHandling.NATIVE)
                        ))
                )
        );
    }
}

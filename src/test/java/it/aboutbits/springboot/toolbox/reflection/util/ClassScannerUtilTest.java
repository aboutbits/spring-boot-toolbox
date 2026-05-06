package it.aboutbits.springboot.toolbox.reflection.util;

import it.aboutbits.springboot.toolbox.reflection.util.testfixtures.ScanTestAbstractImpl;
import it.aboutbits.springboot.toolbox.reflection.util.testfixtures.ScanTestAnnotatedClass;
import it.aboutbits.springboot.toolbox.reflection.util.testfixtures.ScanTestAnnotation;
import it.aboutbits.springboot.toolbox.reflection.util.testfixtures.ScanTestConcreteImpl;
import it.aboutbits.springboot.toolbox.reflection.util.testfixtures.ScanTestInterface;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class ClassScannerUtilTest {
    private static final String FIXTURES_PACKAGE = "it.aboutbits.springboot.toolbox.reflection.util.testfixtures";
    private static final String OTHER_PACKAGE = "it.aboutbits.springboot.toolbox.util";

    @Nested
    class ClassScanner {
        @Nested
        class GetSubTypesOf {
            @Test
            @DisplayName("Should find concrete and abstract subtypes of an interface")
            void shouldFindConcreteAndAbstractSubtypesOfInterface() {
                // given
                var scanner = ClassScannerUtil.getScannerForPackages(FIXTURES_PACKAGE);

                // when
                var result = scanner.getSubTypesOf(ScanTestInterface.class);

                // then
                assertThat(result).containsExactlyInAnyOrder(ScanTestConcreteImpl.class, ScanTestAbstractImpl.class);
            }

            @Test
            @DisplayName("Should not include the target interface itself")
            void shouldNotIncludeTargetInterfaceItself() {
                // given
                var scanner = ClassScannerUtil.getScannerForPackages(FIXTURES_PACKAGE);

                // when
                var result = scanner.getSubTypesOf(ScanTestInterface.class);

                // then
                assertThat(result).doesNotContain(ScanTestInterface.class);
            }

            @Test
            @DisplayName("Should return empty set when no subtypes exist in scanned package")
            void shouldReturnEmptySetWhenNoSubtypesExistInScannedPackage() {
                // given
                var scanner = ClassScannerUtil.getScannerForPackages(OTHER_PACKAGE);

                // when
                var result = scanner.getSubTypesOf(ScanTestInterface.class);

                // then
                assertThat(result).isEmpty();
            }
        }

        @Nested
        class GetClassesAnnotatedWith {
            @Test
            @DisplayName("Should find classes annotated with the given annotation")
            void shouldFindClassesAnnotatedWithGivenAnnotation() {
                // given
                var scanner = ClassScannerUtil.getScannerForPackages(FIXTURES_PACKAGE);

                // when
                var result = scanner.getClassesAnnotatedWith(ScanTestAnnotation.class);

                // then
                assertThat(result).containsExactly(ScanTestAnnotatedClass.class);
            }

            @Test
            @DisplayName("Should return empty set when no annotated classes exist in scanned package")
            void shouldReturnEmptySetWhenNoAnnotatedClassesExistInScannedPackage() {
                // given
                var scanner = ClassScannerUtil.getScannerForPackages(OTHER_PACKAGE);

                // when
                var result = scanner.getClassesAnnotatedWith(ScanTestAnnotation.class);

                // then
                assertThat(result).isEmpty();
            }
        }

        @Nested
        class GetScannedPackages {
            @Test
            @DisplayName("Should return the packages provided at construction")
            void shouldReturnPackagesProvidedAtConstruction() {
                // given
                var scanner = ClassScannerUtil.getScannerForPackages(FIXTURES_PACKAGE, OTHER_PACKAGE);

                // when
                var result = scanner.getScannedPackages();

                // then
                assertThat(result).containsExactlyInAnyOrder(FIXTURES_PACKAGE, OTHER_PACKAGE);
            }
        }
    }
}

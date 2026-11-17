package it.aboutbits.springboot.toolbox;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.CacheMode;
import it.aboutbits.archunit.toolbox.BaseArchRuleCollection;
import it.aboutbits.archunit.toolbox.rule.common.SortMappingsExhaustiveArchRule;
import it.aboutbits.archunit.toolbox.support.ArchIgnoreNoProductionCounterpart;
import org.jspecify.annotations.NullMarked;

// The rules of CommonArchRuleCollection without ControllerRequestMappingsMustBeSecurityTested: the only controllers in this library are test fixtures for the MVC type conversion, and the library has no security to test.
@AnalyzeClasses(
        packagesOf = ArchitectureTest.class,
        cacheMode = CacheMode.PER_CLASS
)
@NullMarked
@ArchIgnoreNoProductionCounterpart
class ArchitectureTest implements BaseArchRuleCollection, SortMappingsExhaustiveArchRule {
}

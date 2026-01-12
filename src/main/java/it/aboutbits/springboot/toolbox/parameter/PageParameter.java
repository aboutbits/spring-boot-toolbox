package it.aboutbits.springboot.toolbox.parameter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Slf4j
@ToString
@Accessors(fluent = true)
@NullMarked
public final class PageParameter {
    @Getter
    @Setter
    @SuppressWarnings({"checkstyle:StaticVariableName", "java:S3008"})
    private static int MAX_PAGE_SIZE = 9999;

    @Getter
    @Setter
    @SuppressWarnings({"checkstyle:StaticVariableName", "java:S3008"})
    private static int DEFAULT_PAGE_SIZE = 50;

    private record PageInfo(int page, int size, boolean paged) {
    }

    private final PageInfo pageInfo;

    private PageParameter(@Nullable Integer page, @Nullable Integer size) {
        var actualPage = (page == null) ? 0 : page;
        var actualSize = (size == null) ? DEFAULT_PAGE_SIZE : size;

        if (actualSize > MAX_PAGE_SIZE) {
            log.warn("Page size exceeded maximum [actualSize={}, maxSize={}]", actualSize, MAX_PAGE_SIZE);
        }

        pageInfo = new PageInfo(
                Math.max(0, actualPage),
                Math.min(actualSize, MAX_PAGE_SIZE),
                true
        );
    }

    private PageParameter() {
        pageInfo = new PageInfo(0, Integer.MAX_VALUE, false);
    }

    public static PageParameter of(@Nullable Integer page) {
        return new PageParameter(page, null);
    }

    public static PageParameter of(@Nullable Integer page, @Nullable Integer size) {
        return new PageParameter(page, size);
    }

    public static PageParameter defaultPage() {
        return new PageParameter(null, null);
    }

    public static PageParameter unpaged() {
        return new PageParameter();
    }

    public int page() {
        return pageInfo.page();
    }

    public int size() {
        return pageInfo.size();
    }

    public boolean isPaged() {
        return pageInfo.paged();
    }

    public boolean isUnpaged() {
        return !pageInfo.paged();
    }

    public PageRequest toPageRequest() {
        return PageRequest.of(page(), size());
    }

    public PageRequest toPageRequest(Sort sort) {
        return PageRequest.of(page(), size(), sort);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof PageParameter pageParameter) {
            return this.pageInfo.equals(pageParameter.pageInfo);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

package group.australia.warehouse_management_system.common.utils;

import java.util.List;

public record PageResponse<T>(
        List<T> data,
        Integer page,
        Integer totalPages,
        Long totalElements
) {
}

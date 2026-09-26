package group.australia.warehouse_management_system.feature.SSCC.exception;

import group.australia.warehouse_management_system.common.exceptions.ConflictException;

public class SsccDuplicateException extends ConflictException {
    public SsccDuplicateException(String sscc) {
        super("SSCC " + sscc + " already exists");
    }
}

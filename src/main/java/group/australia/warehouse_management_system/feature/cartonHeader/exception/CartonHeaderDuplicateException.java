package group.australia.warehouse_management_system.feature.cartonHeader.exception;

import group.australia.warehouse_management_system.common.exceptions.ConflictException;

public class CartonHeaderDuplicateException extends ConflictException {
    public CartonHeaderDuplicateException(String barcode) {
        super("Carton Header with barcode " + barcode + " already exists");
    }
}

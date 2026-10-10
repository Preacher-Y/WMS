package group.australia.warehouse_management_system.feature.warehouse.exception;

import group.australia.warehouse_management_system.common.exceptions.ConflictException;

public class WarehouseDuplicateException extends ConflictException {

    public WarehouseDuplicateException(String warehouseNumber) {
        super("Warehouse with number " + warehouseNumber + " already exists");
    }
}

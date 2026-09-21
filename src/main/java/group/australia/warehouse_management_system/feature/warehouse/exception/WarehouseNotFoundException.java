package group.australia.warehouse_management_system.feature.warehouse.exception;

import group.australia.warehouse_management_system.common.exceptions.NotFoundException;

public class WarehouseNotFoundException extends NotFoundException {

    public WarehouseNotFoundException(String warehouseNumber) {
        super("Warehouse with number " + warehouseNumber + " not found");
    }
}

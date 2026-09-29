package group.australia.warehouse_management_system.feature.cartonHeader.exception;

import group.australia.warehouse_management_system.common.exceptions.NotFoundException;

public class CartonHeaderNotFoundException extends NotFoundException {
    public CartonHeaderNotFoundException(String barcode) {
        super("Carton Header with barcode " + barcode + " not found");
    }
}

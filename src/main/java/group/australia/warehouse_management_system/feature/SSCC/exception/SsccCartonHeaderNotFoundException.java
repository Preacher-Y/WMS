package group.australia.warehouse_management_system.feature.SSCC.exception;

import group.australia.warehouse_management_system.common.exceptions.NotFoundException;

public class SsccCartonHeaderNotFoundException extends NotFoundException {
    public SsccCartonHeaderNotFoundException(String barcode) {
        super("Carton Header with barcode " + barcode + " not found");
    }
}

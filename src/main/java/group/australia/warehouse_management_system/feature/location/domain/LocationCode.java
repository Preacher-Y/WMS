package group.australia.warehouse_management_system.feature.location.domain;

import group.australia.warehouse_management_system.common.exceptions.BadRequestException;

public record LocationCode(int row, int section, int shelf) {

    public static LocationCode from(String locationCode) {
        String message = "Invalid location code '" + locationCode
                + "', expected format row-section-shelf, e.g. 1-2-4";
        if (locationCode == null || !locationCode.matches("\\d+-\\d+-\\d+")) {
            throw new BadRequestException(message);
        }
        String[] parts = locationCode.split("-");
        try {
            return new LocationCode(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2])
            );
        } catch (NumberFormatException e) {
            throw new BadRequestException(message);
        }
    }
}

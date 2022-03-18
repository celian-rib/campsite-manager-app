package pt4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import pt4.flotsblancs.database.model.types.Equipment;
import pt4.flotsblancs.database.model.types.Service;

public class ServiceTest extends DatabaseTest {
    @Test
    public void testIsCompatibleWithCampService() {
        // Demander rien est compatible avec un emplacement qui na rien
        assertTrue(Service.NONE.isCompatibleWithCampService(Service.NONE));
        // Demander rien est compatible avec un emplacement qui a eau et elec
        assertTrue(Service.NONE.isCompatibleWithCampService(Service.WATER_AND_ELECTRICITY));
        assertTrue(Service.NONE.isCompatibleWithCampService(Service.WATER_ONLY));
        assertTrue(Service.NONE.isCompatibleWithCampService(Service.ELECTRICITY_ONLY));

        // Demander l'eau électricité compatible avec un emplacement qui na rien ?
        assertFalse(Service.WATER_AND_ELECTRICITY.isCompatibleWithCampService(Service.NONE));
        assertTrue(Service.WATER_AND_ELECTRICITY
                .isCompatibleWithCampService(Service.WATER_AND_ELECTRICITY));
        // Demander l'eau électricité compatible avec un emplacement qui a que l'eau ?
        assertFalse(Service.WATER_AND_ELECTRICITY.isCompatibleWithCampService(Service.WATER_ONLY));
        // Demander l'eau électricité compatible avec un emplacement qui a que
        // l'electricité ?
        assertFalse(Service.WATER_AND_ELECTRICITY
                .isCompatibleWithCampService(Service.ELECTRICITY_ONLY));

        assertFalse(Service.WATER_ONLY.isCompatibleWithCampService(Service.NONE));
        // Demander l'eau compatible avec un emplacement qui a eau et electricité ?
        assertTrue(Service.WATER_ONLY.isCompatibleWithCampService(Service.WATER_AND_ELECTRICITY));
        assertTrue(Service.WATER_ONLY.isCompatibleWithCampService(Service.WATER_ONLY));
        assertFalse(Service.WATER_ONLY.isCompatibleWithCampService(Service.ELECTRICITY_ONLY));

        assertFalse(Service.ELECTRICITY_ONLY.isCompatibleWithCampService(Service.NONE));
        // Demander l'eau compatible avec un emplacement qui a eau et électricité ?
        assertTrue(Service.ELECTRICITY_ONLY
                .isCompatibleWithCampService(Service.WATER_AND_ELECTRICITY));
        assertFalse(Service.ELECTRICITY_ONLY.isCompatibleWithCampService(Service.WATER_ONLY));
        assertTrue(Service.ELECTRICITY_ONLY.isCompatibleWithCampService(Service.ELECTRICITY_ONLY));
    }

    @Test
    public void getCompatiblesServices() {
        campground.setAllowedEquipments(Equipment.MOBILHOME);
        campground.setProvidedServices(Service.WATER_AND_ELECTRICITY);
        assertEquals(1, campground.getCompatiblesServices().size());
        assertTrue(campground.getCompatiblesServices().contains(Service.WATER_AND_ELECTRICITY));
        
        campground.setAllowedEquipments(Equipment.TENT);
        campground.setProvidedServices(Service.WATER_AND_ELECTRICITY);
        assertEquals(4, campground.getCompatiblesServices().size());
        assertTrue(campground.getCompatiblesServices().contains(Service.WATER_AND_ELECTRICITY));
        assertTrue(campground.getCompatiblesServices().contains(Service.WATER_ONLY));
        assertTrue(campground.getCompatiblesServices().contains(Service.ELECTRICITY_ONLY));
        assertTrue(campground.getCompatiblesServices().contains(Service.NONE));
        
        campground.setAllowedEquipments(Equipment.TENT);
        campground.setProvidedServices(Service.WATER_ONLY);
        assertEquals(2, campground.getCompatiblesServices().size());
        assertTrue(campground.getCompatiblesServices().contains(Service.WATER_ONLY));
        assertTrue(campground.getCompatiblesServices().contains(Service.NONE));
       
        campground.setAllowedEquipments(Equipment.TENT);
        campground.setProvidedServices(Service.ELECTRICITY_ONLY);
        assertEquals(2, campground.getCompatiblesServices().size());
        assertTrue(campground.getCompatiblesServices().contains(Service.ELECTRICITY_ONLY));
        assertTrue(campground.getCompatiblesServices().contains(Service.NONE));
        
        campground.setAllowedEquipments(Equipment.TENT);
        campground.setProvidedServices(Service.NONE);
        assertEquals(1, campground.getCompatiblesServices().size());
        assertTrue(campground.getCompatiblesServices().contains(Service.NONE));
    }
}

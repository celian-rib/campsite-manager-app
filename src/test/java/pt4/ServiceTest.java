package pt4;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import pt4.flotsblancs.database.model.types.Service;

public class ServiceTest {
    @Test
    public void testIsCompatibles() {
        // Demander rien est compatible avec un emplacement qui na rien
        assertTrue(Service.NONE.isCompatible(Service.NONE));
        // Demander rien est compatible avec un emplacement qui a eau et elec
        assertTrue(Service.NONE.isCompatible(Service.WATER_AND_ELECTRICITY));
        assertTrue(Service.NONE.isCompatible(Service.WATER_ONLY));
        assertTrue(Service.NONE.isCompatible(Service.ELECTRICITY_ONLY));

        // Demander l'eau électricité compatible avec un emplacement qui na rien ?
        assertFalse(Service.WATER_AND_ELECTRICITY.isCompatible(Service.NONE));
        assertTrue(Service.WATER_AND_ELECTRICITY.isCompatible(Service.WATER_AND_ELECTRICITY));
        // Demander l'eau électricité compatible avec un emplacement qui a que l'eau ?
        assertFalse(Service.WATER_AND_ELECTRICITY.isCompatible(Service.WATER_ONLY));
        // Demander l'eau électricité compatible avec un emplacement qui a que
        // l'electricité ?
        assertFalse(Service.WATER_AND_ELECTRICITY.isCompatible(Service.ELECTRICITY_ONLY));

        assertFalse(Service.WATER_ONLY.isCompatible(Service.NONE));
        // Demander l'eau compatible avec un emplacement qui a eau et electricité ?
        assertTrue(Service.WATER_ONLY.isCompatible(Service.WATER_AND_ELECTRICITY));
        assertTrue(Service.WATER_ONLY.isCompatible(Service.WATER_ONLY));
        assertFalse(Service.WATER_ONLY.isCompatible(Service.ELECTRICITY_ONLY));

        assertFalse(Service.ELECTRICITY_ONLY.isCompatible(Service.NONE));
        // Demander l'eau compatible avec un emplacement qui a eau et électricité ?
        assertTrue(Service.ELECTRICITY_ONLY.isCompatible(Service.WATER_AND_ELECTRICITY));
        assertFalse(Service.ELECTRICITY_ONLY.isCompatible(Service.WATER_ONLY));
        assertTrue(Service.ELECTRICITY_ONLY.isCompatible(Service.ELECTRICITY_ONLY));
    }

    @Test
    public void testGetCompatibles() {
        var list = Service.NONE.getCompatibles();
        assertArrayEquals(Service.values(), list.toArray());

        list = Service.WATER_AND_ELECTRICITY.getCompatibles();
        assertEquals(list.size(), 1);
        assertTrue(list.contains(Service.WATER_AND_ELECTRICITY));
        
        list = Service.ELECTRICITY_ONLY.getCompatibles();
        assertEquals(list.size(), 2);
        assertTrue(list.contains(Service.WATER_AND_ELECTRICITY));
        assertTrue(list.contains(Service.ELECTRICITY_ONLY));
        
        list = Service.WATER_ONLY.getCompatibles();
        assertEquals(list.size(), 2);
        assertTrue(list.contains(Service.WATER_AND_ELECTRICITY));
        assertTrue(list.contains(Service.WATER_ONLY));
    }
}

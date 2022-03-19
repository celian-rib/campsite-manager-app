package pt4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import pt4.flotsblancs.database.model.types.Equipment;

public class EquipmentTest extends DatabaseTestWrapper {
    @Test
    public void testIsCompatible() {
        // <EQUIPMENT> est compatible avec un emplacement de type <EQUIPMENT>
 
        assertTrue(Equipment.CAMPINGCAR.isCompatibleWithCampEquipment(Equipment.CAMPINGCAR));
        assertFalse(Equipment.CAMPINGCAR.isCompatibleWithCampEquipment(Equipment.MOBILHOME));
        assertFalse(Equipment.CAMPINGCAR.isCompatibleWithCampEquipment(Equipment.TENT));
        assertTrue(Equipment.CAMPINGCAR.isCompatibleWithCampEquipment(Equipment.TENT_AND_CAMPINGCAR));
       
        assertFalse(Equipment.MOBILHOME.isCompatibleWithCampEquipment(Equipment.CAMPINGCAR));
        assertTrue(Equipment.MOBILHOME.isCompatibleWithCampEquipment(Equipment.MOBILHOME));
        assertFalse(Equipment.MOBILHOME.isCompatibleWithCampEquipment(Equipment.TENT));
        assertFalse(Equipment.MOBILHOME.isCompatibleWithCampEquipment(Equipment.TENT_AND_CAMPINGCAR));
        
        assertFalse(Equipment.TENT.isCompatibleWithCampEquipment(Equipment.CAMPINGCAR));
        assertFalse(Equipment.TENT.isCompatibleWithCampEquipment(Equipment.MOBILHOME));
        assertTrue(Equipment.TENT.isCompatibleWithCampEquipment(Equipment.TENT));
        assertTrue(Equipment.TENT.isCompatibleWithCampEquipment(Equipment.TENT_AND_CAMPINGCAR));
        
        assertFalse(Equipment.TENT_AND_CAMPINGCAR.isCompatibleWithCampEquipment(Equipment.CAMPINGCAR));
        assertFalse(Equipment.TENT_AND_CAMPINGCAR.isCompatibleWithCampEquipment(Equipment.MOBILHOME));
        assertFalse(Equipment.TENT_AND_CAMPINGCAR.isCompatibleWithCampEquipment(Equipment.TENT));
        assertTrue(Equipment.TENT_AND_CAMPINGCAR.isCompatibleWithCampEquipment(Equipment.TENT_AND_CAMPINGCAR));
    }

    @Test
    public void testGetCompatibleServices() {
        campground.setAllowedEquipments(Equipment.TENT);
        assertEquals(1, campground.getCompatiblesEquipments().size());
        assertTrue(campground.getCompatiblesEquipments().contains(Equipment.TENT));
        
        campground.setAllowedEquipments(Equipment.CAMPINGCAR);
        assertEquals(1, campground.getCompatiblesEquipments().size());
        assertTrue(campground.getCompatiblesEquipments().contains(Equipment.CAMPINGCAR));
        
        campground.setAllowedEquipments(Equipment.MOBILHOME);
        assertEquals(1, campground.getCompatiblesEquipments().size());
        assertTrue(campground.getCompatiblesEquipments().contains(Equipment.MOBILHOME));
        
        campground.setAllowedEquipments(Equipment.TENT_AND_CAMPINGCAR);
        assertEquals(3, campground.getCompatiblesEquipments().size());
        assertTrue(campground.getCompatiblesEquipments().contains(Equipment.TENT));
        assertTrue(campground.getCompatiblesEquipments().contains(Equipment.CAMPINGCAR));
        assertTrue(campground.getCompatiblesEquipments().contains(Equipment.TENT_AND_CAMPINGCAR));
    }
}

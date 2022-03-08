package pt4;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import pt4.flotsblancs.database.model.types.Equipment;

public class EquipmentTest {
    @Test
    public void testIsCompatible() {
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
}

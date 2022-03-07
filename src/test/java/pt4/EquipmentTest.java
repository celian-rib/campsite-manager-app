package pt4;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import pt4.flotsblancs.database.model.types.Equipment;

public class EquipmentTest {
    @Test
    public void testIsCompatible() {
        assertTrue(Equipment.CAMPINGCAR.isCompatible(Equipment.CAMPINGCAR));
        assertFalse(Equipment.CAMPINGCAR.isCompatible(Equipment.MOBILHOME));
        assertFalse(Equipment.CAMPINGCAR.isCompatible(Equipment.TENT));
        assertTrue(Equipment.CAMPINGCAR.isCompatible(Equipment.TENT_AND_CAMPINGCAR));
       
        assertFalse(Equipment.MOBILHOME.isCompatible(Equipment.CAMPINGCAR));
        assertTrue(Equipment.MOBILHOME.isCompatible(Equipment.MOBILHOME));
        assertFalse(Equipment.MOBILHOME.isCompatible(Equipment.TENT));
        assertFalse(Equipment.MOBILHOME.isCompatible(Equipment.TENT_AND_CAMPINGCAR));
        
        assertFalse(Equipment.TENT.isCompatible(Equipment.CAMPINGCAR));
        assertFalse(Equipment.TENT.isCompatible(Equipment.MOBILHOME));
        assertFalse(Equipment.TENT.isCompatible(Equipment.TENT));
        assertTrue(Equipment.TENT.isCompatible(Equipment.TENT_AND_CAMPINGCAR));
        
        assertTrue(Equipment.TENT_AND_CAMPINGCAR.isCompatible(Equipment.CAMPINGCAR));
        assertFalse(Equipment.TENT_AND_CAMPINGCAR.isCompatible(Equipment.MOBILHOME));
        assertTrue(Equipment.TENT_AND_CAMPINGCAR.isCompatible(Equipment.TENT));
        assertTrue(Equipment.TENT_AND_CAMPINGCAR.isCompatible(Equipment.TENT_AND_CAMPINGCAR));
    }
}

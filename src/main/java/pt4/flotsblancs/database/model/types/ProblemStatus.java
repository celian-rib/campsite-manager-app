package pt4.flotsblancs.database.model.types;

import lombok.Getter;

public enum ProblemStatus {
	OPEN_URGENT("Urgent", 10),
	OPEN("En cours", 100),
	SOLVED("Résolu", 1000);

	@Getter
	public String displayName;

	@Getter
	public int compareScale;
	
	ProblemStatus(String displayName, int compareScale) {
		this.displayName = displayName;
		this.compareScale = compareScale;
	}

	@Override
	public String toString() {
		return displayName;
	}
}

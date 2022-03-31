package pt4.flotsblancs.database.model.types;

import lombok.Getter;

public enum ProblemStatus {
	OPEN_URGENT("Urgent", 10, true),
	OPEN("En cours", 100, true),
	SOLVED("Résolu", 1000, false);

	@Getter
	public String displayName;

	@Getter
	public boolean open;

	@Getter
	public int compareScale;
	
	ProblemStatus(String displayName, int compareScale, boolean open) {
		this.displayName = displayName;
		this.compareScale = compareScale;
		this.open = open;
	}

	@Override
	public String toString() {
		return displayName;
	}
}

package pt4.flotsblancs.database.model.types;

import lombok.Getter;

public enum ProblemStatus {
	OPEN_URGENT("Urgent", 10, true), OPEN("En cours", 100, true), SOLVED("Résolu", 1000, false);

	@Getter
	public String displayName;

	@Getter
	// La boolean est hardcodé pour ne pas faire de if est OPEN ou OPEN_URGENT -> gain performance
	public boolean isOpen;

	@Getter
	public int compareScale;

	ProblemStatus(String displayName, int compareScale, boolean open) {
		this.displayName = displayName;
		this.compareScale = compareScale;
		this.isOpen = open;
	}

	@Override
	public String toString() {
		return displayName;
	}
}

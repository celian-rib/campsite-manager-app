package pt4.flotsblancs.database.model.types;

import lombok.Getter;

public enum ProblemStatus {
	OPEN_URGENT("Urgent"),
	OPEN("En cours"),
	SOLVED("Résolu");

	@Getter
	public String displayName;

	ProblemStatus(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}
}

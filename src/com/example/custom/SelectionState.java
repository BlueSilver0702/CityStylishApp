package com.example.custom;

public enum SelectionState {

	NONE("none", 0),
    TOPS("tops", 1),
	BOTTOMS("bottoms", 2),
    DRESSES("dresses", 3),
	ACCESSORIES("accessories", 4),
    FOOTWEAR("footwear", 5),
	JACKETS("jackets", 6);

    private String stringValue;
    private int intValue;
    private SelectionState(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
	
}

// SelectionState me = SelectionState.TOPS

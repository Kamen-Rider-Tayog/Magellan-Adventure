package org.example;

public class MissionManager {
    private static boolean hasTalkedToKingManuel = false;
    private static boolean hasTalkedToKingCharles = false;
    private static boolean hasTalkedToSailor = false;
    private static boolean hasDealtWithMutineers = false;
    private static boolean hasTalkedToAllSailors = false;
    private static boolean hasTalkedToHumabon = false;

    public static boolean hasTalkedToKingManuel() {
        return hasTalkedToKingManuel;
    }

    public static void setTalkedToKingManuel(boolean talked) {
        hasTalkedToKingManuel = talked;
        System.out.println("Mission updated: King Manuel conversation completed - " + talked);
    }

    public static boolean hasTalkedToKingCharles() {
        return hasTalkedToKingCharles;
    }

    public static void setTalkedToKingCharles(boolean talked) {
        hasTalkedToKingCharles = talked;
        System.out.println("Mission updated: King Charles conversation completed - " + talked);
    }

    public static boolean hasTalkedToSailor() {
        return hasTalkedToSailor;
    }

    public static void setTalkedToSailor(boolean talked) {
        hasTalkedToSailor = talked;
        System.out.println("Mission updated: Sailor conversation completed - " + talked);
    }

    public static boolean hasDealtWithMutineers() {
        return hasDealtWithMutineers;
    }

    public static void setDealtWithMutineers(boolean dealtWith) {
        hasDealtWithMutineers = dealtWith;
        System.out.println("Mission updated: Mutineers dealt with - " + dealtWith);
    }

    public static boolean hasTalkedToAllSailors() {
        return hasTalkedToAllSailors;
    }

    public static void setTalkedToAllSailors(boolean talked) {
        hasTalkedToAllSailors = talked;
        System.out.println("Mission updated: All sailors talked to - " + talked);
    }

    public static boolean hasTalkedToHumabon() {
        return hasTalkedToHumabon;
    }

    public static void setTalkedToHumabon(boolean talked) {
        hasTalkedToHumabon = talked;
        System.out.println("Mission updated: Rajah Humabon conversation completed - " + talked);
    }

    public static void resetAllMissions() {
        hasTalkedToKingManuel = false;
        hasTalkedToKingCharles = false;
        hasTalkedToSailor = false;
        hasDealtWithMutineers = false;
        hasTalkedToAllSailors = false;
        hasTalkedToHumabon = false;
        System.out.println("All missions reset");
    }
}
package org.example;

import java.util.ArrayList;
import java.util.List;

public class InteractiveObjectFactory {

    public static InteractiveObject[] createSceneObjects(int sceneIndex, int sceneCols) {
        return switch (sceneIndex) {
            case 0 -> createScene0Objects(sceneCols);
            case 1 -> createScene1Objects();
            case 2 -> createScene2Objects();
            case 3 -> createScene3Objects();
            case 4 -> createScene4Objects();
            case 5 -> createScene5Objects();
            default -> new InteractiveObject[0];
        };
    }

    private static InteractiveObject[] createScene0Objects(int sceneCols) {
        List<InteractiveObject> objects = new ArrayList<>();
        int middleStart = sceneCols / 2 - 2;

        // King Manuel - with alternating dialogue between King and Magellan
        String[] kingManuelDialogues = {
                "Your Majesty, I believe I can find a new spice route by sailing west, a faster path to the Indies.",
                "A new spice route? Nonsense. I reject your proposal, Magellan. You’re already suspected of illegal trading.",
                "I have only served Portugal faithfully, sir.",
                "Enough. Leave before you test my patience."
        };

        String[] alternatingSpeakers = {
                "Magellan",
                "King Manuel I",
                "Magellan",
                "King Manuel I"
        };

        InteractiveObject kingManuel = new InteractiveObject(
                middleStart + 1, 2,
                "King Manuel I", // Initial speaker
                kingManuelDialogues,
                ObjectType.NPC_KING
        );

        // Set alternating speakers for each dialogue line
        kingManuel.setAlternatingSpeakers(alternatingSpeakers);

        kingManuel.setOnDialogueComplete(() -> {
            MissionManager.setTalkedToKingManuel(true);
            SceneOne.updateExitTiles();
            System.out.println("King Manuel dialogue completed - can now exit throne room!");
        });

        objects.add(kingManuel);
        objects.addAll(createKawalObjects(sceneCols));
        return objects.toArray(new InteractiveObject[0]);
    }

    private static List<InteractiveObject> createKawalObjects(int sceneCols) {
        List<InteractiveObject> kawalList = new ArrayList<>();
        int carpetStartX = sceneCols / 2 - 2;
        int carpetEndX = carpetStartX + 4;
        int[] kawalRows = {5, 9, 13, 17, 21};

        for (int row : kawalRows) {
            kawalList.add(new InteractiveObject(
                    carpetStartX - 1, row,
                    "Kawal", "...", ObjectType.KAWAL
            ));
            kawalList.add(new InteractiveObject(
                    carpetEndX, row,
                    "Soldado", "...", ObjectType.KAWAL
            ));
        }
        return kawalList;
    }

    private static InteractiveObject[] createScene1Objects() {
        // Emperor Charles V - with alternating dialogue between Magellan and Emperor
        String[] charlesDialogues = {
                "Your Majesty, I humbly propose a voyage unlike any before. By sailing west, we can reach the rich Spice Islands faster, and under the flag of Spain.",
                "But Magellan, you are Portuguese. Why should Spain trust you with such an endeavor?",
                "I have sworn loyalty to Your Majesty. I offer my knowledge, my maps, and my life in service to Spain.",
                "Very well. I grant you my support. You shall command five ships under the Spanish crown. May God guide you on your voyage."
        };

        String[] alternatingSpeakers = {
                "Magellan",
                "King Charles V",
                "Magellan",
                "King Charles V"
        };

        InteractiveObject emperorCharles = new InteractiveObject(45, 45, "Emperor Charles V",
                charlesDialogues,
                ObjectType.CHARLES);

        // Set alternating speakers for each dialogue line
        emperorCharles.setAlternatingSpeakers(alternatingSpeakers);

        emperorCharles.setOnDialogueComplete(() -> {
            MissionManager.setTalkedToKingCharles(true);
            SceneTwo.updateExitTile();
            System.out.println("Emperor Charles dialogue completed - can now sail!");
        });

        return new InteractiveObject[]{emperorCharles};
    }

    private static InteractiveObject[] createScene2Objects() {
        List<InteractiveObject> sailors = new ArrayList<>();

        // Main sailor - the one that controls the exit door
        String[] sailorDialogues = {
                "Captain, we've sailed so far south. Are we certain this 'Rio de la Plata' is truly a strait?",
                "It's been nothing but a wide estuary so far. The men are growing restless.",
                "Stay the course. We must continue south. If this is not the passage, then the true strait lies further along the coast.",
                "Tell the men to maintain their discipline."
        };

        String[] alternatingSpeakers = {
                "Crew Member",
                "Crew Member",
                "Magellan",
                "Magellan"
        };

        InteractiveObject mainSailor = new InteractiveObject(27, 23, "Crew Member",
                sailorDialogues,
                ObjectType.SAILOR1);

        // Set alternating speakers for the main sailor
        mainSailor.setAlternatingSpeakers(alternatingSpeakers);

        mainSailor.setOnDialogueComplete(() -> {
            MissionManager.setTalkedToSailor(true);
            SceneThree.updateExitTile();
            System.out.println("Main crew member dialogue completed - exit now available!");
        });

        sailors.add(mainSailor);

        // Add additional sailors (decorative only - no dialogue completion effect)
        InteractiveObject sailor2 = new InteractiveObject(4, 15, "Tayog",
                new String[]{"Puro na lang debug, wala man lang hug.",
                        "Panay quiz, wala man lang kiss."},
                ObjectType.SAILOR2);

        InteractiveObject sailor3 = new InteractiveObject(25, 12, "Sailor",
                new String[]{"Aye, keeping watch on the horizon."},
                ObjectType.SAILOR1);

        InteractiveObject sailor4 = new InteractiveObject(39, 22, "Sailor",
                new String[]{"The sea looks calm ahead."},
                ObjectType.SAILOR1);

        InteractiveObject sailor5 = new InteractiveObject(55, 12, "Sailor",
                new String[]{"Adjusting the sails, Captain."},
                ObjectType.SAILOR2);

        // Add all additional sailors to the list
        sailors.add(sailor2);
        sailors.add(sailor3);
        sailors.add(sailor4);
        sailors.add(sailor5);

        return sailors.toArray(new InteractiveObject[0]);
    }

    private static InteractiveObject[] createScene3Objects() {
        List<InteractiveObject> mutineers = new ArrayList<>();

        // Main mutineer leader - the one that controls the exit door
        String[] mutineerDialogues = {
                "Captain Magellan, we've had enough! This endless search for a passage that doesn't exist... We're turning back to Spain with or without you!",
                "You dare challenge my authority?! This mutiny ends now! I will not allow you to jeopardize this historic expedition!",
                "Your command is over, Magellan! The men are with us! We'll take the ships and return to civilization!",
                "Enough! I will execute the ringleaders and maroon the rest if I must. This voyage continues to the Spice Islands!"
        };

        String[] alternatingSpeakers = {
                "Mutineer Captain",
                "Magellan",
                "Mutineer Captain",
                "Magellan"
        };

        InteractiveObject mutineerLeader = new InteractiveObject(24, 24, "Mutineer Captain",
                mutineerDialogues,
                ObjectType.SAILOR2);

        // Set alternating speakers for the mutineer conversation
        mutineerLeader.setAlternatingSpeakers(alternatingSpeakers);

        mutineerLeader.setOnDialogueComplete(() -> {
            MissionManager.setDealtWithMutineers(true);
            SceneFour.updateExitTile();
            System.out.println("Mutineer confrontation completed - exit now available!");
        });

        mutineers.add(mutineerLeader);

        // Add additional mutineers (decorative only)
        InteractiveObject mutineer2 = new InteractiveObject(20, 22, "Crew Member",
                new String[]{"Captain, we’re now on Port St. Julian and it seems the weather is unfortunately in a bad condition right now.",
                "What shall we do? Shall we continue ahead?"},
                ObjectType.SAILOR1);

        InteractiveObject mutineer3 = new InteractiveObject(28, 26, "Quesada",
                new String[]{"Quickly! Find Mesquita. He sleeps like a dead man after that brackish water and rotten biscuit."},
                ObjectType.SAILOR2);

        InteractiveObject mutineer4 = new InteractiveObject(22, 28, "Cartegana",
                new String[]{"Seize the Master-At-Arms! Lock the loyalists! This ship is ours!",
                "The King’s fleet will sail under Spanish command now!"},
                ObjectType.SAILOR1);

        mutineers.add(mutineer2);
        mutineers.add(mutineer3);
        mutineers.add(mutineer4);

        return mutineers.toArray(new InteractiveObject[0]);
    }

    private static InteractiveObject[] createScene4Objects() {
        List<InteractiveObject> sailors = new ArrayList<>();

        String[] hungrySailorDialogues = {
                "Captain, the provisions are nearly gone. We're eating leather and sawdust, softened in the sea.",
                "The men... they're losing hope. How much longer can this go on?",
                "Hold fast, my friend. We have crossed the greatest ocean. Land must be near. We are closer than you think."
        };

        String[] alternatingSpeakers1 = {
                "Crew Member",
                "Crew Member",
                "Magellan"
        };

        InteractiveObject hungrySailor = new InteractiveObject(15, 15, "Crew Member",
                hungrySailorDialogues, ObjectType.SAILOR1);
        hungrySailor.setAlternatingSpeakers(alternatingSpeakers1);

        // Lookout
        InteractiveObject lookout = new InteractiveObject(10, 10, "Lookout",
                new String[]{"Captain, the wind’s turned against us again! We’ll smash into the cliffs if we go on!"},
                ObjectType.SAILOR2);

        // Additional sailors that need to be talked to
        InteractiveObject sailor1 = new InteractiveObject(20, 18, "Sailor",
                new String[]{"A passage to what, Captain? We’ve lost two ships already, men to hunger and cold.",
                        " There’s nothing but fog and fear ahead."},
                ObjectType.SAILOR1);

        InteractiveObject sailor2 = new InteractiveObject(25, 12, "Sailor",
                new String[]{"Is there truly land ahead? Or is this another mirage?"},
                ObjectType.SAILOR2);

        // Set completion callback on the last sailor (you'll need to track which sailors have been talked to)
        sailor2.setOnDialogueComplete(() -> {
            MissionManager.setTalkedToAllSailors(true);
            SceneFive.updateExitTile();
            System.out.println("All sailors checked on - exit now available!");
        });

        sailors.add(hungrySailor);
        sailors.add(lookout);
        sailors.add(sailor1);
        sailors.add(sailor2);

        return sailors.toArray(new InteractiveObject[0]);
    }

    private static InteractiveObject[] createScene5Objects() {
        List<InteractiveObject> objects = new ArrayList<>();

        // Rajah Humabon conversation
        String[] humabonDialogues = {
                "Greetings! My name is Magellan, we are travellers from the north-west land.",
                "Rajah Humabon, we come in peace, from a land far to the west. We seek friendship and trade. We wish to establish an alliance.",
                "What are your intentions for coming here?",
                "We're not here to cause trouble. Truthfully, we're here to introduce our culture and to be friends.",
                "That's good then! Welcome to our land, Magellan."
        };

        String[] alternatingSpeakers = {
                "Magellan",
                "Magellan",
                "Rajah Humabon",
                "Magellan",
                "Rajah Humabon"
        };

        InteractiveObject rajahHumabon = new InteractiveObject(7, 9, "Rajah Humabon",
                humabonDialogues, ObjectType.HUMABON);

        rajahHumabon.setAlternatingSpeakers(alternatingSpeakers);

        rajahHumabon.setOnDialogueComplete(() -> {
            MissionManager.setTalkedToHumabon(true);
            SceneSix.updateExitTile();
            System.out.println("Rajah Humabon dialogue completed - journey complete!");
        });

        objects.add(rajahHumabon);

        // ADD SAILORS ON THE RIGHT SIDE BEFORE COLLISION AREA (x=50)
        // Position sailors around x=40-48 to be before the water collision at x=50

        // First sailor - near the top right
        String[] sailor1Dialogues = {
                "Captain, the crew is settling in well on this island.",
                "The locals seem friendly and the provisions are plentiful."
        };

        InteractiveObject sailor1 = new InteractiveObject(42, 8, "Crew Member",
                sailor1Dialogues, ObjectType.SAILOR1);
        objects.add(sailor1);

        // Second sailor - middle right area
        String[] sailor2Dialogues = {
                "This land is beautiful, Captain.",
                "The vegetation is lush and the waters are clear.",
                "A welcome sight after our long journey across the Pacific."
        };

        InteractiveObject sailor2 = new InteractiveObject(45, 25, "Navigator",
                sailor2Dialogues, ObjectType.SAILOR2);
        objects.add(sailor2);

        // Third sailor - bottom right area
        String[] sailor3Dialogues = {
                "We're repairing the ships and restocking supplies.",
                "The local craftsmen have been very helpful with materials."
        };

        InteractiveObject sailor3 = new InteractiveObject(48, 40, "Shipwright",
                sailor3Dialogues, ObjectType.SAILOR1);
        objects.add(sailor3);

        // Fourth sailor - near the water edge
        String[] sailor4Dialogues = {
                "Careful with those barrels!",
                "We need to keep our drinking water separate from seawater."
        };

        InteractiveObject sailor4 = new InteractiveObject(47, 55, "Quartermaster",
                sailor4Dialogues, ObjectType.SAILOR2);
        objects.add(sailor4);

        // Fifth sailor - observing the surroundings
        String[] sailor5Dialogues = {
                "The geography here is fascinating, Captain.",
                "These islands form a perfect natural harbor.",
                "It would make an excellent trading post."
        };

        InteractiveObject sailor5 = new InteractiveObject(43, 35, "Cartographer",
                sailor5Dialogues, ObjectType.SAILOR1);
        objects.add(sailor5);

        return objects.toArray(new InteractiveObject[0]);
    }
}
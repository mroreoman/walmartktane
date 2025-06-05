package main;

import main.modules.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoryMode {
    private record Pool(int poolCount, List<Class<? extends ModuleBase>> modulePool) {
        private static final Random rand = new Random();

        private List<Class<? extends ModuleBase>> getModuleList() {
            List<Class<? extends ModuleBase>> moduleList = new ArrayList<>(poolCount);
            for (int i = 0; i < poolCount; i++) {
                moduleList.add(modulePool.get(rand.nextInt(modulePool.size())));
            }
            return moduleList;
        }
    }

    public record StoryModeBomb(String name, int startTimeSecs, int maxStrikes, List<Class<? extends ModuleBase>> requiredModules, List<Pool> pools) {
        public Bomb initialize() {
            List<Class<? extends ModuleBase>> moduleList = new ArrayList<>(pools.size());
            moduleList.addAll(requiredModules);
            for (Pool pool : pools) {
                moduleList.addAll(pool.getModuleList());
            }
            return new Bomb(startTimeSecs, maxStrikes, moduleList);
        }
    }

    private static final StoryModeBomb THE_FIRST_BOMB = new StoryModeBomb(
            "The First Bomb", 300, 3,
            List.of(WiresModule.class, TheButtonModule.class, KeypadsModule.class),
            List.of()
    );

    private static final List<StoryModeBomb> CHAPTER_1 = List.of(
            THE_FIRST_BOMB
    );

    private static final StoryModeBomb STORY_MODE_BOMB = new StoryModeBomb(
            "Something Old, Something New", 300, 3,
            List.of(),
            List.of(
                    new Pool(2, List.of(WiresModule.class, TheButtonModule.class, KeypadsModule.class)),
                    new Pool(1, List.of(SimonSaysModule.class, MemoryModule.class, MazesModule.class))
            )
    );

    private static final StoryModeBomb DOUBLE_YOUR_MONEY = new StoryModeBomb(
            "Double Your Money", 300, 3,
            List.of(WiresModule.class, WiresModule.class, TheButtonModule.class, TheButtonModule.class, KeypadsModule.class, KeypadsModule.class),
            List.of()
    );

    private static final StoryModeBomb ONE_STEP_UP = new StoryModeBomb(
            "One Step Up", 300, 3,
            List.of(),
            List.of(
                    new Pool(2, List.of(WiresModule.class, TheButtonModule.class, KeypadsModule.class)),
                    new Pool(1, List.of(WhosOnFirstModule.class, MemoryModule.class)),
                    new Pool(1, List.of(SimonSaysModule.class, MazesModule.class))
            )
    );

    private static final StoryModeBomb PICK_UP_THE_PACE = new StoryModeBomb(
            "Pick up the Pace", 180, 3,
            List.of(),
            List.of(
                    new Pool(1, List.of(WiresModule.class, TheButtonModule.class, KeypadsModule.class)),
                    new Pool(2, List.of(SimonSaysModule.class, WhosOnFirstModule.class, MemoryModule.class, MazesModule.class))
            )
    );

    private static final List<StoryModeBomb> CHAPTER_2 = List.of(
            STORY_MODE_BOMB, DOUBLE_YOUR_MONEY, ONE_STEP_UP, PICK_UP_THE_PACE
    );

    private static final StoryModeBomb A_HIDDEN_MESSAGE = new StoryModeBomb(
            "A Hidden Message", 300, 3,
            List.of(),
            List.of(
                    new Pool(1, List.of(SimonSaysModule.class, WhosOnFirstModule.class, MemoryModule.class)),
                    new Pool(1, List.of(WiresModule.class, TheButtonModule.class)),
                    new Pool(1, List.of(MorseCodeModule.class, PasswordsModule.class))
            )
    );

    private static final StoryModeBomb SOMETHINGS_DIFFERENT = new StoryModeBomb(
            "Something's Different", 300, 3,
            List.of(WiresModule.class),
            List.of(
                    new Pool(1, List.of(SimonSaysModule.class, MemoryModule.class)),
                    new Pool(1, List.of(WireSequencesModule.class, ComplicatedWiresModule.class))
            )
    );

    private static final StoryModeBomb ONE_GIANT_LEAP = new StoryModeBomb(
            "One Giant Leap", 300, 3,
            List.of(),
            List.of(
                    new Pool(1, List.of(SimonSaysModule.class, WhosOnFirstModule.class, MemoryModule.class, MazesModule.class)),
                    new Pool(2, List.of(MorseCodeModule.class, WireSequencesModule.class, ComplicatedWiresModule.class, PasswordsModule.class)),
                    new Pool(1, List.of(TheButtonModule.class, KeypadsModule.class))
            )
    );

    private static final StoryModeBomb FAIR_GAME = new StoryModeBomb(
            "Fair Game", 300, 3,
            List.of(),
            List.of(
                    new Pool(2, List.of(WiresModule.class, TheButtonModule.class, SimonSaysModule.class, KeypadsModule.class, MemoryModule.class, MazesModule.class)),
                    new Pool(1, List.of(WireSequencesModule.class, ComplicatedWiresModule.class)),
                    new Pool(1, List.of(MorseCodeModule.class, PasswordsModule.class)),
                    new Pool(1, List.of(WhosOnFirstModule.class, MemoryModule.class))
            )
    );

    private static final StoryModeBomb PICK_UP_THE_PACE_II = new StoryModeBomb(
            "Pick up the Pace II", 150, 3,
            List.of(),
            List.of(
                    new Pool(4, List.of(WiresModule.class, TheButtonModule.class, KeypadsModule.class)),
                    new Pool(1, List.of(WhosOnFirstModule.class, MazesModule.class))
            )
    );

    private static final StoryModeBomb NO_ROOM_FOR_ERROR = new StoryModeBomb(
            "No Room for Error", 300, 1,
            List.of(WiresModule.class, WiresModule.class, WhosOnFirstModule.class, MemoryModule.class),
            List.of(
                    new Pool(1, List.of(TheButtonModule.class, KeypadsModule.class))
            )
    );

    private static final StoryModeBomb EIGHT_MINUTES = new StoryModeBomb(
            "Eight Minutes", 480, 3,
            List.of(WiresModule.class, TheButtonModule.class, KeypadsModule.class),
            List.of(
                    new Pool(1, List.of(SimonSaysModule.class, WhosOnFirstModule.class)),
                    new Pool(1, List.of(MorseCodeModule.class, ComplicatedWiresModule.class)),
                    new Pool(1, List.of(MemoryModule.class, MazesModule.class)),
                    new Pool(1, List.of(WireSequencesModule.class, PasswordsModule.class)),
                    new Pool(1, List.of(MorseCodeModule.class, WireSequencesModule.class, ComplicatedWiresModule.class, MazesModule.class, PasswordsModule.class))
            )
    );

    private static final List<StoryModeBomb> CHAPTER_3 = List.of(
            A_HIDDEN_MESSAGE, SOMETHINGS_DIFFERENT, ONE_GIANT_LEAP, FAIR_GAME, PICK_UP_THE_PACE_II, NO_ROOM_FOR_ERROR, EIGHT_MINUTES
    );

    private static final StoryModeBomb THE_BIG_BOMB = new StoryModeBomb(
            "The Big Bomb", 600, 3,
            Bomb.allModules,
            List.of()
    );

    public static final List<List<StoryModeBomb>> ALL_CHAPTERS = List.of(
            CHAPTER_1, CHAPTER_2, CHAPTER_3, List.of(THE_BIG_BOMB)
    );
}
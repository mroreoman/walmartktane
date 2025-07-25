package com.github.mroreoman.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.mroreoman.game.modules.ModuleBase.Module;

public record StoryModeBomb(String name, int startTimeSecs, int maxStrikes, List<Module> requiredModules, List<Pool> pools) {
    public Bomb instantiate(Runnable bombExitAction) {
        List<Module> moduleList = new ArrayList<>(pools.size());
        moduleList.addAll(requiredModules);
        for (Pool pool : pools) {
            moduleList.addAll(pool.getModuleList());
        }
        return new Bomb(new Random(), startTimeSecs, maxStrikes, moduleList, bombExitAction, name);
    }

    private record Pool(int count, List<Module> modulePool) {
        private static final Random rand = new Random(); //FIXME implement universal rng here

        private List<Module> getModuleList() {
            List<Module> moduleList = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                moduleList.add(modulePool.get(rand.nextInt(modulePool.size())));
            }
            return moduleList;
        }
    }

    private static final StoryModeBomb THE_FIRST_BOMB = new StoryModeBomb(
            "The First Bomb", 300, 3,
            List.of(Module.WIRES, Module.THE_BUTTON, Module.KEYPADS),
            List.of()
    );

    private static final List<StoryModeBomb> CHAPTER_1 = List.of(
            THE_FIRST_BOMB
    );

    private static final StoryModeBomb STORY_MODE_BOMB = new StoryModeBomb(
            "Something Old, Something New", 300, 3,
            List.of(),
            List.of(
                    new Pool(2, List.of(Module.WIRES, Module.THE_BUTTON, Module.KEYPADS)),
                    new Pool(1, List.of(Module.SIMON_SAYS, Module.MEMORY, Module.MAZES))
            )
    );

    private static final StoryModeBomb DOUBLE_YOUR_MONEY = new StoryModeBomb(
            "Double Your Money", 300, 3,
            List.of(Module.WIRES, Module.WIRES, Module.THE_BUTTON, Module.THE_BUTTON, Module.KEYPADS, Module.KEYPADS),
            List.of()
    );

    private static final StoryModeBomb ONE_STEP_UP = new StoryModeBomb(
            "One Step Up", 300, 3,
            List.of(),
            List.of(
                    new Pool(2, List.of(Module.WIRES, Module.THE_BUTTON, Module.KEYPADS)),
                    new Pool(1, List.of(Module.WHOS_ON_FIRST, Module.MEMORY)),
                    new Pool(1, List.of(Module.SIMON_SAYS, Module.MAZES))
            )
    );

    private static final StoryModeBomb PICK_UP_THE_PACE = new StoryModeBomb(
            "Pick up the Pace", 180, 3,
            List.of(),
            List.of(
                    new Pool(1, List.of(Module.WIRES, Module.THE_BUTTON, Module.KEYPADS)),
                    new Pool(2, List.of(Module.SIMON_SAYS, Module.WHOS_ON_FIRST, Module.MEMORY, Module.MAZES))
            )
    );

    private static final List<StoryModeBomb> CHAPTER_2 = List.of(
            STORY_MODE_BOMB, DOUBLE_YOUR_MONEY, ONE_STEP_UP, PICK_UP_THE_PACE
    );

    private static final StoryModeBomb A_HIDDEN_MESSAGE = new StoryModeBomb(
            "A Hidden Message", 300, 3,
            List.of(),
            List.of(
                    new Pool(1, List.of(Module.SIMON_SAYS, Module.WHOS_ON_FIRST, Module.MEMORY)),
                    new Pool(1, List.of(Module.WIRES, Module.THE_BUTTON)),
                    new Pool(1, List.of(Module.MORSE_CODE, Module.PASSWORDS))
            )
    );

    private static final StoryModeBomb SOMETHINGS_DIFFERENT = new StoryModeBomb(
            "Something's Different", 300, 3,
            List.of(Module.WIRES),
            List.of(
                    new Pool(1, List.of(Module.SIMON_SAYS, Module.MEMORY)),
                    new Pool(1, List.of(Module.WIRE_SEQUENCES, Module.COMPLICATED_WIRES))
            )
    );

    private static final StoryModeBomb ONE_GIANT_LEAP = new StoryModeBomb(
            "One Giant Leap", 300, 3,
            List.of(),
            List.of(
                    new Pool(1, List.of(Module.SIMON_SAYS, Module.WHOS_ON_FIRST, Module.MEMORY, Module.MAZES)),
                    new Pool(2, List.of(Module.MORSE_CODE, Module.WIRE_SEQUENCES, Module.COMPLICATED_WIRES, Module.PASSWORDS)),
                    new Pool(1, List.of(Module.THE_BUTTON, Module.KEYPADS))
            )
    );

    private static final StoryModeBomb FAIR_GAME = new StoryModeBomb(
            "Fair Game", 300, 3,
            List.of(),
            List.of(
                    new Pool(2, List.of(Module.WIRES, Module.THE_BUTTON, Module.SIMON_SAYS, Module.KEYPADS, Module.MEMORY, Module.MAZES)),
                    new Pool(1, List.of(Module.WIRE_SEQUENCES, Module.COMPLICATED_WIRES)),
                    new Pool(1, List.of(Module.MORSE_CODE, Module.PASSWORDS)),
                    new Pool(1, List.of(Module.WHOS_ON_FIRST, Module.MEMORY))
            )
    );

    private static final StoryModeBomb PICK_UP_THE_PACE_II = new StoryModeBomb(
            "Pick up the Pace II", 150, 3,
            List.of(),
            List.of(
                    new Pool(4, List.of(Module.WIRES, Module.THE_BUTTON, Module.KEYPADS)),
                    new Pool(1, List.of(Module.WHOS_ON_FIRST, Module.MAZES))
            )
    );

    private static final StoryModeBomb NO_ROOM_FOR_ERROR = new StoryModeBomb(
            "No Room for Error", 300, 1,
            List.of(Module.WIRES, Module.WIRES, Module.WHOS_ON_FIRST, Module.MEMORY),
            List.of(
                    new Pool(1, List.of(Module.THE_BUTTON, Module.KEYPADS))
            )
    );

    private static final StoryModeBomb EIGHT_MINUTES = new StoryModeBomb(
            "Eight Minutes", 480, 3,
            List.of(Module.WIRES, Module.THE_BUTTON, Module.KEYPADS),
            List.of(
                    new Pool(1, List.of(Module.SIMON_SAYS, Module.WHOS_ON_FIRST)),
                    new Pool(1, List.of(Module.MORSE_CODE, Module.COMPLICATED_WIRES)),
                    new Pool(1, List.of(Module.MEMORY, Module.MAZES)),
                    new Pool(1, List.of(Module.WIRE_SEQUENCES, Module.PASSWORDS)),
                    new Pool(1, List.of(Module.MORSE_CODE, Module.WIRE_SEQUENCES, Module.COMPLICATED_WIRES, Module.MAZES, Module.PASSWORDS))
            )
    );

    private static final List<StoryModeBomb> CHAPTER_3 = List.of(
            A_HIDDEN_MESSAGE, SOMETHINGS_DIFFERENT, ONE_GIANT_LEAP, FAIR_GAME, PICK_UP_THE_PACE_II, NO_ROOM_FOR_ERROR, EIGHT_MINUTES
    );

    private static final StoryModeBomb THE_BIG_BOMB = new StoryModeBomb(
            "The Big Bomb", 600, 3,
            List.of(Module.values()),
            List.of()
    );

    public static final List<List<StoryModeBomb>> ALL_CHAPTERS = List.of(
            CHAPTER_1, CHAPTER_2, CHAPTER_3, List.of(THE_BIG_BOMB)
    );

}
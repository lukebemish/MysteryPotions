/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

def pool = json?.pools?.find { it?.entries?.any {
    it.name == 'minecraft:glass_bottle'
} }

if (pool != null) {
    pool.entries.add([
        type: 'minecraft:item',
        name: 'mysterypotions:potion_swamp',
        weight: 1
    ])
}

json.pools?.add([
    bonus_rolls: 0.0,
    conditions: [
        [
            condition: 'minecraft:killed_by_player'
        ]
    ],
    entries: [
        [
            type: 'minecraft:item',
            name: 'mysterypotions:potion_villager'
        ],
        [
            type: 'minecraft:empty',
            weight: 20
        ]
    ],
    rolls: [
        type: 'minecraft:uniform',
        min: 0.0,
        max: 1.0
    ]
])

json

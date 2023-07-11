def pool = json?.pools?.find { it?.entries?.any {
    it.name == 'minecraft:diamond'
} }

if (pool != null) {
    pool.entries.add([
        type: 'minecraft:item',
        name: 'mysterypotions:potion_time',
        weight: 15
    ])
    pool.entries.add([
        type: 'minecraft:item',
        name: 'mysterypotions:potion_floating_blood',
        weight: 5
    ])
}

json

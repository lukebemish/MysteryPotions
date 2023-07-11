def pool = json?.pools?.find { it?.entries?.any {
    it.name == 'minecraft:iron_ingot'
} }

if (pool != null) {
    pool.entries.add([
        type: 'minecraft:item',
        name: 'mysterypotions:potion_water',
        weight: 10
    ])
}

json

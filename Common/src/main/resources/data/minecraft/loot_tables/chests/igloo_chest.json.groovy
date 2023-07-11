def pool = json?.pools?.find { it?.entries?.any {
    it.name == 'minecraft:golden_apple'
} }

if (pool != null) {
    pool.entries.add([
        type: 'minecraft:item',
        name: 'mysterypotions:potion_floating_blood'
    ])
}

json
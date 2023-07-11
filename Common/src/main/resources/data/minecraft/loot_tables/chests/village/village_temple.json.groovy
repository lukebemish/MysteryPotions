def pool = json?.pools?.find { it?.entries?.any {
    it.name == 'minecraft:emerald'
} }

if (pool != null) {
    pool.entries.add([
        type: 'minecraft:item',
        name: 'mysterypotions:potion_villager'
    ])
}

json

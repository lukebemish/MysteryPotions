def pool = json?.pools?.find { it?.entries?.any {
    it.name == 'minecraft:saddle'
} }

if (pool != null) {
    pool.entries.add([
        type: 'minecraft:item',
        name: 'mysterypotions:potion_swamp',
        weight: pool.entries.find { it.name == 'minecraft:saddle' }?.weight ?: 1
    ])
}

json

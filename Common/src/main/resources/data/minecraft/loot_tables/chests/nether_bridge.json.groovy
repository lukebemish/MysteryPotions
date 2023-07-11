def pool = json?.pools?.find { it?.entries?.any {
    it.name == 'minecraft:saddle'
} }

if (pool != null) {
    pool.entries.add([
        type: 'minecraft:item',
        name: 'mysterypotions:potion_fire'
    ])
}

json

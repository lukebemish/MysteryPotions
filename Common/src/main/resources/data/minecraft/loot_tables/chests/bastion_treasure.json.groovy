def pool = json?.pools?.find { it?.entries?.any {
    it.name == 'minecraft:spectral_arrow'
} }

if (pool != null) {
    pool.entries.add([
        type: 'minecraft:item',
        name: 'mysterypotions:potion_fire'
    ])
}

json

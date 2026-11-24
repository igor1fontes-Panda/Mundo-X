package com.example.mundoaberto.data.model

data class LocationPoint(
    val x: Float, // 0.0 to 1.0 relative to map coordinates
    val y: Float
)

enum class LocationType {
    PLAZA,
    CAFE,
    LIBRARY,
    PARK,
    RESIDENCE,
    MARKET
}

data class TownLocation(
    val id: String,
    val name: String,
    val type: LocationType,
    val description: String,
    val center: LocationPoint,
    val iconName: String,
    val capacity: Int = 4
) {
    companion object {
        val ALL_LOCATIONS = listOf(
            TownLocation(
                id = "plaza",
                name = "Praça Central",
                type = LocationType.PLAZA,
                description = "O coração da cidade com chafariz de pedra, bancos e murais de recados.",
                center = LocationPoint(0.50f, 0.48f),
                iconName = "fountain"
            ),
            TownLocation(
                id = "cafe",
                name = "Café & Taverna O Encontro",
                type = LocationType.CAFE,
                description = "Local aromático onde se trocam novidades e xícaras de café quente.",
                center = LocationPoint(0.25f, 0.30f),
                iconName = "cafe"
            ),
            TownLocation(
                id = "library",
                name = "Biblioteca & Arquivo",
                type = LocationType.LIBRARY,
                description = "Paz, prateleiras de carvalho e ambiente para reflexão e estudos.",
                center = LocationPoint(0.75f, 0.28f),
                iconName = "book"
            ),
            TownLocation(
                id = "park",
                name = "Parque das Cerejeiras",
                type = LocationType.PARK,
                description = "Jardins floridos, lago ornamental e caminhos de cascalho.",
                center = LocationPoint(0.78f, 0.72f),
                iconName = "nature"
            ),
            TownLocation(
                id = "market",
                name = "Mercado & Oficinas",
                type = LocationType.MARKET,
                description = "Bancadas de artesanato, ferramentas e suprimentos diários.",
                center = LocationPoint(0.24f, 0.70f),
                iconName = "store"
            ),
            TownLocation(
                id = "residences",
                name = "Vila Residencial",
                type = LocationType.RESIDENCE,
                description = "Casas acolhedoras com varandas onde os habitantes descansam.",
                center = LocationPoint(0.50f, 0.82f),
                iconName = "home"
            )
        )
    }
}

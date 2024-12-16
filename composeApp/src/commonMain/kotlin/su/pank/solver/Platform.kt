package su.pank.solver

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
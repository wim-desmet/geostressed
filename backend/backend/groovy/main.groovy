import java.util.Random

def rand = new Random()

def sampleInTriangle(minX, maxX, minY, maxY) {
    def x = minX + rand.nextDouble() * (maxX - minX)

    System.out.println(x)
}

sampleInTriangle(3.12693868, 5.64832663, 49.68725789, 51.09820380)

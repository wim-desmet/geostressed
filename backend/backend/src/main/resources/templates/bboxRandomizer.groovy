import java.util.Random
def crs = sampler.getArguments().getArgumentsAsMap().get("crs")

// Extract the numeric part
def matcher = (crs =~ /^EPSG:(\d+)$/)
if (!matcher.matches()) {
    throw new IllegalArgumentException("Input must be formatted as EPSG:xxx, got: ${input}")
}
def code = matcher[0][1] as int


def sampleInTriangle(rand, minX, minY, maxX, maxY, size) {
    def randX = minX + rand.nextDouble() * (maxX - minX)
    def randY = minY + rand.nextDouble() * (maxY - minY)

    if (randX < minX + (maxX - minX) * (1 - (randY-minY)/(maxY-minY))) {
        def randXO = maxX - (randY - minY ) * (maxX - minX) / (maxY - minY)
        def randYO = maxY - (randX - minX ) * (maxY - minY) / (maxX - minX)

        randX = randXO
        randY = randYO
    }

    return [randX, randY, randX + size, randY + size]
}

def bboxMinX, bboxMinY, bboxMaxX, bboxMaxY
def rand = new Random()
def size = Math.pow(10, 1 + rand.nextDouble() * 4)

switch (code) {
    case 4326:
        def sizeInDeg = size * 0.000014408
        def sample = sampleInTriangle(rand, 3.12693868, 49.68725789, 5.64832663 - sizeInDeg, 51.09820380 - sizeInDeg, sizeInDeg)
        bboxMinX = sample[0]
        bboxMinY = sample[1]
        bboxMaxX = sample[2]
        bboxMaxY = sample[3]

        break
    case 3812:
        def sizeInMeter= size
        def sample = sampleInTriangle(rand, 563000, 542540, 739578 - sizeInMeter, 669500 - sizeInMeter, sizeInMeter)
        bboxMinX = sample[0]
        bboxMinY = sample[1]
        bboxMaxX = sample[2]
        bboxMaxY = sample[3]

        break
    case 3857:
        def sizeInMeter= size
        def sample = sampleInTriangle(rand, 348088, 63923479, 628766 - sizeInMeter, 6638679 - sizeInMeter, sizeInMeter)
        bboxMinX = sample[0]
        bboxMinY = sample[1]
        bboxMaxX = sample[2]
        bboxMaxY = sample[3]
}

// Build result string
def result = [bboxMinX, bboxMinY, bboxMaxX, bboxMaxY].join(",")
log.info(result)
// Store in JMeter variable
vars.put("epsgResult", result)